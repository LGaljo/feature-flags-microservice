package si.uni.lj.fri.lg0775.entities.db;

import si.uni.lj.fri.lg0775.entities.db.base.BaseEntity;
import si.uni.lj.fri.lg0775.entities.enums.DataType;
import si.uni.lj.fri.lg0775.entities.listeners.BaseEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "flags")
@EntityListeners(BaseEntityListener.class)
@NamedQueries({
        @NamedQuery(
                name = "Flag.getFlagsForApplication",
                query = "SELECT f FROM Flag f" +
                        " WHERE f.application.id = :applicationId AND f.deleted = false"
        ),
})
public class Flag extends BaseEntity implements Serializable {
    @Basic
    @NotNull
    private int defaultValue;

    @Basic
    @NotNull
    @Column(unique = true)
    private String name;

    @Basic
    private String description;

    @Basic
    @Enumerated(EnumType.STRING)
    @NotNull
    private DataType dataType;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private Application application;

    public Flag(@NotNull int defaultValue, @NotNull String name, String description, @NotNull DataType dataType, Application application) {
        this.defaultValue = defaultValue;
        this.name = name;
        this.description = description;
        this.dataType = dataType;
        this.application = application;
    }

    public Flag() {
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
}
