package si.uni.lj.fri.lg0775.services.beans;

import si.uni.lj.fri.lg0775.entities.db.Application;
import si.uni.lj.fri.lg0775.entities.db.EndUser;
import si.uni.lj.fri.lg0775.entities.db.Flag;
import si.uni.lj.fri.lg0775.entities.db.Rule;
import si.uni.lj.fri.lg0775.services.dtos.RuleDto;
import si.uni.lj.fri.lg0775.services.lib.DtoMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EndUserBean {

    @PersistenceContext(name = "feature-flags")
    private EntityManager em;

    @Inject
    private ApplicationBean applicationBean;

    @Inject
    private RuleBean ruleBean;

    // Create
    public void create(EndUser e) {
        em.persist(e);
        em.flush();
    }

    // Update
    public void update(EndUser e) {
        em.merge(e);
        em.flush();
    }

    // delete
    public void delete(EndUser e) {
        em.remove(e);
        em.flush();
    }

    // Mark deleted
    public void markDeleted(EndUser e) {
        e.setDeleted(true);

        em.persist(e);
        em.flush();
    }

    // Find
    public EndUser find(Long id) {
        return em.find(EndUser.class, id);
    }

    // Contains
    public boolean contains(Long id) {
        return em.contains(id);
    }

    public EndUser getEndUser(Long id) {
        return em.find(EndUser.class, id);
    }

    @Transactional
    public EndUser heartbeat(String clientId, Long appId) {
        try {
            return em.createNamedQuery("EndUser.getByClientID", EndUser.class)
                    .setParameter("clientId", clientId).getSingleResult();
        } catch (NoResultException nre) {
            return saveEndUser(clientId, appId);
        }
    }

    @Transactional
    public EndUser saveEndUser(String clientId, Long appId) {
        EndUser endUser = new EndUser();
        Application application = applicationBean.getApplication(appId);
        if (clientId != null && application != null) {
            endUser.setApplication(application);
            endUser.setClient(clientId);
            create(endUser);
        }
        List<Flag> flags = applicationBean.getFlags(appId);
        flags.forEach(f -> {
            Rule rule = new Rule();
            rule.setApplication(application);
            rule.setEndUser(endUser);
            rule.setFlag(f);
            rule.setExpirationDate(Timestamp.from(Instant.now().plus(30, ChronoUnit.DAYS)));
            rule.setValue(f.getDefaultValue());
            ruleBean.create(rule);
        });
        return endUser;
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

    public List<RuleDto> getRule(Long user_id) {
        List<Rule> rules = em.createNamedQuery("Rule.getRuleForUserById", Rule.class)
                .setParameter("clientId", user_id)
                .getResultList();

        if (rules.isEmpty()) {
            return null;
        }
        return DtoMapper.toRulesDto(rules);
    }
}
