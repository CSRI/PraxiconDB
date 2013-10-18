/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao;

import csri.poeticon.praxicon.db.entities.MotoricRepresentationGroup;
import java.util.List;

/**
 *
 * @author Erevodifwntas
 */
public interface MotoricRepresentationGroupDao extends Dao<Long, MotoricRepresentationGroup>
{
    List<MotoricRepresentationGroup> findAllByName(String name);
}
