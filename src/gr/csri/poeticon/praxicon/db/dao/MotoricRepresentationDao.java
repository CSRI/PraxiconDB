/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao;

import gr.csri.poeticon.praxicon.db.entities.MotoricRepresentation;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public interface MotoricRepresentationDao extends
        Dao<Long, MotoricRepresentation> {

    public Query getEntityQuery(MotoricRepresentation mr);

// TODO: Delete the two methods below after I make sure they are not used in other projects.    
    //public List<MotoricRepresentation> getEntries(Concept c);
}
