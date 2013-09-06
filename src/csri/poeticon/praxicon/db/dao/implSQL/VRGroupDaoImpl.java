/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao.implSQL;

import csri.poeticon.praxicon.db.dao.VRGroupDao;
import csri.poeticon.praxicon.db.entities.VRGroup;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public class VRGroupDaoImpl extends JpaDao<Long, VRGroup> implements VRGroupDao{
    /**
     * Finds all VRGroup that have a given name
     * @param name the name to search for
     * @return a list of VRGroup found in the database
     */
    @Override
    public List<VRGroup> findAllByName(String name) {
        List<VRGroup> res;
        Query q = getEntityManager().createQuery("SELECT vrg FROM VRGroup vrg "
                + "where vrg.name like ?1");
        q.setParameter(1, "%" + name + "%");
        res = q.getResultList();

        return res;
    }
}
