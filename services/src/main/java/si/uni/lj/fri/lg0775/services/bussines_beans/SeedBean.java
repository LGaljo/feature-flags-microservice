package si.uni.lj.fri.lg0775.services.bussines_beans;

import si.uni.lj.fri.lg0775.entities.db.Application;
import si.uni.lj.fri.lg0775.entities.enums.DataType;
import si.uni.lj.fri.lg0775.services.beans.*;
import si.uni.lj.fri.lg0775.services.dtos.models.FlagDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.logging.Logger;

@ApplicationScoped
public class SeedBean {
    private final Logger LOG = Logger.getLogger(SeedBean.class.getName());

    @Inject
    private ApplicationBean applicationBean;

    @Inject
    private EndUserBean endUserBean;

    @Inject
    private FlagBean flagBean;

    @Inject
    private GradualRolloutBean gradualRolloutBean;

    @Inject
    private RuleBean ruleBean;

    public void onInit() {
        // Create apps
        Application app1 = applicationBean.createApp("instapid");
        Application app2 = applicationBean.createApp("bikebook");
        Application demoApp = applicationBean.createApp("featureflagsdemo");

        Instant expirationDate = Instant.now().plus(60, ChronoUnit.DAYS);

        // Create flags
        FlagDto flagDto1 = new FlagDto();
        flagDto1.setAppId(app1.getId());
        flagDto1.setDataType(DataType.BOOL);
        flagDto1.setDefaultValue(0);
        flagDto1.setName("Flag1");
        flagDto1.setDescription("First flag");
        flagDto1.setExpirationDate(expirationDate);
        flagBean.createFlag(flagDto1, app1);

        FlagDto flagDto2 = new FlagDto();
        flagDto2.setAppId(app1.getId());
        flagDto2.setDataType(DataType.BOOL);
        flagDto2.setDefaultValue(0);
        flagDto2.setName("Flag2");
        flagDto2.setDescription("Second flag");
        flagDto2.setExpirationDate(expirationDate);
        flagBean.createFlag(flagDto2, app1);

        FlagDto flagDto3 = new FlagDto();
        flagDto3.setAppId(app1.getId());
        flagDto3.setDataType(DataType.INT);
        flagDto3.setDefaultValue(1);
        flagDto3.setName("Flag3");
        flagDto3.setDescription("Third flag");
        flagDto3.setExpirationDate(expirationDate);
        flagBean.createFlag(flagDto3, app1);

        FlagDto flagDto4 = new FlagDto();
        flagDto4.setAppId(app2.getId());
        flagDto4.setDataType(DataType.BOOL);
        flagDto4.setDefaultValue(1);
        flagDto4.setName("Flag4");
        flagDto4.setDescription("Forth flag");
        flagDto4.setExpirationDate(expirationDate);
        flagBean.createFlag(flagDto4, app2);

        FlagDto flagDtoDemo1 = new FlagDto();
        flagDtoDemo1.setAppId(demoApp.getId());
        flagDtoDemo1.setDataType(DataType.BOOL);
        flagDtoDemo1.setDefaultValue(0);
        flagDtoDemo1.setName("ui_type");
        flagDtoDemo1.setDescription("Controls which view is shown. Linear or grid type of list.");
        flagDtoDemo1.setExpirationDate(expirationDate);
        flagBean.createFlag(flagDtoDemo1, demoApp);

        FlagDto flagDtoDemo2 = new FlagDto();
        flagDtoDemo2.setAppId(demoApp.getId());
        flagDtoDemo2.setDataType(DataType.BOOL);
        flagDtoDemo2.setDefaultValue(1);
        flagDtoDemo2.setName("plus_content");
        flagDtoDemo2.setDescription("Only specific users are allowed to view plus page.");
        flagDtoDemo2.setExpirationDate(expirationDate);
        flagBean.createFlag(flagDtoDemo2, demoApp);

        FlagDto flagDtoDemo3 = new FlagDto();
        flagDtoDemo3.setAppId(demoApp.getId());
        flagDtoDemo3.setDataType(DataType.BOOL);
        flagDtoDemo3.setDefaultValue(0);
        flagDtoDemo3.setName("future_content");
        flagDtoDemo3.setDescription("Currently unused feature flag.");
        flagDtoDemo3.setExpirationDate(expirationDate);
        flagBean.createFlag(flagDtoDemo3, demoApp);

        // Create EndUsers
        for (int i = 0; i < 100; i++) {
            endUserBean.saveEndUser(UUID.randomUUID().toString(), app1);
        }
        for (int i = 0; i < 100; i++) {
            endUserBean.saveEndUser(UUID.randomUUID().toString(), app2);
        }
        for (int i = 0; i < 10; i++) {
            endUserBean.saveEndUser(UUID.randomUUID().toString(), demoApp);
        }
    }

    public void clearDatabase() {
        LOG.info("Clear database");
        ruleBean.deleteAll();
        gradualRolloutBean.deleteAll();
        endUserBean.deleteAll();
        flagBean.deleteAll();
        applicationBean.deleteAll();
    }
}
