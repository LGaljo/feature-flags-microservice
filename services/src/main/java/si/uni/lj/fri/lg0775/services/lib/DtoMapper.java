package si.uni.lj.fri.lg0775.services.lib;

import si.uni.lj.fri.lg0775.entities.db.*;
import si.uni.lj.fri.lg0775.services.dtos.models.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class DtoMapper {
    public static RuleDto toRuleDto(Rule r) {
        Flag f = r.getFlag();

        return new RuleDto(
                f.getId(),
                f.getName(),
                f.getDescription(),
                f.getDataType(),
                r.getValue(),
                r.getEndUser().getClient()
        );
    }

    public static List<RuleDto> toRulesDto(List<Rule> r) {
        return r.stream().map(DtoMapper::toRuleDto).collect(Collectors.toList());
    }

    public static Flag toFlag(FlagDto flagDto, Application application) {
        return new Flag(
                flagDto.getDefaultValue(),
                flagDto.getName(),
                flagDto.getDescription(),
                flagDto.getDataType(),
                application,
                Timestamp.from(flagDto.getExpirationDate())
        );
    }

    public static FlagDto toFlagDto(Flag f) {
        return new FlagDto(
                f.getApplication().getId(),
                f.getId(),
                f.getDefaultValue(),
                f.getName(),
                f.getDescription(),
                f.getDataType(),
                f.getExpirationDate().toInstant()
        );
    }

    public static List<FlagDto> toFlagsDto(List<Flag> f) {
        return f.stream().map(DtoMapper::toFlagDto).collect(Collectors.toList());
    }

    public static EndUserDto toEndUserDto(EndUser u) {
        return new EndUserDto(
                u.getId(),
                u.getClient()
        );
    }

    public static GradualRolloutDto toRolloutDto(GradualRollout sr) {
        GradualRolloutDto srd = new GradualRolloutDto();
        srd.setId(sr.getId());
        srd.setApplication(sr.getApplication());
        srd.setCompleted(sr.getCompleted());
        srd.setCreatedAt(sr.getCreatedAt().toInstant());
        srd.setFlag(toFlagDto(sr.getFlag()));
        srd.setInterval(sr.getInterval());
        srd.setNewValue(sr.getNewValue());
        srd.setNumOfSteps(sr.getNumOfSteps());
        srd.setUuid(sr.getUuid());
        srd.setTimeUnit(sr.getTimeUnit());

        return srd;
    }

    public static List<GradualRolloutDto> toRolloutDto(List<GradualRollout> lsr) {
        return lsr.stream().map(DtoMapper::toRolloutDto).collect(Collectors.toList());
    }

    public static AppDto toAppDto(Application application) {
        return new AppDto(
                application.getName(),
                application.getId()
        );
    }

    public static List<AppDto> toAppsDto(List<Application> apps) {
        return apps.stream().map(DtoMapper::toAppDto).collect(Collectors.toList());
    }
}
