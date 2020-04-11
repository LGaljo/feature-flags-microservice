package si.uni.lj.fri.lg0775.services.beans;

import javax.enterprise.context.RequestScoped;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotFoundException;

import si.uni.lj.fri.lg0775.entities.DataType;
import si.uni.lj.fri.lg0775.entities.db.Application;
import si.uni.lj.fri.lg0775.entities.db.Flag;
import si.uni.lj.fri.lg0775.services.dtos.FlagDto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestScoped
public class ApplicationBean extends BaseBean {

    @PersistenceContext(name="feature-flags")
    private EntityManager em;

    public Application getApplication(String id) {
        return em.find(Application.class, id);
    }

    public Application getApplication(UUID id) {
        return em.find(Application.class, id.toString());
    }

    public List<Flag> getFlags(String appId) {
        return em.createNamedQuery("Application.getFlagsForApplication", Flag.class)
                .setParameter("applicationId", appId)
                .getResultList();
    }

    @Transactional
    public void createApp(String appName) {
        if (appName != null && !appName.isBlank()) {
            Application application = new Application();
            application.setName(appName);
            application.setId(UUID.randomUUID());
            application.setUpdatedAt(Instant.now());
            application.setCreatedAt(Instant.now());
            application.setDeleted(false);
            application.setFlags(new ArrayList<>());
            em.persist(application);
        }
    }

    public void createFlags(List<FlagDto> flagList, String appId) {
        Application application = getApplication(appId);
        if (application == null) {
            throw new NotFoundException("Application not found");
        }

        flagList.forEach(f -> {
            switch (f.getDataType()) {
                case BOOL:
                    boolean defaultValue;
                    String dv = f.getDefaultValue().toLowerCase();

                    if (dv.equals("true")) {
                        defaultValue = true;
                    } else if (dv.equals("false")) {
                        defaultValue = false;
                    } else {
                        throw new NotAcceptableException("Default value not defined correctly");
                    }

                    Flag<Boolean> flag = new Flag<>();

                    flag.setDataType(DataType.BOOL);
                    flag.setApplication(application);
                    flag.setDescription(f.getDescription());
                    flag.setName(f.getName());
                    flag.setDefaultValue(defaultValue);
                    flag.setValue(defaultValue);

                    em.persist(flag);
                    break;
                case INT:
                    try {
                        int defaultValue2 = Integer.parseInt(f.getDefaultValue());
                        Flag<Integer> flag2 = new Flag<>();

                        flag2.setDataType(DataType.BOOL);
                        flag2.setApplication(application);
                        flag2.setDescription(f.getDescription());
                        flag2.setName(f.getName());
                        flag2.setDefaultValue(defaultValue2);
                        flag2.setValue(defaultValue2);

                        em.persist(flag2);

                    } catch (NumberFormatException nfe) {
                        throw new NotAcceptableException("Default value not a number");
                    }
                    break;
            }
        });
    }
}
