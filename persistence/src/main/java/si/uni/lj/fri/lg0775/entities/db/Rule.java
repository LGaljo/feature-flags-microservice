package si.uni.lj.fri.lg0775.entities.db;

import si.uni.lj.fri.lg0775.entities.db.base.BaseEntity;
import si.uni.lj.fri.lg0775.entities.listeners.BaseEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "rules")
@EntityListeners(BaseEntityListener.class)
public class Rule extends BaseEntity {
    @ManyToOne
    private EndUser endUser;

    @ManyToOne
    private Application application;

    @ManyToOne
    private Flag<Object> flag;

    @Basic
    private Instant expirationDate;

    public boolean hasExpired() {
        return expirationDate.isBefore(Instant.now());
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public EndUser getEndUser() {
        return endUser;
    }

    public void setEndUser(EndUser endUser) {
        this.endUser = endUser;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Flag<Object> getFlag() {
        return flag;
    }

    public void setFlag(Flag<Object> flags) {
        this.flag = flags;
    }
}
