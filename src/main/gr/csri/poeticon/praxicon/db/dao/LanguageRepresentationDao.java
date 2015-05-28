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
import java.util.List;

/**
 *
 * @author dmavroeidis
 */
public interface LanguageRepresentationDao extends
        Dao<Long, LanguageRepresentation> {

    List<LanguageRepresentation> getLanguageRepresentations(
            Language language, String text, PartOfSpeech pos,
            UseStatus useStatus, Productivity productivity, String negation,
            Operator operator);

    List<LanguageRepresentation> getLanguageRepresentations(String text);

    List<LanguageRepresentation> getLanguageRepresentations(
            Language language, String text, PartOfSpeech pos,
            UseStatus pragmaticStatus);

    LanguageRepresentation getLanguageRepresentationsCaseInsensitive(
            Language language, String text, PartOfSpeech pos,
            UseStatus pragmaticStatus);

    List<String> getAllLanguageRepresentationText();

}
