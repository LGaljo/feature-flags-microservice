package si.uni.lj.fri.lg0775.services.beans;

import si.uni.lj.fri.lg0775.entities.db.Application;
import si.uni.lj.fri.lg0775.entities.db.EndUser;
import si.uni.lj.fri.lg0775.entities.db.Flag;
import si.uni.lj.fri.lg0775.entities.db.Rule;
import si.uni.lj.fri.lg0775.services.dtos.models.EndUserDto;
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
import javax.ws.rs.NotFoundException;
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

    @Inject
    private FlagBean flagBean;

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

    @Transactional
    public void deleteAll() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<EndUser> delete = builder.createCriteriaDelete(EndUser.class);
        delete.from(EndUser.class);
        Query query1 = em.createQuery(delete);
        query1.executeUpdate();
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
    public EndUser heartbeat(String clientId, String appName) {
        try {
            return em.createNamedQuery("EndUser.getByClientID", EndUser.class)
                    .setParameter("clientId", clientId).getSingleResult();
        } catch (NoResultException nre) {
            return saveEndUser(clientId, appName);
        }
    }

    // Pridobi vse uporabnike te aplikacije in vrni DTO
    public List<EndUserDto> getUsers(Long appId) {
        if (em.find(Application.class, appId) == null) {
            throw new NotFoundException("Application not found");
        }
        return getUsersOfApp(appId)
                .stream()
                .map(DtoMapper::toEndUserDto)
                .collect(Collectors.toList());
    }

    // Pridobi vse uporabnike te aplikacije
    public List<EndUser> getUsersOfApp(Long appId) {
        return em.createNamedQuery("EndUser.getEndUsersByAppID", EndUser.class)
                .setParameter("applicationId", appId)
                .getResultList();
    }

    public List<Rule> getUsersIfAppWONewValue(Long appId, Long flagId, int newValue) {
        return em.createNamedQuery("EndUser.getUsersIfAppWONewValue", Rule.class)
                .setParameter("applicationId", appId)
                .setParameter("flagId", flagId)
                .setParameter("newValue", newValue)
                .getResultList();
    }

    @Transactional
    public EndUser saveEndUser(String clientId, Application application) {
        EndUser endUser = new EndUser();
        if (clientId != null) {
            endUser.setApplication(application);
            endUser.setClient(clientId);
            create(endUser);
        }

        List<Flag> flags = flagBean.getFlagsForApp(application.getId());
        flags.forEach(f -> {
            Rule rule = new Rule();
            rule.setApplication(application);
            rule.setEndUser(endUser);
            rule.setFlag(f);
            rule.setValue(f.getDefaultValue());
            ruleBean.create(rule);
        });

        return endUser;
    }

    public EndUser saveEndUser(String clientId, String appName) {
        Application application = applicationBean.getApplicationByName(appName);

        if (application == null) {
            throw new NotFoundException("Application not found");
        }

        return saveEndUser(clientId, application);
    }

    @Transactional
    public void removeUser(Long id) {
        List<Rule> rules = ruleBean.getRulesForUserID(id);
        rules.forEach(rule -> ruleBean.markDeleted(rule));
        markDeleted(find(id));
    }
}
