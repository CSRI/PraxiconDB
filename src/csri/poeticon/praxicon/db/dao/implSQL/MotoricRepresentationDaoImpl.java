/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao.implSQL;

import csri.poeticon.praxicon.db.dao.MotoricRepresentationDao;
import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.MotoricRepresentation;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public class MotoricRepresentationDaoImpl extends JpaDao<Long, MotoricRepresentation> implements MotoricRepresentationDao
{

    /**
     * Creates q query to search for a MotoricRepresentation using representation
     * @param entity the MotoricRepresentation to be searched
     * @return a query to search for the MotoricRepresentation
     */
    @Override
    public Query getEntityQuery(MotoricRepresentation entity)
    {
        Query q = getEntityManager().createQuery("SELECT e FROM MotoricRepresentation e " +
                "where UPPER(e.Comment) = ?1"
                );
        q.setParameter(1, entity.getComment().toUpperCase());
        return q;
    }

    /**
     * Gets the Motoric Representations of a given concept
     * @param c the concept
     * @return a list of MotoricRepresentations
     */
    @Override
    public List<MotoricRepresentation> getEntries(Concept c)
    {
        Query q = getEntityManager().createQuery("SELECT entry FROM " +
                "Concept c, IN(c.MotoricRepresentations) as MR, IN(MR.entries) entry " +
                "where c=?1"
                );
        q.setParameter(1, c);
        return q.getResultList();
    }
}
