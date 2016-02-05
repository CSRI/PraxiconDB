/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.VisualRepresentationDao;
import gr.csri.poeticon.praxicon.db.entities.VisualRepresentation;
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
     * @param visualRepresentation
     * @param entity the VisualRepresentation to be searched
     * @return a query to search for the VisualRepresentation
     */
    @Override
    public Query getEntityQuery(VisualRepresentation visualRepresentation) {
        Query q = getEntityManager().createNamedQuery(
                "SELECT e FROM VisualRepresentation e " +
                "WHERE UPPER(e.MediaType) = :mediaType AND" +
                " UPPER(e.Representation) = :languageRepresentationName ");
        q.setParameter("mediaType", visualRepresentation.getMediaType());
        q.setParameter("languageRepresentationName", visualRepresentation.
                getConcept().getLanguageRepresentationsNames());
        return q;
    }
}
