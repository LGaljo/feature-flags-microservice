package si.uni.lj.fri.lg0775.services.beans;

import si.uni.lj.fri.lg0775.entities.db.Application;
import si.uni.lj.fri.lg0775.entities.db.EndUser;
import si.uni.lj.fri.lg0775.entities.db.Flag;
import si.uni.lj.fri.lg0775.entities.db.Rule;
import si.uni.lj.fri.lg0775.services.dtos.CreateRuleDto;
import si.uni.lj.fri.lg0775.services.dtos.RuleDto;
import si.uni.lj.fri.lg0775.services.lib.DtoMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

@RequestScoped
public class RuleBean {
    private static final Logger LOG = Logger.getLogger(RuleBean.class.getName());

    @PersistenceContext(name = "feature-flags")
    private EntityManager em;

    @Inject
    private ApplicationBean applicationBean;

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
            default:
                throw new RuntimeException("This type does not exist or is not supported yet");

        }
        return null;
    }

    private void createRuleForEveryone(CreateRuleDto crd, Application application, Flag flag) {
        // Get all users of that app
        List<EndUser> users = applicationBean.getUsersOfApp(application.getId());

        // Create array of rules for these users
        users.forEach(endUser -> {
            Rule rule = new Rule();
            rule.setApplication(application);
            rule.setEndUser(endUser);
            rule.setExpirationDate(Timestamp.from(crd.getExpirationDate()));
            rule.setValue(crd.getValue());
            rule.setFlag(flag);

            // Persist
            create(rule);
        });
    }

    private void createABTestingRule(CreateRuleDto crd, Application application, Flag flag) {
        // Get all users of that app
        List<EndUser> users = applicationBean.getUsersOfApp(application.getId());
        Random rand = new Random();

        users.forEach(endUser -> {
            Rule rule = new Rule();
            rule.setApplication(application);
            rule.setEndUser(endUser);
            rule.setExpirationDate(Timestamp.from(crd.getExpirationDate()));
            rule.setFlag(flag);

            int rand_int = rand.nextInt(100) + 1;
            if (rand_int < crd.getShareOfA()) {
                rule.setValue(crd.getValue());
            } else {
                rule.setValue(crd.getValueB());
            }

            // Persist
            create(rule);
        });
    }

    public List<RuleDto> getRule(Long user_id) {
        List<Rule> rules = em.createNamedQuery("Rule.getRuleForUser", Rule.class)
                .setParameter("clientId", user_id)
                .getResultList();

        if (rules.isEmpty()) {
            return null;
        }
        return DtoMapper.toRulesDto(rules);
    }
}
