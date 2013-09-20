/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao;

import csri.poeticon.praxicon.db.entities.VisualRepresentationGroup;
import java.util.List;


/**
 *
 * @author Erevodifwntas
 */
public interface VisualRepresentationGroupDao extends Dao<Long, VisualRepresentationGroup>{
    List<VisualRepresentationGroup> findAllByName(String name);
}
