/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implXML;

import gr.csri.poeticon.praxicon.db.dao.MotoricRepresentationDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.JpaDao;
import gr.csri.poeticon.praxicon.db.entities.MotoricRepresentation;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 */
public class MotoricRepresentationDaoImplXML
        extends JpaDao<Long, MotoricRepresentation>
        implements MotoricRepresentationDao {

    @Override
    public Query getEntityQuery(MotoricRepresentation entity) {
        return null;
    }

//    @Override
//    public List<MotoricRepresentation> getEntries(Concept c) {
//        List<MotoricRepresentation> res = new ArrayList<>();
//        for (int i = 0; i < c.getMotoricRepresentations().size(); i++) {
//            res.add(c.getMotoricRepresentations().get(i));
//        }
//        Comparator<MotoricRepresentation> mrCom = new MotoricEntryComparator();
//        Collections.sort(res, mrCom);
//        return res;
//    }
}
