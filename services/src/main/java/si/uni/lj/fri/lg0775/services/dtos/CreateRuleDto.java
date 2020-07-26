package si.uni.lj.fri.lg0775.services.dtos;

import si.uni.lj.fri.lg0775.entities.enums.DataType;
import si.uni.lj.fri.lg0775.entities.enums.RuleType;

import java.time.Instant;

public class CreateRuleDto {
    private long flagId;
    private RuleType ruleType;
    private DataType dataType;
    private Instant expirationDate;
    private int value;
    private int valueB;
    private int shareOfA;
    private Long user;

    public CreateRuleDto() {
    }

    public CreateRuleDto(long flagId, RuleType ruleType, DataType dataType, Instant expirationDate,
                         int value, int valueB, String description, int shareOfA) {
        this.flagId = flagId;
        this.ruleType = ruleType;
        this.dataType = dataType;
        this.expirationDate = expirationDate;
        this.value = value;
        this.valueB = valueB;
        this.shareOfA = shareOfA;
    }

    public long getFlagId() {
        return flagId;
    }

    public void setFlagId(long flagId) {
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

    public int getShareOfA() {
        return shareOfA;
    }

    public void setShareOfA(int shareOfA) {
        this.shareOfA = shareOfA;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long users) {
        this.user = users;
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
                ", shareOfA=" + shareOfA +
                ", user=" + user +
                '}';
    }
}
