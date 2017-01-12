/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao;

import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation.Language;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation.Operator;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation.PartOfSpeech;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation.Productivity;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation.UseStatus;
import java.util.Set;

/**
 *
 * @author dmavroeidis
 */
public interface LanguageRepresentationDao extends
        Dao<Long, LanguageRepresentation> {

    LanguageRepresentation getSingleLanguageRepresentation(
            Language language, String text, PartOfSpeech pos,
            UseStatus useStatus, Productivity productivity, String negation,
            Operator operator);

    Set<LanguageRepresentation> getLanguageRepresentations(String text);

    Set<LanguageRepresentation> getLanguageRepresentations(
            Language language, String text, PartOfSpeech pos,
            UseStatus useStatus);

    LanguageRepresentation getLanguageRepresentationsCaseInsensitive(
            Language language, String text, PartOfSpeech pos,
            UseStatus useStatus);

    Set<String> getAllLanguageRepresentationText();

}
