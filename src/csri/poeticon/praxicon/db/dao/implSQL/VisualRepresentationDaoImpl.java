/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao.implSQL;

import csri.poeticon.praxicon.db.dao.VisualRepresentationDao;
import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.VisualRepresentation;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public class VisualRepresentationDaoImpl extends JpaDao<Long, VisualRepresentation> implements VisualRepresentationDao
{
    /**
     * Creates q query to search for a given VisualRepresentation using media type, representation ant prototype
     * @param entity the VisualRepresentation to be searched
     * @return a query to search for the VisualRepresentation
     */
    @Override
    public Query getEntityQuery(VisualRepresentation entity)
    {
        Query q = getEntityManager().createQuery("SELECT e FROM VisualRepresentation e " +
                "where UPPER(e.mediaType) = ?1 and UPPER(e.representation) = ?2 and e.prototype = ?3"
                );
        q.setParameter(1, entity.getMediaType().toUpperCase());
        q.setParameter(2, entity.getRepresentation().toUpperCase());
        q.setParameter(3, entity.isPrototype());
        return q;
    }

    /**
     * Gets all visual representations of a given concept
     * @param concept the concept to be searched
     * @return a list of VisualRepresentation
     */
    @Override
    public List<VisualRepresentation> getEntries(Concept c)
    {
        Query q = getEntityManager().createQuery("SELECT entry FROM " +
                "Concept c, IN(c.VRs) as VR, IN(VR.entries) entry " +
                "where c=?1"
                );
        q.setParameter(1, c);
        return q.getResultList();
    }
}
