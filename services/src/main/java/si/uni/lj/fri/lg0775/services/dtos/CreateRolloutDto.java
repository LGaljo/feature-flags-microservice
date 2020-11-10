package si.uni.lj.fri.lg0775.services.dtos;

import java.util.concurrent.TimeUnit;

public class CreateRolloutDto {
    private Long appId;
    private Long flagId;
    private Integer interval;
    private Integer newValue;
    private Integer numOfSteps;
    private TimeUnit timeUnit;

    public CreateRolloutDto() {
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getFlagId() {
        return flagId;
    }

    public void setFlagId(Long flagId) {
        this.flagId = flagId;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Integer getNewValue() {
        return newValue;
    }

    public void setNewValue(Integer newValue) {
        this.newValue = newValue;
    }

    public Integer getNumOfSteps() {
        return numOfSteps;
    }

    public void setNumOfSteps(Integer numOfSteps) {
        this.numOfSteps = numOfSteps;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }
}
