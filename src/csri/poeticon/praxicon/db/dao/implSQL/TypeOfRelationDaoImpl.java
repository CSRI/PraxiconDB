/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao.implSQL;

import csri.poeticon.praxicon.db.dao.RelationTypeDao;
import csri.poeticon.praxicon.db.entities.RelationType;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public class TypeOfRelationDaoImpl extends JpaDao<Long, RelationType> implements RelationTypeDao
{
    /**
     * Creates q query to search for a RelationType using forward and backward name
     * @param entity the RelationType to be searched
     * @return a query to search for the RelationType
     */
    @Override
    public Query getEntityQuery(RelationType entity)
    {
        Query q = getEntityManager().createQuery("SELECT e FROM TypeOfRelation e " +
                "where e.forwardName = ?1 and e.backwardName = ?2"
                );
        q.setParameter(1, entity.getForwardName());
        q.setParameter(2, entity.getBackwardName());
        return q;
    }
}
