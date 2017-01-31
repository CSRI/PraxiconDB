/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.EntityMngFactory;
import gr.csri.poeticon.praxicon.db.dao.Dao;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.isNull;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 * @param <K>
 * @param <E>
 */
public abstract class JpaDao<K, E> implements Dao<K, E> {

    protected Class<E> entityClass;

    protected EntityManager entityManager;

    public JpaDao() {
        ParameterizedType genericSuperclass =
                (ParameterizedType)getClass().getGenericSuperclass();
        this.entityClass =
                (Class<E>)genericSuperclass.getActualTypeArguments()[1];
        this.entityManager = EntityMngFactory.getEntityManager();
    }

    public JpaDao(EntityManager entityManager) {
        ParameterizedType genericSuperclass =
                (ParameterizedType)getClass().getGenericSuperclass();
        this.entityClass =
                (Class<E>)genericSuperclass.getActualTypeArguments()[1];
        this.entityManager = entityManager;
    }

    @Override
    public void setEntityManager(EntityManager em) {
        this.entityManager = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    public void persist(E entity) {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        try {
            entityManager.persist(entity);
            entityManager.flush();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        } finally {

        }
    }

    @Override
    public void merge(E entity) {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        try {
            entityManager.merge(entity);
            entityManager.flush();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        } finally {

        }
    }

    @Override
    public void remove(E entity) {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        entityManager.remove(entity);
        entityManager.flush();
        entityManager.getTransaction().commit();

    }

    @Override
    public E findById(K id) {
        return entityManager.find(entityClass, id);
    }

    // This is for MySQL ONLY, doesn't work for all entities.
    // Please override as appropriate.
    @Override
    public void resetAutoIncrement() {
        String name = entityClass.getName().
                substring(entityClass.getName().lastIndexOf('.') + 1);
        if (name.endsWith("DTO")) {
            name = name.substring(0, name.length() - "DTO".length());
        }
        name += "s";
        name = name.substring(name.lastIndexOf('.') + 1);
        name = name.toUpperCase();
        Query q = getEntityManager().createNativeQuery("ALTER TABLE MVCG_" +
                name + " AUTO_INCREMENT = 1");
        q.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<E> findAll() {
        Query q = getEntityManager().createQuery("SELECT h FROM " +
                entityClass.getName().substring(entityClass.getName().
                        lastIndexOf('.') + 1) + " h order by h.id");
        return new ArrayList<>(q.getResultList());
    }

    @Override
    public E getEntity(E entity) {
        Query q = getEntityQuery(entity);
        if (!isNull(q)) {
            List res = new ArrayList<>(q.getResultList());
            if (res.isEmpty()) {
                return entity;
            }
            return (E)res.iterator().next();
        } else {
            return entity;
        }
    }

    @Override
    public Query getEntityQuery(E entity) {
        return null;
    }

    @Override
    public void removeAll() {
        Query q = getEntityManager().createQuery("DELETE FROM " +
                entityClass.getName().substring(entityClass.getName().
                        lastIndexOf('.') + 1) + " h");
        q.executeUpdate();
    }

    @Override
    public void refresh(E entity) {
        getEntityManager().refresh(entity);
    }

    @Override
    public void close() {
        if (!isNull(entityManager)) {
            entityManager.close();
        }
    }

    @Override
    public void restart() {
        EntityMngFactory.restart();
    }
}
