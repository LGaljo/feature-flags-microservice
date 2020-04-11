package si.uni.lj.fri.lg0775.entities.db;

import si.uni.lj.fri.lg0775.entities.db.base.BaseEntity;
import si.uni.lj.fri.lg0775.entities.listeners.BaseEntityListener;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "applications")
@EntityListeners(BaseEntityListener.class)
@NamedQueries({
        @NamedQuery(
                name = "Application.findAll",
                query = "SELECT a FROM Application a"
        ),
        @NamedQuery(
                name = "Application.getFlagsForApplication",
                query = "SELECT f FROM Application a, Flag f" +
                        " WHERE f.application.id = :applicationId"
        ),
})
public class Application extends BaseEntity {
    @Basic
    private String name;

    @OneToMany
    private List<Flag<Object>> flags;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Flag<Object>> getFlags() {
        return flags;
    }

    public void setFlags(List<Flag<Object>> flags) {
        this.flags = flags;
    }
}
