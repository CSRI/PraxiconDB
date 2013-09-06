/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao.implSQL;

import csri.poeticon.praxicon.db.dao.TypeOfRelationDao;
import csri.poeticon.praxicon.db.entities.TypeOfRelation;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public class TypeOfRelationDaoImpl extends JpaDao<Long, TypeOfRelation> implements TypeOfRelationDao{
    /**
     * Creates q query to search for a TypeOfRelation using forward and backward name
     * @param entity the TypeOfRelation to be searched
     * @return a query to search for the TypeOfRelation
     */
    @Override
    public Query getEntityQuery(TypeOfRelation entity)
    {
        Query q = getEntityManager().createQuery("SELECT e FROM TypeOfRelation e " +
                "where e.forwardName = ?1 and e.backwardName = ?2"
                );
        q.setParameter(1, entity.getForwardName());
        q.setParameter(2, entity.getBackwardName());
        return q;
    }
}
