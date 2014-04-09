/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao.implXML;

import csri.poeticon.praxicon.db.dao.implSQL.JpaDao;
import csri.poeticon.praxicon.db.entities.comparators.MotoricEntryComparator;
import csri.poeticon.praxicon.db.dao.MotoricRepresentationDao;
import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.MotoricRepresentation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public class MotoricRepresentationDaoImplXML extends JpaDao<Long, MotoricRepresentation> implements MotoricRepresentationDao
{
    @Override
    public Query getEntityQuery(MotoricRepresentation entity)
    {
        return null;
    }

    @Override
    public List<MotoricRepresentation> getEntries(Concept c)
    {
        List<MotoricRepresentation> res = new ArrayList<>();
        for (int i = 0; i < c.getMotoricRepresentations().size(); i++)
        {
            res.add(c.getMotoricRepresentations().get(i));
        }
        Comparator<MotoricRepresentation> mrCom = new MotoricEntryComparator();
        Collections.sort(res, mrCom);
        return res;
    }
}
