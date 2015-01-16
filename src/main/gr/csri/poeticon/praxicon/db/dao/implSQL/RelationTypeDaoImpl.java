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
     * @param relationType the RelationType to be searched
     * @return a query to search for the RelationType
     */
    @Override
    public Query getEntityQuery(RelationType relationType) {
        Query query = getEntityManager().
                createNamedQuery("getRelationTypeEntityQuery").
                setParameter("backwardName", relationType.getBackwardName()).
                setParameter("forwardName", relationType.getForwardName());
        return query;
    }
}
