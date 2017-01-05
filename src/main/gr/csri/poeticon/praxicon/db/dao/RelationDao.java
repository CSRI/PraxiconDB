/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao;

import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
import gr.csri.poeticon.praxicon.db.entities.RelationType;
import java.util.Set;

/**
 *
 * @author dmavroeidis
 */
public interface RelationDao extends Dao<Long, Relation> {

    Relation getRelation(RelationArgument leftArgument,
            RelationArgument rightArgument,
            RelationType.RelationNameForward relationType);

    Set<Relation> getAllRelationsOfConcept(Concept concept);

    boolean areRelated(Concept concept1, Concept concept2);

    Set<Relation> getRelationsByConceptRelationType(
            Concept concept, RelationType.RelationNameForward relationType);

    Set<Relation> getRelationsByLeftConceptTypeOfRelation(
            Concept concept, RelationType.RelationNameForward relationType);

    Set<Relation> getRelationsByRightConceptTypeOfRelation(
            Concept concept, RelationType.RelationNameForward relationType);

    Set<Relation> getRelationsByRelationType(
            RelationType.RelationNameForward relationType);

    Set<Relation> getAllRelationsOfRelationArgument(
            RelationArgument relationArgument);

    boolean areRelated(RelationArgument relationArgument1,
            RelationArgument relationArgument2);

    Set<Relation> getRelationsByRelationArgumentRelationType(
            RelationArgument relationArgument,
            RelationType.RelationNameForward relationType);

    Set<Relation> getRelationsByLeftRelationArgumentTypeOfRelation(
            RelationArgument relationArgument,
            RelationType.RelationNameForward relationType);

    Set<Relation> getRelationsByRightRelationArgumentTypeOfRelation(
            RelationArgument relationArgument,
            RelationType.RelationNameForward relationType);

    Relation updatedRelation(Relation newRelation);

    Relation getRelation(Relation newRelation);

}
