/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.IntersectionOfRelationChainsDao;
import gr.csri.poeticon.praxicon.db.entities.IntersectionOfRelationChains;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public class IntersectionOfRelationChainsDaoImpl 
        extends JpaDao<Long, IntersectionOfRelationChains>
        implements IntersectionOfRelationChainsDao {

    /**
     * Creates q query to search for a IntersectionOfRelationChains using name
     *
     * @param entity the IntersectionOfRelationChains to be searched
     * @return a query to search for the IntersectionOfRelationChains
     */
    @Override
    public Query getEntityQuery(IntersectionOfRelationChains entity) {
        Query q;
        q = getEntityManager().createQuery(
                "SELECT e FROM IntersectionOfRelationChains e " +
                "where UPPER(e.name) = ?1"
        );
        q.setParameter(1, entity.getName().toUpperCase());
        return q;
    }

}
