/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.VisualRepresentationDao;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.VisualRepresentation;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 * @author dmavroeidis
 */
public class VisualRepresentationDaoImpl extends
        JpaDao<Long, VisualRepresentation> implements VisualRepresentationDao {

    /**
     * Creates q query to search for a given VisualRepresentation using media
     * type, representation ant prototype
     *
     * @param entity the VisualRepresentation to be searched
     * @return a query to search for the VisualRepresentation
     */
    @Override
    public Query getEntityQuery(VisualRepresentation visualRepresentation) {
        Query q = getEntityManager().createQuery(
                "SELECT e FROM VisualRepresentation e " +
                 "WHERE UPPER(e.MediaType) = ?1 AND" +
                 " UPPER(e.Representation) = ?2 AND e.Prototype = ?3"
        );
        q.setParameter(1, visualRepresentation.getMediaType());
        q.setParameter(2, visualRepresentation.getRepresentation().toUpperCase());
        //q.setParameter(3, entity.isPrototype());
        return q;
    }

    /**
     * Gets all visual representations of a given concept
     *
     * @param concept the concept to be searched
     * @return a list of VisualRepresentation
     */
    @Override
    public List<VisualRepresentation> getEntries(Concept concept) {
        Query q = getEntityManager().createQuery(
                "SELECT Entry FROM Concept c, " +
                 "IN(c.VisualRepresentations) AS VR, " +
                 "IN(VR.Entries) entry " +
                 "where c=?1"
        );
        q.setParameter(1, concept);
        return q.getResultList();
    }
}
