package si.uni.lj.fri.lg0775.entities.db;

import si.uni.lj.fri.lg0775.entities.db.base.BaseEntity;
import si.uni.lj.fri.lg0775.entities.listeners.BaseEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "end_users")
@EntityListeners(BaseEntityListener.class)
@NamedQueries({
        @NamedQuery(
                name = "EndUser.getByClientID",
                query = "SELECT u FROM EndUser u WHERE u.client = :clientId"
        ),
        // Pridobi pravila za določenega uporabnika in določeno aplikacijo
        @NamedQuery(
                name = "EndUser.getRulesForApp",
                query = "SELECT r FROM Rule r, Application a, EndUser u" +
                        " WHERE r.application = :applicationId AND r.endUser.client = :clientId"
        ),
})
public class EndUser extends BaseEntity {
    @Basic
    @NotNull
    private UUID client;

    @OneToOne
    @NotNull
    private Application application;

    public UUID getClient() {
        return client;
    }

    public void setClient(UUID client) {
        this.client = client;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
}
