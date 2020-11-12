package si.uni.lj.fri.lg0775.services.beans;

import si.uni.lj.fri.lg0775.entities.db.Application;
import si.uni.lj.fri.lg0775.entities.db.EndUser;
import si.uni.lj.fri.lg0775.entities.db.Flag;
import si.uni.lj.fri.lg0775.entities.db.Rule;
import si.uni.lj.fri.lg0775.services.bussines_beans.RolloutWorkerBean;
import si.uni.lj.fri.lg0775.services.dtos.*;
import si.uni.lj.fri.lg0775.services.dtos.models.RuleDto;
import si.uni.lj.fri.lg0775.services.exceptions.InvalidDataException;
import si.uni.lj.fri.lg0775.services.lib.DtoMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
public class RuleBean {
    private static final Logger LOG = Logger.getLogger(RuleBean.class.getName());

    @PersistenceContext(name = "feature-flags")
    private EntityManager em;

    @Inject
    private ApplicationBean applicationBean;

    @Inject
    private EndUserBean endUserBean;

    @Inject
    private FlagBean flagBean;

    @Inject
    private RolloutWorkerBean rolloutWorkerBean;

    // Create
    @Transactional
    public void create(Rule e) {
        em.persist(e);
        em.flush();
    }

    // Update
    @Transactional
    public void update(Rule e) {
        em.merge(e);
        em.flush();
    }

    // delete
    public void delete(Rule e) {
        em.remove(e);
        em.flush();
    }

    // Mark deleted
    public void markDeleted(Rule e) {
        e.setDeleted(true);

        em.persist(e);
        em.flush();
    }

    @Transactional
    public void deleteAll() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<Rule> delete = builder.createCriteriaDelete(Rule.class);
        delete.from(Rule.class);
        Query query1 = em.createQuery(delete);
        query1.executeUpdate();
    }

    // Find
    public Rule find(Long id) {
        return em.find(Rule.class, id);
    }

    // Contains
    public boolean contains(Rule e) {
        return em.contains(e);
    }

    public Rule getRuleForUser(EndUser endUser, Flag flag) {
        return em.createNamedQuery("Rule.getRuleForUser", Rule.class)
                .setParameter("clientId", endUser.getClient())
                .setParameter("flagId", flag.getId())
                .getSingleResult();
    }

    @Transactional
    public RuleDto createRule(CreateRuleDto crd, Long appId, Long flagId) {
        LOG.info("Create rule " + crd.getRuleType().name());

        Application application = applicationBean.find(appId);
        Flag flag = flagBean.find(flagId);

        if (crd.getShares() == null || crd.getShares().isEmpty()) {
            throw new InvalidDataException("Shares are empty");
        }

        switch (crd.getRuleType()) {
            case SAME_FOR_EVERYONE:
                createRuleForEveryone(crd, application, flag);
                break;
            case AB_TESTING:
                createABTestingRule(crd, application, flag);
                break;
            case USER_SPECIFIC:
                createRuleForSpecificUser(crd, application, flag);
                break;
            default:
                throw new RuntimeException("This type does not exist or is not supported yet");
        }
        return null;
    }

    private void createRuleForSpecificUser(CreateRuleDto crd, Application application, Flag flag) {
        if (crd.getUser() == null) {
            throw new InvalidDataException("No users selected");
        }
        EndUser endUser = endUserBean.find(crd.getUser());
        Rule rule;
        try {
            rule = em.createNamedQuery("Rule.getRuleForUser", Rule.class)
                    .setParameter("clientId", endUser.getClient())
                    .setParameter("flagId", flag.getId())
                    .getResultList().get(0);
        } catch (NoResultException nre) {
            rule = new Rule();
            rule.setApplication(application);
            rule.setEndUser(endUser);
            rule.setFlag(flag);
        }

        rule.setValue(crd.getShares().get(0).getValue());

        // Persist
        if (rule.getId() == null) {
            create(rule);
        } else {
            update(rule);
        }
    }

    private void createRuleForEveryone(CreateRuleDto crd, Application application, Flag flag) {
        // Get all users of that app
        List<EndUser> users = endUserBean.getUsersOfApp(application.getId());

        // Create array of rules for these users
        users.forEach(endUser -> {
            Rule rule;
            try {
                rule = em.createNamedQuery("Rule.getRuleForUser", Rule.class)
                        .setParameter("clientId", endUser.getClient())
                        .setParameter("flagId", flag.getId())
                        .getSingleResult();
            } catch (NoResultException nre) {
                rule = new Rule();
                rule.setApplication(application);
                rule.setEndUser(endUser);
                rule.setFlag(flag);
            }

            rule.setValue(crd.getShares().get(0).getValue());

            // Persist
            if (rule.getId() == null) {
                create(rule);
            } else {
                update(rule);
            }
        });
    }

    @Transactional
    private void createABTestingRule(CreateRuleDto crd, Application application, Flag flag) {
        // Check value of shareOfA
        int sum = 0;
        for (Share share : crd.getShares()) {
            sum += share.getShare();
        }

        if (sum <= 0 || sum > 100) {
            throw new InvalidDataException("Sum of shares should be between 0 and 100");
        }

        // Get all users of that app
        List<EndUser> users = endUserBean.getUsersOfApp(application.getId());
        Collections.shuffle(users);

        int last_amount = 0;
        for (Share s : crd.getShares()) {
            int amount = (int) Math.ceil(users.size() * (double) s.getShare() / 100);

            for (int i = last_amount; i < last_amount + amount && i < users.size(); i++) {
                EndUser endUser = users.get(i);

                Rule rule;
                try {
                    rule = getRuleForUser(endUser, flag);
                } catch (NoResultException nre) {
                    rule = new Rule();
                    rule.setApplication(application);
                    rule.setEndUser(endUser);
                    rule.setFlag(flag);
                }

                rule.setValue(s.getValue());

                // Persist
                if (rule.getId() == null) {
                    create(rule);
                } else {
                    update(rule);
                }
            }

            last_amount += amount;
        }
    }

    public List<Rule> getRulesForFlag(long flag_id) {
        return em.createNamedQuery("Rule.getRulesForFlag", Rule.class)
                .setParameter("flagId", flag_id)
                .getResultList();
    }

    public List<RuleDto> getRulesDtoForFlag(long flag_id) {
        return DtoMapper.toRulesDto(getRulesForFlag(flag_id));
    }

    public List<RuleDto> getRulesForApp(String clientId) {
        return em.createNamedQuery("Rule.getRulesForApp", Rule.class)
                .setParameter("clientId", clientId)
                .getResultList()
                .stream()
                .map(DtoMapper::toRuleDto)
                .collect(Collectors.toList());
    }

    public List<RuleDto> getRulesDtoForUserID(Long user_id) {
        return DtoMapper.toRulesDto(getRulesForUserID(user_id));
    }

    public List<Rule> getRulesForUserID(Long user_id) {
        List<Rule> rules = em.createNamedQuery("Rule.getRuleForUserById", Rule.class)
                .setParameter("clientId", user_id)
                .getResultList();

        if (rules.isEmpty()) {
            return null;
        }

        return rules;
    }

    @Transactional
    public void removeRulesForFlag(Long flag_id) {
        List<Rule> rules = getRulesForFlag(flag_id);
        rules.forEach(this::markDeleted);
    }

    @Transactional
    public void removeFlag(Long id) {
        markDeleted(find(id));
    }

    public void scheduleRollout(CreateRolloutDto crd) {
        Application application = applicationBean.find(crd.getAppId());
        Flag flag = flagBean.find(crd.getFlagId());

        rolloutWorkerBean.scheduleRollout(null, application, flag, crd.getNumOfSteps(),
                crd.getNewValue(), crd.getInterval(), crd.getTimeUnit());
    }
}
