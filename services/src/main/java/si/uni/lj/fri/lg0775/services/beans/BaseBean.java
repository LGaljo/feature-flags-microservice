package si.uni.lj.fri.lg0775.services.beans;

import si.uni.lj.fri.lg0775.entities.db.base.BaseEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

// Reference: https://github.com/logsearchernet/sample-spring-boot-jpa/blob/master/src/main/java/com/sample/dao/BaseDao.java

public class BaseBean<E extends Serializable, K extends Serializable> {
    @PersistenceContext
    protected EntityManager em;

    private Class<E> entityClass;

    @SuppressWarnings("unchecked")
    public BaseBean() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        this.entityClass = (Class<E>) params[0];
    }

    public void create(E e) {
        em.persist(e);
        em.flush();
    }


    public void update(E e) {
        em.merge(e);
        em.flush();
    }

    public void delete(K id) {
        E delete = em.getReference(entityClass, id);

        em.remove(delete);
        em.flush();
    }

    public void markDeleted(K id) {
        BaseEntity be = (BaseEntity) em.getReference(entityClass, id);
        be.setDeleted(true);

        em.persist(be);
        em.flush();
    }

    public E find(K id) {
        return em.find(entityClass, id);
    }

    public boolean contains(K id) {
        return em.contains(id);
    }

}
