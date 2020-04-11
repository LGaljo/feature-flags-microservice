package si.uni.lj.fri.lg0775.entities.db.base;

import javax.persistence.*;
import javax.ws.rs.DefaultValue;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    private UUID id;

    @DefaultValue(value = "false")
    private boolean deleted;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public BaseEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
