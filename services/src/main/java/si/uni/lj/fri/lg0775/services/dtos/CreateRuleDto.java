package si.uni.lj.fri.lg0775.services.dtos;

import si.uni.lj.fri.lg0775.entities.enums.DataType;
import si.uni.lj.fri.lg0775.entities.enums.RuleType;

import java.time.Instant;
import java.util.List;

public class CreateRuleDto {
    private RuleType ruleType;
    private DataType dataType;
    private List<Share> shares;
    private Long user;

    public CreateRuleDto() {
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

    public Long getUser() {
        return user;
    }

    public void setUser(Long users) {
        this.user = users;
    }

    public List<Share> getShares() {
        return shares;
    }

    public void setShares(List<Share> shares) {
        this.shares = shares;
    }
}
