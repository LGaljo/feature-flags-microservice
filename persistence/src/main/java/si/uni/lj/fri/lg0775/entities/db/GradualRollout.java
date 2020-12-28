package si.uni.lj.fri.lg0775.entities.db;

import si.uni.lj.fri.lg0775.entities.db.base.BaseEntity;
import si.uni.lj.fri.lg0775.entities.listeners.BaseEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "gradualrollout")
@EntityListeners(BaseEntityListener.class)
@NamedQueries({
        @NamedQuery(
                name = "GradualRollout.getUnfinished",
                query = "SELECT sr FROM GradualRollout sr" +
                        " WHERE sr.deleted = false" +
                        " AND sr.completed < 100"),
        @NamedQuery(
                name = "GradualRollout.getUnfinishedForApp",
                query = "SELECT sr FROM GradualRollout sr" +
                        " WHERE sr.deleted = false" +
                        " AND sr.application.id = :appId" +
                        " AND sr.completed < 100")
})
public class GradualRollout extends BaseEntity implements Serializable {
    @NotNull
    private String uuid;

    @NotNull
    private Integer newValue;

    @NotNull
    private Integer numOfSteps;

    @NotNull
    private Integer interval;

    @NotNull
    private TimeUnit timeUnit;

    @NotNull
    private Integer completed;

    @ManyToOne
    private Flag flag;

    @ManyToOne
    private Application application;

    public GradualRollout() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public void setNumOfSteps(Integer numOfRollouts) {
        this.numOfSteps = numOfRollouts;
    }

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completedRollouts) {
        this.completed = completedRollouts;
    }

    public Flag getFlag() {
        return flag;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }
}
