/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao;

import javax.persistence.EntityManager;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public interface Dao<K, E>
{
      void persist(E entity);
      void merge(E entity);
      void remove(E entity);
      E findById(K id);
      void setEntityManager(EntityManager em);
      void resetAutoIncrement();
      List<E> findAll();
      void removeAll();
      void refresh(E entity);
      EntityManager getEntityManager();
      void close();
      void restart();
      E getEntity(E entity);
      Query getEntityQuery(E entity);
}
