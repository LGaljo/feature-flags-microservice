package si.uni.lj.fri.lg0775.services.bussines_beans;

import si.uni.lj.fri.lg0775.entities.db.Application;
import si.uni.lj.fri.lg0775.entities.enums.DataType;
import si.uni.lj.fri.lg0775.services.beans.*;
import si.uni.lj.fri.lg0775.services.dtos.FlagDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class SeedBean {
    @Inject
    private ApplicationBean applicationBean;

    @Inject
    private EndUserBean endUserBean;

    @Inject
    private FlagBean flagBean;

    @Inject
    private ScheduledRolloutBean scheduledRolloutBean;

    @Inject
    private RuleBean ruleBean;

    public void onInit() {
        // Create apps
        Application app1 = applicationBean.createApp("instapid");
        Application app2 = applicationBean.createApp("bikebook");
        Application demoApp = applicationBean.createApp("featureflagsdemo");

        // Create flags
        FlagDto flagDto1 = new FlagDto();
        flagDto1.setDataType(DataType.BOOL);
        flagDto1.setDefaultValue(0);
        flagDto1.setName("Flag1");
        flagDto1.setDescription("First flag");
        flagBean.createFlag(flagDto1, app1);

        FlagDto flagDto2 = new FlagDto();
        flagDto2.setDataType(DataType.BOOL);
        flagDto2.setDefaultValue(0);
        flagDto2.setName("Flag2");
        flagDto2.setDescription("Second flag");
        flagBean.createFlag(flagDto2, app1);

        FlagDto flagDto3 = new FlagDto();
        flagDto3.setDataType(DataType.BOOL);
        flagDto3.setDefaultValue(1);
        flagDto3.setName("Flag3");
        flagDto3.setDescription("Third flag");
        flagBean.createFlag(flagDto3, app1);

        FlagDto flagDto4 = new FlagDto();
        flagDto4.setDataType(DataType.BOOL);
        flagDto4.setDefaultValue(1);
        flagDto4.setName("Flag4");
        flagDto4.setDescription("Forth flag");
        flagBean.createFlag(flagDto4, app2);

        FlagDto flagDtoDemo1 = new FlagDto();
        flagDtoDemo1.setDataType(DataType.BOOL);
        flagDtoDemo1.setDefaultValue(0);
        flagDtoDemo1.setName("ui_type");
        flagDtoDemo1.setDescription("Controls which view is shown. Linear or grid type of list.");
        flagBean.createFlag(flagDtoDemo1, demoApp);

        FlagDto flagDtoDemo2 = new FlagDto();
        flagDtoDemo2.setDataType(DataType.BOOL);
        flagDtoDemo2.setDefaultValue(1);
        flagDtoDemo2.setName("plus_content");
        flagDtoDemo2.setDescription("Only specific users are allowed to view plus page.");
        flagBean.createFlag(flagDtoDemo2, demoApp);

        FlagDto flagDtoDemo3 = new FlagDto();
        flagDtoDemo3.setDataType(DataType.BOOL);
        flagDtoDemo3.setDefaultValue(0);
        flagDtoDemo3.setName("future_content");
        flagDtoDemo3.setDescription("Currently unused feature flag.");
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
        ruleBean.deleteAll();
        endUserBean.deleteAll();
        flagBean.deleteAll();
        applicationBean.deleteAll();
        scheduledRolloutBean.deleteAll();
    }
}
