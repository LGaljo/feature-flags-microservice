package si.uni.lj.fri.lg0775.services.dtos;

import si.uni.lj.fri.lg0775.entities.db.Application;
import si.uni.lj.fri.lg0775.entities.db.Flag;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class GradualRolloutDto {
    private Long id;
    private Application application;
    private Integer completed;
    private FlagDto flag;
    private Integer interval;
    private Integer newValue;
    private Integer numOfSteps;
    private TimeUnit timeUnit;
    private String uuid;

    public GradualRolloutDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed;
    }

    public FlagDto getFlag() {
        return flag;
    }

    public void setFlag(FlagDto flag) {
        this.flag = flag;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
