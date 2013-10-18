/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao;

import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.VisualRepresentation;
import java.util.List;

/**
 *
 * @author Erevodifwntas
 */
public interface VisualRepresentationDao extends Dao<Long, VisualRepresentation>
{
    List<VisualRepresentation> getEntries(Concept c);
}
