/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao;

import gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Concept.Status;
import gr.csri.poeticon.praxicon.db.entities.RelationType;
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

    List<Concept> getConceptsByExternalSourceId(String name);

    Concept getConceptByExternalSourceIdExact(String name);

    List<Concept> getConceptsByLanguageRepresentation(String queryString);

    List<Concept> getConceptsByLanguageRepresentationExact(String queryString);

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

    List<Concept> getConceptsRelatedWithByRelationType(
            Concept concept, RelationType relationType);

    void clearManager();
}
