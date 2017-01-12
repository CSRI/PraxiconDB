/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao;

import gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Concept.Status;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author dmavroeidis
 *
 */
public interface ConceptDao extends Dao<Long, Concept> {

    Set<Concept> getAllConcepts();

    Set<Concept> getAllBasicLevelConcepts();

    Set<Concept> getAllNonBasicLevelConcepts();

    Concept getConceptByConceptId(long conceptId);

    Concept getConcept(Concept concept);

    Set<Concept> getConceptsByName(String name);

    Concept getConceptByNameExact(String name);

    Set<Concept> getConceptsByNameAndStatus(String Name, Status status);

    Set<Concept> getConceptsByExternalSourceId(String externalSourceId);

    Concept getConceptByExternalSourceIdExact(String externalSourceId);

    Set<Concept> getConceptsByLanguageRepresentation(String queryString);

    Set<Concept> getConceptsByLanguageRepresentationExact(String queryString);

    Set<Concept> getConceptsByLanguageRepresentationStartsWith(
            String queryString);

    Set<Concept> getConceptsByLanguageRepresentationEndsWith(
            String queryString);

    Set<Concept> getConceptsByStatus(Status status);

    Concept getConceptWithExternalSourceIdOrId(String v);

    Concept updatedConcept(Concept newConcept);

    void update(Concept newConcept);

    void update(Concept oldConcept, Concept newConcept);

    Set<Concept> getChildren(Concept concept);

    Set<Concept> getParents(Concept concept);

    Set<Concept> getAllAncestors(Concept concept);

    Set<Concept> getAllOffsprings(Concept concept);

    Set<Concept> getClassesOfInstance(Concept concept);

    Set<Concept> getInstancesOf(Concept concept);

    Set<Concept> getBasicLevelConcepts(Concept concept);

    Set<Map.Entry<Concept, ConceptDaoImpl.Direction>>
            getBasicLevelConceptsOld(Concept concept);

    void clearManager();

}
