package si.uni.lj.fri.lg0775.services.beans;

import si.uni.lj.fri.lg0775.entities.enums.DataType;
import si.uni.lj.fri.lg0775.entities.db.Application;
import si.uni.lj.fri.lg0775.entities.db.Flag;
import si.uni.lj.fri.lg0775.entities.enums.RuleType;
import si.uni.lj.fri.lg0775.services.dtos.CreateRuleDto;
import si.uni.lj.fri.lg0775.services.dtos.FlagDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.List;

@ApplicationScoped
public class FlagBean {
    @PersistenceContext(name="feature-flags")
    private EntityManager em;

    @Inject
    private ApplicationBean applicationBean;

    @Inject
    private RuleBean ruleBean;

    // Create
    public void create(Flag e) {
        em.persist(e);
        em.flush();
    }

    // Update
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

    // Find
    public Flag find(Long id) {
        return em.find(Flag.class, id);
    }

    // Contains
    public boolean contains(Long id) {
        return em.contains(id);
    }

    // Aplikaciji doda seznam zastavic
    @Transactional
    public void createFlags(List<FlagDto> flagList, Long appId) {
        Application application = applicationBean.getApplication(appId);
        if (application == null) {
            throw new NotFoundException("Application not found");
        }

        flagList.forEach(f -> {
            createFlag(f, application);
        });
    }

    public void createFlag(FlagDto f, Long appId) {
        Application application = applicationBean.getApplication(appId);
        if (application == null) {
            throw new NotFoundException("Application not found");
        }
        createFlag(f, application);
    }

    // Ustvari zastavico, ki jo doda aplikaciji
    @Transactional
    public void createFlag(FlagDto f, Application application) {
        Flag flag = new Flag();
        switch (f.getDataType()) {
            case BOOL:
                flag.setDataType(DataType.BOOL);
                flag.setApplication(application);
                flag.setDescription(f.getDescription());
                flag.setName(f.getName());
                flag.setDefaultValue(f.getDefaultValue());
                break;
            case INT:
                flag.setDataType(DataType.INT);
                flag.setApplication(application);
                flag.setDescription(f.getDescription());
                flag.setName(f.getName());
                flag.setDefaultValue(f.getDefaultValue());
                break;
        }
        create(flag);

        CreateRuleDto crd = new CreateRuleDto();
        crd.setDataType(flag.getDataType());
        crd.setExpirationDate(Instant.now().plus(45, ChronoUnit.DAYS));
        crd.setRuleType(RuleType.SAME_FOR_EVERYONE);
        crd.setValue(flag.getDefaultValue());
        ruleBean.createRule(crd, application.getId(), flag.getId());
    }
}
