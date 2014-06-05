/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implXML;

import gr.csri.poeticon.praxicon.db.dao.RelationChainDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.JpaDao;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.RelationChain;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 */
public class RelationChainDaoImplXML extends JpaDao<Long, RelationChain>
        implements RelationChainDao {

    @Override
    public List<RelationChain> getRelationChainsContainingConcept(Concept c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

//    @Override
//    public List<RelationChain> getRelationChainsContainingConcept(Concept c)
//    {
//        List<RelationChain> res = new ArrayList<>();
//
//        Enumeration en = Constants.globalConcepts.elements();
//        while(en.hasMoreElements())
//        {
//            Concept con = (Concept)en.nextElement();
//            for (int i =0; i < con.getRelations().size(); i++)
//            {
//                for (int j = 0; j < con.getRelations().size(); j++)
//                {
//                    for (int k = 0; k < con.getRelations().size(); k++)
//                    {
//                        List<Relation> rc = con.getRelations().get(k).getActualRelations();
//                        for (Relation rc1 : rc) {
//                            if (rc1.getSubject().getName().equalsIgnoreCase(c.getName()) || rc1.getObject().getName().equalsIgnoreCase(c.getName())) {
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
    /**
     *
     * @param entity
     * @return null //for now
     */
    @Override
    public Query getEntityQuery(RelationChain entity) {
        return null;
    }

}
