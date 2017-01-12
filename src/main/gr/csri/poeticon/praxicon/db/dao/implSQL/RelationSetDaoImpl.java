/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.ConceptDao;
import gr.csri.poeticon.praxicon.db.dao.RelationArgumentDao;
import gr.csri.poeticon.praxicon.db.dao.RelationDao;
import gr.csri.poeticon.praxicon.db.dao.RelationSetDao;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Concepts;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation;
import gr.csri.poeticon.praxicon.db.entities.MotoricRepresentation;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
import gr.csri.poeticon.praxicon.db.entities.RelationSet;
import gr.csri.poeticon.praxicon.db.entities.RelationSet_Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationSets;
import gr.csri.poeticon.praxicon.db.entities.Relations;
import gr.csri.poeticon.praxicon.db.entities.VisualRepresentation;
import java.util.LinkedHashSet;
import static java.util.Objects.isNull;
import java.util.Set;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 */
public class RelationSetDaoImpl extends JpaDao<Long, RelationSet>
        implements RelationSetDao {

    /**
     * Finds a RelationSet according to its name.
     *
     * @param relationSetName
     *
     * @return a relation set
     */
    @Override
    public RelationSet getRelationSetByName(String relationSetName) {
        Query query = getEntityManager().createNamedQuery(
                "findRelationSetByName").
                setParameter("relationSetName", relationSetName);
        Set<RelationSet> retrievedRelationSetsList =
                new LinkedHashSet<>(query.getResultList());
        if (!retrievedRelationSetsList.isEmpty()) {
            return retrievedRelationSetsList.iterator().next();
        } else {
            return null;
        }
    }

    /**
     * Finds a RelationSet.
     *
     * @param relationSet
     *
     * @return a relation set
     */
    @Override
    public RelationSet getRelationSet(RelationSet relationSet) {
        /*
         * Need to follow different strategy, since there could be numerous
         * Relations in a Relation Set:
         * 1. Retrieve relations from provided relation set
         * 2. Check if relations exist in DB.
         * 2.1. If all of them exist, continue to step 3.
         * 3. Check if they belong to a relation set.
         * 3.1. If they do, check the rest of the relation set's characteristics
         * 3.1.1. If they are the same, return the relation set
         * 4. Return null
         */
        ConceptDao cDao = new ConceptDaoImpl();
        RelationDao rDao = new RelationDaoImpl();
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        RelationSetDao rsDao = new RelationSetDaoImpl();
        Concepts newConcepts = new Concepts();
        Relations newRelations = new Relations();
        RelationSets newRelationSets = new RelationSets();
        RelationSet newRelationSet = new RelationSet();

        // 1. Retrieve relations from provided relation set
        Set<Relation> retrievedRelations = relationSet.getRelationsList();

        // 2. Check if relations exist in DB.
        for (Relation relation : retrievedRelations) {
            // Check if relation arguments exist in database.
            // Check left argument
            RelationArgument leftArgument = relation.getLeftArgument();
            RelationArgument newLeftArgument = new RelationArgument();
            RelationArgument rightArgument = relation.getRightArgument();
            RelationArgument newRightArgument = new RelationArgument();
            Concept newLeftConcept;
            Concept newRightConcept;
            RelationSet newLeftRelationSet;
            RelationSet newRightRelationSet;

            if (leftArgument.isConcept()) {
                Concept retrievedLeftConcept = cDao.getConcept(leftArgument.
                        getConcept());
                newLeftConcept = newConcepts.
                        storeConcept(retrievedLeftConcept);
                RelationArgument retrievedLeftArgument = raDao.
                        getRelationArgument(newLeftConcept);
                if (!isNull(retrievedLeftArgument)) {
                    raDao.merge(retrievedLeftArgument);
                    newLeftArgument = retrievedLeftArgument;
                } else {

                    RelationArgument newLeftRelationArgument =
                            new RelationArgument(newLeftConcept);
                    raDao.persist(newLeftRelationArgument);
                    newLeftArgument = newLeftRelationArgument;
                }
            } else if (leftArgument.isRelationSet()) {
                RelationSet retrievedLeftRelationSet = rsDao.getRelationSet(
                        leftArgument.getRelationSet());
                newLeftRelationSet = newRelationSets.storeRelationSet(
                        retrievedLeftRelationSet);
                RelationArgument retrievedLeftArgument = raDao.
                        getRelationArgument(retrievedLeftRelationSet);
                if (!isNull(retrievedLeftArgument)) {
                    raDao.merge(retrievedLeftArgument);
                    newLeftArgument = retrievedLeftArgument;
                } else {
                    RelationArgument newLeftRelationArgument =
                            new RelationArgument(newLeftRelationSet);
                    raDao.persist(newLeftRelationArgument);
                    newLeftArgument = newLeftRelationArgument;
                }
            }

            // Check right argument
            if (rightArgument.isConcept()) {
                Concept retrievedRightConcept = cDao.getConcept(rightArgument.
                        getConcept());
                newRightConcept = newConcepts.
                        storeConcept(retrievedRightConcept);
                RelationArgument retrievedRightArgument = raDao.
                        getRelationArgument(newRightConcept);
                if (!isNull(retrievedRightArgument)) {
                    raDao.merge(retrievedRightArgument);
                    newRightArgument = retrievedRightArgument;
                } else {
                    RelationArgument newRightRelationArgument =
                            new RelationArgument(newRightConcept);
                    raDao.persist(newRightRelationArgument);
                    newRightArgument = newRightRelationArgument;
                }
            } else if (rightArgument.isRelationSet()) {
                RelationSet retrievedRightRelationSet = rsDao.getRelationSet(
                        rightArgument.getRelationSet());
                newRightRelationSet = newRelationSets.storeRelationSet(
                        retrievedRightRelationSet);
                RelationArgument retrievedRightArgument = raDao.
                        getRelationArgument(retrievedRightRelationSet);
                if (!isNull(retrievedRightArgument)) {
                    raDao.merge(retrievedRightArgument);
                    newRightArgument = retrievedRightArgument;
                } else {
                    RelationArgument newRightRelationArgument =
                            new RelationArgument(newRightRelationSet);
                    raDao.persist(newRightRelationArgument);
                    newRightArgument = newRightRelationArgument;
                }
            }

            Relation myRelation = new Relation();
            myRelation.setLeftArgument(newLeftArgument);
            myRelation.setRightArgument(newRightArgument);
            myRelation.setRelationType(relation.getRelationType());
            myRelation.setLinguisticSupport(relation.
                    getLinguisticallySupported());
            myRelation.setInferred(relation.getInferred());

            Relation newRelation;

            // Check relation's existence in the database
            Relation retrievedRelation = rDao.getRelation(newLeftArgument,
                    newRightArgument, relation.getRelationType().
                    getForwardName());
            if (!isNull(retrievedRelation)) {
                newRelation = newRelations.storeRelation(retrievedRelation);
            } else {
                newRelation = newRelations.storeRelation(myRelation);
            }
            newRelationSet.addRelation(newRelation);
        }

        newRelationSet.setName(relationSet.getName());
        if (!isNull(relationSet.getLanguageRepresentations())) {
            for (LanguageRepresentation lr : relationSet.
                    getLanguageRepresentations()) {
                newRelationSet.addLanguageRepresentation(lr);
            }
        }
        if (!isNull(relationSet.getMotoricRepresentations())) {

            for (MotoricRepresentation mr : relationSet.
                    getMotoricRepresentations()) {
                newRelationSet.addMotoricRepresentation(mr);
            }
        }
        if (!isNull(relationSet.getVisualRepresentations())) {
            for (VisualRepresentation vr : relationSet.
                    getVisualRepresentations()) {
                newRelationSet.addVisualRepresentation(vr);
            }
        }

        rsDao.persist(newRelationSet);
        Query query = getEntityManager().createNamedQuery("findRelationSet").
                setParameter("relationSet", newRelationSet);
        Set<RelationSet> retrievedRelationSetsList =
                new LinkedHashSet<>(query.getResultList());
        if (retrievedRelationSetsList.isEmpty()) {
            return new RelationSet();
        }
        return retrievedRelationSetsList.iterator().next();
    }

    /**
     * Finds all RelationSets that have at least one relation with leftArgument
     * or rightArgument.
     *
     * @param relationArgument the relation argument to search by
     *
     * @return a list of RelationSets
     */
    @Override
    public Set<RelationSet> getRelationSetsByRelationArgument(
            RelationArgument relationArgument) {
        Query query = getEntityManager().createNamedQuery(
                "findRelationSetsByRelationArgument").setParameter(
                        "relationArgument", relationArgument);
        Set<RelationSet> retrievedRelationSetsList =
                new LinkedHashSet<>(query.getResultList());
        return retrievedRelationSetsList;
    }

    /**
     * Finds all RelationSets that have at least one relation with a concept
     * as LeftArgument or RightArgument.
     *
     * @param concept the concept to search by
     *
     * @return a list of RelationSets
     */
    @Override
    public Set<RelationSet> getRelationSetsByConcept(Concept concept) {
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        RelationArgument newRelationArgument = raDao.
                getRelationArgument(concept);
        Set<RelationSet> retrievedRelationSetsList =
                getRelationSetsByRelationArgument(
                        newRelationArgument);
        if (retrievedRelationSetsList.isEmpty()) {
            return new LinkedHashSet<>();
        }
        return retrievedRelationSetsList;
    }

    /**
     * Finds relations that have a given relationArgument as leftArgument
     *
     * @param relationArgument the relation argument to search by
     *
     * @return a list of RelationSets
     */
    @Override
    public Set<RelationSet> getRelationSetsWithRelationArgumentAsLeftArgument(
            RelationArgument relationArgument) {
        Query query = getEntityManager().createNamedQuery(
                "findRelationSetsByLeftRelationArgument").
                setParameter("relationArgument", relationArgument);
        Set<RelationSet> retrievedRelationSetsList =
                new LinkedHashSet<>(query.getResultList());
        return retrievedRelationSetsList;
    }

    /**
     * Finds relations that have a given relationArgument as rightArgument
     *
     * @param relationArgument the relation argument to search by
     *
     * @return a list of RelationSets
     */
    @Override
    public Set<RelationSet> getRelationSetsWithRelationArgumentAsRightArgument(
            RelationArgument relationArgument) {
        Query query = getEntityManager().createNamedQuery(
                "findRelationSetsByRightRelationArgument").
                setParameter("relationArgument", relationArgument);
        Set<RelationSet> retrievedRelationSetsList = 
                new LinkedHashSet<>(query.getResultList());
        return retrievedRelationSetsList;
    }

    /**
     * Finds relations sets that contain relations with a given concept as a
     * rightArgument.
     *
     * @param concept the concept to search by
     *
     * @return a list of relation sets
     */
    @Override
    public Set<RelationSet> getRelationSetsWithConceptAsRightArgument(
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
     *
     * @return a list of relation sets
     */
    @Override
    public Set<RelationSet> getRelationSetsByRelation(Relation relation) {
        // First get the relation from the database
        RelationDao rDao = new RelationDaoImpl();
        Relation retrievedRelation = rDao.
                getRelation(relation.getLeftArgument(), relation.
                        getRightArgument(), relation.getRelationType().
                        getForwardName());
        Query query = getEntityManager().createNamedQuery(
                "findRelationSetsByRelation").
                setParameter("relation", retrievedRelation);
        Set<RelationSet> retrievedRelationSetsList = 
                new LinkedHashSet<>(query.getResultList());
        return retrievedRelationSetsList;
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
     *
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
        int i = 0;
        for (RelationSet_Relation item : relationSet.getRelations()) {
            i++;
            q.setParameter(i + 2, item);
        }
        return q;
    }

}
