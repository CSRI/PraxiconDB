/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao.implSQL;

import csri.poeticon.praxicon.db.dao.UnionOfIntersectionsDao;
import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.IntersectionOfRelations;
import csri.poeticon.praxicon.db.entities.Relation;
import csri.poeticon.praxicon.db.entities.RelationChain;
import csri.poeticon.praxicon.db.entities.TypeOfRelation;
import csri.poeticon.praxicon.db.entities.UnionOfIntersections;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public class UnionOfIntersectionsDaoImpl extends JpaDao<Long, UnionOfIntersections> implements UnionOfIntersectionsDao{
    /**
     * Creates q query to search for a UnionOfIntersections using names of the intersections of relations
     * @param entity the UnionOfIntersections to be searched
     * @return a query to search for the UnionOfIntersections
     */
    @Override
    public Query getEntityQuery(UnionOfIntersections entity)
    {
        StringBuilder sb = new StringBuilder("SELECT e FROM UnionOfIntersections e");
        for (int i = 0; i  < entity.getIntersections().size(); i++)
        {
            sb.append(", IN (e.intersections) as intersection").append(i);
        }
        sb.append(" where UPPER(e.name) = ?1");
        for (int i = 0; i  < entity.getIntersections().size(); i++)
        {
            sb.append("and intersection").append(i).append("=?").append(i+2);
        }
        Query q = getEntityManager().createQuery(sb.toString());
        q.setParameter(1, entity.getName().toUpperCase());
        for (int i = 0; i  < entity.getIntersections().size(); i++)
        {
            q.setParameter(i+2, entity.getIntersections().get(i));
        }
        return q;
    }

    /**
     * Not supported yet. To be added to a fytyre version
     * @param c the concept to be searched
     * @return a list of UnionOfIntersections
     */
    @Override
    public List<UnionOfIntersections> getAllUnions(Concept c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Transforms a union of intersections to have a given concept as head
     * @param union the union of intersections to be transformed
     * @param c the concept to be used
     * @return a union of intersections
     */
    @Override
    public UnionOfIntersections transformUnion(UnionOfIntersections union, Concept c) {
        UnionOfIntersections res = new UnionOfIntersections();
        List<IntersectionOfRelations> inters = union.getIntersections();
        for (int i = 0; i < inters.size(); i++)
        {
            boolean stop = false;
            List<RelationChain>chains = inters.get(i).getRelations();
            for (int j = 0; j < chains.size() && !stop; j++)
            {
                List<Relation>relations = chains.get(j).getActualRelations();
                for (int k = 0; k < relations.size() && !stop; k++)
                {
                    if (relations.get(k).getSubject().equals(c)
                            || relations.get(k).getObject().equals(c))
                    {
                        RelationChain chain = new RelationChain();
                        RelationChain chainBack = new RelationChain();
                        if (relations.get(k).getSubject().equals(c))
                        {
                            chain.addRelation(relations.get(k), 1);
                        }
                        else
                        {
                            chainBack.addRelation(relations.get(k), 1);
                        }
                        for (int l = k + 1 ; l < relations.size(); l++)
                        {
                            chain.addRelation(relations.get(l), chain.getRelations().size()+1);
                        }
                        for (int l = k - 1 ; l >= 0; l--)
                        {
                            Relation r = chain.getRelations().get(l).getRelation();
                            TypeOfRelation type = new TypeOfRelation();
                            type.setForwardName(r.getType().getBackwardName());
                            type.setBackwardName(r.getType().getForwardName());
                            Relation newRelation = new Relation();
                            newRelation.setType(type);
                            newRelation.setObject(r.getSubject());
                            newRelation.setSubject(r.getObject());
                            chainBack.addRelation(newRelation, chain.getRelations().size()+1);
                        }
                        IntersectionOfRelations inter = new IntersectionOfRelations();
                        inter.addRelationChain(chain);
                        for (int l = 0; l < chains.size(); l++)
                        {
                            if (l != j)
                            {
                                RelationChain tmpChain = new RelationChain();
                                for (int m = 0; m < chainBack.getRelations().size(); m++)
                                {
                                    tmpChain.addRelation(chainBack.getRelations().get(m).getRelation(), chainBack.getRelations().get(m).getRelationOrder());
                                }
                                for (int m = 0; m < chains.get(l).getRelations().size(); m++)
                                {
                                    tmpChain.addRelation(chains.get(l).getRelations().get(m).getRelation(), chains.get(l).getRelations().get(m).getRelationOrder()+chainBack.getRelations().size());
                                }
                                inter.addRelationChain(tmpChain);
                            }
                        }
                        stop = true;
                        res.addIntersection(inter);
                    }
                }
            }
        }

        return res;
    }

    /**
     * Creats a union of intersections containing only one relation with given forward and backward name
     * and given subject and object
     * @param forwardName the forward name of the relation
     * @param backwardName the backward name of the relation
     * @param value the object of the relation
     * @param owner the subject of the relation (and owner of the union)
     * @return a union of intersections
     */
    @Override
    public UnionOfIntersections createUnion(String forwardName, String backwardName, Concept value, Concept owner)
    {
        UnionOfIntersections union = new UnionOfIntersections();
        TypeOfRelation rt = new TypeOfRelation();
        rt.setBackwardName(backwardName);
        rt.setForwardName(forwardName);
        Relation rel = new Relation();
        rel.setType(rt);
        rel.setSubject(owner);
        rel.setObject(value);
        RelationChain rc = new RelationChain();
        rc.addRelation(rel, 0);
        IntersectionOfRelations inter = new IntersectionOfRelations();
        inter.addRelationChain(rc);
        union.addIntersection(inter);
        union.setConcept(owner);

        return union;
    }

}
