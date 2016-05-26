/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao;

import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
import gr.csri.poeticon.praxicon.db.entities.RelationSet;
import java.util.List;

/**
 *
 * @author dmavroeidis
 */
public interface RelationSetDao extends Dao<Long, RelationSet> {

    RelationSet getRelationSetByName(String relationSetName);

    RelationSet getRelationSet(RelationSet relationSet);

    List<RelationSet> getRelationSetsByRelationArgument(
            RelationArgument relationArgument);

    List<RelationSet> getRelationSetsByConcept(Concept concept);

    List<RelationSet> getRelationSetsWithRelationArgumentAsLeftArgument(
            RelationArgument relationArgument);

    List<RelationSet> getRelationSetsWithRelationArgumentAsRightArgument(
            RelationArgument relationArgument);

    List<RelationSet> getRelationSetsWithConceptAsRightArgument(
            Concept concept);

    RelationSet updatedRelationSet(RelationSet relationSet);

}
