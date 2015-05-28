/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implXML;

import gr.csri.poeticon.praxicon.Constants;
import gr.csri.poeticon.praxicon.db.dao.LanguageRepresentationDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.JpaDao;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation.Language;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation.PartOfSpeech;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation.UseStatus;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 */
public class LanguageRepresentationDaoImplXML extends
        JpaDao<Long, LanguageRepresentation>
        implements LanguageRepresentationDao {

    @Override
    public LanguageRepresentation getLanguageRepresentationsCaseInsensitive(
            Language language, String text, PartOfSpeech pos,
            UseStatus pragmaticStatus) {
        Enumeration en = Constants.globalConcepts.elements();
        while (en.hasMoreElements()) {
            Concept concept = (Concept)en.nextElement();
            for (LanguageRepresentation tmpLanguageRepresentation : concept.
                    getLanguageRepresentations()) {
                if (tmpLanguageRepresentation.getLanguage().name().
                        equals(language) &&
                        tmpLanguageRepresentation.getText().
                        equalsIgnoreCase(text) &&
                        tmpLanguageRepresentation.getPartOfSpeech() == pos) {
                    return tmpLanguageRepresentation;
                }
            }
        }
        return null;
    }

    @Override
    public List<LanguageRepresentation> getLanguageRepresentations(
            String searchString) {
        List<LanguageRepresentation> res = new ArrayList<>();
        Enumeration en = Constants.globalConcepts.elements();
        while (en.hasMoreElements()) {
            Concept concept = (Concept)en.nextElement();
            for (LanguageRepresentation tmpLanguageRepresentation : concept.
                    getLanguageRepresentations()) {
                if (tmpLanguageRepresentation.getText().toUpperCase().
                        contains(searchString.toUpperCase())) {
                    res.add(tmpLanguageRepresentation);
                }
            }
        }
        return res;
    }

    @Override
    public Query getEntityQuery(LanguageRepresentation entity) {
        return null;
    }

    @Override
    public List<LanguageRepresentation> getLanguageRepresentations(Language language,
            String text, PartOfSpeech pos, UseStatus pragmaticStatus) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getAllLanguageRepresentationText() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
