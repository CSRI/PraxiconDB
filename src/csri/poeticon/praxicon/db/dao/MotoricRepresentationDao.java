/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao;

import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.MotoricRepresentation;
import java.util.List;

/**
 *
 * @author Erevodifwntas
 */
public interface MotoricRepresentationDao extends Dao<Long, MotoricRepresentation>{

    public List<MotoricRepresentation> getEntries(Concept c);
}
