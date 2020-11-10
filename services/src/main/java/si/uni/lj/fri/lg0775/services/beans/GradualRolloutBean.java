package si.uni.lj.fri.lg0775.services.beans;

import si.uni.lj.fri.lg0775.entities.db.GradualRollout;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class GradualRolloutBean {
    private static final Logger LOG = Logger.getLogger(GradualRolloutBean.class.getName());

    @PersistenceContext(name = "feature-flags")
    private EntityManager em;

    // Create
    @Transactional
    public void create(GradualRollout e) {
        em.persist(e);
        em.flush();
    }

    // Update
    @Transactional
    public void update(GradualRollout e) {
        em.merge(e);
        em.flush();
    }

    // delete
    public void delete(GradualRollout e) {
        em.remove(e);
        em.flush();
    }

    // Mark deleted
    public void markDeleted(GradualRollout e) {
        e.setDeleted(true);

        em.persist(e);
        em.flush();
    }

    @Transactional
    public void deleteAll() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<GradualRollout> delete = builder.createCriteriaDelete(GradualRollout.class);
        delete.from(GradualRollout.class);
        Query query1 = em.createQuery(delete);
        query1.executeUpdate();
    }

    // Find
    public GradualRollout find(Long id) {
        return em.find(GradualRollout.class, id);
    }

    // Contains
    public boolean contains(GradualRollout e) {
        return em.contains(e);
    }

    public List<GradualRollout> getUnfinishedRollouts() {
        return em.createNamedQuery("GradualRollout.getUnfinished", GradualRollout.class).getResultList();
    }

    public List<GradualRollout> getUnfinishedRolloutsForApp(Long appId) {
        return em.createNamedQuery("GradualRollout.getUnfinishedForApp", GradualRollout.class)
                .setParameter("appId", appId)
                .getResultList();
    }

    public GradualRollout getRollout(Long srId) {
        GradualRollout sr = find(srId);
        if (sr == null) {
            throw new NotFoundException("This object does not exist");
        }
        return sr;
    }
}
