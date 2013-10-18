/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao.implXML;

import csri.poeticon.praxicon.db.dao.implSQL.JpaDao;
import csri.poeticon.praxicon.Constants;
import csri.poeticon.praxicon.db.dao.RelationChainDao;
import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.Relation;
import csri.poeticon.praxicon.db.entities.RelationChain;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public class RelationChainDaoImplXML extends JpaDao<Long, RelationChain> implements RelationChainDao
{
    @Override
    public List<RelationChain> getRelationChainsContainingConcept(Concept c)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }


//    @Override
//    public List<RelationChain> getRelationChainsContainingConcept(Concept c)
//    {
//        List<RelationChain> res = new ArrayList<RelationChain>();
//
//        Enumeration en = Constants.globalConcepts.elements();
//        while(en.hasMoreElements())
//        {
//            Concept con = (Concept)en.nextElement();
//            for (int i =0; i < con.getRelations().size(); i++)
//            {
//                for (int j = 0; j < con.getRelations().get(i).getIntersections().size(); j++)
//                {
//                    for (int k = 0; k < con.getRelations().get(i).getIntersections().get(j).getRelations().size(); k++)
//                    {
//                        List<Relation> rc = con.getRelations().get(i).getIntersections().get(j).getRelations().get(k).getActualRelations();
//                        for (int l = 0; l < rc.size(); l++)
//                        {
//                            if (rc.get(l).getSubject().getName().equalsIgnoreCase(c.getName()) || rc.get(l).getObject().getName().equalsIgnoreCase(c.getName()))
//                            {
//                                res.add(con.getRelations().get(i).getIntersections().get(j).getRelations().get(k));
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return res;
//    }

    @Override
    public Query getEntityQuery(RelationChain entity)
    {
        return null;
    }

}
