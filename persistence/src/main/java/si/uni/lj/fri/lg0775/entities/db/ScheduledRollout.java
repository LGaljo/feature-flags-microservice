package si.uni.lj.fri.lg0775.entities.db;

import si.uni.lj.fri.lg0775.entities.db.base.BaseEntity;
import si.uni.lj.fri.lg0775.entities.listeners.BaseEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "scheduledrollout")
@EntityListeners(BaseEntityListener.class)
@NamedQueries({
        @NamedQuery(
                name = "ScheduledRollout.getUnfinished",
                query = "SELECT sr FROM ScheduledRollout sr" +
                        " WHERE sr.deleted <> false" +
                        " AND sr.completed <> true")
})
public class ScheduledRollout extends BaseEntity implements Serializable {
    @NotNull
    private String uuid;

    @NotNull
    private Integer newValue;

    @NotNull
    private Integer numOfRollouts;

    @NotNull
    private Long interval;

    @NotNull
    private TimeUnit timeUnit;

    @NotNull
    private Timestamp expirationDate;

    @NotNull
    private Boolean completed;

    @ManyToOne
    private Flag flag;

    @ManyToOne
    private Application application;

    public ScheduledRollout() {
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

    public Integer getNumOfRollouts() {
        return numOfRollouts;
    }

    public void setNumOfRollouts(Integer numOfRollouts) {
        this.numOfRollouts = numOfRollouts;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completedRollouts) {
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

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }
}
