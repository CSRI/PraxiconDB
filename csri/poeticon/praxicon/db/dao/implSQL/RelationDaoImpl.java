/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csri.poeticon.praxicon.db.dao.implSQL;

import csri.poeticon.praxicon.db.dao.RelationDao;
import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.IntersectionOfRelationChains;
import csri.poeticon.praxicon.db.entities.Relation;
import csri.poeticon.praxicon.db.entities.RelationChain;
import csri.poeticon.praxicon.db.entities.RelationType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 * @author Dimitris Mavroeidis
 *
 */
public class RelationDaoImpl extends JpaDao<Long, Relation> implements
        RelationDao {

    /**
     * Finds all concepts are owners of (have a union of intersections
     * containing) a given relation
     *
     * @param rel the relation to be searched
     * @return a list of concepts
     */
    @Override
    public List<Concept> getOwners(Relation rel) {
        Query q = getEntityManager().createQuery(
                "SELECT c FROM Concept c, " +
                 "IN(c.relations) AS union, " +
                 "IntersectionOfRelations inter, " +
                 "IN(inter.unions) AS interUnion, " +
                 "IN(inter.relations) AS interRel, " +
                 "IN(interRel.relations) AS rcr, " +
                 "RelationChain_Relation rc " +
                 "WHERE union.union = interUnion AND rcr = rc AND " +
                 "rc.relation = ?1");
        q.setParameter(1, rel);
        return q.getResultList();
    }

    /**
     * Finds relations that have a given concept as object and creates Union of
     * Intersections that contain only one relation each
     *
     * @param c the concept to be searched
     * @return a list of IntersectionOfRelationChains
     */
    @Override
    public List<IntersectionOfRelationChains> getObjRelations(Concept c) {
        Query q = getEntityManager().createQuery("SELECT r FROM Relation r " +
                 "WHERE r.Object = ?1 or r.Subject = ?1");
        q.setParameter(1, c);
        List<Relation> objRels = q.getResultList();
        List<IntersectionOfRelationChains> res = new ArrayList<>();
        for (Relation r : objRels) {
            if (r.getObject().equals(c)) {
                r.setObject(r.getSubject());
                r.setSubject(c);
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
            if (!c.getIntersectionsOfRelationChains().contains(ui)) {
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
    public List<Relation> allRelationsOf(Concept c) {
        getEntityManager().clear();

        Query q = getEntityManager().createQuery("SELECT r FROM Relation r " +
                 "WHERE r.Object = ?1");
        q.setParameter(1, c);

        List<Relation> res1 = q.getResultList();

        q = getEntityManager().createQuery("SELECT r FROM Relation r " +
                 "WHERE r.Subject = ?1");
        q.setParameter(1, c);
        List<Relation> res2 = q.getResultList();
        List<Relation> res = new ArrayList<>(res1.size() + res2.size());
        res.addAll(res1);
        res.addAll(res2);
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
    public boolean areRelated(Concept c1, Concept c2) {
        Query q = getEntityManager().createQuery(
                "SELECT r FROM Relation r " +
                 "WHERE (r.Object = ?1 AND r.Subject = ?2) OR " +
                 "(r.Subject = ?1 AND r.Object = ?2)");
        q.setParameter(1, c1);
        q.setParameter(2, c2);
        List<Relation> objRels = q.getResultList();
        return objRels.size() > 0;
    }

    /**
     * Finds the relations of a given concept that have a certain
     * typeofrelation. Checks only for the given concept as a subject
     *
     * @param concept the concept
     * @param type    the type of relation
     * @return List of relations
     */
    @Override
    public List<Relation> findRelationsByConceptTypeOfRelation(
            Concept concept, RelationType type) {
        Query q = getEntityManager().createQuery(
                "SELECT r FROM Relation r, RelationType tr " +
                 "WHERE (r.Subject = ?1 AND r.Type = tr AND " +
                 "tr.ForwardName = ?2)");
        q.setParameter(1, concept);
        q.setParameter(2, type.getForwardName());
        return q.getResultList();
    }
}
