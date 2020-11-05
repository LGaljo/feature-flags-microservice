package si.uni.lj.fri.lg0775.entities.db;

import si.uni.lj.fri.lg0775.entities.db.base.BaseEntity;
import si.uni.lj.fri.lg0775.entities.listeners.BaseEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "rules")
@EntityListeners(BaseEntityListener.class)
@NamedQueries({
        // Pridobi pravila za določenega uporabnika in določeno aplikacijo
        @NamedQuery(
                name = "Rule.getRulesForApp",
                query = "SELECT r FROM Rule r" +
                        " WHERE r.endUser.client = :clientId" +
                        " AND r.deleted = false"
        ),
        @NamedQuery(
                name = "Rule.getRulesForFlag",
                query = "SELECT r FROM Rule r" +
                        " WHERE r.flag.id = :flagId" +
                        " AND r.deleted = false"
        ),
        @NamedQuery(
                name = "Rule.getRuleForUser",
                query = "SELECT r FROM Rule r" +
                        " WHERE r.endUser.client = :clientId" +
                        " AND r.flag.id = :flagId" +
                        " AND r.deleted = false" +
                        " ORDER BY r.createdAt DESC"
        ),
        @NamedQuery(
                name = "Rule.getRuleForUserById",
                query = "SELECT r FROM Rule r" +
                        " WHERE r.endUser.id = :clientId" +
                        " AND r.deleted = false" +
                        " ORDER BY r.createdAt DESC"
        ),
})
public class Rule extends BaseEntity implements Serializable {
    @ManyToOne
    private EndUser endUser;

    @ManyToOne
    private Application application;

    @ManyToOne
    private Flag flag;

    @Basic
    @NotNull
    private int value;

    @Basic
    @NotNull
    private Timestamp expirationDate;

    public boolean hasExpired() {
        return expirationDate.toInstant().isBefore(Instant.now());
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
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

    public Flag getFlag() {
        return flag;
    }

    public void setFlag(Flag flags) {
        this.flag = flags;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
