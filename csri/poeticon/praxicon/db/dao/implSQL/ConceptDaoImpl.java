    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csri.poeticon.praxicon.db.dao.implSQL;

import csri.poeticon.praxicon.db.dao.ConceptDao;
import csri.poeticon.praxicon.db.dao.RelationDao;
import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.Concept.status;
import csri.poeticon.praxicon.db.entities.IntersectionOfRelationChains;
import csri.poeticon.praxicon.db.entities.LanguageRepresentation;
import csri.poeticon.praxicon.db.entities.Relation;
import csri.poeticon.praxicon.db.entities.RelationChain;
import csri.poeticon.praxicon.db.entities.RelationChain_Relation;
import csri.poeticon.praxicon.db.entities.RelationType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Dimitris Mavroeidis
 */
public class ConceptDaoImpl extends JpaDao<Long, Concept> implements
        ConceptDao {

    /**
     * Finds all the concepts
     *
     * @return a list of all concepts in the database
     */
    @Override
    public List<Concept> findAll() {
        Query query = getEntityManager().createNamedQuery("findAllConcepts");
        List<Concept> concepts = query.getResultList();
        return concepts;
    }

    /**
     * Finds all concepts that have a language representation containing a given
     * string
     *
     * @param queryString the string to search for
     * @return a list of concepts found in the database
     */
    @Override
    public List<Concept> findAllByLanguageRepresentation(String queryString) {
        Query q = getEntityManager().createQuery("SELECT c FROM Concept c, " +
                "IN (c.LanguageRepresentation) as clr, " +
                "IN (clr.LanguageRepresentationConstituents) as entry " +
                "where entry.Name like ?1");
        q.setParameter(1, "%" + queryString + "%");
        return q.getResultList();
    }

    /**
     * Finds all concepts that have a language representation equal to a given
     * string
     *
     * @param queryString the string to search for
     * @return a list of concepts found in the database
     */
    @Override
    public List<Concept> findByLanguageRepresentation(String queryString) {
        this.clearManager();
        Query q = getEntityManager().createQuery(
                "SELECT e FROM LanguageRepresentation e " +
                "where UPPER(e.Text) = ?1"
        );
        q.setParameter(1, queryString.toUpperCase());
        List<LanguageRepresentation> lrs = q.getResultList();

        List<LanguageRepresentation> lrgs = new ArrayList<>();
        for (LanguageRepresentation lr : lrs) {
            lrgs.addAll(lr.getLanguageRepresentations());
        }

        List<Concept> res = new ArrayList<>();
        for (LanguageRepresentation lrg : lrgs) {
            res.addAll(lrg.getConcepts());
        }
        return res;
    }

    /**
     * Finds all concepts that have a name or language representation containing
     * a given string
     *
     * @param name
     * @return a list of concepts found in the database
     */
    @Override
    public List<Concept> findAllByName(String name) {
        List<Concept> res = findByLanguageRepresentation(name);
        res.addAll(findAllByLanguageRepresentation(name));
        Query q = getEntityManager().createQuery("SELECT c FROM Concept c " +
                 "where c.Name like ?1");
        q.setParameter(1, "%" + name + "%");
        res.addAll(q.getResultList());

        return res;
    }

    /**
     * Finds all concepts that have a name equal to a given string
     *
     * @param name
     * @return a list of concepts found in the database
     */
    @Override
    public List<Concept> findByName(String name) {
        Query q = getEntityManager().createQuery("SELECT c FROM Concept c " +
                 "where c.Name = ?1");
        q.setParameter(1, name);
        return q.getResultList();
    }

    /**
     * Finds all concepts that have a status equal to a given status
     *
     * @param status the concept status to search for
     * @return a list of concepts found in the database
     */
    @Override
    public List<Concept> findByStatus(status status) {
        Query q = getEntityManager().createQuery("SELECT c FROM Concept c " +
                 "where c.Status = ?1");
        q.setParameter(1, status);
        return q.getResultList();
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
            q = getEntityManager().createQuery(
                    "SELECT c FROM Concept c where c.Name=?1");
            q.setParameter(1, v.trim());
            List res = q.getResultList();
            for (Object re : res) {
                Concept tmp = (Concept)re;
                if (tmp.getName().trim().equalsIgnoreCase(v.trim())) {
                    return (Concept)re;
                }
            }
        } else {
            q = getEntityManager().createQuery(
                    "SELECT c FROM Concept c where c.Id = ?1");
            q.setParameter(1, id);
            List res = q.getResultList();
            if (res.size() >= 1) {
                return (Concept)res.get(0);
            }
        }
        return null;
    }

    /**
     * Updates a concept from the database that has the same name as another
     * concept that is used as source of the update
     *
     * @param newCon concept to use as source
     * @return the updated concept
     */
    @Override
    public Concept updatedConcept(Concept newCon) {
        Query q = getEntityManager().createQuery("SELECT c FROM Concept c " +
                 "where c.Name = ?1");
        q.setParameter(1, newCon.getName());
        List tmp = q.getResultList();

        Concept oldCon = null;
        if (tmp.isEmpty()) {
            return newCon;
        } else {
            oldCon = (Concept)tmp.get(0);
        }

        oldCon.setConceptType(newCon.getConceptType());
        oldCon.setPragmaticStatus(newCon.getPragmaticStatus());
        oldCon.setStatus(newCon.getStatus());
        oldCon.setSpecificityLevel(newCon.getSpecificityLevel());
        oldCon.setComment(newCon.getComment());

        updateLanguageRepresentations(newCon, oldCon);
        updateVisualRepresentations(newCon, oldCon);
        updateMotoricRepresentations(newCon, oldCon);
        updateObjOfRelations(newCon, oldCon);
        updateRelations(newCon, oldCon);

        return oldCon;
    }

    /**
     * Updates a concept from the database (in place) that has the same name as
     * another concept that is used as source of the update
     *
     * @param newCon concept to use as source
     */
    @Override
    public void update(Concept newCon) {
        try {
            Query q = getEntityManager().createQuery(
                    "SELECT c FROM Concept c " +
                     "where c.Name = ?1");
            q.setParameter(1, newCon.getName());
            List tmp = q.getResultList();

            Concept oldCon = null;

            if (tmp.isEmpty()) {
                oldCon = new Concept();
            } else {
                oldCon = (Concept)tmp.get(0);
            }
            if (oldCon.getConceptType() == null ||
                    oldCon.getConceptType() == Concept.type.UNKNOWN) {
                oldCon.setConceptType(newCon.getConceptType());
            }
            if (oldCon.getPragmaticStatus() == null) {
                oldCon.setPragmaticStatus(newCon.getPragmaticStatus());
            }
            if (oldCon.getStatus() == null) {
                oldCon.setStatus(newCon.getStatus());
            }

            oldCon.setSpecificityLevel(newCon.getSpecificityLevel());
            if (oldCon.getComment() == null ||
                    oldCon.getComment().equalsIgnoreCase("") ||
                    oldCon.getComment().equalsIgnoreCase("Unknown")) {
                oldCon.setComment(newCon.getComment());
            }
            if (newCon.getSource() != null && !newCon.getSource().isEmpty()) {
                oldCon.setSource(newCon.getSource());
            }

            if (!getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().begin();
            }
            updateLanguageRepresentations(newCon, oldCon);
            oldCon = entityManager.merge(oldCon);
            entityManager.getTransaction().commit();
            if (!getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().begin();
            }
            updateVisualRepresentations(newCon, oldCon);
            oldCon = entityManager.merge(oldCon);
            entityManager.getTransaction().commit();
            if (!getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().begin();
            }
            updateMotoricRepresentations(newCon, oldCon);
            oldCon = entityManager.merge(oldCon);
            entityManager.getTransaction().commit();
            if (!getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().begin();
            }
            updateObjOfRelations(newCon, oldCon);
            oldCon = entityManager.merge(oldCon);
            entityManager.getTransaction().commit();
            if (!getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().begin();
            }
            updateRelations(newCon, oldCon);
            oldCon = entityManager.merge(oldCon);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }

    }

    /**
     * Updates a concept using another concept (in place).
     *
     * @param oldCon concept to be updated
     * @param newCon concept to use as source
     */
    @Override
    public void update(Concept oldCon, Concept newCon) {
        try {
            //     entityManager.getTransaction().begin();
            if (oldCon.getConceptType() == null ||
                    oldCon.getConceptType() == Concept.type.UNKNOWN) {
                oldCon.setConceptType(newCon.getConceptType());
            }
            if (oldCon.getPragmaticStatus() == null) {
                oldCon.setPragmaticStatus(newCon.getPragmaticStatus());
            }
            if (oldCon.getStatus() == null) {
                oldCon.setStatus(newCon.getStatus());
            }
            oldCon.setSpecificityLevel(newCon.getSpecificityLevel());
            if (oldCon.getComment() == null ||
                    oldCon.getComment().equalsIgnoreCase("") ||
                    oldCon.getComment().equalsIgnoreCase("Unknown")) {
                oldCon.setComment(newCon.getComment());
            }
            updateVisualRepresentations(newCon, oldCon);
            updateMotoricRepresentations(newCon, oldCon);
            updateObjOfRelations(newCon, oldCon);
            updateRelations(newCon, oldCon);
            merge(oldCon);
            updateLanguageRepresentations(newCon, oldCon);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Checks if two concepts are related by a certain relation
     *
     * @param conA     first concept
     * @param relation relation name as a string
     * @param conB     second concept
     * @return true/false
     */
    @Override
    public boolean areRelated(Concept conA, String relation, Concept conB) {
        for (int intersection = 0;
                intersection < conA.getIntersectionsOfRelationChains().size();
                intersection++) {
            IntersectionOfRelationChains tmpIntersection =
                    conA.getIntersectionsOfRelationChains().get(intersection);
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
                        if (conB.equals(tmpRelationChain.getRelations().
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
    public List<Concept> getChildrenOf(Concept c) {
        List<Concept> res = new ArrayList<>();

        RelationDao rDao = new RelationDaoImpl();
        List<Relation> relations = rDao.allRelationsOf(c);
        for (Relation relation : relations) {
            if (relation.getType().getForwardName() ==
                    RelationType.relation_name_forward.TYPE_TOKEN &&
                    relation.getSubject().equals(c)) {
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
    public List<Concept> getParentsOf(Concept c) {
        List<Concept> res = new ArrayList<>();

        RelationDao rDao = new RelationDaoImpl();
        List<Relation> relations = rDao.allRelationsOf(c);
        for (Relation relation : relations) {
            if (relation.getType().getForwardName() ==
                    RelationType.relation_name_forward.TYPE_TOKEN &&
                    relation.getObject().equals(c)) {
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

        List<Concept> parents = getParentsOf(concept);
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
        List<Concept> children = getChildrenOf(concept);
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
    public List<Concept> getClassesOfInstance(Concept c) {
        List<Concept> res = new ArrayList<>();
        RelationDao rDao = new RelationDaoImpl();
        List<Relation> relations = rDao.allRelationsOf(c);
        for (Relation relation : relations) {
            if (relation.getType().getForwardName() ==
                    RelationType.relation_name_forward.HAS_INSTANCE &&
                    relation.getObject().equals(c)) {
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
    public List<Concept> getInstancesOf(Concept c) {
        List<Concept> res = new ArrayList<>();
        RelationDao rDao = new RelationDaoImpl();
        List<Relation> relations = rDao.allRelationsOf(c);
        for (Relation relation : relations) {
            if (relation.getType().getForwardName() ==
                    RelationType.relation_name_forward.HAS_INSTANCE &&
                    relation.getObject().equals(c)) {
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
    public List<Concept> getBasicLevel(Concept c) {
// Temporarily disabled the block below until cleared
//        if (c.getOrigin() == Concept.Origin.MOVEMENT)
//        {
//            return getBasicLevelOfMovementOriginConcept(c);
//        }
//      else
//        {

        // AN BL επιστρέφει λίστα με τον εαυτό του.
        // Αν είναι above BL getBLofanabstractlevel
        // ελσε ιφ below BL τρέξε BL entity concept
        if (c.getConceptType() == Concept.type.ABSTRACT) {
            return getBasicLevelOfAnAbstractConcept(c);
        } else {
            if (c.getConceptType() == Concept.type.ENTITY ||
                     c.getConceptType() == Concept.type.MOVEMENT ||
                     c.getConceptType() == Concept.type.FEATURE) {
                return getBasicLevelOfAnEntityConcept(c);
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
    public List<Concept> getBasicLevelOfAnEntityConcept(Concept con) {
        List<Concept> res = new ArrayList<>();

        if (con.getSpecificityLevel() !=
                Concept.specificity_level.BASIC_LEVEL &&
                con.getConceptType() != Concept.type.ABSTRACT) {
            List<Concept> parents = getParentsOf(con);
            for (Concept parent : parents) {
                res.addAll(getBasicLevelOfAnEntityConcept(parent));
            }

            if (parents.isEmpty()) {
                List<Concept> classes = getClassesOfInstance(con);
                for (Concept classe : classes) {
                    res.addAll(getBasicLevelOfAnEntityConcept(classe));
                }
            }
        } else {
            if (con.getSpecificityLevel() ==
                    Concept.specificity_level.BASIC_LEVEL) {
                res.add(con);
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
    public List<Concept> getBasicLevelOfAnAbstractConcept(Concept c) {
        List<Concept> res = new ArrayList<>();

        if (c.getSpecificityLevel() != Concept.specificity_level.BASIC_LEVEL &&
                c.getConceptType() == Concept.type.ABSTRACT) {
            List<Concept> children = getChildrenOf(c);
            for (Concept children1 : children) {
                res.addAll(getBasicLevelOfAnAbstractConcept(children1));
            }
        } else {
            if (c.getSpecificityLevel() ==
                    Concept.specificity_level.BASIC_LEVEL) {
                res.add(c);
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
    private List<Concept> getBasicLevelOfMovementOriginConcept(Concept c) {
        List<Concept> res = new ArrayList<>();

        if (c.getSpecificityLevel() == Concept.specificity_level.BASIC_LEVEL) {
            res.add(c);
        } else {
            res.addAll(getBasicLevelOfMovementOriginConceptGoingDown(c));
            res.addAll(getBasicLevelOfMovementOriginConceptGoingUp(c));
        }
        return res;
    }

    /**
     * Finds all the Basic Level concepts for the given concept, moving only up
     * in the hierarchy.
     *
     * @param c concept to be checked
     * @return The list of BL
     */
    private List<Concept> getBasicLevelOfMovementOriginConceptGoingUp(
            Concept con) {
        List<Concept> res = new ArrayList<>();

        if (con.getSpecificityLevel() !=
                Concept.specificity_level.BASIC_LEVEL) {
            List<Concept> parents = getParentsOf(con);
            for (Concept parent : parents) {
                res.addAll(getBasicLevelOfMovementOriginConceptGoingUp(parent));
            }

            if (parents.isEmpty()) {
                List<Concept> classes = getClassesOfInstance(con);
                for (Concept classe : classes) {
                    res.addAll(getBasicLevelOfMovementOriginConceptGoingUp(
                            classe));
                }
            }
        } else {
            if (con.getSpecificityLevel() ==
                    Concept.specificity_level.BASIC_LEVEL) {
                res.add(con);
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
            Concept con) {
        List<Concept> res = new ArrayList<>();

        if (con.getSpecificityLevel() !=
                Concept.specificity_level.BASIC_LEVEL) {
            List<Concept> children = getChildrenOf(con);
            for (Concept children1 : children) {
                res.addAll(getBasicLevelOfMovementOriginConceptGoingDown(
                        children1));
            }
        } else {
            if (con.getSpecificityLevel() ==
                    Concept.specificity_level.BASIC_LEVEL) {
                res.add(con);
            }
        }
        return res;
    }

    /**
     * Finds all concepts that are related toa given concept using a given
     * relation type
     *
     * @param c     the concept
     * @param rtype the type of relation (direction sensitive)
     * @return a list of concepts
     */
    @Override
    public List<Concept> getConceptsRelatedWithByRelationType(
            Concept c, RelationType rtype) {
        List<Concept> res = new ArrayList<>();
        Query q = getEntityManager().createQuery(
                "SELECT r FROM Relation r, TypeOfRelation type " +
                "where ((r.Subject = ?1 or r.Object = ?1) and" +
                " r.Type = type and type.ForwardName = ?2 " +
                "and type.BackwardName =?3)");
        q.setParameter(1, c);
        q.setParameter(2, rtype.getForwardName());
        q.setParameter(3, rtype.getBackwardName());

        List<Relation> tmpR = q.getResultList();
        if (tmpR != null && tmpR.size() > 0) {
            for (Relation tmpR1 : tmpR) {
                if (tmpR1.getSubject().equals(c)) {
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
        Query q = getEntityManager().createQuery("SELECT e FROM Concept e " +
                "where e.Name = ?1 and e.ConceptType = ?2 and e.Status = ?3" +
                " and e.PragmaticStatus = ?4");
        System.out.println("Concept name: " + concept.getName());
        q.setParameter(1, concept.getName());
        q.setParameter(2, concept.getConceptType());
        q.setParameter(3, concept.getStatus());
        q.setParameter(4, concept.getPragmaticStatus());
        return q;
    }

    /**
     * Updates the language representations of a concept, adding the
     * LanguageRepresentation of another concept (removing them from that
     * concept).
     *
     * @param newCon concept with LanguageRepresentation to be moved
     * @param oldCon concept to be updated
     */
    private void updateLanguageRepresentations(Concept newCon, Concept oldCon) {
        try {
            for (int i = 0; i < newCon.getLanguageRepresentations().size();
                    i++) {
                if (!oldCon.getLanguageRepresentations().
                        contains(newCon.getLanguageRepresentations().get(i))) {
                    LanguageRepresentation tmpLanguageRepresentation =
                            newCon.getLanguageRepresentations().get(i);
                    tmpLanguageRepresentation.getConcepts().remove(newCon);
                    tmpLanguageRepresentation.getConcepts().add(oldCon);
                    oldCon.getLanguageRepresentations().
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
     * @param newCon concept with MotoricRepresentations to be moved
     * @param oldCon concept to be updated
     */
    private void updateMotoricRepresentations(Concept newCon, Concept oldCon) {
        for (int i = 0; i < newCon.getMotoricRepresentations().size(); i++) {
            if (!oldCon.getMotoricRepresentations().
                    contains(newCon.getMotoricRepresentations().get(i))) {
                oldCon.getMotoricRepresentations().
                        add(newCon.getMotoricRepresentations().get(i));
            }
        }
    }

    /**
     * Updates the ObjectOf relations of a concept, adding the ObjectOf
     * relations of another concept (removing them from that concept).
     *
     * @param newCon concept with ObjectOf relations to be moved
     * @param oldCon concept to be updated
     */
    private void updateObjOfRelations(Concept newCon, Concept oldCon) {
        for (int i = 0;
                i < newCon.getRelationsContainingConceptAsObject().size();
                i++) {
            if (!oldCon.getRelationsContainingConceptAsObject().
                    contains(newCon.getRelationsContainingConceptAsObject().
                            get(i))) {
                if (newCon.getRelationsContainingConceptAsObject().get(i).
                        getObject().equals(newCon)) {
                    newCon.getRelationsContainingConceptAsObject().get(i).
                            setObject(oldCon);
                } else {
                    newCon.getRelationsContainingConceptAsObject().get(i).
                            setSubject(oldCon);
                }
                oldCon.getRelationsContainingConceptAsObject().
                        add(newCon.getRelationsContainingConceptAsObject().
                                get(i));
            }
        }
    }

    /**
     * Updates the relations of a concept, adding the relations of another
     * concept (removing them from that concept).
     *
     * @param newCon concept with relations to be moved
     * @param oldCon concept to be updated
     */
    private void updateRelations(Concept newCon, Concept oldCon) {
        for (int i = 0; i < newCon.getIntersectionsOfRelationChains().size();
                i++) {
            if (!oldCon.getIntersectionsOfRelationChains().
                    contains(newCon.getIntersectionsOfRelationChains().
                            get(i))) {
                newCon.getIntersectionsOfRelationChains().get(i).
                        setConcept(oldCon);
                IntersectionOfRelationChains inter =
                        newCon.getIntersectionsOfRelationChains().get(i);
                for (int k = 0; k < inter.getRelationChains().size(); k++) {
                    RelationChain rc = inter.getRelationChains().get(k);
                    for (int l = 0; l < rc.getRelations().size(); l++) {
                        RelationChain_Relation rcr =
                                 rc.getRelations().get(l);
                        Relation rel = rcr.getRelation();
                        if (rel.getSubject().getName().
                                equalsIgnoreCase(newCon.getName())) {
                            rel.setSubject(oldCon);
                        } else {
                            if (rel.getObject().getName().
                                    equalsIgnoreCase(newCon.getName())) {
                                rel.setObject(oldCon);
                            }
                        }
                    }
                }

                oldCon.getIntersectionsOfRelationChains().
                        add(newCon.getIntersectionsOfRelationChains().get(i));
            }
        }
    }

    /**
     * Updates the visual representations of a concept, adding the
     * VisualRepresentations of another concept (removing them from that
     * concept).
     *
     * @param newCon concept with VisualRepresentations to be moved
     * @param oldCon concept to be updated
     */
    private void updateVisualRepresentations(Concept newCon, Concept oldCon) {
        for (int i = 0; i < newCon.getVisualRepresentationsEntries().size();
                i++) {
            if (!oldCon.getVisualRepresentationsEntries().
                    contains(newCon.getVisualRepresentationsEntries().get(i))) {
                oldCon.getVisualRepresentationsEntries().
                        add(newCon.getVisualRepresentationsEntries().get(i));
            }
        }
    }
}
