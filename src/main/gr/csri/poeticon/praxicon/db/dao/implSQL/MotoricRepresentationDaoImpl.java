/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.MotoricRepresentationDao;
import gr.csri.poeticon.praxicon.db.entities.MotoricRepresentation;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 */
public class MotoricRepresentationDaoImpl
        extends JpaDao<Long, MotoricRepresentation>
        implements MotoricRepresentationDao {

    /**
     * Creates q query to search for a MotoricRepresentation using
     * representation
     *
     * @param entity the MotoricRepresentation to be searched
     * @return a query to search for the MotoricRepresentation
     */
    @Override
    public Query getEntityQuery(MotoricRepresentation motoricRepresentation) {
        Query query = getEntityManager().createNamedQuery(
                "getLanguageRepresentationEntityQuery").setParameter("comment",
                        motoricRepresentation.getComment().toUpperCase());
        return query;
    }
}
