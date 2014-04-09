/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao.implSQL;

import csri.poeticon.praxicon.db.dao.RelationChainDao;
import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.RelationChain;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public class RelationChainDaoImpl extends JpaDao<Long, RelationChain> implements RelationChainDao
{

    /**
     * Finds all RelationChain that have a ralation with subject or object a given concept
     * @param c the concept
     * @return a list of RelationChains
     */
    @Override
    public List<RelationChain> getRelationChainsContainingConcept(Concept c)
    {
        Query q = getEntityManager().createQuery("SELECT rc FROM RelationChain rc," +
                " IN(rc.relations) as rc_rel " +
                "WHERE rc_rel.relation.obj = ?1 or rc_rel.relation.subject = ?1");
        q.setParameter(1, c);
        return q.getResultList();
    }

    /**
     * Creates q query to search for a RelationChain using relations
     * @param entity the RelationChain to be searched
     * @return a query to search for the RelationChain
     */
    @Override
    public Query getEntityQuery(RelationChain entity)
    {
        StringBuilder sb = new StringBuilder("SELECT e FROM RelationChain e");
        for (int i = 0; i  < entity.getRelations().size(); i++)
        {
            sb.append(", IN (e.relations) as rel").append(i);
        }
        sb.append(" where UPPER(e.name) = ?1");
        for (int i = 0; i  < entity.getRelations().size(); i++)
        {
            sb.append("and rel").append(i).append("=?").append(i+2);
        }
        Query q = getEntityManager().createQuery(sb.toString());
        q.setParameter(1, entity.getName().toUpperCase());
        for (int i = 0; i  < entity.getRelations().size(); i++)
        {
            q.setParameter(i+2, entity.getRelations().get(i));
        }
        return q;
    }

}
