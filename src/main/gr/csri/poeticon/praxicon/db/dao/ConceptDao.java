/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao;

import gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Concept.Status;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dmavroeidis
 *
 */
public interface ConceptDao extends Dao<Long, Concept> {

    List<Concept> getAllConcepts();

    List<Concept> getAllBasicLevelConcepts();

    List<Concept> getAllNonBasicLevelConcepts();

    Concept getConceptByConceptId(long conceptId);

    Concept getConcept(Concept concept);

    List<Concept> getConceptsByName(String name);

    Concept getConceptByNameExact(String name);

    List<Concept> getConceptsByExternalSourceId(String externalSourceId);

    Concept getConceptByExternalSourceIdExact(String externalSourceId);

    List<Concept> getConceptsByLanguageRepresentation(String queryString);

    List<Concept> getConceptsByLanguageRepresentationExact(String queryString);

    List<Concept> getConceptsByLanguageRepresentationStartsWith(
            String queryString);

    List<Concept> getConceptsByLanguageRepresentationEndsWith(
            String queryString);

    List<Concept> getConceptsByStatus(Status status);

    Concept getConceptWithExternalSourceIdOrId(String v);

    Concept updatedConcept(Concept newConcept);

    void update(Concept newConcept);

    void update(Concept oldConcept, Concept newConcept);

    List<Concept> getChildren(Concept concept);

    List<Concept> getParents(Concept concept);

    List<Concept> getAllAncestors(Concept concept);

    List<Concept> getAllOffsprings(Concept concept);

    List<Concept> getClassesOfInstance(Concept concept);

    List<Concept> getInstancesOf(Concept concept);

    List<Concept> getBasicLevelConcepts(Concept concept);

    List<Map.Entry<Concept, ConceptDaoImpl.Direction>>
            getBasicLevelConceptsOld(Concept concept);

    void clearManager();

}
