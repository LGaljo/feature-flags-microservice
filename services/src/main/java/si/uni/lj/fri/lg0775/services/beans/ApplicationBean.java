package si.uni.lj.fri.lg0775.services.beans;

import si.uni.lj.fri.lg0775.entities.db.Application;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ApplicationBean {
    private static final Logger LOG = Logger.getLogger(ApplicationBean.class.getName());

    @PersistenceContext(name = "feature-flags")
    private EntityManager em;

    @Inject
    private FlagBean flagBean;

    // Create
    public void create(Application e) {
        em.persist(e);
        em.flush();
    }

    // Update
    public void update(Application e) {
        em.merge(e);
        em.flush();
    }

    // delete
    public void delete(Application e) {
        em.remove(e);
        em.flush();
    }

    @Transactional
    public void deleteAll() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<Application> delete = builder.createCriteriaDelete(Application.class);
        delete.from(Application.class);
        Query query1 = em.createQuery(delete);
        query1.executeUpdate();
    }

    // Mark deleted
    public void markDeleted(Application e) {
        e.setDeleted(true);

        em.persist(e);
        em.flush();
    }

    // Find
    public Application find(Long id) {
        return em.find(Application.class, id);
    }

    // Contains
    public boolean contains(Long id) {
        return em.contains(id);
    }

    // Pridobi vse aplikacije
    @Transactional
    public List<Application> getAll() {
        return em.createNamedQuery("Application.findAllExceptDeleted", Application.class).getResultList();
    }

    // Ustvari novo aplikacijo, ki na začetku vsebuje prazen seznam zastavic
    @Transactional
    public Application createApp(String appName) {
        Application application = new Application();
        if (appName != null && !appName.isBlank()) {
            application.setName(appName);
//            application.setFlags(new ArrayList<>());
            create(application);
        }
        return application;
    }

    @Transactional
    public Application getApplicationByName(String appName) {
        return em.createNamedQuery("Application.getAppByName", Application.class)
                .setParameter("applicationName", appName)
                .getSingleResult();
    }

    @Transactional
    public void removeApp(Long id) {
        markDeleted(find(id));
        flagBean.removeFlagsForApp(id);
    }
}
