/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao;

import csri.poeticon.praxicon.db.entities.SimplifiedRelationName;


/**
 *
 * @author Erevodifwntas
 */
public interface SimplifiedRelationNameDao extends Dao<Long, SimplifiedRelationName>{
    String getSimplifiedName(String complexName);
}
