/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao;

import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
import gr.csri.poeticon.praxicon.db.entities.RelationSet;
import java.util.Set;

/**
 *
 * @author dmavroeidis
 */
public interface RelationSetDao extends Dao<Long, RelationSet> {

    RelationSet getRelationSetByName(String relationSetName);

    RelationSet getRelationSet(RelationSet relationSet);

    Set<RelationSet> getRelationSetsByRelationArgument(
            RelationArgument relationArgument);

    Set<RelationSet> getRelationSetsByRelation(Relation relation);

    Set<RelationSet> getRelationSetsByConcept(Concept concept);

    Set<RelationSet> getRelationSetsWithRelationArgumentAsLeftArgument(
            RelationArgument relationArgument);

    Set<RelationSet> getRelationSetsWithRelationArgumentAsRightArgument(
            RelationArgument relationArgument);

    Set<RelationSet> getRelationSetsWithConceptAsRightArgument(
            Concept concept);

    RelationSet updatedRelationSet(RelationSet relationSet);

}
