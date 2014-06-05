/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.RelationChainDao;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.RelationChain;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 */
public class RelationChainDaoImpl extends JpaDao<Long, RelationChain>
        implements RelationChainDao {

    /**
     * Finds all RelationChain that have a relation with subject or object a
     * given concept.
     *
     * @param concept the concept
     * @return a list of RelationChains
     */
    @Override
    public List<RelationChain> getRelationChainsContainingConcept(
            Concept concept) {
        Query q = getEntityManager().createNamedQuery(
                "getRelationChainsByConcept").setParameter("concept_id",
                        concept.getId());
        return q.getResultList();
    }

    
    //TODO: Convert it to Named query asap.
    /**
     * Creates q query to search for a RelationChain using relations.
     *
     * @param relationChain the RelationChain to be searched
     * @return a query to search for the RelationChain
     */
    @Override
    public Query getEntityQuery(RelationChain relationChain) {
        StringBuilder sb = new StringBuilder("SELECT rc FROM RelationChain rc");
        for (int i = 0; i < relationChain.getRelations().size(); i++) {
            sb.append(", IN (rc.relations) as rel").append(i);
        }
        sb.append(" where UPPER(rc.name) = ?1");
        for (int i = 0; i < relationChain.getRelations().size(); i++) {
            sb.append("and rel").append(i).append("=?").append(i + 2);
        }
        Query q = getEntityManager().createQuery(sb.toString());
        q.setParameter(1, relationChain.getName().toUpperCase());
        for (int i = 0; i < relationChain.getRelations().size(); i++) {
            q.setParameter(i + 2, relationChain.getRelations().get(i));
        }
        return q;
    }

}
