package si.uni.lj.fri.lg0775.services.beans;

import si.uni.lj.fri.lg0775.entities.db.Application;
import si.uni.lj.fri.lg0775.entities.db.EndUser;
import si.uni.lj.fri.lg0775.entities.db.Flag;
import si.uni.lj.fri.lg0775.services.dtos.EndUserDto;
import si.uni.lj.fri.lg0775.services.dtos.FlagDto;
import si.uni.lj.fri.lg0775.services.lib.DtoMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
public class ApplicationBean {
    private static final Logger LOG = Logger.getLogger(ApplicationBean.class.getName());

    @PersistenceContext(name = "feature-flags")
    private EntityManager em;

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

    // Pridobi objekt aplikacije
    public Application getApplication(Long id) {
        Application application;
        if ((application = em.find(Application.class, id)) == null) {
            throw new NotFoundException("Application not found");
        }
        return application;
    }

    // Pridobi vse aplikacije
    public List<Application> getAll() {
        return em.createNamedQuery("Application.findAllExceptDeleted", Application.class).getResultList();
    }

    // Pridobi vse zastavice za aplikacijo, ki niso izbrisane
    public List<Flag> getFlags(Long appId) {
        getApplication(appId);
        return em.createNamedQuery("Flag.getFlagsForApplication", Flag.class)
                .setParameter("applicationId", appId)
                .getResultList();
    }

    // Pridobi vse zastavice za aplikacijo, ki niso izbrisane
    public List<FlagDto> getFlagsDto(Long appId) {
        getApplication(appId);

        return em.createNamedQuery("Flag.getFlagsForApplication", Flag.class)
                .setParameter("applicationId", appId)
                .getResultList()
                .stream()
                .map(DtoMapper::toFlagDto)
                .collect(Collectors.toList());
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

    // Ustvari novo aplikacijo, ki na začetku vsebuje prazen seznam zastavic
    @Transactional
    public Application createApp(String appName) {
        Application application = new Application();
        if (appName != null && !appName.isBlank()) {
            application.setName(appName);
            application.setFlags(new ArrayList<>());
            create(application);
        }
        return application;
    }
}
