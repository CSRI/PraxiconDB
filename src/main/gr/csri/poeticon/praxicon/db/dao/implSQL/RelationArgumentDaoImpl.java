/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.RelationArgumentDao;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
import gr.csri.poeticon.praxicon.db.entities.RelationType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 *
 */
public class RelationArgumentDaoImpl extends JpaDao<Long, RelationArgument>
        implements
        RelationArgumentDao {

    /**
     * Finds all relation arguments that are related to a given
     * relation argument using a given relation type
     *
     * @param relationArgument the relation argument
     * @param relationType     the type of relation (direction sensitive)
     * @return a list of relationArguments
     */
    @Override
    public List<RelationArgument> getRelationArgumentsRelatedWithByRelationType(
            RelationArgument relationArgument, RelationType relationType) {
        List<RelationArgument> res = new ArrayList<>();
        Query query = getEntityManager().createNamedQuery(
                "findRelationArgumentsRelatedWithByRelationType").
                setParameter("leftArgumentRelationArgumnet", relationArgument.
                        getId()).
                setParameter("rightArgumentRelationArgument", relationArgument.
                        getId()).
                setParameter("relationType", relationType);

        List<Relation> relationsList = query.getResultList();
        if (relationsList != null && relationsList.size() > 0) {
            for (Relation tmpRelation : relationsList) {
                if (tmpRelation.getLeftArgument().equals(relationArgument)) {
                    res.add(tmpRelation.getRightArgument());
                } else {
                    res.add(tmpRelation.getLeftArgument());
                }
            }
        }
        return res;
    }

    /**
     * Finds the relation argument that is connected with the given concept.
     *
     * @param concept the concept connected to the relation argument
     * @return relationArgument
     */
    @Override
    public RelationArgument getRelationArgumentByConcept(Concept concept) {
        Query query = getEntityManager().createNamedQuery(
                "findRelationArgumentByConcept").
                setParameter("conceptId", concept.getId());
        List<RelationArgument> relationArgumentsList =
                (List<RelationArgument>)query.getResultList();
        if (relationArgumentsList.isEmpty()) {
            return null;
        }
        return relationArgumentsList.get(0);
    }
}
