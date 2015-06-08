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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 */
public class RelationDaoImpl extends JpaDao<Long, Relation> implements
        RelationDao {

    /**
     * Finds relations that have a specific relationArgument as rightArgument,
     * a specific relationArgument as leftArgument and a specific relationType.
     * That way, we retrieve a unique relation from the database.
     *
     * @param leftArgument  the left relation argument
     * @param rightArgument the right relation arguments
     * @param relationType  the type of relation
     * @return a list of relation sets
     */
    @Override
    public Relation getRelation(RelationArgument leftArgument,
            RelationArgument rightArgument, RelationType relationType) {
        Query query = getEntityManager().createNamedQuery("findRelations").
                setParameter("leftRelationArgumentId", leftArgument.getId()).
                setParameter("rightRelationArgumentId", rightArgument.getId()).
                setParameter("relationType", relationType);
        List<Relation> relationsList = (List<Relation>)query.getResultList();
        if (relationsList.isEmpty()) {
            return null;
        }
        return relationsList.get(0);
    }

    /**
     * Finds all relations of a given concept
     *
     * @param concept the concept
     * @return a list of Relation
     */
    @Override
    public List<Relation> getAllRelationsOfConcept(Concept concept) {
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        RelationArgument conceptRelationArgument = raDao.
                getRelationArgumentByConcept(concept);
        return getAllRelationsOfRelationArgument(conceptRelationArgument);
    }

    /**
     * Checks if two concepts are related
     *
     * @param concept1 the first concept
     * @param concept2 the second concept
     * @return true/false
     */
    @Override
    public boolean areRelated(Concept concept1, Concept concept2) {
        // TODO: This will not work. First retrieve the arguments from the DB.
        RelationArgument relationArgument1 = new RelationArgument(concept1);
        RelationArgument relationArgument2 = new RelationArgument(concept2);
        return areRelated(relationArgument1, relationArgument2);
    }

    /**
     * Finds the relations of a given concept that have a certain
     * type of relation.
     *
     * @param concept      the concept
     * @param relationType the type of relation
     * @return List of relations
     */
    @Override
    public List<Relation> getRelationsByConceptRelationType(
            Concept concept, RelationType.RelationNameForward relationType) {
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        RelationArgument retrievedRelationArgument = raDao.
                getRelationArgumentByConcept(concept);
        return getRelationsByRelationArgumentRelationType(
                retrievedRelationArgument, relationType);
    }

    /**
     * Finds the relations that have a certain type of relation.
     *
     * @param relationType the type of relation
     * @return List of relations
     */
    @Override
    public List<Relation> getRelationsByRelationType(
            RelationType.RelationNameForward relationType) {
        Query query = getEntityManager().createNamedQuery(
                "findRelationsByRelationType").
                setParameter("relationType", relationType);
        List<Relation> relations = new ArrayList<>();
        relations = (List<Relation>)query.getResultList();
        return relations;
    }

    /**
     * Finds all relations of a given relation argument
     *
     * @param relationArgument the relation argument to be searched
     * @return A list of Relations
     */
    @Override
    public List<Relation> getAllRelationsOfRelationArgument(
            RelationArgument relationArgument) {
        getEntityManager().clear();
        Query query = getEntityManager().createNamedQuery(
                "findRelationsByRelationArgumentRightArgumentOrLeftArgument").
                setParameter("relationArgument", relationArgument);
        List<Relation> res = query.getResultList();
        return res;
    }

    /**
     * Checks if two relationArguments are related
     *
     * @param relationArgument1 the first relationArgument
     * @param relationArgument2 the second relationArgument
     * @return true/false
     */
    @Override
    public boolean areRelated(RelationArgument relationArgument1,
            RelationArgument relationArgument2) {
        Query query = getEntityManager().createNamedQuery("areRelated").
                setParameter("relationArgumentId1", relationArgument1.getId()).
                setParameter("relationArgumentId2", relationArgument2.getId());
        List<Relation> objRels = query.getResultList();
        return objRels.size() > 0;
    }

    /**
     * Finds the relations of a given relation argument that have a certain
     * type of relation.
     *
     * @param relationArgument the relation argument
     * @param relationType     the type of relation
     * @return A list of relations
     */
    @Override
    public List<Relation> getRelationsByRelationArgumentRelationType(
            RelationArgument relationArgument,
            RelationType.RelationNameForward relationType) {
        Query query = getEntityManager().createNamedQuery(
                "findRelationsByRelationArgumentRelationType").
                setParameter("relationArgumentId", relationArgument.getId()).
                setParameter("relationType", relationType);
        return query.getResultList();
    }

    /**
     * Finds the relations of a given relation argument that have a certain
     * type of relation. Checks only for the given relation argument as a leftArgument
     *
     * @param relationArgument the relation argument
     * @param relationType     the type of relation
     * @return A list of relations
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
     * type of relation. Checks only for the given relation argument as a rightArgument
     *
     * @param relationArgument the relation argument
     * @param relationType     the type of relation
     * @return A list of relations
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
     * @return the updated relation
     */
    @Override
    public Relation updatedRelation(Relation newRelation) {
        Relation oldRelation = new Relation(newRelation);
        try {
            oldRelation = this.getRelation(newRelation.getLeftArgument(),
                    newRelation.getRightArgument(), newRelation.
                    getRelationType());
        } catch (Exception e) {
            return newRelation;
        } finally {
            return oldRelation;
        }
    }

}
