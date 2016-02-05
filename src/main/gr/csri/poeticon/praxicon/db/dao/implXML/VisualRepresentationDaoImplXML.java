/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implXML;

import gr.csri.poeticon.praxicon.db.dao.VisualRepresentationDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.JpaDao;
import gr.csri.poeticon.praxicon.db.entities.VisualRepresentation;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 */
public class VisualRepresentationDaoImplXML
        extends JpaDao<Long, VisualRepresentation>
        implements VisualRepresentationDao {

    @Override
    public Query getEntityQuery(VisualRepresentation entity) {
        return null;
    }
}
