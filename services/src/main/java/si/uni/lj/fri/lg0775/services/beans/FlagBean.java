package si.uni.lj.fri.lg0775.services.beans;

import si.uni.lj.fri.lg0775.entities.enums.DataType;
import si.uni.lj.fri.lg0775.entities.db.Application;
import si.uni.lj.fri.lg0775.entities.db.Flag;
import si.uni.lj.fri.lg0775.services.dtos.FlagDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class FlagBean {
    @PersistenceContext(name="feature-flags")
    private EntityManager em;

    @Inject
    private ApplicationBean applicationBean;

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
            switch (f.getDataType()) {
                case BOOL:
                    Flag flag = new Flag();
                    flag.setDataType(DataType.BOOL);
                    flag.setApplication(application);
                    flag.setDescription(f.getDescription());
                    flag.setName(f.getName());
                    flag.setDefaultValue(f.getDefaultValue());

                    em.persist(flag);
                    break;
                case INT:
                    Flag flag2 = new Flag();
                    flag2.setDataType(DataType.INT);
                    flag2.setApplication(application);
                    flag2.setDescription(f.getDescription());
                    flag2.setName(f.getName());
                    flag2.setDefaultValue(f.getDefaultValue());

                    em.persist(flag2);
                    break;
            }
        });
    }

    // Ustvari zastavico, ki jo doda aplikaciji
    @Transactional
    public void createFlag(FlagDto f, Long appId) {
        Application application = applicationBean.getApplication(appId);
        Flag flag = new Flag();

        flag.setApplication(application);
        flag.setDataType(f.getDataType());
        flag.setDefaultValue(f.getDefaultValue());
        flag.setDescription(f.getDescription());
        flag.setName(f.getName());

        em.persist(flag);
    }
}
