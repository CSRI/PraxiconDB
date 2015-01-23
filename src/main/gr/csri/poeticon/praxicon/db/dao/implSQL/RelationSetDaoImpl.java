/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.RelationArgumentDao;
import gr.csri.poeticon.praxicon.db.dao.RelationSetDao;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
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
     * Finds all RelationSets that have at least one relation with leftArgument 
     * or rightArgument a given relationArgument.
     *
     * @param relationArgument the relation argument to search by
     * @return a list of RelationSets
     */
    @Override
    public List<RelationSet> getRelationSetsByRelationArgument(
            RelationArgument relationArgument) {
        Query q = getEntityManager().createNamedQuery(
                "findRelationSetsByRelationArgument").setParameter(
                        "relationArgument", relationArgument);
        return q.getResultList();
    }

    /**
     * Finds all RelationSets that have at least one relation with a concept 
     * as LeftArgument or RightArgument.
     *
     * @param concept the concept to search by
     * @return a list of RelationSets
     */
    @Override
    public List<RelationSet> getRelationSetsByConcept(Concept concept) {
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        RelationArgument newRelationArgument = raDao.getRelationArgumentByConcept(concept);
        
//        RelationArgument newRelationArgument = new RelationArgument(concept);
        return getRelationSetsByRelationArgument(newRelationArgument);
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
