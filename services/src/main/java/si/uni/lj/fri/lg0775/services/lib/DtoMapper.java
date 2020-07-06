package si.uni.lj.fri.lg0775.services.lib;

import si.uni.lj.fri.lg0775.entities.db.EndUser;
import si.uni.lj.fri.lg0775.entities.db.Flag;
import si.uni.lj.fri.lg0775.entities.db.Rule;
import si.uni.lj.fri.lg0775.services.dtos.EndUserDto;
import si.uni.lj.fri.lg0775.services.dtos.FlagDto;
import si.uni.lj.fri.lg0775.services.dtos.RuleDto;

public class DtoMapper {
    public static RuleDto toRuleDto(Rule r) {
        Flag f = r.getFlag();

        return new RuleDto(
                f.getId(),
                f.getName(),
                f.getDescription(),
                f.getDataType(),
                r.getValue()
        );
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
}
