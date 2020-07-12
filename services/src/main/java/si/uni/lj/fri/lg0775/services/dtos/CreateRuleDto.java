package si.uni.lj.fri.lg0775.services.dtos;

import si.uni.lj.fri.lg0775.entities.enums.DataType;
import si.uni.lj.fri.lg0775.entities.enums.RuleType;

import java.time.Instant;

public class CreateRuleDto {
    private Long flagId;
    private RuleType ruleType;
    private DataType dataType;
    private Instant expirationDate;
    private int value;
    private int valueB;
    private String name;
    private String description;
    private int shareOfA;

    public CreateRuleDto() {
    }

    public CreateRuleDto(Long flagId, RuleType ruleType, DataType dataType, Instant expirationDate,
                         int value, int valueB, String name, String description, int shareOfA) {
        this.flagId = flagId;
        this.ruleType = ruleType;
        this.dataType = dataType;
        this.expirationDate = expirationDate;
        this.value = value;
        this.valueB = valueB;
        this.name = name;
        this.description = description;
        this.shareOfA = shareOfA;
    }

    public Long getFlagId() {
        return flagId;
    }

    public void setFlagId(Long flagId) {
        this.flagId = flagId;
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    public void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValueB() {
        return valueB;
    }

    public void setValueB(int valueB) {
        this.valueB = valueB;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getShareOfA() {
        return shareOfA;
    }

    public void setShareOfA(int shareOfA) {
        this.shareOfA = shareOfA;
    }

    @Override
    public String toString() {
        return "CreateRuleDto{" +
                "flagId=" + flagId +
                ", ruleType=" + ruleType +
                ", dataType=" + dataType +
                ", expirationDate=" + expirationDate +
                ", value=" + value +
                ", valueB=" + valueB +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", shareOfA=" + shareOfA +
                '}';
    }
}
