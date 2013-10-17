/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao.implXML;

import csri.poeticon.praxicon.db.dao.implSQL.JpaDao;
import csri.poeticon.praxicon.db.entities.comparators.VisualEntryComparator;
import csri.poeticon.praxicon.db.dao.VisualRepresentationDao;
import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.VisualRepresentation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public class VisualRepresentationDaoImplXML extends JpaDao<Long, VisualRepresentation> implements VisualRepresentationDao{

    @Override
    public Query getEntityQuery(VisualRepresentation entity)
    {
        return null;
    }

    @Override
    public List<VisualRepresentation> getEntries(Concept c) {
        List<VisualRepresentation> res = new ArrayList<VisualRepresentation>();
        for (int i = 0; i < c.getVisualRepresentations().size(); i++)
        {
            res.add(c.getVisualRepresentations().get(i));
        }
        Comparator<VisualRepresentation> mrCom = new VisualEntryComparator();
        Collections.sort(res, mrCom);
        return res;
    }
}
