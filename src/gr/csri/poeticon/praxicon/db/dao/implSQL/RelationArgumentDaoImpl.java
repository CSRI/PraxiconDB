/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.RelationArgumentDao;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
import gr.csri.poeticon.praxicon.db.entities.RelationType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 * @author Dimitris Mavroeidis
 *
 */
public class RelationArgumentDaoImpl extends JpaDao<Long, Relation> implements
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
                setParameter("subjectRelationArgumnet", relationArgument.getId()).
                setParameter("objectRelationArgument", relationArgument.getId()).
                setParameter("relationType", relationType);

        List<Relation> relationsList = query.getResultList();
        if (relationsList != null && relationsList.size() > 0) {
            for (Relation tmpRelation : relationsList) {
                if (tmpRelation.getSubject().equals(relationArgument)) {
                    res.add(tmpRelation.getObject());
                } else {
                    res.add(tmpRelation.getSubject());
                }
            }
        }
        return res;
    }
}
