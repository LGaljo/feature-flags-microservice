package si.uni.lj.fri.lg0775.entities.db;

import si.uni.lj.fri.lg0775.entities.db.base.BaseEntity;
import si.uni.lj.fri.lg0775.entities.listeners.BaseEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "applications")
@EntityListeners(BaseEntityListener.class)
@NamedQueries({
        @NamedQuery(
                name = "Application.findAllExceptDeleted",
                query = "SELECT a FROM Application a WHERE a.deleted = false"
        ),
        // Get users of this application
        @NamedQuery(
                name = "Application.getEndUsers",
                query = "SELECT u FROM EndUser u" +
                        " WHERE u.deleted = false" +
                        " AND u.application.id = :applicationId"
        ),
})
/*
    Aplikacija je objekt, ki shrani njeno ime
 */
public class Application extends BaseEntity implements Serializable {
    @Basic
    @NotNull
    private String name;

    @Transient
    private List<Flag> flags;

    @Transient
    private List<EndUser> users;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Flag> getFlags() {
        return flags;
    }

    public void setFlags(List<Flag> flags) {
        this.flags = flags;
    }

    public List<EndUser> getUsers() {
        return users;
    }

    public void setUsers(List<EndUser> users) {
        this.users = users;
    }
}
