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
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 */
public class RelationSetDaoImpl extends JpaDao<Long, RelationSet>
        implements RelationSetDao {

    /**
     * Finds a RelationSet.
     *
     * @param relationSet
     * @return a relation set
     */
    @Override
    public RelationSet getRelationSet(RelationSet relationSet) {
        Query query = getEntityManager().createNamedQuery("findRelationSet").
                setParameter("relationSet", relationSet);
        List<RelationSet> relationSetsList = (List<RelationSet>)query.
                getResultList();
        if (relationSetsList.isEmpty()) {
            return null;
        }
        return relationSetsList.get(0);
    }

    /**
     * Finds all RelationSets that have at least one relation with leftArgument
     * or rightArgument.
     *
     * @param relationArgument the relation argument to search by
     * @return a list of RelationSets
     */
    @Override
    public List<RelationSet> getRelationSetsByRelationArgument(
            RelationArgument relationArgument) {

        Query query = getEntityManager().createNamedQuery(
                "findRelationSetsByRelationArgument").setParameter(
                        "relationArgument", relationArgument);
        List<RelationSet> relationSets = (List<RelationSet>)query.
                getResultList();
        return relationSets;
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
                getRelationArgument(concept);
        List<RelationSet> relationSetsList = getRelationSetsByRelationArgument(
                newRelationArgument);
        if (relationSetsList.isEmpty()) {
            return null;
        }
        return relationSetsList;
    }

    /**
     * Finds relations that have a given relationArgument as leftArgument
     *
     * @param relationArgument the relation argument to search by
     * @return a list of RelationSets
     */
    @Override
    public List<RelationSet> getRelationSetsWithRelationArgumentAsLeftArgument(
            RelationArgument relationArgument) {
        Query query = getEntityManager().createNamedQuery(
                "findRelationSetsByLeftRelationArgument").
                setParameter("relationArgument", relationArgument);
        List<RelationSet> relationSetsList = (List<RelationSet>)query.
                getResultList();
        return relationSetsList;
    }

    /**
     * Finds relations that have a given relationArgument as rightArgument
     *
     * @param relationArgument the relation argument to search by
     * @return a list of RelationSets
     */
    @Override
    public List<RelationSet> getRelationSetsWithRelationArgumentAsRightArgument(
            RelationArgument relationArgument) {
        Query query = getEntityManager().createNamedQuery(
                "findRelationSetsByRightRelationArgument").
                setParameter("relationArgument", relationArgument);
        List<RelationSet> relationSetsList =
                (List<RelationSet>)query.getResultList();
        return relationSetsList;
    }

    /**
     * Finds relations sets that contain relations with a given concept as a
     * rightArgument.
     *
     * @param concept the concept to search by
     * @return a list of relation sets
     */
    @Override
    public List<RelationSet> getRelationSetsWithConceptAsRightArgument(
            Concept concept) {
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        RelationArgument newRelationArgument = raDao.
                getRelationArgument(concept);
        return getRelationSetsWithRelationArgumentAsRightArgument(
                newRelationArgument);
    }

    /**
     * Finds relations sets that contain a specific relation.
     *
     * @param relation the relation to search by
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
        List<RelationSet> relationSets = (List<RelationSet>)query.
                getResultList();
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
