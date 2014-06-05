/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.RelationTypeDao;
import gr.csri.poeticon.praxicon.db.entities.RelationType;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 */
public class RelationTypeDaoImpl extends JpaDao<Long, RelationType> implements
        RelationTypeDao {

    /**
     * Creates q query to search for a RelationType using forward
     * and backward name
     *
     * @param entity the RelationType to be searched
     * @return a query to search for the RelationType
     */
    @Override
    public Query getEntityQuery(RelationType relationType) {
        Query q = getEntityManager().createQuery(
                "SELECT e FROM RelationType e " +
                "where e.ForwardName = ?1 and e.BackwardName = ?2"
        );
        q.setParameter(1, relationType.getForwardName());
        q.setParameter(2, relationType.getBackwardName());
        return q;
    }
}
