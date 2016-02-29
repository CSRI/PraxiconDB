/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.RelationArgumentDao;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
import gr.csri.poeticon.praxicon.db.entities.RelationSet;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 *
 */
public class RelationArgumentDaoImpl extends JpaDao<Long, RelationArgument>
        implements RelationArgumentDao {

    /**
     * Finds the relation argument that is connected with the given concept.
     *
     * @param concept the concept connected to the relation argument
     * @return relationArgument
     */
    @Override
    public RelationArgument getRelationArgument(Concept concept) {
        Query query = getEntityManager().createNamedQuery(
                "findRelationArgumentByConcept").
                setParameter("concept", concept);
        List<RelationArgument> relationArgumentsList =
                (List<RelationArgument>)query.getResultList();
        if (relationArgumentsList.isEmpty()) {
            return null;
        }
        System.out.println("relationArgumentList --> " + relationArgumentsList.get(0));
        return relationArgumentsList.get(0);
    }

    /**
     * Finds the relation argument that is connected with the given concept.
     *
     * @param concept the concept connected to the relation argument
     * @return relationArgument
     */
    @Override
    public RelationArgument getRelationArgument(
            RelationSet relationSet) {
        Query query = getEntityManager().createNamedQuery(
                "findRelationArgumentByRelationSet").
                setParameter("relationSet", relationSet);
        List<RelationArgument> relationArgumentsList =
                (List<RelationArgument>)query.getResultList();
        if (relationArgumentsList.isEmpty()) {
            return null;
        }
        return relationArgumentsList.get(0);
    }
}
