/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao;

import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Concept.status;
import gr.csri.poeticon.praxicon.db.entities.RelationType;
import java.util.List;

/**
 *
 * @author Erevodifwntas
 *
 */
public interface ConceptDao extends Dao<Long, Concept> {

    @Override
    List<Concept> findAll();

    List<Concept> findAllByLanguageRepresentation(String queryString);

    List<Concept> findByLanguageRepresentation(String queryString);

    List<Concept> findAllByName(String name);

    List<Concept> findByName(String name);

    List<Concept> findByStatus(status status);

    Concept getConceptWithNameOrID(String v);

    Concept updatedConcept(Concept newCon);

    void update(Concept newCon);

    void update(Concept oldCon, Concept newCon);

    boolean areRelated(Concept conA, String relation, Concept conB);

    List<Concept> getChildrenOf(Concept c);

    List<Concept> getParentsOf(Concept c);

    public List<Concept> getAllAncestors(Concept c);

    public List<Concept> getAllOffsprings(Concept c);

    List<Concept> getClassesOfInstance(Concept c);

    List<Concept> getInstancesOf(Concept c);

    public List<Concept> getBasicLevel(Concept c);

    public List<Concept> getBasicLevelOfAnEntityConcept(Concept con);

    public List<Concept> getBasicLevelOfAnAbstractConcept(Concept c);

    public List<Concept> getConceptsRelatedWithByRelationType(
            Concept c, RelationType rtype);

    public void clearManager();
}
