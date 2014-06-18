/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.ConceptDao;
import gr.csri.poeticon.praxicon.db.dao.RelationDao;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Concept.status;
import gr.csri.poeticon.praxicon.db.entities.Concept_LanguageRepresentation;
import gr.csri.poeticon.praxicon.db.entities.IntersectionOfRelationChains;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationChain;
import gr.csri.poeticon.praxicon.db.entities.RelationChain_Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 */
public class ConceptDaoImpl extends JpaDao<Long, Concept> implements
        ConceptDao {

    private String String;

    /**
     * Finds all the concepts
     *
     * @return a list of all concepts in the database
     */
    @Override
    public List<Concept> findAllConcepts() {
        Query query = getEntityManager().createNamedQuery("findAllConcepts");
        List<Concept> concepts = query.getResultList();
        return concepts;
    }

    /**
     * Finds all concepts that have a specific conceptId
     *
     * @param concept_id the concept id to search for
     * @return a list of concepts found in the database
     */
    @Override
    public Concept findConceptByConceptId(long conceptId) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByConceptId").
                setParameter("concept_id", conceptId);
        return (Concept)query.getSingleResult();
    }

    /**
     * Finds all concepts that have a name or language representation containing
     * a given string
     *
     * @param concept_name the concept name to search for
     * @return a list of concepts found in the database
     */
    @Override
    public List<Concept> findConceptsByName(String conceptName) {
        Query query = getEntityManager().createNamedQuery("findConceptsByName").
                setParameter("concept_name", "%" + conceptName + "%");
        return query.getResultList();
    }

    /**
     * Finds all concepts that have a name equal to a given string
     *
     * @param concept_name the concept name to search for
     * @return a list of concepts found in the database
     */
    @Override
    public Concept findConceptByNameExact(String conceptName) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptByNameExact").
                setParameter("concept_name", conceptName);
        return (Concept)query.getSingleResult();
    }

    /**
     * Finds all concepts that have a language representation containing a given
     * string
     *
     * @param language_representation_name the language representation name to
     *                                     search for
     * @return a list of concepts found in the database
     */
    @Override
    public List<Concept> findConceptsByLanguageRepresentation(
            String languageRepresentationName) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByLanguageRepresentation").
                setParameter("lr_name", "%" + languageRepresentationName +
                        "%");
        return query.getResultList();
    }

    /**
     * Finds all concepts that have a language representation equal to a given
     * string
     *
     * @param queryString the string to search for
     * @return a list of concepts found in the database
     */
    @Override
    public List<Concept> findConceptsByLanguageRepresentationExact(
            String languageRepresentationName) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByLanguageRepresentationExact").
                setParameter("lr_name", languageRepresentationName);
        return query.getResultList();
    }

    /**
     * Finds all concepts that have a status equal to a given status
     *
     * @param status the concept status to search for
     * @return a list of concepts found in the database
     */
    @Override
    public List<Concept> findConceptsByStatus(status status) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByStatusExact").
                setParameter("status", status);
        return query.getResultList();
    }

    /**
     * Finds all concepts that have a name or id equal to a given string
     *
     * @param v
     * @return the concept found in the database (null if not found)
     */
    @Override
    public Concept getConceptWithNameOrID(String v) {
        Query q;
        long id = -1;
        try {
            id = Long.parseLong(v);
        } catch (NumberFormatException e) {
            //it is the name of the concept
        }
        if (id == -1) {
            return findConceptByNameExact(v.trim());
        } else {
            return findConceptByConceptId(id);
        }
    }

    /**
     * Updates a concept from the database that has the same name as another
     * concept that is used as source of the update
     *
     * @param newConcept concept to use as source
     * @return the updated concept
     */
    @Override
    public Concept updatedConcept(Concept newConcept) {

        Concept oldConcept = new Concept();
        try {
            oldConcept = this.findConceptByNameExact(newConcept.getName());
        } catch (Exception e) {

            return newConcept;
        } finally {
            oldConcept.setConceptType(newConcept.getConceptType());
            oldConcept.setPragmaticStatus(newConcept.getPragmaticStatus());
            oldConcept.setStatus(newConcept.getStatus());
            oldConcept.setSpecificityLevel(newConcept.getSpecificityLevel());
            oldConcept.setComment(newConcept.getComment());

            updateLanguageRepresentations(newConcept, oldConcept);
            updateVisualRepresentations(newConcept, oldConcept);
            updateMotoricRepresentations(newConcept, oldConcept);
            updateObjOfRelations(newConcept, oldConcept);
            updateRelations(newConcept, oldConcept);
            return oldConcept;
        }
    }

    /**
     * Updates a concept from the database (in place) that has the same name as
     * another concept that is used as source of the update
     *
     * @param newConcept concept to use as source
     */
    @Override
    public void update(Concept newConcept) {
        try {
            Query query = getEntityManager().createNamedQuery(
                    "findConceptByNameExact").
                    setParameter("concept_name", newConcept.getName());
            Concept tmpConcept = (Concept)query.getSingleResult();

            Concept oldConcept = null;

            if (tmpConcept.equals(null)) {
                oldConcept = new Concept();
            } else {
                oldConcept = tmpConcept;
            }
            if (oldConcept.getConceptType() == null ||
                    oldConcept.getConceptType() == Concept.type.UNKNOWN) {
                oldConcept.setConceptType(newConcept.getConceptType());
            }
            if (oldConcept.getPragmaticStatus() == null) {
                oldConcept.setPragmaticStatus(newConcept.getPragmaticStatus());
            }
            if (oldConcept.getStatus() == null) {
                oldConcept.setStatus(newConcept.getStatus());
            }

            oldConcept.setSpecificityLevel(newConcept.getSpecificityLevel());
            if (oldConcept.getComment() == null ||
                    oldConcept.getComment().equalsIgnoreCase("") ||
                    oldConcept.getComment().equalsIgnoreCase("Unknown")) {
                oldConcept.setComment(newConcept.getComment());
            }
            if (newConcept.getSource() != null && !newConcept.getSource().
                    isEmpty()) {
                oldConcept.setSource(newConcept.getSource());
            }

            if (!getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().begin();
            }
            updateLanguageRepresentations(newConcept, oldConcept);
            oldConcept = entityManager.merge(oldConcept);
            entityManager.getTransaction().commit();
            if (!getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().begin();
            }
            updateVisualRepresentations(newConcept, oldConcept);
            oldConcept = entityManager.merge(oldConcept);
            entityManager.getTransaction().commit();
            if (!getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().begin();
            }
            updateMotoricRepresentations(newConcept, oldConcept);
            oldConcept = entityManager.merge(oldConcept);
            entityManager.getTransaction().commit();
            if (!getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().begin();
            }
            updateObjOfRelations(newConcept, oldConcept);
            oldConcept = entityManager.merge(oldConcept);
            entityManager.getTransaction().commit();
            if (!getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().begin();
            }
            updateRelations(newConcept, oldConcept);
            oldConcept = entityManager.merge(oldConcept);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }

    }

    /**
     * Updates a concept using another concept (in place).
     *
     * @param oldConcept concept to be updated
     * @param newConcept concept to use as source
     */
    @Override
    public void update(Concept oldConcept, Concept newConcept) {
        try {
            //     entityManager.getTransaction().begin();
            if (oldConcept.getConceptType() == null ||
                    oldConcept.getConceptType() == Concept.type.UNKNOWN) {
                oldConcept.setConceptType(newConcept.getConceptType());
            }
            if (oldConcept.getPragmaticStatus() == null) {
                oldConcept.setPragmaticStatus(newConcept.getPragmaticStatus());
            }
            if (oldConcept.getStatus() == null) {
                oldConcept.setStatus(newConcept.getStatus());
            }
            oldConcept.setSpecificityLevel(newConcept.getSpecificityLevel());
            if (oldConcept.getComment() == null ||
                    oldConcept.getComment().equalsIgnoreCase("") ||
                    oldConcept.getComment().equalsIgnoreCase("Unknown")) {
                oldConcept.setComment(newConcept.getComment());
            }
            updateVisualRepresentations(newConcept, oldConcept);
            updateMotoricRepresentations(newConcept, oldConcept);
            updateObjOfRelations(newConcept, oldConcept);
            updateRelations(newConcept, oldConcept);
            merge(oldConcept);
            updateLanguageRepresentations(newConcept, oldConcept);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Checks if two concepts are related by a certain relation
     *
     * @param conceptA first concept
     * @param relation relation name as a string
     * @param conceptB second concept
     * @return true/false
     */
    @Override
    public boolean areRelated(Concept conceptA, String relation,
            Concept conceptB) {
        for (int intersection = 0;
                intersection < conceptA.getIntersectionsOfRelationChains().
                size();
                intersection++) {
            IntersectionOfRelationChains tmpIntersection =
                    conceptA.getIntersectionsOfRelationChains().get(
                            intersection);
            for (int relationChain = 0;
                    relationChain < tmpIntersection.getRelationChains().size();
                    relationChain++) {
                RelationChain tmpRelationChain =
                        tmpIntersection.getRelationChains().get(relationChain);
                for (int rel = 0; rel < tmpRelationChain.getRelations().size();
                        rel++) {
                    if (tmpRelationChain.getRelations().get(rel).
                            getRelationOrder() == 0) {
                        RelationType tmpTypeOfRelation =
                                tmpRelationChain.getRelations().get(rel).
                                getRelation().getType();
                        if (conceptB.equals(tmpRelationChain.getRelations().
                                get(rel).getRelation().getObject())) {
                            if (tmpTypeOfRelation.getForwardName() ==
                                    RelationType.relation_name_forward.
                                    valueOf(relation) ||
                                    tmpTypeOfRelation.getBackwardName() ==
                                    RelationType.relation_name_backward.
                                    valueOf(relation)) {
                                return true;
                            }
                        }
                    }
                }
                //               }
            }
        }
        return false;
    }

    /**
     * Finds all concepts that are children (type-token related) of a given
     * concept
     *
     * @param c the concept
     * @return a list of concepts
     */
    @Override
    public List<Concept> getChildrenOfConcept(Concept concept) {
        List<Concept> res = new ArrayList<>();

        RelationDao rDao = new RelationDaoImpl();
        List<Relation> relations = rDao.getAllRelationsOfConcept(concept);
        for (Relation relation : relations) {
            if (relation.getType().getForwardName() ==
                    RelationType.relation_name_forward.TYPE_TOKEN &&
                    relation.getSubject().equals(concept)) {
                res.add(relation.getObject());
            }
        }
        entityManager.clear();
        return res;
    }

    /**
     * Finds all concepts that are parents (token-type related) of a given
     * concept
     *
     * @param c the concept
     * @return a list of concepts
     */
    @Override
    public List<Concept> getParentsOfConcept(Concept concept) {
        List<Concept> res = new ArrayList<>();

        RelationDao rDao = new RelationDaoImpl();
        List<Relation> relations = rDao.getAllRelationsOfConcept(concept);
        for (Relation relation : relations) {
            if (relation.getType().getForwardName() ==
                    RelationType.relation_name_forward.TYPE_TOKEN &&
                    relation.getObject().equals(concept)) {
                res.add(relation.getSubject());
            }
        }
        entityManager.clear();
        return res;
    }

    /**
     * Finds all concepts that are ancestors (higher in hierarchy) of a given
     * concept
     *
     * @param concept
     * @return a list of concepts
     */
    @Override
    public List<Concept> getAllAncestors(Concept concept) {
        List<Concept> res = new ArrayList<>();

        List<Concept> parents = getParentsOfConcept(concept);
        for (Concept parent : parents) {
            if (!res.contains(parent)) {
                res.add(parent);
            }
            List<Concept> tmp = getAllAncestors(parent);
            for (Concept tmpC : tmp) {
                if (!res.contains(tmpC)) {
                    res.add(tmpC);
                }
            }
        }
        return res;
    }

    /**
     * Finds all concepts that are offsprings (lower in hierarchy) of a given
     * concept
     *
     * @param concept
     * @return a list of concepts
     */
    @Override
    public List<Concept> getAllOffsprings(Concept concept) {
        List<Concept> res = new ArrayList<>();
        List<Concept> children = getChildrenOfConcept(concept);
        for (Concept child : children) {
            if (!res.contains(child)) {
                res.add(child);
            }
            List<Concept> tmp = getAllOffsprings(child);
            for (Concept tmpC : tmp) {
                if (!res.contains(tmpC)) {
                    res.add(tmpC);
                }
            }
        }
        return res;
    }

    /**
     * Finds all concepts that are classes of instance (has-instance related) of
     * a given concept
     *
     * @param c the concept
     * @return a list of concepts
     */
    @Override
    public List<Concept> getClassesOfInstance(Concept concept) {
        List<Concept> res = new ArrayList<>();
        RelationDao rDao = new RelationDaoImpl();
        List<Relation> relations = rDao.getAllRelationsOfConcept(concept);
        for (Relation relation : relations) {
            if (relation.getType().getForwardName() ==
                    RelationType.relation_name_forward.HAS_INSTANCE &&
                    relation.getObject().equals(concept)) {
                res.add(relation.getSubject());
            }
        }
        return res;
    }

    /**
     * Finds all concepts that are instances (instance-of related) of a given
     * concept
     *
     * @param c the concept
     * @return a list of concepts
     */
    @Override
    public List<Concept> getInstancesOf(Concept concept) {
        List<Concept> res = new ArrayList<>();
        RelationDao rDao = new RelationDaoImpl();
        List<Relation> relations = rDao.getAllRelationsOfConcept(concept);
        for (Relation relation : relations) {
            if (relation.getType().getForwardName() ==
                    RelationType.relation_name_forward.HAS_INSTANCE &&
                    relation.getObject().equals(concept)) {
                res.add(relation.getSubject());
            }
        }

        return res;
    }

    /**
     * Finds all the Basic Level concepts for the given concept
     *
     * @param c concept to be checked
     * @return The list of BL
     */
    @Override
    public List<Concept> getBasicLevel(Concept concept) {
// Temporarily disabled the block below until cleared
//        if (c.getOrigin() == Concept.Origin.MOVEMENT)
//        {
//            return getBasicLevelOfMovementOriginConcept(concept);
//        }
//      else
//        {

        // AN BL επιστρέφει λίστα με τον εαυτό του.
        // Αν είναι above BL getBLofanabstractlevel
        // ελσε ιφ below BL τρέξε BL entity concept
        if (concept.getConceptType() == Concept.type.ABSTRACT) {
            return getBasicLevelOfAnAbstractConcept(concept);
        } else {
            if (concept.getConceptType() == Concept.type.ENTITY ||
                    concept.getConceptType() == Concept.type.MOVEMENT ||
                    concept.getConceptType() == Concept.type.FEATURE) {
                return getBasicLevelOfAnEntityConcept(concept);
            }
        }
//        }

        return new ArrayList<>();
    }

    /**
     * Finds all the Basic Level concepts for the given non abstract concept.
     *
     * @param con
     * @return The list of BL
     */
    @Override
    public List<Concept> getBasicLevelOfAnEntityConcept(Concept concept) {
        List<Concept> res = new ArrayList<>();

        if (concept.getSpecificityLevel() !=
                Concept.specificity_level.BASIC_LEVEL &&
                concept.getConceptType() != Concept.type.ABSTRACT) {
            List<Concept> parents = getParentsOfConcept(concept);
            for (Concept parent : parents) {
                res.addAll(getBasicLevelOfAnEntityConcept(parent));
            }

            if (parents.isEmpty()) {
                List<Concept> classes = getClassesOfInstance(concept);
                for (Concept classe : classes) {
                    res.addAll(getBasicLevelOfAnEntityConcept(classe));
                }
            }
        } else {
            if (concept.getSpecificityLevel() ==
                    Concept.specificity_level.BASIC_LEVEL) {
                res.add(concept);
            }
        }
        return res;
    }

    /**
     * Finds all the Basic Level concepts for the given abstract concept.
     *
     * @param c concept to be checked
     * @return The list of BL
     */
    @Override
    public List<Concept> getBasicLevelOfAnAbstractConcept(Concept concept) {
        List<Concept> res = new ArrayList<>();

        if (concept.getSpecificityLevel() !=
                Concept.specificity_level.BASIC_LEVEL &&
                concept.getConceptType() == Concept.type.ABSTRACT) {
            List<Concept> children = getChildrenOfConcept(concept);
            for (Concept children1 : children) {
                res.addAll(getBasicLevelOfAnAbstractConcept(children1));
            }
        } else {
            if (concept.getSpecificityLevel() ==
                    Concept.specificity_level.BASIC_LEVEL) {
                res.add(concept);
            }
        }
        return res;
    }

    /**
     * Finds all the Basic Level concepts for the given movement origin concept.
     *
     * @param c concept to be checked
     * @return The list of BL
     */
    // special getting BL for movement origin concepts 
    // lookin up and down regardless type
    private List<Concept> getBasicLevelOfMovementOriginConcept(Concept concept) {
        List<Concept> res = new ArrayList<>();

        if (concept.getSpecificityLevel() ==
                Concept.specificity_level.BASIC_LEVEL) {
            res.add(concept);
        } else {
            res.addAll(getBasicLevelOfMovementOriginConceptGoingDown(concept));
            res.addAll(getBasicLevelOfMovementOriginConceptGoingUp(concept));
        }
        return res;
    }

    /**
     * Finds all the Basic Level concepts for the given concept, moving only up
     * in the hierarchy.
     *
     * @param concept concept to be checked
     * @return The list of BL
     */
    private List<Concept> getBasicLevelOfMovementOriginConceptGoingUp(
            Concept concept) {
        List<Concept> res = new ArrayList<>();

        if (concept.getSpecificityLevel() !=
                Concept.specificity_level.BASIC_LEVEL) {
            List<Concept> parents = getParentsOfConcept(concept);
            for (Concept parent : parents) {
                res.addAll(getBasicLevelOfMovementOriginConceptGoingUp(parent));
            }

            if (parents.isEmpty()) {
                List<Concept> classes = getClassesOfInstance(concept);
                for (Concept classe : classes) {
                    res.addAll(getBasicLevelOfMovementOriginConceptGoingUp(
                            classe));
                }
            }
        } else {
            if (concept.getSpecificityLevel() ==
                    Concept.specificity_level.BASIC_LEVEL) {
                res.add(concept);
            }
        }
        return res;
    }

    /**
     * Finds all the Basic Level concepts for the given concept, moving only
     * down in the hierarchy.
     *
     * @param c concept to be checked
     * @return The list of BL
     */
    private List<Concept> getBasicLevelOfMovementOriginConceptGoingDown(
            Concept concept) {
        List<Concept> res = new ArrayList<>();

        if (concept.getSpecificityLevel() !=
                Concept.specificity_level.BASIC_LEVEL) {
            List<Concept> children = getChildrenOfConcept(concept);
            for (Concept children1 : children) {
                res.addAll(getBasicLevelOfMovementOriginConceptGoingDown(
                        children1));
            }
        } else {
            if (concept.getSpecificityLevel() ==
                    Concept.specificity_level.BASIC_LEVEL) {
                res.add(concept);
            }
        }
        return res;
    }

    /**
     * Finds all concepts that are related to a given concept using a given
     * relation type
     *
     * @param c             the concept
     * @param relation_type the type of relation (direction sensitive)
     * @return a list of concepts
     */
    @Override
    public List<Concept> getConceptsRelatedWithByRelationType(
            Concept concept, RelationType relationType) {
        List<Concept> res = new ArrayList<>();
        Query query = getEntityManager().createNamedQuery(
                "findRelationsByRelationType").
                setParameter("subject_concept_id", concept.getId()).
                setParameter("object_concept_id", concept.getId()).
                setParameter("relation_type_id", relationType);
        Concept tmpConcept = (Concept)query.getSingleResult();

        List<Relation> tmpR = query.getResultList();
        if (tmpR != null && tmpR.size() > 0) {
            for (Relation tmpR1 : tmpR) {
                if (tmpR1.getSubject().equals(concept)) {
                    res.add(tmpR1.getObject());
                } else {
                    res.add(tmpR1.getSubject());
                }
            }
        }
        return res;
    }

    /**
     * Clears the entity manager
     */
    @Override
    public void clearManager() {
        getEntityManager().clear();
    }

    // TODO: All methods below are not referenced in ConceptDao
    /**
     * Creates q query to search for a concept using name, type, status and
     * pragmatic status
     *
     * @param concept the concept to be searched
     * @return a query to search for the concept
     */
    @Override
    public Query getEntityQuery(Concept concept) {
        Query query = getEntityManager().createNamedQuery(
                "getConceptEntityQuery").
                setParameter("name", concept.getName()).
                setParameter("type", concept.getStatus()).
                setParameter("status", concept.getStatus()).
                setParameter("pragmatic_status", concept.getPragmaticStatus());
        System.out.println("Concept name: " + concept.getName());
        return query;
    }

    /**
     * Updates the language representations of a concept, adding the
     * LanguageRepresentation of another concept (removing them from that
     * concept).
     *
     * @param newConcept concept with LanguageRepresentation to be moved
     * @param oldConcept concept to be updated
     */
    private void updateLanguageRepresentations(Concept newConcept,
            Concept oldConcept) {
        try {
            for (int i = 0; i < newConcept.getLanguageRepresentations().size();
                    i++) {
                if (!oldConcept.getLanguageRepresentations().
                        contains(newConcept.getLanguageRepresentations().
                                get(i))) {
                    Concept_LanguageRepresentation tmpLanguageRepresentation =
                            newConcept.getLanguageRepresentations().get(i);
                    tmpLanguageRepresentation.getLanguageRepresentation().
                            getConcepts().remove(newConcept);
                    tmpLanguageRepresentation.getLanguageRepresentation().
                            getConcepts().add(oldConcept);
                    oldConcept.getLanguageRepresentations().
                            add(tmpLanguageRepresentation);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //    entityManager.getTransaction().rollback();
        }
        // System.out.println("DONE WITH LanguageRepresentation");

    }

    /**
     * Updates the motoric representations of a concept, adding the
     * MotoricRepresentations of another concept (removing them from that
     * concept).
     *
     * @param newConcept concept with MotoricRepresentations to be moved
     * @param oldConcept concept to be updated
     */
    private void updateMotoricRepresentations(Concept newConcept,
            Concept oldConcept) {
        for (int i = 0; i < newConcept.getMotoricRepresentations().size();
                i++) {
            if (!oldConcept.getMotoricRepresentations().
                    contains(newConcept.getMotoricRepresentations().get(i))) {
                oldConcept.getMotoricRepresentations().
                        add(newConcept.getMotoricRepresentations().get(i));
            }
        }
    }

    /**
     * Updates the ObjectOf relations of a concept, adding the ObjectOf
     * relations of another concept (removing them from that concept).
     *
     * @param newConcept concept with ObjectOf relations to be moved
     * @param oldConcept concept to be updated
     */
    private void updateObjOfRelations(Concept newConcept, Concept oldConcept) {
        for (int i = 0;
                i < newConcept.getRelationsContainingConceptAsObject().size();
                i++) {
            if (!oldConcept.getRelationsContainingConceptAsObject().
                    contains(newConcept.getRelationsContainingConceptAsObject().
                            get(i))) {
                if (newConcept.getRelationsContainingConceptAsObject().get(i).
                        getObject().equals(newConcept)) {
                    newConcept.getRelationsContainingConceptAsObject().get(i).
                            setObject(oldConcept);
                } else {
                    newConcept.getRelationsContainingConceptAsObject().get(i).
                            setSubject(oldConcept);
                }
                oldConcept.getRelationsContainingConceptAsObject().
                        add(newConcept.getRelationsContainingConceptAsObject().
                                get(i));
            }
        }
    }

    /**
     * Updates the relations of a concept, adding the relations of another
     * concept (removing them from that concept).
     *
     * @param newConcept concept with relations to be moved
     * @param oldConcept concept to be updated
     */
    private void updateRelations(Concept newConcept, Concept oldConcept) {
        for (int i = 0; i < newConcept.getIntersectionsOfRelationChains().size();
                i++) {
            if (!oldConcept.getIntersectionsOfRelationChains().
                    contains(newConcept.getIntersectionsOfRelationChains().
                            get(i))) {
                newConcept.getIntersectionsOfRelationChains().get(i).
                        setConcept(oldConcept);
                IntersectionOfRelationChains inter =
                        newConcept.getIntersectionsOfRelationChains().get(i);
                for (int k = 0; k < inter.getRelationChains().size(); k++) {
                    RelationChain rc = inter.getRelationChains().get(k);
                    for (int l = 0; l < rc.getRelations().size(); l++) {
                        RelationChain_Relation rcr =
                                rc.getRelations().get(l);
                        Relation rel = rcr.getRelation();
                        if (rel.getSubject().getName().
                                equalsIgnoreCase(newConcept.getName())) {
                            rel.setSubject(oldConcept);
                        } else {
                            if (rel.getObject().getName().
                                    equalsIgnoreCase(newConcept.getName())) {
                                rel.setObject(oldConcept);
                            }
                        }
                    }
                }

                oldConcept.getIntersectionsOfRelationChains().
                        add(newConcept.getIntersectionsOfRelationChains().
                                get(i));
            }
        }
    }

    /**
     * Updates the visual representations of a concept, adding the
     * VisualRepresentations of another concept (removing them from that
     * concept).
     *
     * @param newConcept concept with VisualRepresentations to be moved
     * @param oldConcept concept to be updated
     */
    private void updateVisualRepresentations(Concept newConcept,
            Concept oldConcept) {
        for (int i = 0; i < newConcept.getVisualRepresentationsEntries().size();
                i++) {
            if (!oldConcept.getVisualRepresentationsEntries().
                    contains(newConcept.getVisualRepresentationsEntries().
                            get(i))) {
                oldConcept.getVisualRepresentationsEntries().
                        add(newConcept.getVisualRepresentationsEntries().
                                get(i));
            }
        }
    }
}
