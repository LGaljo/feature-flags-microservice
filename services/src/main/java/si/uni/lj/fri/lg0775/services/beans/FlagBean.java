package si.uni.lj.fri.lg0775.services.beans;

import si.uni.lj.fri.lg0775.entities.db.Application;
import si.uni.lj.fri.lg0775.entities.db.Flag;
import si.uni.lj.fri.lg0775.entities.enums.RuleType;
import si.uni.lj.fri.lg0775.services.dtos.CreateRuleDto;
import si.uni.lj.fri.lg0775.services.dtos.models.FlagDto;
import si.uni.lj.fri.lg0775.services.dtos.Share;
import si.uni.lj.fri.lg0775.services.lib.DtoMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class FlagBean {
    @PersistenceContext(name = "feature-flags")
    private EntityManager em;

    @Inject
    private ApplicationBean applicationBean;

    @Inject
    private RuleBean ruleBean;

    // Create
    @Transactional
    public void create(Flag e) {
        em.persist(e);
        em.flush();
    }

    // Update
    @Transactional
    public void update(Flag e) {
        em.merge(e);
        em.flush();
    }

    // delete
    public void delete(Flag e) {
        em.remove(e);
        em.flush();
    }

    // Mark deleted
    public void markDeleted(Flag e) {
        e.setDeleted(true);

        em.persist(e);
        em.flush();
    }

    @Transactional
    public void deleteAll() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<Flag> delete = builder.createCriteriaDelete(Flag.class);
        delete.from(Flag.class);
        Query query1 = em.createQuery(delete);
        query1.executeUpdate();
    }

    // Find
    public Flag find(Long id) {
        return em.find(Flag.class, id);
    }

    public Flag get(Long id) {
        Flag f = find(id);
        if (f.isDeleted()) {
            throw new NotFoundException();
        }
        return f;
    }

    // Contains
    public boolean contains(Long id) {
        return em.contains(id);
    }


    // Pridobi vse zastavice za aplikacijo, ki niso izbrisane
    public List<Flag> getFlagsForApp(Long appId) {
        applicationBean.find(appId);
        return em.createNamedQuery("Flag.getFlagsForApplication", Flag.class)
                .setParameter("applicationId", appId)
                .getResultList();
    }

    // Pridobi vse zastavice za aplikacijo, ki niso izbrisane
    public List<FlagDto> getFlagsDto(Long appId) {
        applicationBean.find(appId);

        return em.createNamedQuery("Flag.getFlagsForApplication", Flag.class)
                .setParameter("applicationId", appId)
                .getResultList()
                .stream()
                .map(DtoMapper::toFlagDto)
                .collect(Collectors.toList());
    }

    // Aplikaciji doda seznam zastavic
    @Transactional
    public void createFlags(List<FlagDto> flagList) {

        flagList.forEach(f -> {
            Application application = applicationBean.find(f.getAppId());
            createFlag(f, application);
        });
    }

    public void createFlag(FlagDto f) {
        Application application = applicationBean.find(f.getAppId());
        if (application == null) {
            throw new NotFoundException("Application not found");
        }
        createFlag(f, application);
    }

    // Ustvari zastavico, ki jo doda aplikaciji
    @Transactional
    public void createFlag(FlagDto f, Application application) {
        Flag flag = DtoMapper.toFlag(f, application);
        create(flag);

        CreateRuleDto crd = new CreateRuleDto();
        crd.setDataType(flag.getDataType());
        crd.setRuleType(RuleType.SAME_FOR_EVERYONE);
        List<Share> shares = new ArrayList<>();
        shares.add(new Share(flag.getDefaultValue(), 100));
        crd.setShares(shares);
        ruleBean.createRule(crd, application.getId(), flag.getId());
    }

    // Also remove rules
    @Transactional
    public void removeFlag(Long id) {
        ruleBean.removeRulesForFlag(id);
        markDeleted(find(id));
    }

    public void removeFlagsForApp(Long app_id) {
        List<Flag> flags = getFlagsForApp(app_id);
        flags.forEach(flag -> removeFlag(flag.getId()));
    }
}
