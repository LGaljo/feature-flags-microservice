package si.uni.lj.fri.lg0775.services.beans;

import si.uni.lj.fri.lg0775.entities.db.Application;
import si.uni.lj.fri.lg0775.entities.db.EndUser;
import si.uni.lj.fri.lg0775.entities.db.Flag;
import si.uni.lj.fri.lg0775.entities.db.Rule;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class EndUserBean extends BaseBean {

    @PersistenceContext(name="feature-flags")
    private EntityManager em;

    @Inject
    private ApplicationBean applicationBean;

    public EndUser getEndUser(String id) {
        return em.find(EndUser.class, id);
    }

    @Transactional
    public EndUser saveEndUser(EndUser endUser) {
        if (endUser != null) {
            em.persist(endUser);
            List<Flag> flags = applicationBean.getFlags(endUser.getApplication().getId().toString());

        }
        return endUser;
    }

    public List<Flag<Object>> getFlagsForApp(String appId) {
        List<Rule> rules = em.createNamedQuery("EndUser.getRulesForApp", Rule.class)
                .setParameter("clientId", appId)
                .setParameter("applicationId", appId)
                .getResultList();

        return rules.stream().map(r -> {
            if (!r.hasExpired() && !r.isDeleted()) {
                return r.getFlag();
            }
            return null;
        }).collect(Collectors.toList());
    }
}
