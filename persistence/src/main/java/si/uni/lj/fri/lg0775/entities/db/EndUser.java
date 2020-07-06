package si.uni.lj.fri.lg0775.entities.db;

import si.uni.lj.fri.lg0775.entities.db.base.BaseEntity;
import si.uni.lj.fri.lg0775.entities.listeners.BaseEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "end_users")
@EntityListeners(BaseEntityListener.class)
@NamedQueries({
        // Pridobi podatke o uporabniku
        @NamedQuery(
                name = "EndUser.getByClientID",
                query = "SELECT u FROM EndUser u WHERE u.client = :clientId"
        ),
})
public class EndUser extends BaseEntity implements Serializable {
    @Basic
    @NotNull
    @Column(unique = true)
    private String client;

    @ManyToOne
    @NotNull
    private Application application;

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
}
