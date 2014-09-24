/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao;

import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
import gr.csri.poeticon.praxicon.db.entities.RelationSet;
import gr.csri.poeticon.praxicon.db.entities.RelationType;
import java.util.List;

/**
 *
 * @author dmavroeidis
 */
public interface RelationDao extends Dao<Long, Relation> {

    //List<Concept> getOwners(Relation relation);
    List<RelationSet> getRelationSetsWithConceptAsObject(
            Concept concept);

    List<Relation> getAllRelationsOfConcept(Concept concept);

    boolean areRelated(Concept concept1, Concept concept2);

    List<Relation> getRelationsByConceptTypeOfRelation(
            Concept concept, RelationType relationType);

    List<RelationSet> getRelationSetsWithRelationArgumentAsObject(
            RelationArgument relationArgument);

    List<Relation> getAllRelationsOfRelationArgument(
            RelationArgument relationArgument);

    boolean areRelated(RelationArgument relationArgument1,
            RelationArgument relationArgument2);

    List<Relation> getRelationsByRelationArgumentTypeOfRelation(
            RelationArgument relationArgument, RelationType relationType);
}
