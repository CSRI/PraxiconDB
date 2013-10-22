/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao.implSQL;

import csri.poeticon.praxicon.db.dao.RelationDao;
import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.Relation;
import csri.poeticon.praxicon.db.entities.RelationChain;
import csri.poeticon.praxicon.db.entities.RelationChain_Relation;
import csri.poeticon.praxicon.db.entities.RelationType;
//import csri.poeticon.praxicon.db.entities.RelationType.RELATION_NAME;
import csri.poeticon.praxicon.db.entities.IntersectionOfRelationChains;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public class RelationDaoImpl extends JpaDao<Long, Relation> implements RelationDao
{
    /**
     * Finds all concepts are owners of (have a union of intersections containing) a given relation
     * @param rel the relation to be searched
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
     * Finds relations that have a given concept as object and creates Union of
     * Intersections that contain only one relation each
     * @param c the concept to be searched
     * @return a list of IntersectionOfRelationChains
    */
    @Override
    public List<IntersectionOfRelationChains> getObjRelations(Concept c)
    {
        Query q = getEntityManager().createQuery("SELECT r FROM Relation r " +
                "WHERE r.obj = ?1 or r.subject = ?1");
        q.setParameter(1, c);
        List<Relation> objRels = q.getResultList();
        List<IntersectionOfRelationChains> res = new ArrayList<IntersectionOfRelationChains>();
        for (int i = 0; i < objRels.size(); i++)
        {
            Relation r = objRels.get(i);
            if (r.getObject().equals(c))
            {
                r.setObject(r.getSubject());
                r.setSubject(c);
                RelationType tmpType = new RelationType();
                RelationType.relation_name tmp = r.getType().getBackwardName();
                tmpType.setBackwardName(r.getType().getForwardName());
                tmpType.setForwardName(tmp);
                r.setType(tmpType);
            }
            RelationChain rc = new RelationChain();
            rc.addRelation(r, 0);
            IntersectionOfRelationChains ir = new IntersectionOfRelationChains();
            ir.getRelations().add(rc);
            rc.getIntersections().add(ir);
            IntersectionOfRelationChains ui = new IntersectionOfRelationChains();

// TODO: Temporarily commented --> UnionOfIntersections should be converted to IntersectionOfRelationChains
//            ui.setInherent(false);
//            ui.setPercentage(0);
//            ui.getIntersections().add(ir);
//            ir.getUnions().add(ui);
//            ui.setConcept(c);
            if (!c.getIntersectionsOfRelationChains().contains(ui))
            {
                res.add(ui);
            }
        }
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
            if (r.getObject().equals(c))
            {
                if (!res.contains(r.getSubject()))
                {
                    res.add(r.getSubject());
                }
            }
            else
            {
                if (!res.contains(r.getObject()))
                {
                    res.add(r.getObject());
                }
            }

            List<RelationChain_Relation> tmp = r.getMainFunctions();
            for (int j = 0; j < tmp.size(); j++)
            {
                List<IntersectionOfRelationChains> inters = tmp.get(j).getRelationChain().getIntersections();

// TODO: Temporarily commented --> UnionOfIntersections should be converted to IntersectionOfRelationChains
//                for (int k = 0; k < inters.size(); k++)
//                {
//                    List<IntersectionOfRelationChains> unions = inters.get(k).getUnions();
//                    for (int l = 0; l < unions.size(); l++)
//                    {
//                        Concept tmpC = unions.get(l).getConcept();
//                        if (!res.contains(tmpC) && tmpC != null)
//                        {
//                            res.add(tmpC);
//                        }
//                    }
//                }
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
     * Finds the relations of a given concept that have a certain typeofrelation.
     * Checks for the given concept as a subject or object (reverse type of relation for object)
     * @param concept the concept
     * @param type the type of relation
     * @return List of relations
    */
    @Override
    public List<Relation> allRelationsOfByRelationType(Concept concept, RelationType type)
    {
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
    public List<Relation> allRelationsOfByRelationName(Concept concept, RelationType.relation_name name)
    {
        Query q = getEntityManager().createQuery("SELECT r FROM Relation r, TypeOfRelation tr "
                + "WHERE (r.subject = ?1 and r.type = tr and tr.forwardName = ?2) or"
                + "(r.obj = ?1 and r.type = tr and tr.backwardName = ?2) ");
        q.setParameter(1, concept);
        q.setParameter(2, name);
        return q.getResultList();
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
        q.setParameter(2, RelationType.relation_name.TYPE_TOKEN);
        q.setParameter(3, RelationType.relation_name.TOKEN_TYPE);

        List<Relation> objRels = q.getResultList();
        return objRels;
    }

    /**
     * Finds Concepts that are objects of relations with a given name
     * @param nameOfTheRelation the relation name to be searched
     * @return a list of concepts
     */
    @Override
    public List<Concept> getObjectsOfARelation(String name_of_the_relation)
    {
        RelationType.relation_name t = RelationType.relation_name.valueOf(name_of_the_relation);
        Query q = getEntityManager().createQuery("SELECT r FROM Relation r, TypeOfRelation rtype " +
                "WHERE r.type = rtype.id and rtype.forwardName = ?1");
         q.setParameter(1, t);

        List<Relation> rels = q.getResultList();
        if(rels!=null)
        {
            List<Concept> res = new ArrayList<Concept>();
            for (int i = 0; i < rels.size(); i++)
            {
                res.add(rels.get(i).getObject());
            }
         return res;
        }
        return null;
    }

    /**
     * Finds relations with a given relation name
     * @param nameOfTheRelation the relation name to be searched
     * @return a list of relations
     */
    @Override
    public List<Relation> findByType(String nameOfTheRelation)
    {
        Query q = getEntityManager().createQuery("SELECT r FROM Relation r, TypeOfRelation rt " + 
                "WHERE r.type = rt and (rt.forwardName = ?1 or  rt.backwardName = ?1)");
         q.setParameter(1, RelationType.relation_name.valueOf(nameOfTheRelation));
        return q.getResultList();        
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
                    if (!tmpR.getObject().equals(c))
                    {
                        res.add(tmpR.getObject());
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
     * Finds the relations of a given concept that have a certain typeofrelation.
     * Checks only for the given concept as a subject
     * @param concept the concept
     * @param type the type of relation
     * @return List of relations
     */
    @Override
    public List<Relation> findRelationsByConceptTypeOfRelation(Concept concept, RelationType type)
    {
        Query q = getEntityManager().createQuery("SELECT r FROM Relation r, TypeOfRelation tr "
                + "WHERE (r.subject = ?1 and r.type = tr and tr.forwardName = ?2)");
        q.setParameter(1, concept);
        q.setParameter(2, type.getForwardName());
        return q.getResultList();
    }

//  TODO: Check whether this should be deleted or declared in RelationDao, as it wasn't part of it
//    /**
//     * Creates q query to search for a relation using subject, object and type
//     * @param entity the relation to be searched
//     * @return a query to search for the relation
//     */
//    @Override
//    public Query getEntityQuery(Relation entity)
//    {
//        Query q = getEntityManager().createQuery("SELECT e FROM Relation e " +
//                "where e.subject = ?1 and e.obj = ?2 and e.type = ?3"
//                );
//        q.setParameter(1, entity.getSubject());
//        q.setParameter(2, entity.getObject());
//        q.setParameter(3, entity.getType());
//        return q;
//    }

// TODO: Should be deleted -or replaced- as unions ceized to exist
//    /**
//     * CHecks if a relation belongs to an inherent union of intersections
//     * @param r the relation to check
//     * @return true/false
//     */
//    @Override
//    public boolean isPartOfInherentUnion (Relation r)
//    {
//        return r.getMainFunctions().get(0).getRelationChain().getIntersections().get(0).isInherent();
//    }
}
