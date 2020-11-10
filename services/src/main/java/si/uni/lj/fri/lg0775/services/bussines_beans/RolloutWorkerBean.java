package si.uni.lj.fri.lg0775.services.bussines_beans;

import si.uni.lj.fri.lg0775.entities.db.*;
import si.uni.lj.fri.lg0775.services.beans.EndUserBean;
import si.uni.lj.fri.lg0775.services.beans.RuleBean;
import si.uni.lj.fri.lg0775.services.beans.GradualRolloutBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
public class RolloutWorkerBean {
    private final Logger LOG = Logger.getLogger(RolloutWorkerBean.class.getName());

    @Inject
    private EndUserBean endUserBean;

    @Inject
    private RuleBean ruleBean;

    @Inject
    private GradualRolloutBean srBean;

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
        List<GradualRollout> unfinishedRollouts = srBean.getUnfinishedRollouts();

        if (unfinishedRollouts.isEmpty()) {
            LOG.info("No unfinished rollouts found");
            return;
        }

        for (GradualRollout sr : unfinishedRollouts) {
            scheduleRollout(
                    sr,
                    sr.getApplication(),
                    sr.getFlag(),
                    sr.getNumOfSteps(),
                    sr.getNewValue(),
                    sr.getInterval(),
                    sr.getTimeUnit());
        }
    }

    private void removeTask(GradualRollout sr) {
        LOG.info("Stop rollout");
        this.scheduledFutureList.get(sr.getUuid()).cancel(true);
        this.scheduledFutureList.remove(sr.getUuid());
        sr.setCompleted(100);
        srBean.update(sr);
    }

    public void scheduleRollout(GradualRollout sr, Application application, Flag flag, int numOfRollouts,
                                int newValue, Integer interval, TimeUnit timeUnit) {

        if (sr == null) {
            sr = new GradualRollout();
            sr.setUuid(UUID.randomUUID().toString());
            sr.setNewValue(newValue);
            sr.setNumOfSteps(numOfRollouts);
            sr.setApplication(application);
            sr.setCompleted(0);
            sr.setFlag(flag);
            sr.setTimeUnit(timeUnit);
            sr.setInterval(interval);

            srBean.create(sr);
        }


        GradualRollout finalSr = sr;
        Runnable runnable = () -> {
            LOG.info("Running rollout " + finalSr.getUuid());

            // Pridobi samo tiste, ki še nimajo nove vrednosti
            // Določi novo skupino uporabnikov, ki jim bomo posodobili vrednost
            List<Rule> rules = endUserBean.getUsersIfAppWONewValue(application.getId(), flag.getId(), newValue);
            List<EndUser> users = rules.stream().map(Rule::getEndUser).collect(Collectors.toList());

            // In case there is no users to move to new value
            if (users.isEmpty()) {
                removeTask(finalSr);
            }

            // Get all users of specific app
            List<EndUser> usersAll = endUserBean.getUsersOfApp(application.getId());

            // Calc the size of a group
            double groupSize = Math.ceil((double) usersAll.size() / (double) numOfRollouts);

            double rate = ((usersAll.size() - users.size() + groupSize) / usersAll.size()) * 100;
            finalSr.setCompleted((int) rate);
            srBean.update(finalSr);

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

                rule.setValue(newValue);

                // Persist changes
                if (rule.getId() == null) {
                    ruleBean.create(rule);
                } else {
                    ruleBean.update(rule);
                }
            }

            //  If there is no remaining users to move to new value
            if (users.size() - groupSize <= 0) {
                removeTask(finalSr);
            }
        };

        LOG.info(String.format("Schedule task with interval %d %s for %d rounds", interval, timeUnit, numOfRollouts));
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService
                .scheduleAtFixedRate(runnable, 0, interval, timeUnit);
        scheduledFutureList.put(sr.getUuid(), scheduledFuture);
    }
}
