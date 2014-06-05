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
    public LanguageRepresentation findLanguageRepresentationsCaseInsensitive(
            String language,
            String text, String pos) {

        Enumeration en = Constants.globalConcepts.elements();
        while (en.hasMoreElements()) {
            Concept con = (Concept)en.nextElement();
            for (LanguageRepresentation entry : con.getLanguageRepresentations()) {
                if (entry.getLanguage().name().equalsIgnoreCase(language) &&
                        entry.getText().equalsIgnoreCase(text) &&
                        entry.getPartOfSpeech() ==
                        LanguageRepresentation.part_of_speech.valueOf(pos)) {
                    return entry;
                }
            }
        }
        return null;
    }

    @Override
    public List<LanguageRepresentation> findLanguageRepresentations(
            String searchString) {
        List<LanguageRepresentation> res = new ArrayList<>();
        Enumeration en = Constants.globalConcepts.elements();
        while (en.hasMoreElements()) {
            Concept con = (Concept)en.nextElement();
            for (LanguageRepresentation entry : con.getLanguageRepresentations()) {
                if (entry.getText().toUpperCase().contains(searchString.
                        toUpperCase())) {
                    res.add(entry);
                }
            }
        }
        return res;
    }

    @Override
    public Query getEntityQuery(LanguageRepresentation entity) {
        return null;
    }

// TODO: Delete the two methods below after I make sure they are not used 
//       in other projects.
//    @Override
//    public List<LanguageRepresentation> getEntriesSorted(Concept c) {
//        List<LanguageRepresentation> res = new ArrayList<>();
//        for (LanguageRepresentation lr : c.getLanguageRepresentations()) {
//            res.add(lr);
//        }
//        Comparator<LanguageRepresentation> leCom = new LexicalEntryComparator();
//        Collections.sort(res, leCom);
//        return res;
//    }
//
//    @Override
//    public List<LanguageRepresentation> getEntries(Concept c) {
//        List<LanguageRepresentation> res = new ArrayList<>();
//        for (LanguageRepresentation lr : c.getLanguageRepresentations()) {
//            res.add(lr);
//        }
//        return res;
//    }
    @Override
    public LanguageRepresentation findLanguageRepresentations(String language,
            String text, String pos) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
