package si.uni.lj.fri.lg0775.entities.db;

import si.uni.lj.fri.lg0775.entities.DataType;
import si.uni.lj.fri.lg0775.entities.db.base.BaseEntity;
import si.uni.lj.fri.lg0775.entities.listeners.BaseEntityListener;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "flags")
@EntityListeners(BaseEntityListener.class)
public class Flag<T> extends BaseEntity {
    @Basic
    private T value;

    @Basic
    private T defaultValue;

    @Basic
    private String name;

    @Basic
    private String description;

    @Basic
    @Enumerated(EnumType.STRING)
    private DataType dataType;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private Application application;

    public T getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
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
