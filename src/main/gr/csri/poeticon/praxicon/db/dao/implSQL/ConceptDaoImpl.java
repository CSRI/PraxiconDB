/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.ConceptDao;
import gr.csri.poeticon.praxicon.db.dao.RelationDao;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import static gr.csri.poeticon.praxicon.db.entities.Concept.SpecificityLevel.BASIC_LEVEL;
import static gr.csri.poeticon.praxicon.db.entities.Concept.SpecificityLevel.SUBORDINATE;
import static gr.csri.poeticon.praxicon.db.entities.Concept.SpecificityLevel.SUPERORDINATE;
import gr.csri.poeticon.praxicon.db.entities.Concept.Status;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation;
import gr.csri.poeticon.praxicon.db.entities.MotoricRepresentation;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationType;
import static gr.csri.poeticon.praxicon.db.entities.RelationType.RelationNameForward.HAS_INSTANCE;
import static gr.csri.poeticon.praxicon.db.entities.RelationType.RelationNameForward.TYPE_TOKEN;
import gr.csri.poeticon.praxicon.db.entities.VisualRepresentation;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 */
public class ConceptDaoImpl extends JpaDao<Long, Concept> implements
        ConceptDao, Serializable {

    private String String;
    private Object cDao;

    public static enum Direction {

        UP, DOWN, NONE;

        @Override
        public String toString() {
            return this.name();
        }
    }

    /**
     * Finds all the retrievedConceptsSet.
     *
     * @return a list of all retrievedConceptsSet in the database
     */
    @Override
    public Set<Concept> getAllConcepts() {
        Query query = getEntityManager().createNamedQuery("findAllConcepts");
        Set<Concept> retrievedConceptsSet = new LinkedHashSet<Concept>(query.
                getResultList());
        return retrievedConceptsSet;
    }

    /**
     * Finds a unique concept according to another concept.
     *
     * @param concept the concept to search for.
     * @return the first concept which has the exact characteristics as the one
     *         in the input.
     *
     */
    @Override
    public Concept getConcept(Concept concept) {
        Query query = getEntityManager().createNamedQuery("findConcept").
                setParameter("name", concept.getName()).
                setParameter("externalSourceId", concept.getExternalSourceId()).
                setParameter("type", concept.getConceptType()).
                setParameter("specificityLevel", concept.getSpecificityLevel()).
                setParameter("status", concept.getStatus()).
                setParameter("pragmaticStatus", concept.getPragmaticStatus()).
                setParameter("uniqueInstance", concept.getUniqueInstance())//.
                //                setParameter("ontologicalDomain",
                //                        concept.getOntologicalDomain())
                ;
        Set<Concept> retrievedConceptsSet = new LinkedHashSet<>(query.
                getResultList());
        if (retrievedConceptsSet.isEmpty()) {
            return null;
        }
        return retrievedConceptsSet.iterator().next();
    }

    /**
     * Finds all basic level retrievedConceptsSet
     *
     * @return a list of all retrievedConceptsSet in the database
     */
    @Override
    public Set<Concept> getAllBasicLevelConcepts() {
        Query query = getEntityManager().createNamedQuery(
                "findAllBasicLevelConcepts");
        Set<Concept> retrievedConceptsSet =
                new LinkedHashSet<>(query.getResultList());
        return retrievedConceptsSet;
    }

    /**
     * Finds all non-basic level retrievedConceptsSet
     *
     * @return a list of all retrievedConceptsSet in the database
     */
    @Override
    public Set<Concept> getAllNonBasicLevelConcepts() {
        Query query = getEntityManager().createNamedQuery(
                "findAllNonBasicLevelConcepts");
        Set<Concept> retrievedConceptsSet =
                new LinkedHashSet<>(query.getResultList());
        return retrievedConceptsSet;
    }

    /**
     * Finds all retrievedConceptsSet that have a specific conceptId
     *
     * @param conceptId the concept id to find
     * @return a list of retrievedConceptsSet found in the database
     */
    @Override
    public Concept getConceptByConceptId(long conceptId) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByConceptId").
                setParameter("conceptId", conceptId);
        Set<Concept> retrievedConceptsSet =
                new LinkedHashSet<>(query.getResultList());
        if (retrievedConceptsSet.isEmpty()) {
            return null;
        }
        return retrievedConceptsSet.iterator().next();
    }

    /**
     * Finds all retrievedConceptsSet that have a name containing a given string
     *
     * @param conceptName the external source id of the concept to find
     * @return a list of retrievedConceptsSet found in the database
     */
    @Override
    public Set<Concept> getConceptsByName(
            String conceptName) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByName").
                setParameter("conceptName", "%" +
                        conceptName + "%");
        Set<Concept> retrievedConceptsSet =
                new LinkedHashSet<>(query.getResultList());
        return retrievedConceptsSet;
    }

    /**
     * Finds all retrievedConceptsSet that have a name equal to a given string
     *
     * @param conceptName the concept name to find
     * @return a unique concept found in the database
     */
    @Override
    public Concept getConceptByNameExact(
            String conceptName) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptByNameExact").
                setParameter("conceptName", conceptName);
        Set<Concept> retrievedConceptsSet = new LinkedHashSet<>(query.
                getResultList());
        if (retrievedConceptsSet.isEmpty()) {
            return null;
        }
        return retrievedConceptsSet.iterator().next();
    }

    /**
     * Finds all retrievedConceptsSet that have a name containing a given string
     *
     * @param conceptName the external source id of the concept to find
     * @param status      the status of the concept to find
     * @return a list of retrievedConceptsSet found in the database
     */
    @Override
    public LinkedHashSet<Concept> getConceptsByNameAndStatus(
            String conceptName, Status status) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByNameAndStatus").
                setParameter("conceptName", "%" + conceptName + "%").
                setParameter("conceptStatus", status);
        LinkedHashSet<Concept> retrievedConceptsSet =
                new LinkedHashSet<>(query.getResultList());
        return retrievedConceptsSet;
    }

    /**
     * Finds all retrievedConceptsSet that have an externalSourceId containing given string
     *
     * @param conceptExternalSourceId the external source id of the concept
     * @return a list of retrievedConceptsSet found in the database
     */
    @Override
    public LinkedHashSet<Concept> getConceptsByExternalSourceId(
            String conceptExternalSourceId) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByExternalSourceId").
                setParameter("conceptExternalSourceId", "%" +
                        conceptExternalSourceId + "%");
        LinkedHashSet<Concept> retrievedConceptsSet =
                new LinkedHashSet<>(query.getResultList());
        return retrievedConceptsSet;
    }

    /**
     * Finds all retrievedConceptsSet that have an ExternalSourceId equal to a given string
     *
     * @param conceptExternalSourceId the concept's external source id
     * @return a unique concept found in the database
     */
    @Override
    public Concept getConceptByExternalSourceIdExact(
            String conceptExternalSourceId) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptByExternalSourceIdExact").
                setParameter("conceptExternalSourceId", conceptExternalSourceId);
        LinkedHashSet<Concept> retrievedConceptsSet =
                new LinkedHashSet<>(query.getResultList());
        if (retrievedConceptsSet.isEmpty()) {
            return null;
        }
        return retrievedConceptsSet.iterator().next();
    }

    /**
     * Finds all retrievedConceptsSet that have a language representation containing a given
     * string
     *
     * @param languageRepresentationName the language representation name
     * @return a list of retrievedConceptsSet found in the database
     */
    @Override
    public LinkedHashSet<Concept> getConceptsByLanguageRepresentation(
            String languageRepresentationName) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByLanguageRepresentation").
                setParameter("languageRepresentationName", "%" +
                        languageRepresentationName + "%");
        LinkedHashSet<Concept> retrievedConceptsSet =
                new LinkedHashSet<>(query.getResultList());
        return retrievedConceptsSet;
    }

    /**
     * Finds all retrievedConceptsSet that have a language representation as an exact match
     * to the provided string
     *
     * @param languageRepresentationName the string to search for
     * @return a list of retrievedConceptsSet found in the database
     */
    @Override
    public LinkedHashSet<Concept> getConceptsByLanguageRepresentationExact(
            String languageRepresentationName) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByLanguageRepresentationExact").
                setParameter("languageRepresentationName",
                        languageRepresentationName);
        LinkedHashSet<Concept> retrievedConceptsSet =
                new LinkedHashSet<>(query.getResultList());
        return retrievedConceptsSet;
    }

    /**
     * Finds all retrievedConceptsSet that have a language representation starting with the
     * provided string
     *
     * @param languageRepresentationNameStart the string to search for
     * @return a list of retrievedConceptsSet found in the database
     */
    @Override
    public LinkedHashSet<Concept> getConceptsByLanguageRepresentationStartsWith(
            String languageRepresentationNameStart) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByLanguageRepresentationStartsWith").
                setParameter("languageRepresentationNameStart",
                        languageRepresentationNameStart + "%");
        LinkedHashSet<Concept> retrievedConceptsSet =
                new LinkedHashSet<>(query.getResultList());
        return retrievedConceptsSet;
    }

    /**
     * Finds all retrievedConceptsSet that have a language representation ending with the
     * provided string
     *
     * @param languageRepresentationNameEnd the string to search for
     * @return a list of retrievedConceptsSet found in the database
     */
    @Override
    public LinkedHashSet<Concept> getConceptsByLanguageRepresentationEndsWith(
            String languageRepresentationNameEnd) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByLanguageRepresentationEndsWith").
                setParameter("languageRepresentationNameEnd", "%" +
                        languageRepresentationNameEnd);
        LinkedHashSet<Concept> retrievedConceptsSet =
                new LinkedHashSet<>(query.getResultList());
        return retrievedConceptsSet;
    }

    /**
     * Finds all retrievedConceptsSet that have a Status equal to a given Status
     *
     * @param status the concept Status to search for
     * @return a list of retrievedConceptsSet found in the database
     */
    @Override
    public LinkedHashSet<Concept> getConceptsByStatus(Status status) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByStatusExact").
                setParameter("status", status);
        LinkedHashSet<Concept> retrievedConceptsSet =
                new LinkedHashSet<>(query.getResultList());
        return retrievedConceptsSet;
    }

    /**
     * Finds all retrievedConceptsSet that have a name or id equal to a given string
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
            return getConceptByExternalSourceIdExact(v.trim());
        } else {
            return getConceptByConceptId(id);
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
            oldConcept = this.getConcept(newConcept);
        } catch (Exception e) {
            return newConcept;
        } finally {
            oldConcept = new Concept(newConcept, true, true, true);
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
                    oldConcept.getConceptType() == Concept.ConceptType.UNKNOWN) {
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
                    oldConcept.getConceptType() == Concept.ConceptType.UNKNOWN) {
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
     * Finds all retrievedConceptsSet that are children (TYPE_TOKEN relation) of a given
     * concept.
     *
     * @param concept the concept
     * @return a list of retrievedConceptsSet
     */
    @Override
    public Set<Concept> getChildren(Concept concept) {
        Set<Concept> retrievedConceptsSet = new LinkedHashSet<>();
        RelationDao rDao = new RelationDaoImpl();
        Set<Relation> retrievedRelationsSet =
                rDao.getRelationsByLeftConceptTypeOfRelation(concept,
                        TYPE_TOKEN);
        for (Relation relation : retrievedRelationsSet) {
            if (relation.getRightArgument().isConcept()) {
                retrievedConceptsSet.add(relation.getRightArgument().
                        getConcept());
            }
        }
        entityManager.clear();
        return retrievedConceptsSet;
    }

    /**
     * Finds all retrievedConceptsSet that are parents (TOKEN_TYPE relation) of a given
     * concept.
     *
     * @param concept the concept
     * @return a list of retrievedConceptsSet
     */
    @Override
    public Set<Concept> getParents(Concept concept) {
        Set<Concept> retrievedConceptsSet = new LinkedHashSet<>();
        RelationDao rDao = new RelationDaoImpl();
        Set<Relation> retrievedRelationsSet =
                rDao.getRelationsByRightConceptTypeOfRelation(concept,
                        TYPE_TOKEN);
        for (Relation relation : retrievedRelationsSet) {
            if (relation.getLeftArgument().isConcept()) {
                retrievedConceptsSet.add(relation.getLeftArgument().
                        getConcept());
            }
        }
        entityManager.clear();
        return retrievedConceptsSet;
    }

    /**
     * Finds all retrievedConceptsSet that are ancestors (higher in hierarchy) of a given
     * concept
     *
     * @param concept
     * @return a list of retrievedConceptsSet
     */
    @Override
    public LinkedHashSet<Concept> getAllAncestors(Concept concept) {
        LinkedHashSet<Concept> ancestorConcepts = new LinkedHashSet<>();

        Set<Concept> parents = getParents(concept);
        for (Concept parent : parents) {
            if (!ancestorConcepts.contains(parent)) {
                ancestorConcepts.add(parent);
            }
            LinkedHashSet<Concept> ancestors = getAllAncestors(parent);
            for (Concept ancestor : ancestors) {
                if (!ancestorConcepts.contains(ancestor)) {
                    ancestorConcepts.add(ancestor);
                }
            }
        }
        return ancestorConcepts;
    }

    /**
     * Finds all retrievedConceptsSet that are offsprings (lower in hierarchy) of a given
     * concept
     *
     * @param concept
     * @return a list of retrievedConceptsSet
     */
    @Override
    public Set<Concept> getAllOffsprings(Concept concept) {
        Set<Concept> offspringConcepts = new LinkedHashSet<>();

        Set<Concept> children = getChildren(concept);

        for (Concept child : children) {
            if (!offspringConcepts.contains(child)) {
                offspringConcepts.add(child);
            }
            Set<Concept> offsprings = getAllOffsprings(child);
            for (Concept offspring : offsprings) {
                if (!offspringConcepts.contains(offspring)) {
                    offspringConcepts.add(offspring);
                }
            }
        }
        return offspringConcepts;
    }

    /**
     * Finds all the Basic Level retrievedConceptsSet for the given concept.
     * New algorithm which takes advantage of the direct basic level TYPE_TOKEN
     * retrievedRelationsSet added after the creation of the database:
     * Return all retrievedConceptsSet related to the concept and are BASIC_LEVEL.
     *
     * @param concept concept to be checked
     * @return A list of Basic Level Concepts
     */
    @Override
    public Set<Concept> getBasicLevelConcepts(Concept concept) {
        Set<Concept> basicLevelConceptsSet;
        basicLevelConceptsSet = new LinkedHashSet<>();
        RelationDao rDao = new RelationDaoImpl();
        Concept.SpecificityLevel specificityLevel = concept.
                getSpecificityLevel();

        if (specificityLevel == BASIC_LEVEL) {
            basicLevelConceptsSet.add(concept);
        } else if (specificityLevel == SUBORDINATE ||
                specificityLevel == SUPERORDINATE) {
            Set<Relation> relations = rDao.getRelationsByConceptRelationType(
                    concept, RelationType.RelationNameForward.TYPE_TOKEN);
            for (Relation relation : relations) {
                if (relation.getLeftArgument().isConcept()) {
                    Concept leftConcept = relation.getLeftArgument().
                            getConcept();
                    if (leftConcept.getSpecificityLevel() == BASIC_LEVEL) {
                        basicLevelConceptsSet.add(leftConcept);
                    }
                }
                if (relation.getRightArgument().isConcept()) {
                    Concept rightConcept = relation.getRightArgument().
                            getConcept();
                    if (rightConcept.getSpecificityLevel() == BASIC_LEVEL) {
                        basicLevelConceptsSet.add(rightConcept);
                    }
                }
            }
        }
        return basicLevelConceptsSet;
    }

    /**
     * This is the old implementation of finding the basic levels of a concept.
     * Algorithm:
     * If the concept is subordinate, then:
     * - Store all ancestors of concept in concept list
     * Else if the concept is superordinate then:
     * - Store both offsprings and ancestors of concept in concept list
     * For each concept in concept list:
     * - If the concept is basic level, add it to the list of BL retrievedConceptsSet
     * Return the list of BL retrievedConceptsSet
     *
     * @param concept concept to be checked
     * @return The list of Basic Level Concepts
     */
    @Override
    public Set<Map.Entry<Concept, Direction>>
            getBasicLevelConceptsOld(Concept concept) {
        Set<Concept> conceptsSetUp = new LinkedHashSet<>();
        Set<Concept> conceptsSetDown = new LinkedHashSet<>();
        Set<Map.Entry<Concept, Direction>> basicLevelConceptsSet;
        basicLevelConceptsSet = new LinkedHashSet<>();
        Concept.SpecificityLevel specificityLevel = concept.
                getSpecificityLevel();

        if (specificityLevel == BASIC_LEVEL) {
            AbstractMap.SimpleEntry<Concept, Direction> pair =
                    new java.util.AbstractMap.SimpleEntry<>(concept,
                            Direction.NONE);
            basicLevelConceptsSet.add(pair);
        } else if (specificityLevel == SUBORDINATE) {
            conceptsSetUp.addAll(getAllAncestors(concept));
            for (Concept upConcept : conceptsSetUp) {
                Concept.SpecificityLevel specificityLevelUp = upConcept.
                        getSpecificityLevel();
                if (specificityLevelUp == BASIC_LEVEL) {
                    AbstractMap.SimpleEntry<Concept, Direction> pair =
                            new java.util.AbstractMap.SimpleEntry<>(upConcept,
                                    Direction.UP);
                    basicLevelConceptsSet.add(pair);
                }
            }
        } else if (specificityLevel == SUPERORDINATE) {
            conceptsSetDown.addAll(getAllOffsprings(concept));
            conceptsSetUp.addAll(getAllAncestors(concept));

            for (Concept downConcept : conceptsSetDown) {
                Concept.SpecificityLevel specificityLevelDown = downConcept.
                        getSpecificityLevel();
                if (specificityLevelDown == BASIC_LEVEL) {
                    AbstractMap.SimpleEntry<Concept, Direction> pair =
                            new java.util.AbstractMap.SimpleEntry<>(downConcept,
                                    Direction.DOWN);
                    basicLevelConceptsSet.add(pair);
                }
            }
            for (Concept upConcept : conceptsSetUp) {
                Concept.SpecificityLevel specificityLevelUp = upConcept.
                        getSpecificityLevel();
                if (specificityLevelUp == BASIC_LEVEL) {
                    AbstractMap.SimpleEntry<Concept, Direction> pair =
                            new java.util.AbstractMap.SimpleEntry<>(upConcept,
                                    Direction.UP);
                    basicLevelConceptsSet.add(pair);
                }
            }
        }
        return basicLevelConceptsSet;
    }

    /**
     * Finds all retrievedConceptsSet that are classes of instance (has-instance related) of
     * a given concept
     *
     * @param concept the concept
     * @return a list of retrievedConceptsSet
     */
    @Override
    public Set<Concept> getClassesOfInstance(Concept concept) {
        Set<Concept> conceptSet = new LinkedHashSet<>();
        RelationDao rDao = new RelationDaoImpl();
        Set<Relation> relations =
                rDao.getRelationsByRightConceptTypeOfRelation(concept,
                        HAS_INSTANCE);
        for (Relation relation : relations) {
            if (relation.getLeftArgument().isConcept()) {
                conceptSet.add(relation.getLeftArgument().getConcept());
            }
        }
        entityManager.clear();
        return conceptSet;
    }

    /**
     * Finds all retrievedConceptsSet that are instances (instance-of related) of a given
     * concept
     *
     * @param concept the concept
     * @return a list of retrievedConceptsSet
     */
    @Override
    public Set<Concept> getInstancesOf(Concept concept) {
        Set<Concept> conceptSet = new LinkedHashSet<>();
        RelationDao rDao = new RelationDaoImpl();
        Set<Relation> relations =
                rDao.getRelationsByLeftConceptTypeOfRelation(concept,
                        HAS_INSTANCE);
        for (Relation relation : relations) {
            if (relation.getRightArgument().isConcept()) {
                conceptSet.add(relation.getRightArgument().getConcept());
            }
        }
        entityManager.clear();
        return conceptSet;
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
     * Creates q query to search for a concept using name, ConceptType, Status and
     * pragmatic Status
     *
     * @param concept the concept to be searched
     * @return a query to search for the concept
     */
    @Override
    public Query getEntityQuery(Concept concept) {
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
            for (LanguageRepresentation lr : newConcept.
                    getLanguageRepresentations()) {
                if (!oldConcept.getLanguageRepresentations().contains(lr)) {
                    LanguageRepresentation tmpLanguageRepresentation = lr;
                    tmpLanguageRepresentation.getConcepts().remove(newConcept);
                    tmpLanguageRepresentation.getConcepts().add(oldConcept);
                    oldConcept.getLanguageRepresentations().
                            add(tmpLanguageRepresentation);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
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
        for (MotoricRepresentation mr : newConcept.getMotoricRepresentations()) {
            if (!oldConcept.getMotoricRepresentations().contains(mr)) {
                oldConcept.getMotoricRepresentations().add(mr);
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
        for (VisualRepresentation vr : newConcept.
                getVisualRepresentationsEntries()) {
            if (!oldConcept.getVisualRepresentationsEntries().contains(vr)) {
                oldConcept.getVisualRepresentationsEntries().add(vr);
            }
        }
    }
}
