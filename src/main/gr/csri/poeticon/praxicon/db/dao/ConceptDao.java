/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao;

import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Concept.Status;
import gr.csri.poeticon.praxicon.db.entities.RelationType;
import java.util.List;

/**
 *
 * @author dmavroeidis
 *
 */
public interface ConceptDao extends Dao<Long, Concept> {

    List<Concept> findAllConcepts();

    List<Concept> findAllBasicLevelConcepts();

    List<Concept> findAllNonBasicLevelConcepts();

    Concept findConceptByConceptId(long conceptId);

    List<Concept> findConceptsByExternalSourceId(String name);

    Concept findConceptByExternalSourceIdExact(String name);

    List<Concept> findConceptsByLanguageRepresentation(String queryString);

    List<Concept> findConceptsByLanguageRepresentationExact(String queryString);

    List<Concept> findConceptsByStatus(Status status);

    Concept getConceptWithExternalSourceIdOrId(String v);

    Concept updatedConcept(Concept newConcept);

    void update(Concept newConcept);

    void update(Concept oldConcept, Concept newConcept);

    List<Concept> getChildrenOfConcept(Concept concept);

    List<Concept> getParentsOfConcept(Concept concept);

    public List<Concept> getAllAncestors(Concept concept);

    public List<Concept> getAllOffsprings(Concept concept);

    List<Concept> getClassesOfInstance(Concept concept);

    List<Concept> getInstancesOf(Concept concept);

    public List<Concept> getBasicLevelConcepts(Concept concept);

    public List<Concept> getConceptsRelatedWithByRelationType(
            Concept concept, RelationType relationType);

    public void clearManager();
}
