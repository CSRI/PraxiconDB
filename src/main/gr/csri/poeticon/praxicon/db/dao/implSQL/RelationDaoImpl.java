/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.RelationArgumentDao;
import gr.csri.poeticon.praxicon.db.dao.RelationDao;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
import gr.csri.poeticon.praxicon.db.entities.RelationType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 */
public class RelationDaoImpl extends JpaDao<Long, Relation> implements
        RelationDao, Serializable {

    /**
     * Finds relations that have a specific relationArgument as rightArgument,
     * a specific relationArgument as leftArgument and a specific relationType.
     * That way, we retrieve a unique relation from the database.
     *
     * @param leftArgument  the left relation argument
     * @param rightArgument the right relation arguments
     * @param relationType  the type of relation
     *
     * @return a list of relations
     */
    @Override
    public Relation getRelation(RelationArgument leftArgument,
            RelationArgument rightArgument,
            RelationType.RelationNameForward relationType) {
        Query query = getEntityManager().createNamedQuery("findRelation").
                setParameter("leftRelationArgument", leftArgument).
                setParameter("rightRelationArgument", rightArgument).
                setParameter("relationType", relationType);
        List<Relation> retrievedRelationsList = (List<Relation>)query.
                getResultList();
        if (retrievedRelationsList.isEmpty()) {
            return null;
        }
        return retrievedRelationsList.get(0);
    }

    /**
     * Finds relations that have a specific relationArgument as rightArgument,
     * a specific relationArgument as leftArgument and a specific relationType.
     * That way, we retrieve a unique relation from the database.
     *
     * @param relation the relation to search for
     *
     * @return a list of relations
     */
    @Override
    public Relation getRelation(Relation relation) {
        Query query = getEntityManager().createNamedQuery("findRelation").
                setParameter("leftRelationArgument", relation.
                        getLeftArgument()).
                setParameter("rightRelationArgument", relation.
                        getRightArgument()).
                setParameter("relationType", relation.getRelationType().
                        getForwardName());
        List<Relation> retrievedRelationsList = (List<Relation>)query.
                getResultList();
        if (retrievedRelationsList.isEmpty()) {
            return null;
        }
        return retrievedRelationsList.get(0);
    }

    /**
     * Finds all relations of a given concept
     *
     * @param concept the concept
     *
     * @return a list of relations
     */
    @Override
    public List<Relation> getAllRelationsOfConcept(Concept concept) {
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        RelationArgument conceptRelationArgument = raDao.getRelationArgument(
                concept);
        return new ArrayList<>(getAllRelationsOfRelationArgument(
                conceptRelationArgument));
    }

    /**
     * Checks if two concepts are related
     *
     * @param concept1 the first concept
     * @param concept2 the second concept
     *
     * @return true/false
     */
    @Override
    public boolean areRelated(Concept concept1, Concept concept2) {
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        RelationArgument relationArgument1 = raDao.getRelationArgument(
                concept1);
        RelationArgument relationArgument2 = raDao.getRelationArgument(
                concept2);
        return areRelated(relationArgument1, relationArgument2);
    }

    /**
     * Finds the relations of a given concept that have a certain
     * type of relation.
     *
     * @param concept      the concept
     * @param relationType the type of relation
     *
     * @return a list of relations
     */
    @Override
    public List<Relation> getRelationsByConceptRelationType(
            Concept concept, RelationType.RelationNameForward relationType) {
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        RelationArgument retrievedRelationArgument = raDao.
                getRelationArgument(
                        concept);
        return getRelationsByRelationArgumentRelationType(
                retrievedRelationArgument, relationType);
    }

    /**
     * Finds the relations of a given concept that have a certain
     * type of relation. Checks only for the given concept as a leftArgument
     *
     * @param concept      the relation concept
     * @param relationType the type of relation
     *
     * @return a list of relations
     */
    @Override
    public List<Relation> getRelationsByLeftConceptTypeOfRelation(
            Concept concept, RelationType.RelationNameForward relationType) {
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        RelationArgument retrievedRelationArgument = raDao.
                getRelationArgument(concept);
        if (retrievedRelationArgument == null) {
            return new ArrayList<>();
        } else {
            return getRelationsByLeftRelationArgumentTypeOfRelation(
                    retrievedRelationArgument, relationType);
        }
    }

    /**
     * Finds the relations of a given concept that have a certain
     * type of relation. Checks only for the given concept as a rightArgument
     *
     * @param concept      the relation concept
     * @param relationType the type of relation
     *
     * @return a list of relations
     */
    @Override
    public List<Relation> getRelationsByRightConceptTypeOfRelation(
            Concept concept, RelationType.RelationNameForward relationType) {
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        RelationArgument retrievedRelationArgument = raDao.
                getRelationArgument(concept);
        if (retrievedRelationArgument == null) {
            return new ArrayList<>();
        } else {
            return getRelationsByRightRelationArgumentTypeOfRelation(
                    retrievedRelationArgument, relationType);
        }
    }

    /**
     * Finds the relations that have a certain type of relation.
     *
     * @param relationType the type of relation
     *
     * @return a list of relations
     */
    @Override
    public List<Relation> getRelationsByRelationType(
            RelationType.RelationNameForward relationType) {
        Query query = getEntityManager().createNamedQuery(
                "findRelationsByRelationType").
                setParameter("relationType", relationType);
        List<Relation> retrievedRelationsList = new ArrayList<>();
        retrievedRelationsList = (List<Relation>)query.getResultList();
        return retrievedRelationsList;
    }

    /**
     * Finds all relations of a given relation argument
     *
     * @param relationArgument the relation argument to be searched
     *
     * @return a list of relations
     */
    @Override
    public List<Relation> getAllRelationsOfRelationArgument(
            RelationArgument relationArgument) {
        getEntityManager().clear();
        Query query = getEntityManager().createNamedQuery(
                "findRelationsByRelationArgumentRightArgumentOrLeftArgument").
                setParameter("relationArgument", relationArgument);
        List<Relation> retrievedRelationsList = query.getResultList();
        return retrievedRelationsList;
    }

    /**
     * Checks if two relationArguments are related
     *
     * @param relationArgument1 the first relationArgument
     * @param relationArgument2 the second relationArgument
     *
     * @return true/false
     */
    @Override
    public boolean areRelated(RelationArgument relationArgument1,
            RelationArgument relationArgument2) {
        Query query = getEntityManager().createNamedQuery("areRelated").
                setParameter("relationArgument1", relationArgument1).
                setParameter("relationArgument2", relationArgument2);
        List<Relation> retrievedRelationsList = query.getResultList();
        return retrievedRelationsList.size() > 0;
    }

    /**
     * Finds the relations of a given relation argument that have a certain
     * type of relation.
     *
     * @param relationArgument the relation argument
     * @param relationType     the type of relation
     *
     * @return a list of relations
     */
    @Override
    public List<Relation> getRelationsByRelationArgumentRelationType(
            RelationArgument relationArgument,
            RelationType.RelationNameForward relationType) {
        Query query = getEntityManager().createNamedQuery(
                "findRelationsByRelationArgumentRelationType").
                setParameter("relationArgument", relationArgument).
                setParameter("relationType", relationType);
        return query.getResultList();
    }

    /**
     * Finds the relations of a given relation argument that have a certain
     * type of relation. Checks only for the given relation argument
     * as a leftArgument
     *
     * @param relationArgument the relation argument
     * @param relationType     the type of relation
     *
     * @return a list of relations
     */
    @Override
    public List<Relation> getRelationsByLeftRelationArgumentTypeOfRelation(
            RelationArgument relationArgument,
            RelationType.RelationNameForward relationType) {
        Query query = getEntityManager().createNamedQuery(
                "findRelationsByLeftRelationArgumentRelationType").
                setParameter("relationArgument", relationArgument).
                setParameter("relationType", relationType);
        return query.getResultList();
    }

    /**
     * Finds the relations of a given relation argument that have a certain
     * type of relation. Checks only for the given relation argument
     * as a rightArgument
     *
     * @param relationArgument the relation argument
     * @param relationType     the type of relation
     *
     * @return a list of relations
     */
    @Override
    public List<Relation> getRelationsByRightRelationArgumentTypeOfRelation(
            RelationArgument relationArgument,
            RelationType.RelationNameForward relationType) {
        Query query = getEntityManager().createNamedQuery(
                "findRelationsByRightRelationArgumentRelationType").
                setParameter("relationArgument", relationArgument).
                setParameter("relationType", relationType);
        return query.getResultList();
    }

    /**
     * Updates a relation from the database that has the same left and right
     * argument, as well as relation type. In essence, this is checks
     * whether a relation exists in the database. If it doesn't, it is added.
     *
     * @param newRelation relation to use as source
     *
     * @return the updated relation
     */
    @Override
    public Relation updatedRelation(Relation newRelation) {
        Relation oldRelation = new Relation(newRelation);
        try {
            oldRelation = this.getRelation(newRelation.getLeftArgument(),
                    newRelation.getRightArgument(), newRelation.
                    getRelationType().getForwardName());
        } catch (Exception e) {
            return newRelation;
        }
        return oldRelation;
    }

}
