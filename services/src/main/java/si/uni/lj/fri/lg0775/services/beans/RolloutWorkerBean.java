package si.uni.lj.fri.lg0775.services.beans;

import si.uni.lj.fri.lg0775.entities.db.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
public class RolloutWorkerBean {
    private final Logger LOG = Logger.getLogger("RolloutWorkerBean");

    @Inject
    private EndUserBean endUserBean;

    @Inject
    private RuleBean ruleBean;

    @Inject
    private ScheduledRolloutBean srBean;

    private ScheduledExecutorService scheduledExecutorService;
    private HashMap<String, ScheduledFuture<?>> scheduledFutureList;

    @PostConstruct
    private void init() {
        LOG.info("Init worker");
        scheduledFutureList = new HashMap<>();
        scheduledExecutorService = Executors.newScheduledThreadPool(4);
    }

    @PreDestroy
    private void destroy() {
            LOG.info("Destroy Executors");
            try {
                scheduledExecutorService.awaitTermination(3L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                LOG.severe("Rollout was interrupted during termination");
            }
    }

    private void begin(@Observes @Initialized(ApplicationScoped.class) Object init) {
        // Check for unfinished rollouts
        rerunUnfinished();
    }

    private void rerunUnfinished() {
        List<ScheduledRollout> unfinishedRollouts = srBean.getUnfinishedRolouts();

        if (unfinishedRollouts.isEmpty()) {
            LOG.info("No unfinished rollouts found");
            return;
        }

        for (ScheduledRollout sr : unfinishedRollouts) {
            scheduleRollout(
                    sr.getApplication(),
                    sr.getFlag(),
                    sr.getNumOfRollouts(),
                    sr.getNewValue(),
                    sr.getInterval(),
                    sr.getTimeUnit(),
                    sr.getExpirationDate());
        }
    }

    private void removeScheduledTask(ScheduledRollout sr) {
        this.scheduledFutureList.get(sr.getUuid()).cancel(true);
        this.scheduledFutureList.remove(sr.getUuid());
        sr.setCompleted(true);
        srBean.update(sr);
    }

    public void scheduleRollout(Application application, Flag flag, int numOfRollouts,
                                int newValue, Long interval, TimeUnit timeUnit, Timestamp expirationDate) {
        ScheduledRollout sr = new ScheduledRollout();
        sr.setUuid(UUID.randomUUID().toString());
        sr.setNewValue(newValue);
        sr.setNumOfRollouts(numOfRollouts);
        sr.setApplication(application);
        sr.setCompleted(false);
        sr.setFlag(flag);

        srBean.create(sr);

        Runnable runnable = () -> {
            // Pridobi samo tiste, ki še nimajo nove vrednosti
            // Določi novo skupino uporabnikov, ki jim bomo posodobili vrednost
            List<Rule> rules = endUserBean.getUsersIfAppWONewValue(application.getId(), flag.getId(), newValue);
            List<EndUser> users = rules.stream().map(Rule::getEndUser).collect(Collectors.toList());

            // In case there is no users to move to new value
            if (users.isEmpty()) {
                LOG.info("Stop rollout");
                removeScheduledTask(sr);
            }

            // Get all users of specific app
            List<EndUser> usersAll = endUserBean.getUsersOfApp(application.getId());

            // Calc the size of a group
            int groupSize = usersAll.size() / numOfRollouts;

            for (int i = 0; i < groupSize; i++) {
                EndUser endUser = users.get(i);
                Rule rule;
                try {
                    rule = ruleBean.getRuleForUser(endUser, flag);
                } catch (NoResultException nre) {
                    rule = new Rule();
                    rule.setApplication(application);
                    rule.setEndUser(endUser);
                    rule.setFlag(flag);
                }

                rule.setExpirationDate(expirationDate);
                rule.setValue(newValue);

                // Persist changes
                if (rule.getId() == null) {
                    ruleBean.create(rule);
                } else {
                    ruleBean.update(rule);
                }
            };

            //  If there is no remaining users to move to new value
            if (users.size() - groupSize <= 0) {
                LOG.info("Stop rollout");
                removeScheduledTask(sr);
            }
        };

        LOG.info(String.format("Schedule task with interval %d %s for %d rounds", interval, timeUnit, numOfRollouts));
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService
                .scheduleAtFixedRate(runnable, 0, interval, timeUnit);
        scheduledFutureList.put(sr.getUuid(), scheduledFuture);
    }
}
