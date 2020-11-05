package si.uni.lj.fri.lg0775.services.dtos;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class CreateRolloutDto {
    private Long appId;
    private Long flagId;
    private Integer numberOfRollouts;
    private Integer newValue;
    private Long interval;
    private TimeUnit timeUnit;
    private Instant expirationDate;

    public CreateRolloutDto() {
    }

    public Integer getNumberOfRollouts() {
        return numberOfRollouts;
    }

    public void setNumberOfRollouts(Integer numberOfRollouts) {
        this.numberOfRollouts = numberOfRollouts;
    }

    public Integer getNewValue() {
        return newValue;
    }

    public void setNewValue(Integer newValue) {
        this.newValue = newValue;
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

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }
}
