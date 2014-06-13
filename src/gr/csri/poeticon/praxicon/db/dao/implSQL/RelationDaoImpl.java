/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.RelationDao;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.IntersectionOfRelationChains;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationChain;
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
     * Finds relations that have a given concept as object and creates
     * Intersections of RelationChains that contain only one relation each
     *
     * @param c the concept to be searched
     * @return a list of IntersectionOfRelationChains
     */
    @Override
    public List<IntersectionOfRelationChains> getIntersectionsWithConceptAsObject(
            Concept concept) {
        Query query = getEntityManager().createNamedQuery(
                "findRelationsByConceptObjectOrSubject").
                setParameter("concept_id", concept.getId());
        List<Relation> objRels = query.getResultList();
        List<IntersectionOfRelationChains> res = new ArrayList<>();
        for (Relation r : objRels) {
            if (r.getObject().equals(concept)) {
                r.setObject(r.getSubject());
                r.setSubject(concept);
                RelationType tmpType = new RelationType();
                RelationType.relation_name_backward tmp =
                        r.getType().getBackwardName();
                tmpType.setForwardName(r.getType().getForwardName());
                tmpType.setBackwardName(tmp);
                r.setType(tmpType);
            }
            RelationChain rc = new RelationChain();
            rc.addRelation(r, 0);
            IntersectionOfRelationChains ir =
                    new IntersectionOfRelationChains();
            ir.getRelationChains().add(rc);
            rc.getIntersections().add(ir);
            IntersectionOfRelationChains ui =
                    new IntersectionOfRelationChains();
            if (!concept.getIntersectionsOfRelationChains().contains(ui)) {
                res.add(ui);
            }
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
                setParameter("concept_name", concept.getName());
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
                setParameter("concept_id_subject", concept1).
                setParameter("concept_id_object", concept2);
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
                setParameter("concept_id", concept).
                setParameter("relation_type", relationType.getForwardName());
        return query.getResultList();
    }
}
