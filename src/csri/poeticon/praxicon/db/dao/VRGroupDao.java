/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao;

import csri.poeticon.praxicon.db.entities.VRGroup;
import java.util.List;


/**
 *
 * @author Erevodifwntas
 */
public interface VRGroupDao extends Dao<Long, VRGroup>{
    List<VRGroup> findAllByName(String name);
}
