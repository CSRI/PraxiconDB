/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.RelationSetDao;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.RelationSet;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 */
public class RelationSetDaoImpl extends JpaDao<Long, RelationSet>
        implements RelationSetDao {

    /**
     * Finds all RelationChain that have a relation with subject or object a
     * given concept.
     *
     * @param concept the concept
     * @return a list of RelationChains
     */
    @Override
    public List<RelationSet> getRelationSetsContainingConcept(
            Concept concept) {
        Query q = getEntityManager().createNamedQuery(
                "findRelationSetsByConcept").setParameter("concept_id",
                        concept.getId());
        return q.getResultList();
    }

    
    //TODO: Convert it to Named query asap.
    /**
     * Creates q query to search for a RelationSet using relations.
     *
     * @param relationSet the RelationSet to be searched
     * @return a query to search for the RelationSet
     */
    @Override
    public Query getEntityQuery(RelationSet relationSet) {
        StringBuilder sb = new StringBuilder("SELECT rc FROM RelationSet rs");
        for (int i = 0; i < relationSet.getRelations().size(); i++) {
            sb.append(", IN (rs.relations) as rel").append(i);
        }
        sb.append(" where UPPER(rs.name) = ?1");
        for (int i = 0; i < relationSet.getRelations().size(); i++) {
            sb.append("and rel").append(i).append("=?").append(i + 2);
        }
        Query q = getEntityManager().createQuery(sb.toString());
        q.setParameter(1, relationSet.getName().toUpperCase());
        for (int i = 0; i < relationSet.getRelations().size(); i++) {
            q.setParameter(i + 2, relationSet.getRelations().get(i));
        }
        return q;
    }

}
