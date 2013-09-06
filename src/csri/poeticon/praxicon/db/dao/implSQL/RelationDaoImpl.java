/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao.implSQL;

import csri.poeticon.praxicon.db.dao.RelationDao;
import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.IntersectionOfRelations;
import csri.poeticon.praxicon.db.entities.Relation;
import csri.poeticon.praxicon.db.entities.RelationChain;
import csri.poeticon.praxicon.db.entities.RelationChain_Relation;
import csri.poeticon.praxicon.db.entities.TypeOfRelation;
import csri.poeticon.praxicon.db.entities.TypeOfRelation.RELATION_NAME;
import csri.poeticon.praxicon.db.entities.UnionOfIntersections;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public class RelationDaoImpl extends JpaDao<Long, Relation> implements RelationDao{
    /**
     * Finds relations with a given relation name
     * @param nameOfTheRelation the relation name to be searched
     * @return a list of relations
     */
    public List<Relation> findByType(String nameOfTheRelation)
    {
        Query q = getEntityManager().createQuery("SELECT r FROM Relation r, TypeOfRelation rt " + 
                "WHERE r.type = rt and (rt.forwardName = ?1 or  rt.backwardName = ?1)");
         q.setParameter(1, TypeOfRelation.RELATION_NAME.valueOf(nameOfTheRelation));
        return q.getResultList();        
    }

    /**
     * Finds Concepts that are objects of relations with a given name
     * @param nameOfTheRelation the relation name to be searched
     * @return a list of concepts
     */
    @Override
    public List<Concept> getObjectsOfARelation(String nameOfTheRelation)
    {
        RELATION_NAME t = TypeOfRelation.RELATION_NAME.valueOf(nameOfTheRelation);
        Query q = getEntityManager().createQuery("SELECT r FROM Relation r, TypeOfRelation rtype " +
                "WHERE r.type = rtype.id and rtype.forwardName = ?1");
         q.setParameter(1, t);
         
        List<Relation> rels = q.getResultList();
        if(rels!=null)
        {
            List<Concept> res = new ArrayList<Concept>();
            for (int i = 0; i < rels.size(); i++)
            {
                res.add(rels.get(i).getObj());
            }
         return res;
        }
        return null;
    }

    /**
     * Finds Concepts that are related to a given concept
     * @param c the concept to be searched
     * @return a list of concepts
     */
    @Override
    public List<Concept> getRelatedConcepts(Concept c)
    {
        Query q = getEntityManager().createQuery("SELECT r FROM Relation r " +
                "WHERE r.obj = ?1 or r.subject = ?1");
        q.setParameter(1, c);
        List<Relation> rels = q.getResultList();

        List<Concept> res = new ArrayList<Concept>();

        for(Relation r:rels)
        {
            List<RelationChain_Relation> functions = r.getMainFunctions();
            for (RelationChain_Relation rr: functions)
            {
                RelationChain rc = rr.getRelationChain();
                List<Relation> relations = rc.getActualRelations();
                for(Relation tmpR: relations)
                {
                    if (!tmpR.getObj().equals(c))
                    {
                        res.add(tmpR.getObj());
                    }
                    if (!tmpR.getSubject().equals(c))
                    {
                        res.add(tmpR.getSubject());
                    }
                }
            }
        }

        return res;
    }

    /**
     * Finds relations that have a given concept as object and creates Union of
     * Intersections that contain only one relation each
     * @param c the concept to be searched
     * @return a list of UnionOfIntersections
     */
    @Override
    public List<UnionOfIntersections> getObjRelations(Concept c)
    {
        Query q = getEntityManager().createQuery("SELECT r FROM Relation r " +
                "WHERE r.obj = ?1 or r.subject = ?1");
        q.setParameter(1, c);
        List<Relation> objRels = q.getResultList();
        List<UnionOfIntersections> res = new ArrayList<UnionOfIntersections>();
        for (int i = 0; i < objRels.size(); i++)
        {
            Relation r = objRels.get(i);
            if (r.getObj().equals(c))
            {
                r.setObj(r.getSubject());
                r.setSubject(c);
                TypeOfRelation tmpType = new TypeOfRelation();
                TypeOfRelation.RELATION_NAME tmp = r.getType().getBackwardName();
                tmpType.setBackwardName(r.getType().getForwardName());
                tmpType.setForwardName(tmp);
                r.setType(tmpType);
            }
            RelationChain rc = new RelationChain();
            rc.addRelation(r, 0);
            IntersectionOfRelations ir = new IntersectionOfRelations();
            ir.getRelations().add(rc);
            rc.getIntersections().add(ir);
            UnionOfIntersections ui = new UnionOfIntersections();
            ui.setInherent(false);
            ui.setPercentage(0);
            ui.getIntersections().add(ir);
            ir.getUnions().add(ui);
            ui.setConcept(c);
            if (!c.getRelations().contains(ui))
            {
                res.add(ui);
            }
        }
        return res;
    }

    /**
     * Finds all relations of a given concept
     * @param c the concept to be searched
     * @return a list of Relation
     */
    @Override
    public List<Relation> allRelationsOf(Concept c)
    {
        getEntityManager().clear();

        Query q = getEntityManager().createQuery("SELECT r FROM Relation r " +
                "WHERE r.obj = ?1");
        q.setParameter(1, c);
        
        List<Relation> res1 = q.getResultList();

        q = getEntityManager().createQuery("SELECT r FROM Relation r " +
                "WHERE r.subject = ?1");
        q.setParameter(1, c);
        List<Relation> res2 = q.getResultList();
        List<Relation> res = new ArrayList<Relation>(res1.size()+res2.size());
        res.addAll(res1);
        res.addAll(res2);
        return res;
    }

    /**
     * Finds all concepts related to a given concept
     * @param c the concept to be searched
     * @return a list of concepts
     */
    @Override
    public List<Concept> relatedConcepts(Concept c)
    {
        Query q = getEntityManager().createQuery("SELECT r FROM Relation r " +
                "WHERE r.obj = ?1 or r.subject = ?1");
        q.setParameter(1, c);
        List<Relation> objRels = q.getResultList();
        List<Concept> res = new ArrayList<Concept>();

        for (int i = 0; i < objRels.size(); i++)
        {
            Relation r = objRels.get(i);
            if (r.getObj().equals(c))
            {
                if (!res.contains(r.getSubject()))
                {
                    res.add(r.getSubject());
                }
            }
            else
            {
                if (!res.contains(r.getObj()))
                {
                    res.add(r.getObj());
                }
            }

            List<RelationChain_Relation> tmp = r.getMainFunctions();
            for (int j = 0; j < tmp.size(); j++)
            {
                List<IntersectionOfRelations> inters = tmp.get(j).getRelationChain().getIntersections();
                for (int k = 0; k < inters.size(); k++)
                {
                    List<UnionOfIntersections> unions = inters.get(k).getUnions();
                    for (int l = 0; l < unions.size(); l++)
                    {
                        Concept tmpC = unions.get(l).getConcept();
                        if (!res.contains(tmpC) && tmpC != null)
                        {
                            res.add(tmpC);
                        }
                    }
                }
            }
        }
        return res;
    }

    /**
     * Finds all concepts are owners of (have a union of intersections containing) a given relation
     * @param rel the realtion to be searched
     * @return a list of concepts
     */
    @Override
    public List<Concept> getOwners(Relation rel)
    {
        Query q = getEntityManager()
                .createQuery("Select c From Concept c, IN(c.relations) as union, IntersectionOfRelations inter, " +
                             "IN(inter.unions) as interUnion, " +
                             "IN(inter.relations) AS interRel, IN(interRel.relations) AS rcr, RelationChain_Relation rc " +
                             "where union.union = interUnion and rcr = rc and rc.relation = ?1");
        q.setParameter(1, rel);
        return q.getResultList();
    }

    /**
     * Creates q query to search for a relation using subject, object and type
     * @param entity the relation to be searched
     * @return a query to search for the relation
     */
    @Override
    public Query getEntityQuery(Relation entity)
    {
        Query q = getEntityManager().createQuery("SELECT e FROM Relation e " +
                "where e.subject = ?1 and e.obj = ?2 and e.type = ?3"
                );
        q.setParameter(1, entity.getSubject());
        q.setParameter(2, entity.getObj());
        q.setParameter(3, entity.getType());
        return q;
    }

    /**
     * Checks if two concepts are related
     * @param c1 the first concept
     * @param c2 the second concept
     * @return true/false
     */
    @Override
    public boolean areRelated(Concept c1, Concept c2)
    {
       Query q = getEntityManager().createQuery("SELECT r FROM Relation r " +
               "WHERE (r.obj = ?1 and r.subject = ?2) or (r.subject = ?1 and r.obj = ?2)");
       q.setParameter(1, c1);
       q.setParameter(2, c2);
       List<Relation> objRels = q.getResultList();
       return objRels.size() > 0;
    }

    /**
     * Gets the type-token relations with subject a given concept and
     * the token-type relations with object a given concept
     * @param c the concept
     * @return List of relations
     */
    @Override
    public List<Relation> getSubConcepts(Concept c)
    {
        Query q = getEntityManager().createQuery("SELECT r FROM Relation r, TypeOfRelation tr " +
                   "WHERE (r.subject = ?1 and r.type = tr and tr.forwardName = ?2 and tr.backwardName = ?3) "
                   + "or (r.obj = ?1 and r.type = tr and tr.forwardName = ?3 and tr.backwardName = ?2) ");

        q.setParameter(1, c);
        q.setParameter(2, TypeOfRelation.RELATION_NAME.TYPE_TOKEN);
        q.setParameter(3, TypeOfRelation.RELATION_NAME.TOKEN_TYPE);

        List<Relation> objRels = q.getResultList();
        return objRels;
    }

    /**
     * Finds the relations of a given concept that have a certain typeofrelation.
     * Checks only for the given concept as a subject
     * @param concept the concept
     * @param type the type of relation
     * @return List of relations
     */
    @Override
    public List<Relation> findRelationsByConceptTypeOfRelation(Concept concept, TypeOfRelation type) {
        Query q = getEntityManager().createQuery("SELECT r FROM Relation r, TypeOfRelation tr "
                + "WHERE (r.subject = ?1 and r.type = tr and tr.forwardName = ?2)");
        q.setParameter(1, concept);
        q.setParameter(2, type.getForwardName());
        return q.getResultList();
    }

    /**
     * Finds the relations of a given concept that have a certain typeofrelation.
     * Checks for the given concept as a subject or object (reverse type of relation for object)
     * @param concept the concept
     * @param type the type of relation
     * @return List of relations
     */
    @Override
    public List<Relation> allRelationsOfByRelationType(Concept concept, TypeOfRelation type) {
        Query q = getEntityManager().createQuery("SELECT r FROM Relation r, TypeOfRelation tr "
                + "WHERE (r.subject = ?1 and r.type = tr and tr.forwardName = ?2) or"
                + "(r.obj = ?1 and r.type = tr and tr.forwardName = ?3) ");
        q.setParameter(1, concept);
        q.setParameter(2, type.getForwardName());
        q.setParameter(3, type.getBackwardName());
        return q.getResultList();
    }

    /**
     * Finds the relations of a given concept that have a certain relation name.
     * Checks for the given concept as a subject or object (reverse relation name for object)
     * @param concept the concept
     * @param name the relation name
     * @return List of relations
     */
    @Override
    public List<Relation> allRelationsOfByRelationName(Concept concept, TypeOfRelation.RELATION_NAME name) {
        Query q = getEntityManager().createQuery("SELECT r FROM Relation r, TypeOfRelation tr "
                + "WHERE (r.subject = ?1 and r.type = tr and tr.forwardName = ?2) or"
                + "(r.obj = ?1 and r.type = tr and tr.backwardName = ?2) ");
        q.setParameter(1, concept);
        q.setParameter(2, name);
        return q.getResultList();
    }

    /**
     * CHecks if a relation belongs to an inherent union of intersections
     * @param r the relation to check
     * @return true/false
     */
    @Override
    public boolean isPartOfInherentUnion (Relation r)
    {
        return r.getMainFunctions().get(0).getRelationChain().getIntersections().get(0).getUnions().get(0).isInherent();
    }

}
