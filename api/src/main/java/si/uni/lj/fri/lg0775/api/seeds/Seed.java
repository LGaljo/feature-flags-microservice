package si.uni.lj.fri.lg0775.api.seeds;

import si.uni.lj.fri.lg0775.entities.enums.DataType;
import si.uni.lj.fri.lg0775.entities.db.Application;
import si.uni.lj.fri.lg0775.services.beans.ApplicationBean;
import si.uni.lj.fri.lg0775.services.beans.EndUserBean;
import si.uni.lj.fri.lg0775.services.beans.FlagBean;
import si.uni.lj.fri.lg0775.services.dtos.FlagDto;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class Seed {
    @Inject
    private ApplicationBean applicationBean;

    @Inject
    private EndUserBean endUserBean;

    @Inject
    private FlagBean flagBean;

    public void onInit(@Observes @Initialized(ApplicationScoped.class) Object init) {
        // Create apps
        Application app1 = applicationBean.createApp("instapid");
        Application app2 = applicationBean.createApp("bikebook");

        // Create flags
        FlagDto flagDto1 = new FlagDto();
        flagDto1.setDataType(DataType.INT);
        flagDto1.setDefaultValue(0);
        flagDto1.setName("Flag1");
        flagDto1.setDescription("First flag");
        flagBean.createFlag(flagDto1, app1.getId());

        FlagDto flagDto2 = new FlagDto();
        flagDto2.setDataType(DataType.INT);
        flagDto2.setDefaultValue(0);
        flagDto2.setName("Flag2");
        flagDto2.setDescription("Second flag");
        flagBean.createFlag(flagDto2, app1.getId());

        FlagDto flagDto3 = new FlagDto();
        flagDto3.setDataType(DataType.INT);
        flagDto3.setDefaultValue(0);
        flagDto3.setName("Flag3");
        flagDto3.setDescription("Third flag");
        flagBean.createFlag(flagDto3, app1.getId());

        FlagDto flagDto4 = new FlagDto();
        flagDto4.setDataType(DataType.BOOL);
        flagDto4.setDefaultValue(1);
        flagDto4.setName("Flag4");
        flagDto4.setDescription("Forth flag");
        flagBean.createFlag(flagDto4, app2.getId());

        // Create EndUser
        endUserBean.saveEndUser("ccd61ffc-7ca0-11ea-bc55-0242ac130003", app1.getId());
        endUserBean.saveEndUser("d6baaedc-b35b-42d5-92dd-5b1bd6ead4d3", app1.getId());
        endUserBean.saveEndUser("1e4c4c61-2a22-49de-aa50-026e47bb3a6c", app2.getId());
    }
}
