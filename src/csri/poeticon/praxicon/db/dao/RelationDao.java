/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao;

import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.Relation;
import csri.poeticon.praxicon.db.entities.TypeOfRelation;
import csri.poeticon.praxicon.db.entities.UnionOfIntersections;
import java.util.List;

/**
 *
 * @author Erevodifwntas
 */
public interface RelationDao extends Dao<Long, Relation> {

    List<Concept> getOwners(Relation rel);
    List<UnionOfIntersections> getObjRelations(Concept c);
    List<Concept> relatedConcepts(Concept c);
    List<Relation> allRelationsOf(Concept c);
    List<Relation> allRelationsOfByRelationType(Concept c, TypeOfRelation type);
    List<Relation> allRelationsOfByRelationName(Concept concept, TypeOfRelation.RELATION_NAME name);
    boolean areRelated(Concept c1, Concept c2);
    public List<Relation> getSubConcepts(Concept c);
    List<Concept> getObjectsOfARelation(String nameOfTheRelation);
    List<Relation> findByType(String nameOfTheRelation);
    public List<Concept> getRelatedConcepts(Concept c);
    List<Relation> findRelationsByConceptTypeOfRelation(Concept concept, TypeOfRelation type);
    boolean isPartOfInherentUnion (Relation r);
}
