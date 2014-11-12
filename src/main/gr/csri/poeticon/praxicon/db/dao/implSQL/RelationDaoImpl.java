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
import gr.csri.poeticon.praxicon.db.entities.RelationSet;
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
     * Finds relations that have a given relationArgument as rightArgument
     *
     * @param concept the concept which we want the relation sets of
     * @return a list of relation sets
     */
    // TODO: this needs repair. Find another way to get the related relations.
    @Override
    public List<RelationSet> getRelationSetsWithConceptAsRightArgument(
            Concept concept) {
        RelationArgument newRelationArgument = new RelationArgument(concept);
        return getRelationSetsWithRelationArgumentAsRightArgument(
                newRelationArgument);
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
        RelationArgument relationArgument1 = new RelationArgument(concept1);
        RelationArgument relationArgument2 = new RelationArgument(concept2);
        return areRelated(relationArgument1, relationArgument2);
    }

    /**
     * Finds the relations of a given concept that have a certain
     * type of relation. Checks only for the given concept as a leftArgument
     *
     * @param concept      the concept
     * @param relationType the type of relation
     * @return List of relations
     */
    @Override
    public List<Relation> getRelationsByConceptTypeOfRelation(
            Concept concept, RelationType relationType) {
        RelationArgument newRelationArgument = new RelationArgument(concept);
        return getRelationsByRelationArgumentTypeOfRelation(newRelationArgument,
                relationType);
    }

    /**
     * Finds relations that have a given relationArgument as rightArgument
     *
     * @param relationArgument the relation argument to be searched
     * @return a list of RelationSets
     */
    @Override
    public List<RelationSet> getRelationSetsWithRelationArgumentAsRightArgument(
            RelationArgument relationArgument) {
        Query query = getEntityManager().createNamedQuery(
                "findRelationsByRelationArgumentRightArgumentOrleftArgument").
                setParameter("relationArgumentId", relationArgument.getId());
        List<Relation> rightArgumentRelations = query.getResultList();
        List<RelationSet> res = new ArrayList<>();
        for (Relation r : rightArgumentRelations) {
            if (r.getRightArgument().equals(relationArgument)) {
                r.setRightArgument(r.getLeftArgument());
                r.setLeftArgument(relationArgument);
                RelationType tmpType = new RelationType();
                RelationType.RelationNameBackward tmp =
                        r.getType().getBackwardName();
                tmpType.setForwardName(r.getType().getForwardName());
                tmpType.setBackwardName(tmp);
                r.setType(tmpType);
            }
            RelationSet rs = new RelationSet();
            rs.addRelation(r);
        }
        return res;
    }

    /**
     * Finds all relations of a given concept
     *
     * @param relationArgument the relation argument to be searched
     * @return A list of Relations
     */
    @Override
    public List<Relation> getAllRelationsOfRelationArgument(
            RelationArgument relationArgument) {
        getEntityManager().clear();
        Query query = getEntityManager().createNamedQuery(
                "findRelationsByRelationArgumentRightArgumentOrleftArgument").
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
                setParameter("relationArgumentId1", relationArgument1).
                setParameter("relationArgumentId2", relationArgument2);
        List<Relation> objRels = query.getResultList();
        return objRels.size() > 0;
    }

    /**
     * Finds the relations of a given concept that have a certain
     * type of relation. Checks only for the given concept as a leftArgument
     *
     * @param relationArgument the relation argument
     * @param relationType     the type of relation
     * @return A list of relations
     */
    @Override
    public List<Relation> getRelationsByRelationArgumentTypeOfRelation(
            RelationArgument relationArgument, RelationType relationType) {
        Query query = getEntityManager().createNamedQuery(
                "findRelationsByRelationArgumentRelationType").
                setParameter("relationArgumentId", relationArgument.getId()).
                setParameter("relationType", relationType.getForwardName());
        return query.getResultList();
    }
}
