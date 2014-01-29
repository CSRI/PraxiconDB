/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao;

import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.LanguageRepresentation;
import java.util.List;

/**
 *
 * @author Erevodifwntas
 */
public interface LanguageRepresentationDao extends Dao<Long, LanguageRepresentation>
{
    List<LanguageRepresentation> find(String searchString);
    LanguageRepresentation findByLanguageRepresentation(String language, String text, String pos);
    LanguageRepresentation findLanguageRepresentation(String language, String text, String pos);
    List<LanguageRepresentation> getEntriesSorted(Concept c);
    List<LanguageRepresentation> getEntries(Concept c);
}
