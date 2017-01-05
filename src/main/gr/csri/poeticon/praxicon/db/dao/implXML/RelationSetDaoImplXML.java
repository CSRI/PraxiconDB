/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implXML;

import gr.csri.poeticon.praxicon.db.dao.RelationSetDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.JpaDao;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
import gr.csri.poeticon.praxicon.db.entities.RelationSet;
import java.util.Set;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 */
public class RelationSetDaoImplXML extends JpaDao<Long, RelationSet>
        implements RelationSetDao {

    @Override
    public Set<RelationSet> getRelationSetsByRelationArgument(
            RelationArgument relationArgument) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<RelationSet> getRelationSetsByConcept(Concept concept) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<RelationSet> getRelationSetsByRelation(Relation relation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

////    @Override
//    public List<RelationSet> getRelationSetsContainingConcept(Concept c)
//    {
//        List<RelationSet> res = new ArrayList<>();
//
//        Enumeration en = Constants.globalConcepts.elements();
//        while(en.hasMoreElements())
//        {
//            Concept concept = (Concept)en.nextElement();
//            for (int i =0; i < concept.getRelations().size(); i++)
//            {
//                for (int j = 0; j < concept.getRelations().size(); j++)
//                {
//                    for (int k = 0; k < concept.getRelations().size(); k++)
//                    {
//                        List<Relation> rc = concept.getRelations().get(k).getActualRelations();
//                        for (Relation rc1 : rc) {
//                            if (rc1.getLeftArgument().getName().equalsIgnoreCase(c.getName()) || rc1.getObject().getName().equalsIgnoreCase(c.getName())) {
//                                res.add(concept.getRelations().get(i).getIntersections().get(j).getRelations().get(k));
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
    public RelationSet updatedRelationSet(RelationSet relationSet) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param entity
     *
     * @return null //for now
     */
    @Override
    public Query getEntityQuery(RelationSet entity) {
        return null;
    }

    @Override
    public Set<RelationSet> getRelationSetsWithRelationArgumentAsRightArgument(
            RelationArgument relationArgument) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<RelationSet> getRelationSetsWithConceptAsRightArgument(
            Concept concept) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RelationSet getRelationSet(RelationSet relationSet) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<RelationSet> getRelationSetsWithRelationArgumentAsLeftArgument(
            RelationArgument relationArgument) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RelationSet getRelationSetByName(String relationSetName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
