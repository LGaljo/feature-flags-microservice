package si.uni.lj.fri.lg0775.services.dtos;

import java.util.List;

public class CreateFlagDto {
    private List<FlagDto> flags;
    private Long appId;

    public CreateFlagDto() {
    }

    public List<FlagDto> getFlags() {
        return flags;
    }

    public void setFlags(List<FlagDto> flags) {
        this.flags = flags;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
