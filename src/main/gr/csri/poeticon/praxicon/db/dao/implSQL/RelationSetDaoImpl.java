/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.RelationArgumentDao;
import gr.csri.poeticon.praxicon.db.dao.RelationDao;
import gr.csri.poeticon.praxicon.db.dao.RelationSetDao;
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
public class RelationSetDaoImpl extends JpaDao<Long, RelationSet>
        implements RelationSetDao {

    /**
     * Finds all RelationSets that have at least one relation with leftArgument
     * or rightArgument a given relationArgument.
     *
     * @param relationArgument the relation argument to search by
     * @return a list of RelationSets
     */
    @Override
    public List<RelationSet> getRelationSetsByRelationArgument(
            RelationArgument relationArgument) {

        Query q = getEntityManager().createNamedQuery(
                "findRelationSetsByRelationArgument").setParameter(
                        "relationArgument", relationArgument);
        return q.getResultList();
    }

    /**
     * Finds all RelationSets that have at least one relation with a concept
     * as LeftArgument or RightArgument.
     *
     * @param concept the concept to search by
     * @return a list of RelationSets
     */
    @Override
    public List<RelationSet> getRelationSetsByConcept(Concept concept) {
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        RelationArgument newRelationArgument = raDao.
                getRelationArgumentByConcept(concept);

//        RelationArgument newRelationArgument = new RelationArgument(concept);
        return getRelationSetsByRelationArgument(newRelationArgument);
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
                "findRelationsByRelationArgumentRightArgumentOrLeftArgument").
                setParameter("relationArgumentId", relationArgument.getId());
        List<Relation> rightArgumentRelations = new ArrayList<>();
        rightArgumentRelations = (List<Relation>)query.getResultList();
        List<RelationSet> res = new ArrayList<>();
        for (Relation r : rightArgumentRelations) {
            if (r.getRightArgument().equals(relationArgument)) {
                r.setRightArgument(r.getLeftArgument());
                r.setLeftArgument(relationArgument);
                RelationType tmpType = new RelationType();
                RelationType.RelationNameBackward tmp =
                        r.getRelationType().getBackwardName();
                tmpType.setForwardName(r.getRelationType().getForwardName());
                tmpType.setBackwardName(tmp);
                r.setRelationType(tmpType);
            }
            RelationSet rs = new RelationSet();
            rs.addRelation(r);
        }
        return res;
    }

    /**
     * Finds relations sets that contain relations with a given concept as a
     * rightArgument.
     *
     * @param concept the concept which we want the relation sets of
     * @return a list of relation sets
     */
    @Override
    public List<RelationSet> getRelationSetsWithConceptAsRightArgument(
            Concept concept) {
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        RelationArgument newRelationArgument = raDao.
                getRelationArgumentByConcept(concept);
        return getRelationSetsWithRelationArgumentAsRightArgument(
                newRelationArgument);
    }

    /**
     * Finds relations sets that contain a specific relation.
     *
     * @param relation the relation to search for
     * @return a list of relation sets
     */
    public List<RelationSet> getRelationSetsByRelation(Relation relation) {
        // First get the relation from the database
        RelationDao rDao = new RelationDaoImpl();
        Relation retrievedRelation = rDao.
                getRelation(relation.getLeftArgument(), relation.
                        getRightArgument(), relation.getRelationType().
                        getForwardName());
        Query query = getEntityManager().createNamedQuery(
                "findRelationSetsByRelation").
                setParameter("relation", retrievedRelation);
        List<RelationSet> relationSets = new ArrayList<>();
        relationSets = (List<RelationSet>)query.getResultList();
        return relationSets;
    }

    @Override
    public RelationSet updatedRelationSet(RelationSet newRelationSet) {
        // We try to find if the relation set exists in the database.
        // Will recursively try to get the members of the relation set
        // and check if they are the same. If one fails, it will
        // return the new relation set, otherwise, will not merge
        // the new relation set.
        //EntityManager em = this.entityManager;

//        this.entityManager.getTransaction().begin();
//        this.entityManager.merge(newRelationSet);
//        this.entityManager.getTransaction().commit();
//        this.entityManager.flush();
        //TODO: May need a RelationSet comparison method.
        return newRelationSet;
    }

    /**
     * Creates a query to search for a RelationSet using relations.
     *
     * @param relationSet the RelationSet to be searched
     * @return a query to search for the RelationSet
     */
    @Override
    public Query getEntityQuery(RelationSet relationSet) {
        StringBuilder sb = new StringBuilder("SELECT rc FROM RelationSet rs");
        for (int i = 0; i < relationSet.getRelations().size(); i++) {
            sb.append(", IN (rs.relations) as rel").append(i);
        }
        sb.append(" where UPPER(rs.name) = ?1");
        for (int i = 0; i < relationSet.getRelations().size(); i++) {
            sb.append("and rel").append(i).append("=?").append(i + 2);
        }
        Query q = getEntityManager().createQuery(sb.toString());
        q.setParameter(1, relationSet.getName().toUpperCase());
        for (int i = 0; i < relationSet.getRelations().size(); i++) {
            q.setParameter(i + 2, relationSet.getRelations().get(i));
        }
        return q;
    }

}
