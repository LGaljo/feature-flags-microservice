package si.uni.lj.fri.lg0775.services.beans;

import si.uni.lj.fri.lg0775.entities.db.Flag;
import si.uni.lj.fri.lg0775.entities.db.ScheduledRollout;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ScheduledRolloutBean {
    private static final Logger LOG = Logger.getLogger(ScheduledRolloutBean.class.getName());

    @PersistenceContext(name = "feature-flags")
    private EntityManager em;

    // Create
    @Transactional
    public void create(ScheduledRollout e) {
        em.persist(e);
        em.flush();
    }

    // Update
    public void update(ScheduledRollout e) {
        em.merge(e);
        em.flush();
    }

    // delete
    public void delete(ScheduledRollout e) {
        em.remove(e);
        em.flush();
    }

    // Mark deleted
    public void markDeleted(ScheduledRollout e) {
        e.setDeleted(true);

        em.persist(e);
        em.flush();
    }

    @Transactional
    public void deleteAll() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<ScheduledRollout> delete = builder.createCriteriaDelete(ScheduledRollout.class);
        delete.from(ScheduledRollout.class);
        Query query1 = em.createQuery(delete);
        query1.executeUpdate();
    }

    // Find
    public ScheduledRollout find(Long id) {
        return em.find(ScheduledRollout.class, id);
    }

    // Contains
    public boolean contains(Long id) {
        return em.contains(id);
    }

    public List<ScheduledRollout> getUnfinishedRolouts() {
        return em.createNamedQuery("ScheduledRollout.getUnfinished", ScheduledRollout.class).getResultList();
    }

}
