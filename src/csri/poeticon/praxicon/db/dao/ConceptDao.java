/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao;

import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.Concept.Status;
import csri.poeticon.praxicon.db.entities.TypeOfRelation;
import csri.poeticon.praxicon.db.entities.UnionOfIntersections;
import csri.poeticon.praxicon.db.entities.VisualRepresentation;
import java.util.List;

/**
 *
 * @author Erevodifwntas
 */
public interface ConceptDao extends Dao<Long, Concept>{

    @Override
    List<Concept> findAll();
    List<Concept> findAllByLanguageRepresentation(String queryString);
    List<Concept> findAllByLanguageRepresentationStarting(String queryString);
    List<Concept> findAllByName(String name);
    List<Concept> findByName(String name);
    List<Concept> findByStatus(Status status);
    VisualRepresentation getPrototypeRepresentation(Concept concept);
    Concept getConceptWithNameOrID(String v);
    Concept updatedConcept(Concept newCon);
    void update(Concept newCon);
    boolean areRelated(Concept conA, String relation, Concept conB);
    void update(Concept oldCon, Concept newCon);
    Concept simpleUpdate(Concept oldCon, Concept newCon);
    UnionOfIntersections getRelationUnion(Concept conA, Concept conB);
    List<Concept> findByLanguageRepresentation(String queryString);
    public List<Concept> findAllByNameStarting(String name);    
    List<Concept> getChildrenOf(Concept c);
    List<Concept> getParentsOf(Concept c);
    List<Concept> getClassesOfInstance(Concept c);
    List<Concept> getInstancesOf(Concept c);
    public List<Concept> getBasicLevel(Concept c);
    public List<Concept> getBasicLevelOfAnEntityConcept(Concept con);
    public List<Concept> getBasicLevelOfAnAbstractConcept(Concept c);
    public List<Concept> getAllAncestors(Concept c);
    public List<Concept> getAllOffsprings(Concept c);

    public void clearManager();

    public List<Concept> getConceptsRelatedWithByRelationType(Concept c, TypeOfRelation rtype);
}
