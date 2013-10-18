/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao.implSQL;

import csri.poeticon.praxicon.db.dao.MotoricRepresentationGroupDao;
import csri.poeticon.praxicon.db.entities.MotoricRepresentationGroup;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public class MotoricRepresentationGroupDaoImpl extends JpaDao<Long, MotoricRepresentationGroup> implements MotoricRepresentationGroupDao
{
    /**
     * Finds the MotoricRepresentation Group that have a given name
     * @param name the name to search
     * @return A list of MotoricRepresentationGroups
     */
    @Override
    public List<MotoricRepresentationGroup> findAllByName(String name)
    {
        List<MotoricRepresentationGroup> res;
        Query q = getEntityManager().createQuery("SELECT mrg FROM MotoricRepresentationGroup mrg "
                + "where mrg.name like ?1");
        q.setParameter(1, "%" + name + "%");
        res = q.getResultList();

        return res;
    }
}
