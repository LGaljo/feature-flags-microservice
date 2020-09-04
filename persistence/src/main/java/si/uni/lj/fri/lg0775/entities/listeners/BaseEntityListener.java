package si.uni.lj.fri.lg0775.entities.listeners;

import si.uni.lj.fri.lg0775.entities.db.base.BaseEntity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.sql.Timestamp;
import java.time.Instant;

public class BaseEntityListener {
    @PrePersist
    private void prePersist(Object entity) {
        BaseEntity baseEntity = (BaseEntity) entity;
        Timestamp now = Timestamp.from(Instant.now());
//        baseEntity.setDeleted(false);
        baseEntity.setCreatedAt(now);
        baseEntity.setUpdatedAt(now);
    }

    @PreUpdate
    private void preUpdate(Object entity) {
        BaseEntity baseEntity = (BaseEntity) entity;
        baseEntity.setUpdatedAt(Timestamp.from(Instant.now()));
    }
}
