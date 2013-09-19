/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao.implXML;

import csri.poeticon.praxicon.db.dao.implSQL.JpaDao;
import csri.poeticon.praxicon.Constants;
import csri.poeticon.praxicon.db.dao.UnionOfIntersectionsDao;
import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.IntersectionOfRelations;
import csri.poeticon.praxicon.db.entities.Relation;
import csri.poeticon.praxicon.db.entities.RelationChain;
import csri.poeticon.praxicon.db.entities.TypeOfRelation;
import csri.poeticon.praxicon.db.entities.UnionOfIntersections;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public class UnionOfIntersectionsDaoImplXML extends JpaDao<Long, UnionOfIntersections> implements UnionOfIntersectionsDao{


    @Override
    public Query getEntityQuery(UnionOfIntersections entity)
    {
        return null;
    }

    private boolean containsRelation(List<UnionOfIntersections> res, Relation r)
    {
        RelationChain rc1 = new RelationChain();
        rc1.addRelation(r, 0);
        IntersectionOfRelations ir = new IntersectionOfRelations();
        ir.getRelations().add(rc1);
        rc1.getIntersections().add(ir);
        UnionOfIntersections ui = new UnionOfIntersections();
        ui.setInherent(false);
        ui.setPercentage(0);
        ui.getIntersections().add(ir);
        ir.getUnions().add(ui);
        return res.contains(ui);
    }

    @Override
    public List<UnionOfIntersections> getAllUnions(Concept c) {
        List<UnionOfIntersections> res = new ArrayList<UnionOfIntersections>();
        res.addAll(c.getRelations());

        Enumeration en = Constants.globalConcepts.elements();
        while(en.hasMoreElements())
        {
            Concept con = (Concept)en.nextElement();
            if(!con.equals(c))
            {
                for (int i =0; i < con.getRelations().size(); i++)
                {
                    for (int j = 0; j < con.getRelations().get(i).getIntersections().size(); j++)
                    {
                        for (int k = 0; k < con.getRelations().get(i).getIntersections().get(j).getRelations().size(); k++)
                        {
                            List<Relation> rc = con.getRelations().get(i).getIntersections().get(j).getRelations().get(k).getActualRelations();
                            for (int l = 0; l < rc.size(); l++)
                            {
                                if (rc.get(l).getSubject().getName().equalsIgnoreCase(c.getName()) || rc.get(l).getObject().getName().equalsIgnoreCase(c.getName()))
                                {
                                    Relation r = rc.get(l);
                                    if (!containsRelation(res, r))
                                    {
                                        Relation newR;
                                        if (r.getObject().getName().equalsIgnoreCase(c.getName()))
                                        {
                                            newR = new Relation();
                                            newR.setObject(r.getSubject());
                                            newR.setSubject(c);
                                            TypeOfRelation.RELATION_NAME tmp = r.getType().getBackwardName();
                                            newR.setType(new TypeOfRelation());
                                            newR.getType().setBackwardName(r.getType().getForwardName());
                                            newR.getType().setForwardName(tmp);
                                            newR.getType().setXmlRelationBackward(r.getType().getXmlRelationForward());
                                            newR.getType().setXmlRelationForward(r.getType().getXmlRelationBackward());
                                        }
                                        else
                                            newR =r;
                                        RelationChain rc1 = new RelationChain();
                                        rc1.addRelation(newR, 0);
                                        IntersectionOfRelations ir = new IntersectionOfRelations();
                                        ir.getRelations().add(rc1);
                                        rc1.getIntersections().add(ir);
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
                                }
                            }
                        }
                    }
                }
            }
        }
        return res;
    }

    @Override
    public UnionOfIntersections transformUnion(UnionOfIntersections union, Concept c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UnionOfIntersections createUnion(String forwardName, String backwardName, Concept value, Concept owner)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
