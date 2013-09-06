/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao;

import csri.poeticon.praxicon.db.entities.MRGroup;
import java.util.List;

/**
 *
 * @author Erevodifwntas
 */
public interface MRGroupDao extends Dao<Long, MRGroup>{

    List<MRGroup> findAllByName(String name);
}
