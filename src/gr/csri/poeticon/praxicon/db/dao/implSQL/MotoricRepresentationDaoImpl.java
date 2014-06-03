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
 * @author Erevodifwntas
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

// TODO: Delete the two methods below after I make sure they are not used in other projects.
//    /**
//     * Gets the Motoric Representations of a given concept
//     *
//     * @param c the concept
//     * @return a list of MotoricRepresentations
//     */
//    @Override
//    public List<MotoricRepresentation> getEntries(Concept c) {
//        Query q = getEntityManager().createQuery(
//                "SELECT entry FROM " +
//                "Concept c, IN(c.MotoricRepresentations) as MR, " +
//                "IN(MR.entries) entry " +
//                "where c=?1"
//        );
//        q.setParameter(1, c);
//        return q.getResultList();
//    }
}
