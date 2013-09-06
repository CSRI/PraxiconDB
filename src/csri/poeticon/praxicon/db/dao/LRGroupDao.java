/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao;

import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.LRGroup;
import java.util.List;

/**
 *
 * @author Erevodifwntas
 */
public interface LRGroupDao extends Dao<Long, LRGroup>{

    List<Concept> findConceptsWithLRs(List<String> names, String language);
    List<LRGroup> findAllByName(String name);
}
