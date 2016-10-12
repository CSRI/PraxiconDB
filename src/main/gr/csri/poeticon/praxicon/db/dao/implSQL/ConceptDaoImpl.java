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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.hibernate.Session;

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
     * Finds all the retrievedConceptsList.
     *
     * @return a list of all retrievedConceptsList in the database
     */
    @Override
    public List<Concept> getAllConcepts() {
        Query query = getEntityManager().createNamedQuery("findAllConcepts");
        List<Concept> retrievedConceptsList = query.getResultList();
        return retrievedConceptsList;
    }

    /**
     * Finds a unique concept according to another concept.
     *
     * @param concept the concept to search for.
     *
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
        List<Concept> retrievedConceptsList = (List<Concept>)query.
                getResultList();
        if (retrievedConceptsList.isEmpty()) {
            return null;
        }
        return retrievedConceptsList.get(0);
    }

    /**
     * Finds all basic level retrievedConceptsList
     *
     * @return a list of all retrievedConceptsList in the database
     */
    @Override
    public List<Concept> getAllBasicLevelConcepts() {
        Query query = getEntityManager().createNamedQuery(
                "findAllBasicLevelConcepts");
        List<Concept> retrievedConceptsList = (List<Concept>)query.
                getResultList();
        return retrievedConceptsList;
    }

    /**
     * Finds all non-basic level retrievedConceptsList
     *
     * @return a list of all retrievedConceptsList in the database
     */
    @Override
    public List<Concept> getAllNonBasicLevelConcepts() {
        Query query = getEntityManager().createNamedQuery(
                "findAllNonBasicLevelConcepts");
        List<Concept> retrievedConceptsList = (List<Concept>)query.
                getResultList();
        return retrievedConceptsList;
    }

    /**
     * Finds all retrievedConceptsList that have a specific conceptId
     *
     * @param conceptId the concept id to find
     *
     * @return a list of retrievedConceptsList found in the database
     */
    @Override
    public Concept getConceptByConceptId(long conceptId) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByConceptId").
                setParameter("conceptId", conceptId);
        List<Concept> retrievedConceptsList = (List<Concept>)query.
                getResultList();
        if (retrievedConceptsList.isEmpty()) {
            return null;
        }
        return retrievedConceptsList.get(0);
    }

    /**
     * Finds all retrievedConceptsList that have a name containing a given
     * string
     *
     * @param conceptName the external source id of the concept to find
     *
     * @return a list of retrievedConceptsList found in the database
     */
    @Override
    public List<Concept> getConceptsByName(
            String conceptName) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByName").setParameter("conceptName", "%" +
                        conceptName + "%");
        List<Concept> retrievedConceptsList = (List<Concept>)query.
                getResultList();
        return retrievedConceptsList;
    }

    /**
     * Finds all retrievedConceptsList that have a name equal to a given string
     *
     * @param conceptName the concept name to find
     *
     * @return a unique concept found in the database
     */
    @Override
    public Concept getConceptByNameExact(
            String conceptName) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptByNameExact").
                setParameter("conceptName", conceptName);
        List<Concept> retrievedConceptsList = (List<Concept>)query.
                getResultList();
        if (retrievedConceptsList.isEmpty()) {
            return null;
        }
        return retrievedConceptsList.get(0);
    }

    /**
     * Finds all retrievedConceptsList that have a name containing a given
     * string
     *
     * @param conceptName the external source id of the concept to find
     * @param status      the status of the concept to find
     *
     * @return a list of retrievedConceptsList found in the database
     */
    @Override
    public List<Concept> getConceptsByNameAndStatus(
            String conceptName, Status status) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByNameAndStatus").
                setParameter("conceptName", "%" + conceptName + "%").
                setParameter("conceptStatus", status);
        List<Concept> retrievedConceptsList = (List<Concept>)query.
                getResultList();
        return retrievedConceptsList;
    }

    /**
     * Finds all retrievedConceptsList that have an externalSourceId containing
     * given string
     *
     * @param conceptExternalSourceId the external source id of the concept
     *
     * @return a list of retrievedConceptsList found in the database
     */
    @Override
    public List<Concept> getConceptsByExternalSourceId(
            String conceptExternalSourceId) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByExternalSourceId").
                setParameter("conceptExternalSourceId", "%" +
                        conceptExternalSourceId + "%");
        List<Concept> retrievedConceptsList = (List<Concept>)query.
                getResultList();
        return retrievedConceptsList;
    }

    /**
     * Finds all retrievedConceptsList that have an ExternalSourceId equal to a
     * given string
     *
     * @param conceptExternalSourceId the concept's external source id
     *
     * @return a unique concept found in the database
     */
    @Override
    public Concept getConceptByExternalSourceIdExact(
            String conceptExternalSourceId) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptByExternalSourceIdExact").
                setParameter("conceptExternalSourceId",
                        conceptExternalSourceId);
        List<Concept> retrievedConceptsList = (List<Concept>)query.
                getResultList();
        if (retrievedConceptsList.isEmpty()) {
            return null;
        }
        return retrievedConceptsList.get(0);
    }

    /**
     * Finds all retrievedConceptsList that have a language representation
     * containing a given string
     *
     * @param languageRepresentationName the language representation name
     *
     * @return a list of retrievedConceptsList found in the database
     */
    @Override
    public List<Concept> getConceptsByLanguageRepresentation(
            String languageRepresentationName) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByLanguageRepresentation").
                setParameter("languageRepresentationName", "%" +
                        languageRepresentationName + "%");
        List<Concept> retrievedConceptsList = (List<Concept>)query.
                getResultList();
        return retrievedConceptsList;
    }

    /**
     * Finds all retrievedConceptsList that have a language representation as an
     * exact match to the provided string
     *
     * @param languageRepresentationName the string to search for
     *
     * @return a list of retrievedConceptsList found in the database
     */
    @Override
    public List<Concept> getConceptsByLanguageRepresentationExact(
            String languageRepresentationName) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByLanguageRepresentationExact").
                setParameter("languageRepresentationName",
                        languageRepresentationName);
        List<Concept> retrievedConceptsList = (List<Concept>)query.
                getResultList();
        return retrievedConceptsList;
    }

    /**
     * Finds all retrievedConceptsList that have a language representation
     * starting with the provided string
     *
     * @param languageRepresentationNameStart the string to search for
     *
     * @return a list of retrievedConceptsList found in the database
     */
    @Override
    public List<Concept> getConceptsByLanguageRepresentationStartsWith(
            String languageRepresentationNameStart) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByLanguageRepresentationStartsWith").
                setParameter("languageRepresentationNameStart",
                        languageRepresentationNameStart + "%");
        List<Concept> retrievedConceptsList = (List<Concept>)query.
                getResultList();
        return retrievedConceptsList;
    }

    /**
     * Finds all retrievedConceptsList that have a language representation
     * ending with the provided string
     *
     * @param languageRepresentationNameEnd the string to search for
     *
     * @return a list of retrievedConceptsList found in the database
     */
    @Override
    public List<Concept> getConceptsByLanguageRepresentationEndsWith(
            String languageRepresentationNameEnd) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByLanguageRepresentationEndsWith").
                setParameter("languageRepresentationNameEnd", "%" +
                        languageRepresentationNameEnd);
        List<Concept> retrievedConceptsList = (List<Concept>)query.
                getResultList();
        return retrievedConceptsList;
    }

    /**
     * Finds all retrievedConceptsList that have a Status equal to a given
     * Status
     *
     * @param status the concept Status to search for
     *
     * @return a list of retrievedConceptsList found in the database
     */
    @Override
    public List<Concept> getConceptsByStatus(Status status) {
        Query query = getEntityManager().createNamedQuery(
                "findConceptsByStatusExact").
                setParameter("status", status);
        List<Concept> retrievedConceptsList = (List<Concept>)query.
                getResultList();
        return retrievedConceptsList;
    }

    /**
     * Finds all retrievedConceptsList that have a name or id equal to a given
     * string
     *
     * @param v
     *
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
     *
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
     * Finds all retrievedConceptsList that are children (TYPE_TOKEN relation)
     * of a given concept.
     *
     * @param concept the concept
     *
     * @return a list of retrievedConceptsList
     */
    @Override
    public List<Concept> getChildren(Concept concept) {
        List<Concept> retrievedConceptsList = new ArrayList<>();
        RelationDao rDao = new RelationDaoImpl();
        List<Relation> retrievedRelationsList =
                rDao.getRelationsByLeftConceptTypeOfRelation(concept,
                        TYPE_TOKEN);
        for (Relation relation : retrievedRelationsList) {
            if (relation.getRightArgument().isConcept()) {
                retrievedConceptsList.add(relation.getRightArgument().
                        getConcept());
            }
        }
        entityManager.clear();
        return retrievedConceptsList;
    }

    /**
     * Finds all retrievedConceptsList that are parents (TOKEN_TYPE relation) of
     * a given concept.
     *
     * @param concept the concept
     *
     * @return a list of retrievedConceptsList
     */
    @Override
    public List<Concept> getParents(Concept concept) {
        List<Concept> retrievedConceptsList = new ArrayList<>();
        RelationDao rDao = new RelationDaoImpl();
        List<Relation> retrievedRelationsList =
                rDao.getRelationsByRightConceptTypeOfRelation(concept,
                        TYPE_TOKEN);
        for (Relation relation : retrievedRelationsList) {
            if (relation.getLeftArgument().isConcept()) {
                retrievedConceptsList.add(relation.getLeftArgument().
                        getConcept());
            }
        }
        entityManager.clear();
        return retrievedConceptsList;
    }

    /**
     * Finds all retrievedConceptsList that are ancestors (higher in hierarchy)
     * of a given concept
     *
     * @param concept
     *
     * @return a list of retrievedConceptsList
     */
    @Override
    public List<Concept> getAllAncestors(Concept concept) {
        List<Concept> ancestorConcepts = new ArrayList<>();

        List<Concept> parents = getParents(concept);
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
     * Finds all retrievedConceptsList that are offsprings (lower in hierarchy)
     * of a given concept
     *
     * @param concept
     *
     * @return a list of retrievedConceptsList
     */
    @Override
    public List<Concept> getAllOffsprings(Concept concept) {
        EntityManager em = getEntityManager();
        Session session = em.unwrap(org.hibernate.Session.class);

        List<Concept> offspringConcepts = new ArrayList<>();
        List<Concept> children = getChildren(concept);

        for (Concept child : children) {
            /*
             * Check if child is contained in the active session and save it
             * if it's not.
             */
            if (!session.contains(child)) {
                session.save(child);
            }

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
     * Finds all the Basic Level retrievedConceptsList for the given concept.
     * New algorithm which takes advantage of the direct basic level TYPE_TOKEN
     * retrievedRelationsList added after the creation of the database: Return
     * all retrievedConceptsList related to the concept and are BASIC_LEVEL.
     *
     * @param concept concept to be checked
     *
     * @return A list of Basic Level Concepts
     */
    @Override
    public List<Concept> getBasicLevelConcepts(Concept concept) {
        List<Concept> basicLevelConceptsList;
        basicLevelConceptsList = new ArrayList<>();
        RelationDao rDao = new RelationDaoImpl();
        Concept.SpecificityLevel specificityLevel = concept.
                getSpecificityLevel();

        if (specificityLevel == BASIC_LEVEL) {
            basicLevelConceptsList.add(concept);
        } else if (specificityLevel == SUBORDINATE ||
                specificityLevel == SUPERORDINATE) {
            List<Relation> relations = rDao.getRelationsByConceptRelationType(
                    concept, RelationType.RelationNameForward.TYPE_TOKEN);
            for (Relation relation : relations) {
                if (relation.getLeftArgument().isConcept()) {
                    Concept leftConcept = relation.getLeftArgument().
                            getConcept();
                    if (leftConcept.getSpecificityLevel() == BASIC_LEVEL) {
                        basicLevelConceptsList.add(leftConcept);
                    }
                }
                if (relation.getRightArgument().isConcept()) {
                    Concept rightConcept = relation.getRightArgument().
                            getConcept();
                    if (rightConcept.getSpecificityLevel() == BASIC_LEVEL) {
                        basicLevelConceptsList.add(rightConcept);
                    }
                }
            }
        }
        return basicLevelConceptsList;
    }

    /**
     * This is the old implementation of finding the basic levels of a concept.
     * Algorithm: If the concept is subordinate, then:
     * - Store all ancestors of concept in concept list
     * Else if the concept is superordinate then:
     * - Store both offsprings and ancestors of concept in concept list
     * For each concept in concept list:
     * - If the concept is basic level, add it to the list of BL
     * retrievedConceptsList Return the list of BL retrievedConceptsList
     *
     * @param concept concept to be checked
     *
     * @return The list of Basic Level Concepts
     */
    @Override
    public List<Map.Entry<Concept, Direction>>
            getBasicLevelConceptsOld(Concept concept) {
        List<Concept> conceptsListUp = new ArrayList<>();
        List<Concept> conceptsListDown = new ArrayList<>();
        List<Map.Entry<Concept, Direction>> basicLevelConceptsList;
        basicLevelConceptsList = new ArrayList<>();
        AbstractMap.SimpleEntry<Concept, Direction> pair =
                new java.util.AbstractMap.SimpleEntry<>(concept,
                        Direction.NONE);
        Concept.SpecificityLevel specificityLevel = concept.
                getSpecificityLevel();

        switch (specificityLevel) {
            case BASIC_LEVEL:
                pair = new java.util.AbstractMap.SimpleEntry<>(concept,
                        Direction.NONE);
                basicLevelConceptsList.add(pair);
                break;
            case SUBORDINATE:
                conceptsListUp.addAll(getAllAncestors(concept));
                for (Concept upConcept : conceptsListUp) {
                    Concept.SpecificityLevel specificityLevelUp = upConcept.
                            getSpecificityLevel();
                    if (specificityLevelUp == BASIC_LEVEL) {
                        pair = new java.util.AbstractMap.SimpleEntry<>(
                                upConcept, Direction.UP);
                        basicLevelConceptsList.add(pair);
                    }
                }
                break;
            case SUPERORDINATE:
                conceptsListDown.addAll(getAllOffsprings(concept));
                conceptsListUp.addAll(getAllAncestors(concept));
                for (Concept downConcept : conceptsListDown) {
                    Concept.SpecificityLevel specificityLevelDown =
                            downConcept.getSpecificityLevel();
                    if (specificityLevelDown == BASIC_LEVEL) {
                        pair = new java.util.AbstractMap.SimpleEntry<>(
                                downConcept, Direction.DOWN);
                        basicLevelConceptsList.add(pair);
                    }
                }
                for (Concept upConcept : conceptsListUp) {
                    Concept.SpecificityLevel specificityLevelUp = upConcept.
                            getSpecificityLevel();
                    if (specificityLevelUp == BASIC_LEVEL) {
                        pair = new java.util.AbstractMap.SimpleEntry<>(
                                upConcept, Direction.UP);
                        basicLevelConceptsList.add(pair);
                    }
                }
                break;
            default:
                break;
        }
        return basicLevelConceptsList;
    }

    /**
     * Finds all retrievedConceptsList that are classes of instance
     * (has-instance related) of a given concept
     *
     * @param concept the concept
     *
     * @return a list of retrievedConceptsList
     */
    @Override
    public List<Concept> getClassesOfInstance(Concept concept) {
        List<Concept> conceptList = new ArrayList<>();
        RelationDao rDao = new RelationDaoImpl();
        List<Relation> relations =
                rDao.getRelationsByRightConceptTypeOfRelation(concept,
                        HAS_INSTANCE);
        for (Relation relation : relations) {
            if (relation.getLeftArgument().isConcept()) {
                conceptList.add(relation.getLeftArgument().getConcept());
            }
        }
        entityManager.clear();
        return conceptList;
    }

    /**
     * Finds all retrievedConceptsList that are instances (instance-of related)
     * of a given concept
     *
     * @param concept the concept
     *
     * @return a list of retrievedConceptsList
     */
    @Override
    public List<Concept> getInstancesOf(Concept concept) {
        List<Concept> conceptList = new ArrayList<>();
        RelationDao rDao = new RelationDaoImpl();
        List<Relation> relations =
                rDao.getRelationsByLeftConceptTypeOfRelation(concept,
                        HAS_INSTANCE);
        for (Relation relation : relations) {
            if (relation.getRightArgument().isConcept()) {
                conceptList.add(relation.getRightArgument().getConcept());
            }
        }
        entityManager.clear();
        return conceptList;
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
     * Creates q query to search for a concept using name, ConceptType, Status
     * and pragmatic Status
     *
     * @param concept the concept to be searched
     *
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
