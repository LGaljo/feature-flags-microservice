package si.uni.lj.fri.lg0775.services.beans;

import si.uni.lj.fri.lg0775.entities.db.Application;
import si.uni.lj.fri.lg0775.entities.db.EndUser;
import si.uni.lj.fri.lg0775.entities.db.Flag;
import si.uni.lj.fri.lg0775.entities.db.Rule;
import si.uni.lj.fri.lg0775.entities.enums.DataType;
import si.uni.lj.fri.lg0775.services.dtos.CreateRuleDto;
import si.uni.lj.fri.lg0775.services.dtos.RuleDto;
import si.uni.lj.fri.lg0775.services.exceptions.InvalidDataException;
import si.uni.lj.fri.lg0775.services.lib.DtoMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;
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

    // Create
    public void create(Rule e) {
        em.persist(e);
        em.flush();
    }

    // Update
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

    // Find
    public Rule find(Long id) {
        return em.find(Rule.class, id);
    }

    // Contains
    public boolean contains(Long id) {
        return em.contains(id);
    }

    @Transactional
    public RuleDto createRule(CreateRuleDto crd, Long appId, Long flagId) {
        LOG.info("Create rule " + crd.getRuleType().name());

        Application application = applicationBean.find(appId);
        Flag flag = flagBean.find(flagId);

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
        Rule rule = null;
        try {
            rule = em.createNamedQuery("Rule.getRuleForUser", Rule.class)
                    .setParameter("clientId", endUser.getClient())
                    .getResultList().get(0);
        } catch (NoResultException nre) {
            rule = new Rule();
            rule.setApplication(application);
            rule.setEndUser(endUser);
            rule.setFlag(flag);
        }

        rule.setExpirationDate(Timestamp.from(crd.getExpirationDate()));
        rule.setValue(crd.getValue());

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
            Rule rule = null;
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

            rule.setExpirationDate(Timestamp.from(crd.getExpirationDate()));
            rule.setValue(crd.getValue());

            // Persist
            if (rule.getId() == null) {
                create(rule);
            } else {
                update(rule);
            }
        });
    }

    private void createABTestingRule(CreateRuleDto crd, Application application, Flag flag) {
        // Get all users of that app
        List<EndUser> users = endUserBean.getUsersOfApp(application.getId());
        Random rand = new Random();

        users.forEach(endUser -> {
            Rule rule = null;
            try {
                rule = em.createNamedQuery("Rule.getRuleForUser", Rule.class)
                        .setParameter("clientId", endUser.getClient())
                        .getSingleResult();
            } catch (NoResultException nre) {
                rule = new Rule();
                rule.setApplication(application);
                rule.setEndUser(endUser);
                rule.setFlag(flag);
            }

            rule.setExpirationDate(Timestamp.from(crd.getExpirationDate()));
            int rand_int = rand.nextInt(100) + 1;
            if (rand_int < crd.getShareOfA()) {
                rule.setValue(crd.getValue());
            } else {
                if (crd.getDataType() == DataType.BOOL) {
                    rule.setValue(Math.abs(crd.getValue() - 1));
                } else {
                    rule.setValue(crd.getValueB());
                }
            }

            // Persist
            if (rule.getId() == null) {
                create(rule);
            } else {
                update(rule);
            }
        });
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
                .filter(r -> !r.hasExpired())
                .map(DtoMapper::toRuleDto)
                .collect(Collectors.toList());
    }

    public List<RuleDto> getRulesForUserID(Long user_id) {
        List<Rule> rules = em.createNamedQuery("Rule.getRuleForUserById", Rule.class)
                .setParameter("clientId", user_id)
                .getResultList();

        if (rules.isEmpty()) {
            return null;
        }
        return DtoMapper.toRulesDto(rules);
    }

    public void removeRulesForFlag(Long flag_id) {
        List<Rule> rules = getRulesForFlag(flag_id);
        rules.forEach(this::markDeleted);
    }
}
