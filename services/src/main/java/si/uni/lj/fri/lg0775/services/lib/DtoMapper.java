package si.uni.lj.fri.lg0775.services.lib;

import si.uni.lj.fri.lg0775.entities.db.EndUser;
import si.uni.lj.fri.lg0775.entities.db.Flag;
import si.uni.lj.fri.lg0775.entities.db.Rule;
import si.uni.lj.fri.lg0775.entities.db.GradualRollout;
import si.uni.lj.fri.lg0775.services.dtos.EndUserDto;
import si.uni.lj.fri.lg0775.services.dtos.FlagDto;
import si.uni.lj.fri.lg0775.services.dtos.RuleDto;
import si.uni.lj.fri.lg0775.services.dtos.GradualRolloutDto;

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

    public static FlagDto toFlagDto(Flag f) {
        return new FlagDto(
                f.getId(),
                f.getDefaultValue(),
                f.getName(),
                f.getDescription(),
                f.getDataType()
        );
    }

    public static EndUserDto toEndUserDto(EndUser u) {
        return new EndUserDto(
                u.getId(),
                u.getClient(),
                u.getCreatedAt()
        );
    }

    public static GradualRolloutDto toRolloutDto(GradualRollout sr) {
        GradualRolloutDto srd = new GradualRolloutDto();
        srd.setId(sr.getId());
        srd.setApplication(sr.getApplication());
        srd.setCompleted(sr.getCompleted());
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
}
