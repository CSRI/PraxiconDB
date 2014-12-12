/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao;

import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation.Language;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation.PartOfSpeech;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation.UseStatus;
import java.util.List;

/**
 *
 * @author dmavroeidis
 */
public interface LanguageRepresentationDao extends
        Dao<Long, LanguageRepresentation> {

    List<LanguageRepresentation> findLanguageRepresentations(String text);

    LanguageRepresentation findLanguageRepresentations(
            Language language, String text, PartOfSpeech pos,
            UseStatus pragmaticStatus);

    LanguageRepresentation findLanguageRepresentationsCaseInsensitive(
            Language language, String text, PartOfSpeech pos,
            UseStatus pragmaticStatus);
    
    List<String> getAllLanguageRepresentationText();

// TODO: Delete the two methods below after I make sure they are not used 
//       in other projects.
//    List<LanguageRepresentation> getLanguageRepresentationsOfConceptSortedByLanguage(
//            Concept concept);
//
//    List<LanguageRepresentation> getLanguageRepresentationsOfConcept(
//            Concept concept);
}
