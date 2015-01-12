/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.ConceptDao;
import gr.csri.poeticon.praxicon.db.dao.RelationDao;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import static gr.csri.poeticon.praxicon.db.entities.Concept.SpecificityLevel.BASIC_LEVEL;
import static gr.csri.poeticon.praxicon.db.entities.Concept.SpecificityLevel.BASIC_LEVEL_EXTENDED;
import static gr.csri.poeticon.praxicon.db.entities.Concept.SpecificityLevel.SUBORDINATE;
import static gr.csri.poeticon.praxicon.db.entities.Concept.SpecificityLevel.SUPERORDINATE;
import gr.csri.poeticon.praxicon.db.entities.Concept.Status;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationType;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 */
public class ConceptDaoImpl extends JpaDao<Long, Concept> implements
        ConceptDao {

    private String String;

    public static enum Direction {

        UP, DOWN, NONE;

        @Override
        public String toString() {
            return this.name();
        }
    }

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
     * Finds all basic level concepts
     *
     * @return a list of all concepts in the database
     */
    @Override
    public List<Concept> findAllBasicLevelConcepts() {
        Query query = getEntityManager().createNamedQuery(
                "findAllBasicLevelConcepts");
        List<Concept> concepts = query.getResultList();
        return concepts;
    }

    /**
     * Finds all non-basic level concepts
     *
     * @return a list of all concepts in the database
     */
    @Override
    public List<Concept> findAllNonBasicLevelConcepts() {
        Query query = getEntityManager().createNamedQuery(
                "findAllNonBasicLevelConcepts");
        List<Concept> concepts = query.getResultList();
        return concepts;
    }

    /**
     * Finds all concepts that have a specific conceptId
     *
     * @param conceptId the concept id to search for
     * @return a list of concepts found in the database
     */
    @Override
    public Concept findConceptByConceptId(long conceptId) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByConceptId").
                setParameter("conceptId", conceptId);
        return (Concept)query.getSingleResult();
    }

    /**
     * Finds all concepts that have a name or language representation containing
     * a given string
     *
     * @param conceptExternalSourceId the external source id of the concept
     * @return a list of concepts found in the database
     */
    @Override
    public List<Concept> findConceptsByExternalSourceId(
            String conceptExternalSourceId) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByExternalSourceId").
                setParameter("conceptExternalSourceId", "%" +
                        conceptExternalSourceId + "%");
        return query.getResultList();
    }

    /**
     * Finds all concepts that have a name equal to a given string
     *
     * @param conceptExternalSourceId the concept name to search for
     * @return a list of concepts found in the database
     */
    @Override
    public Concept findConceptByExternalSourceIdExact(
            String conceptExternalSourceId) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptByExternalSourceIdExact").
                setParameter("conceptExternalSourceId", conceptExternalSourceId);
        return (Concept)query.getSingleResult();
    }

    /**
     * Finds all concepts that have a language representation containing a given
     * string
     *
     * @param languageRepresentationName the language representation name to
     *                                   search for
     * @return a list of concepts found in the database
     */
    @Override
    public List<Concept> findConceptsByLanguageRepresentation(
            String languageRepresentationName) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByLanguageRepresentation").
                setParameter("languageRepresentationName", "%" +
                        languageRepresentationName + "%");
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
                setParameter("languageRepresentationName",
                        languageRepresentationName);
        return query.getResultList();
    }

    /**
     * Finds all concepts that have a Status equal to a given Status
     *
     * @param status the concept Status to search for
     * @return a list of concepts found in the database
     */
    @Override
    public List<Concept> findConceptsByStatus(Status status) {
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
    public Concept getConceptWithExternalSourceIdOrId(String v) {
        Query q;
        long id = -1;
        try {
            id = Long.parseLong(v);
        } catch (NumberFormatException e) {
            //it is the name of the concept
        }
        if (id == -1) {
            return findConceptByExternalSourceIdExact(v.trim());
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
            oldConcept = this.findConceptByExternalSourceIdExact(newConcept.
                    getExternalSourceId());
        } catch (Exception e) {

            return newConcept;
        } finally {
            oldConcept.setConceptType(newConcept.getConceptType());
            oldConcept.setStatus(newConcept.getStatus());
            oldConcept.setSpecificityLevel(newConcept.getSpecificityLevel());
            oldConcept.setComment(newConcept.getComment());

            updateLanguageRepresentations(newConcept, oldConcept);
            updateVisualRepresentations(newConcept, oldConcept);
            updateMotoricRepresentations(newConcept, oldConcept);
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
                    "findConceptByExternalSourceIdExact").
                    setParameter("conceptExternalSourceId", newConcept.
                            getExternalSourceId());
            Concept tmpConcept = (Concept)query.getSingleResult();

            Concept oldConcept = null;

            if (tmpConcept.equals(null)) {
                oldConcept = new Concept();
            } else {
                oldConcept = tmpConcept;
            }
            if (oldConcept.getConceptType() == null ||
                    oldConcept.getConceptType() == Concept.Type.UNKNOWN) {
                oldConcept.setConceptType(newConcept.getConceptType());
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
// These are not needed any more since the relation argument has replace concept 
// as the rightArgument of a relation
//            updateObjOfRelations(newConcept, oldConcept);
            oldConcept = entityManager.merge(oldConcept);
            entityManager.getTransaction().commit();
            if (!getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().begin();
            }
// These are not needed any more since the relation argument has replace concept 
// as the object of a relation
//            updateRelations(newConcept, oldConcept);
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
                    oldConcept.getConceptType() == Concept.Type.UNKNOWN) {
                oldConcept.setConceptType(newConcept.getConceptType());
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
            merge(oldConcept);
            updateLanguageRepresentations(newConcept, oldConcept);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds all concepts that are children (TYPE_TOKEN relation) of a given
     * concept.
     *
     * @param concept the concept
     * @return a list of concepts
     */
    @Override
    public List<Concept> getChildrenOfConcept(Concept concept) {
        List<Concept> conceptList = new ArrayList<>();
        RelationDao rDao = new RelationDaoImpl();
        List<Relation> relations = rDao.getAllRelationsOfConcept(concept);
        for (Relation relation : relations) {
            if (relation.getType().getForwardName() ==
                    RelationType.RelationNameForward.TYPE_TOKEN &&
                    relation.getLeftArgument().getConcept().equals(concept)) {
                if (relation.getRightArgument().isConcept()) {
                    conceptList.add(relation.getRightArgument().getConcept());
                } else {
                    System.err.println("A relation set cannot have children");
                }
            }
        }
        entityManager.clear();
        return conceptList;
    }

    /**
     * Finds all concepts that are parents (TOKEN_TYPE relation) of a given
     * concept.
     *
     * @param concept the concept
     * @return a list of concepts
     */
    @Override
    public List<Concept> getParentsOfConcept(Concept concept) {
        List<Concept> conceptList = new ArrayList<>();
        RelationDao rDao = new RelationDaoImpl();
        List<Relation> relations = rDao.getAllRelationsOfConcept(concept);
        for (Relation relation : relations) {
            if (relation.getType().getForwardName() ==
                    RelationType.RelationNameForward.TYPE_TOKEN) {
                if (relation.getLeftArgument().isConcept() &&
                        relation.getRightArgument().getConcept().equals(concept)) {
                    conceptList.add(relation.getLeftArgument().getConcept());
                } else {
                    System.err.println("A relation set cannot have children");
                }
            }
        }
        entityManager.clear();
        return conceptList;
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
        List<Concept> ancestorConcepts = new ArrayList<>();

        List<Concept> parents = getParentsOfConcept(concept);
        for (Concept parent : parents) {
            if (!ancestorConcepts.contains(parent)) {
                ancestorConcepts.add(parent);
            }
            List<Concept> ancestors = getAllAncestors(parent);
            for (Concept ancestor : ancestors) {
                if (!ancestorConcepts.contains(ancestor)) {
                    ancestorConcepts.add(ancestor);
                }
            }
        }
        return ancestorConcepts;
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
        List<Concept> offspringConcepts = new ArrayList<>();

        List<Concept> children = getChildrenOfConcept(concept);

        for (Concept child : children) {
            if (!offspringConcepts.contains(child)) {
                offspringConcepts.add(child);
            }
            List<Concept> offsprings = getAllOffsprings(child);
            for (Concept offspring : offsprings) {
                if (!offspringConcepts.contains(offspring)) {
                    offspringConcepts.add(offspring);
                }
            }
        }
        return offspringConcepts;
    }

    /**
     * This is the old implementation of finding the basic levels of a concept.
     * Algorithm:
     * If the concept is subordinate, then:
     * - Store all ancestors of concept in concept list
     * Else if the concept is superordinate then:
     * - Store both offsprings and ancestors of concept in concept list
     * For each concept in concept list:
     * - If the concept is basic level, add it to the list of BL concepts
     * Return the list of BL concepts
     *
     * @param concept concept to be checked
     * @return The list of Basic Level Concepts
     */
    public List<Map.Entry<Concept, Direction>>
            getBasicLevelConceptsOld(Concept concept) {
        List<Concept> conceptsListUp = new ArrayList<>();
        List<Concept> conceptsListDown = new ArrayList<>();
        List<Map.Entry<Concept, Direction>> basicLevelConceptsList;
        basicLevelConceptsList = new ArrayList<>();
        Concept.SpecificityLevel specificityLevel = concept.
                getSpecificityLevel();

        if (specificityLevel == BASIC_LEVEL || specificityLevel ==
                BASIC_LEVEL_EXTENDED) {
            AbstractMap.SimpleEntry<Concept, Direction> pair =
                    new java.util.AbstractMap.SimpleEntry<>(concept,
                            Direction.NONE);
            basicLevelConceptsList.add(pair);
        } else if (specificityLevel == SUBORDINATE) {
            conceptsListUp.addAll(getAllAncestors(concept));
            for (Concept upConcept : conceptsListUp) {
                Concept.SpecificityLevel specificityLevelUp = upConcept.
                        getSpecificityLevel();
                if (specificityLevelUp == BASIC_LEVEL || specificityLevelUp ==
                        BASIC_LEVEL_EXTENDED) {
                    AbstractMap.SimpleEntry<Concept, Direction> pair =
                            new java.util.AbstractMap.SimpleEntry<>(upConcept,
                                    Direction.UP);
                    basicLevelConceptsList.add(pair);
                }
            }
        } else if (specificityLevel == SUPERORDINATE) {
            conceptsListDown.addAll(getAllOffsprings(concept));
            conceptsListUp.addAll(getAllAncestors(concept));

            for (Concept downConcept : conceptsListDown) {
                Concept.SpecificityLevel specificityLevelDown = downConcept.
                        getSpecificityLevel();
                if (specificityLevelDown == BASIC_LEVEL ||
                        specificityLevelDown == BASIC_LEVEL_EXTENDED) {
                    AbstractMap.SimpleEntry<Concept, Direction> pair =
                            new java.util.AbstractMap.SimpleEntry<>(downConcept,
                                    Direction.DOWN);
                    basicLevelConceptsList.add(pair);
                }
            }
            for (Concept upConcept : conceptsListUp) {
                Concept.SpecificityLevel specificityLevelUp = upConcept.
                        getSpecificityLevel();
                if (specificityLevelUp == BASIC_LEVEL || specificityLevelUp ==
                        BASIC_LEVEL_EXTENDED) {
                    AbstractMap.SimpleEntry<Concept, Direction> pair =
                            new java.util.AbstractMap.SimpleEntry<>(upConcept,
                                    Direction.UP);
                    basicLevelConceptsList.add(pair);
                }
            }
        }
        return basicLevelConceptsList;
    }

    /**
     * Finds all the Basic Level concepts for the given concept.
     * New algorithm which takes advantage of the direct basic level TYPE_TOKEN
     * relations added after the creation of the database:
     * Return all concepts related to the concept and are BASIC_LEVEL or
     * BASIC_LEVEL_EXTENDED.
     *
     * @param concept concept to be checked
     * @return A list of Basic Level Concepts
     */
    @Override
    public List<Concept> getBasicLevelConcepts(Concept concept) {
        List<Concept> basicLevelConceptsList;
        basicLevelConceptsList = new ArrayList<>();
        RelationDao rDao = new RelationDaoImpl();
        Concept.SpecificityLevel specificityLevel = concept.
                getSpecificityLevel();

        if (specificityLevel == BASIC_LEVEL || specificityLevel ==
                BASIC_LEVEL_EXTENDED) {
            basicLevelConceptsList.add(concept);
        } else if (specificityLevel == SUBORDINATE ||
                specificityLevel == SUPERORDINATE) {
            List<Relation> rightRelations = rDao.
                    getRelationsByRightConceptRelationType(
                            concept, RelationType.RelationNameForward.TYPE_TOKEN);
            List<Relation> leftRelations = rDao.
                    getRelationsByLeftConceptRelationType(
                            concept, RelationType.RelationNameForward.TYPE_TOKEN);

            for (Relation relation : leftRelations) {
                Concept rightConcept = relation.getRightArgument().
                        getConcept();
                if (rightConcept.getSpecificityLevel() == BASIC_LEVEL ||
                        rightConcept.getSpecificityLevel() ==
                        BASIC_LEVEL_EXTENDED) {
                    basicLevelConceptsList.add(rightConcept);
                }
            }
            for (Relation relation : rightRelations) {
                Concept leftConcept = relation.getLeftArgument().
                        getConcept();
                if (leftConcept.getSpecificityLevel() == BASIC_LEVEL ||
                        leftConcept.getSpecificityLevel() ==
                        BASIC_LEVEL_EXTENDED) {
                    basicLevelConceptsList.add(leftConcept);
                }
            }
        }
        return basicLevelConceptsList;
    }

    /**
     * Finds all concepts that are classes of instance (has-instance related) of
     * a given concept
     *
     * @param concept the concept
     * @return a list of concepts
     */
    @Override
    public List<Concept> getClassesOfInstance(Concept concept
    ) {
        List<Concept> res = new ArrayList<>();
        RelationDao rDao = new RelationDaoImpl();
        List<Relation> relations = rDao.getAllRelationsOfConcept(concept);
        for (Relation relation : relations) {
            if (relation.getType().getForwardName() ==
                    RelationType.RelationNameForward.HAS_INSTANCE &&
                    relation.getRightArgument().equals(concept)) {
                if (relation.getRightArgument().isConcept()) {
                    res.add(relation.getLeftArgument().getConcept());
                }
            }
        }
        return res;
    }

    /**
     * Finds all concepts that are instances (instance-of related) of a given
     * concept
     *
     * @param concept the concept
     * @return a list of concepts
     */
    @Override
    public List<Concept> getInstancesOf(Concept concept
    ) {
        List<Concept> res = new ArrayList<>();
        RelationDao rDao = new RelationDaoImpl();
        List<Relation> relations = rDao.getAllRelationsOfConcept(concept);
        for (Relation relation : relations) {
            if (relation.getType().getForwardName() ==
                    RelationType.RelationNameForward.HAS_INSTANCE &&
                    relation.getRightArgument().equals(concept)) {
                if (relation.getRightArgument().isConcept()) {
                    res.add(relation.getLeftArgument().getConcept());
                }
            }
        }

        return res;
    }

//    /**
//     * Finds all the Basic Level concepts for the given non abstract concept.
//     *
//     * @param concept
//     * @return The list of BL
//     */
//    @Override
//    public List<Concept> getBasicLevelOfAnEntityConcept(Concept concept) {
//        List<Concept> offspringConcepts = new ArrayList<>();
//        if (concept.getSpecificityLevel() !=
//                Concept.SpecificityLevel.BASIC_LEVEL &&
//                concept.getConceptType() != Concept.Type.ABSTRACT) {
//            List<Concept> parents = getParentsOfConcept(concept);
//            for (Concept parent : parents) {
//                offspringConcepts.addAll(getBasicLevelOfAnEntityConcept(parent));
//            }
//
//            if (parents.isEmpty()) {
//                List<Concept> classes = getClassesOfInstance(concept);
//                for (Concept classe : classes) {
//                    offspringConcepts.addAll(getBasicLevelOfAnEntityConcept(classe));
//                }
//            }
//        } else {
//            if (concept.getSpecificityLevel() ==
//                    Concept.SpecificityLevel.BASIC_LEVEL) {
//                offspringConcepts.add(concept);
//            }
//        }
//        return offspringConcepts;
//    }
//    /**
//     * Finds all the Basic Level concepts for the given abstract concept.
//     *
//     * @param concept concept to be checked
//     * @return The list of BL
//     */
//    @Override
//    public List<Concept> getBasicLevelOfAnAbstractConcept(Concept concept) {
//        List<Concept> offspringConcepts = new ArrayList<>();
//
//        if (concept.getSpecificityLevel() !=
//                Concept.SpecificityLevel.BASIC_LEVEL &&
//                concept.getConceptType() == Concept.Type.ABSTRACT) {
//            List<Concept> children = getChildrenOfConcept(concept);
//            for (Concept children1 : children) {
//                offspringConcepts.addAll(getBasicLevelOfAnAbstractConcept(children1));
//            }
//        } else {
//            if (concept.getSpecificityLevel() ==
//                    Concept.SpecificityLevel.BASIC_LEVEL) {
//                offspringConcepts.add(concept);
//            }
//        }
//        return offspringConcepts;
//    }
//    /**
//     * Finds all the Basic Level concepts for the given movement origin concept.
//     *
//     * @param concept concept to be checked
//     * @return The list of BL
//     */
//    // special getting BL for movement origin concepts 
//    // lookin up and down regardless Type
//    private List<Concept> getBasicLevelOfMovementOriginConcept(Concept concept) {
//        List<Concept> offspringConcepts = new ArrayList<>();
//
//        if (concept.getSpecificityLevel() ==
//                Concept.SpecificityLevel.BASIC_LEVEL) {
//            offspringConcepts.add(concept);
//        } else {
//            offspringConcepts.addAll(getBasicLevelOfMovementOriginConceptGoingDown(concept));
//            offspringConcepts.addAll(getBasicLevelOfMovementOriginConceptGoingUp(concept));
//        }
//        return offspringConcepts;
//    }
//    /**
//     * Finds all the Basic Level concepts for the given concept, moving only up
//     * in the hierarchy.
//     *
//     * @param concept concept to be checked
//     * @return The list of BL
//     */
//    private List<Concept> getBasicLevelOfMovementOriginConceptGoingUp(
//            Concept concept) {
//        List<Concept> offspringConcepts = new ArrayList<>();
//
//        if (concept.getSpecificityLevel() !=
//                Concept.SpecificityLevel.BASIC_LEVEL) {
//            List<Concept> parents = getParentsOfConcept(concept);
//            for (Concept parent : parents) {
//                offspringConcepts.addAll(getBasicLevelOfMovementOriginConceptGoingUp(parent));
//            }
//
//            if (parents.isEmpty()) {
//                List<Concept> classes = getClassesOfInstance(concept);
//                for (Concept classe : classes) {
//                    offspringConcepts.addAll(getBasicLevelOfMovementOriginConceptGoingUp(
//                            classe));
//                }
//            }
//        } else {
//            if (concept.getSpecificityLevel() ==
//                    Concept.SpecificityLevel.BASIC_LEVEL) {
//                offspringConcepts.add(concept);
//            }
//        }
//        return offspringConcepts;
//    }
//    /**
//     * Finds all the Basic Level concepts for the given concept, moving only
//     * down in the hierarchy.
//     *
//     * @param concept concept to be checked
//     * @return The list of BL
//     */
//    private List<Concept> getBasicLevelOfMovementOriginConceptGoingDown(
//            Concept concept) {
//        List<Concept> offspringConcepts = new ArrayList<>();
//
//        if (concept.getSpecificityLevel() !=
//                Concept.SpecificityLevel.BASIC_LEVEL) {
//            List<Concept> children = getChildrenOfConcept(concept);
//            for (Concept children1 : children) {
//                offspringConcepts.addAll(getBasicLevelOfMovementOriginConceptGoingDown(
//                        children1));
//            }
//        } else {
//            if (concept.getSpecificityLevel() ==
//                    Concept.SpecificityLevel.BASIC_LEVEL) {
//                offspringConcepts.add(concept);
//            }
//        }
//        return offspringConcepts;
//    }
    /**
     * Finds all concepts that are related to a given concept using a given
     * relation Type
     *
     * @param concept      the concept
     * @param relationType the Type of relation (direction sensitive)
     * @return a list of concepts
     */
    @Override
    public List<Concept> getConceptsRelatedWithByRelationType(
            Concept concept, RelationType relationType
    ) {
        List<Concept> res = new ArrayList<>();
        Query query = getEntityManager().createNamedQuery(
                "findRelationsByRelationType").
                setParameter("leftArgumentConceptId", concept.getId()).
                setParameter("rightArgumentConceptId", concept.getId()).
                setParameter("relationTypeId", relationType);
        Concept tmpConcept = (Concept)query.getSingleResult();

        List<Relation> tmpR = query.getResultList();
        if (tmpR != null && tmpR.size() > 0) {
            for (Relation tmpR1 : tmpR) {
                if (tmpR1.getLeftArgument().isConcept()) {
                    if (tmpR1.getLeftArgument().getConcept().equals(concept)) {
                        res.add(tmpR1.getRightArgument().getConcept());
                    } else {
                        res.add(tmpR1.getLeftArgument().getConcept());
                    }
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
     * Creates q query to search for a concept using name, Type, Status and
     * pragmatic Status
     *
     * @param concept the concept to be searched
     * @return a query to search for the concept
     */
    @Override
    public Query getEntityQuery(Concept concept
    ) {
        Query query = getEntityManager().createNamedQuery(
                "getConceptEntityQuery").
                setParameter("externalSourceId", concept.
                        getExternalSourceId()).
                setParameter("type", concept.getStatus()).
                setParameter("status", concept.getStatus()).
                setParameter("pragmaticStatus", concept.getPragmaticStatus());
        System.out.println("Concept externalSourceId: " + concept.
                getExternalSourceId());
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
                    LanguageRepresentation tmpLanguageRepresentation =
                            newConcept.getLanguageRepresentations().get(i);
                    tmpLanguageRepresentation.getConcepts().remove(newConcept);
                    tmpLanguageRepresentation.getConcepts().add(oldConcept);
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
