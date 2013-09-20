/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao.implSQL;

import csri.poeticon.praxicon.db.dao.VisualRepresentationGroupDao;
import csri.poeticon.praxicon.db.entities.VisualRepresentationGroup;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public class VisualRepresentationGroupDaoImpl extends JpaDao<Long, VisualRepresentationGroup> implements VisualRepresentationGroupDao{
    /**
     * Finds all VisualRepresentationGroup that have a given name
     * @param name the name to search for
     * @return a list of VisualRepresentationGroup found in the database
     */
    @Override
    public List<VisualRepresentationGroup> findAllByName(String name) {
        List<VisualRepresentationGroup> res;
        Query q = getEntityManager().createQuery("SELECT vrg FROM VisualRepresentationGroup vrg "
                + "where vrg.name like ?1");
        q.setParameter(1, "%" + name + "%");
        res = q.getResultList();

        return res;
    }
}
