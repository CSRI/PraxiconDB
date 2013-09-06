/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao.implSQL;

import csri.poeticon.praxicon.db.dao.MRGroupDao;
import csri.poeticon.praxicon.db.entities.MRGroup;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public class MRGroupDaoImpl extends JpaDao<Long, MRGroup> implements MRGroupDao{
    /**
     * Finds the MR Group that have a given name
     * @param name the name to search
     * @return A list of MRGroups
     */
    @Override
    public List<MRGroup> findAllByName(String name) {
        List<MRGroup> res;
        Query q = getEntityManager().createQuery("SELECT mrg FROM MRGroup mrg "
                + "where mrg.name like ?1");
        q.setParameter(1, "%" + name + "%");
        res = q.getResultList();

        return res;
    }
}
