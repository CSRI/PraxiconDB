/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao;

import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.LanguageRepresentationGroup;
import java.util.List;

/**
 *
 * @author Erevodifwntas
 */
public interface LanguageRepresentationGroupDao extends Dao<Long, LanguageRepresentationGroup>{

    List<Concept> findConceptsWithLanguageRepresentations(List<String> names, String language);
    List<LanguageRepresentationGroup> findAllByName(String name);
}
