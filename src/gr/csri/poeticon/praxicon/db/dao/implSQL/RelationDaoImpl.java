/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

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
 * @author Dimitris Mavroeidis
 *
 */
public class RelationDaoImpl extends JpaDao<Long, Relation> implements
        RelationDao {

//    /**
//     * Finds all concepts are owners of (have a union of intersections
//     * containing) a given relation
//     *
//     * @param rel the relation to be searched
//     * @return a list of concepts
//     */
//    @Override
//    public List<Concept> getOwners(Relation relation) {
//        Query q = getEntityManager().createQuery(
//                "SELECT c FROM Concept c, " +
//                 "IN(c.relations) AS union, " +
//                 "IntersectionOfRelations inter, " +
//                 "IN(inter.unions) AS interUnion, " +
//                 "IN(inter.relations) AS interRel, " +
//                 "IN(interRel.relations) AS rcr, " +
//                 "RelationChain_Relation rc " +
//                 "WHERE union.union = interUnion AND rcr = rc AND " +
//                 "rc.relation = ?1");
//        q.setParameter(1, relation);
//        return q.getResultList();
//    }
    /**
     * Finds relations that have a given relationArgument as object
     *
     * @param relationArgument the relation argument to be searched
     * @return a list of IntersectionOfRelationChains
     */
    
    // TODO: this needs repair. Find another way to get the related relations.
    
    @Override
    public List<RelationSet> getRelationSetsWithConceptAsObject(
            Concept concept) {
        Query query = getEntityManager().createNamedQuery(
                "findRelationsByConceptObjectOrSubject").
                setParameter("conceptId", concept.getId());
        List<Relation> objRels = query.getResultList();
        List<RelationSet> res = new ArrayList<>();
        for (Relation r : objRels) {
            if (r.getObject().isConcept()) {
                if (r.getObject().getConcept().equals(concept)) {
                    r.setObject(r.getSubject());
                    //r.setSubject(concept.getRelationArgument());
                    RelationType tmpType = new RelationType();
                    RelationType.relation_name_backward tmp =
                            r.getType().getBackwardName();
                    tmpType.setForwardName(r.getType().getForwardName());
                    tmpType.setBackwardName(tmp);
                    r.setType(tmpType);
                }
            }
            RelationSet rs = new RelationSet();
            rs.addRelation(r, (short)0);
        }
        return res;
    }

    /**
     * Finds all relations of a given concept
     *
     * @param c the concept to be searched
     * @return a list of Relation
     */
    @Override
    public List<Relation> getAllRelationsOfConcept(Concept concept) {
        getEntityManager().clear();
        Query query = getEntityManager().createNamedQuery(
                "findRelationsByConceptObjectOrSubject").
                setParameter("concept", concept);
        List<Relation> res = query.getResultList();
        return res;
    }

    /**
     * Checks if two concepts are related
     *
     * @param c1 the first concept
     * @param c2 the second concept
     * @return true/false
     */
    @Override
    public boolean areRelated(Concept concept1, Concept concept2) {
        Query query = getEntityManager().createNamedQuery("areRelated").
                setParameter("conceptIdSubject", concept1).
                setParameter("conceptIdObject", concept2);
        List<Relation> objRels = query.getResultList();
        return objRels.size() > 0;
    }

    /**
     * Finds the relations of a given concept that have a certain
     * type of relation. Checks only for the given concept as a subject
     *
     * @param concept the concept
     * @param type    the type of relation
     * @return List of relations
     */
    @Override
    public List<Relation> findRelationsByConceptTypeOfRelation(
            Concept concept, RelationType relationType) {
        Query query = getEntityManager().createNamedQuery(
                "findRelationsByConceptRelationType").
                setParameter("conceptId", concept).
                setParameter("relationType", relationType.getForwardName());
        return query.getResultList();
    }

    /**
     * Finds relations that have a given relationArgument as object
     *
     * @param relationArgument the relation argument to be searched
     * @return a list of IntersectionOfRelationChains
     */
    @Override
    public List<RelationSet> getRelationSetsWithRelationArgumentAsObject(
            RelationArgument relationArgument) {
        Query query = getEntityManager().createNamedQuery(
                "findRelationsByRelationArgumentObjectOrSubject").
                setParameter("conceptId", relationArgument.getId());
        List<Relation> objRels = query.getResultList();
        List<RelationSet> res = new ArrayList<>();
        for (Relation r : objRels) {
            if (r.getObject().equals(relationArgument)) {
                r.setObject(r.getSubject());
                r.setSubject(relationArgument);
                RelationType tmpType = new RelationType();
                RelationType.relation_name_backward tmp =
                        r.getType().getBackwardName();
                tmpType.setForwardName(r.getType().getForwardName());
                tmpType.setBackwardName(tmp);
                r.setType(tmpType);
            }
            RelationSet rs = new RelationSet();
            rs.addRelation(r, (short)0);
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
                "findRelationsByRelationArgumentObjectOrSubject").
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
                setParameter("relationArgumentSubject", relationArgument1).
                setParameter("relationArgumentObject", relationArgument2);
        List<Relation> objRels = query.getResultList();
        return objRels.size() > 0;
    }

    /**
     * Finds the relations of a given concept that have a certain
     * type of relation. Checks only for the given concept as a subject
     *
     * @param relationArgument the relation argument
     * @param relationType     the type of relation
     * @return A list of relations
     */
    @Override
    public List<Relation> findRelationsByRelationArgumentTypeOfRelation(
            RelationArgument relationArgument, RelationType relationType) {
        Query query = getEntityManager().createNamedQuery(
                "findRelationsByRelationArgumentRelationType").
                setParameter("relationArgument", relationArgument).
                setParameter("relationType", relationType.getForwardName());
        return query.getResultList();
    }
}
