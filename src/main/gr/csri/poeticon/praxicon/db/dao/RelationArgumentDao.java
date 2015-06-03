/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao;

import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
import gr.csri.poeticon.praxicon.db.entities.RelationType;
import java.util.List;

/**
 *
 * @author dmavroeidis
 */
public interface RelationArgumentDao extends Dao<Long, RelationArgument> {

    List<RelationArgument> getRelationArgumentsRelatedWithByRelationType(
            RelationArgument relationArgument, RelationType relationType);

    RelationArgument getRelationArgumentByConcept(Concept concept);

}
