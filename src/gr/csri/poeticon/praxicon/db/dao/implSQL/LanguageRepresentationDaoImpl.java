/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.LanguageRepresentationDao;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 */
public class LanguageRepresentationDaoImpl extends
        JpaDao<Long, LanguageRepresentation> implements
        LanguageRepresentationDao {

    /**
     * Finds the LanguageRepresentation that has the given language, text and
     * part of speech
     * Overloaded.
     *
     * @param language the language to search
     * @param text     the text to search
     * @param pos      the pos to search
     * @return A LanguageRepresentation (null if not found)
     */
    @Override
    public LanguageRepresentation
            findLanguageRepresentations(
                    String language, String text, String pos) {
                Query query = getEntityManager().createNamedQuery(
                        "findLanguageRepresentationsByTextLanguagePos").
                        setParameter("text", text).
                        setParameter("language", language).
                        setParameter("pos", pos);
                List res = query.getResultList();
                if (res.size() > 0) {
                    return (LanguageRepresentation)res.get(0);
                } else {
                    return null;
                }
            }

            /**
             * Finds the LanguageRepresentation that has the given language, text and
             * part of speech
             * Case insensitive search.
             *
             * @param language the language to search
             * @param text     the text to search
             * @param pos      the pos to search
             * @return A LanguageRepresentation (null if not found)
             */
            @Override
            public LanguageRepresentation
                    findLanguageRepresentationsCaseInsensitive(
                            String language, String text, String pos) {
                        Query query = getEntityManager().createNamedQuery(
                                "findLanguageRepresentationsByTextLanguagePosCase" +
                                "Insensitive").
                                setParameter("text", text).
                                setParameter("language", language).
                                setParameter("pos", pos);
                        List res = query.getResultList();
                        if (res.size() > 0) {
                            return (LanguageRepresentation)res.get(0);
                        } else {
                            return null;
                        }
                    }

                    /**
                     * Finds the LanguageRepresentations that have text field containing the
                     * given string Case insensitive search.
                     * Overloaded.
                     *
                     * @param text the language representation text
                     * @return A list of LanguageRepresentations
                     */
                    @Override
                    public List<LanguageRepresentation> findLanguageRepresentations(
                            String text) {
                        Query query = getEntityManager().createNamedQuery(
                                "findLanguageRepresentationsByText").
                                setParameter("text", text);
                        return query.getResultList();
                    }

                    /**
                     * Creates q query to search for a LanguageRepresentation using text, lang
                     * and pos
                     *
                     * @param lr the LanguageRepresentation
                     * @return a query to search for the LanguageRepresentation
                     */
                    @Override
                    public Query getEntityQuery(
                            LanguageRepresentation languageRepresentation) {

                        Query query = getEntityManager().createNamedQuery(
                                "getLanguageRepresentationEntityQuery").
                                setParameter("text", languageRepresentation.
                                        getText().toUpperCase()).
                                setParameter("language", languageRepresentation.
                                        getLanguage().name().toUpperCase()).
                                setParameter("status", languageRepresentation.
                                        getPartOfSpeech().toString().
                                        toUpperCase()).
                                setParameter("pragmaticStatus",
                                        languageRepresentation.
                                        getPragmaticStatus().toString().
                                        toUpperCase());
                        System.out.println("Language Representation Text: " +
                                languageRepresentation.getText());
                        return query;
                    }

// TODO: Delete the two methods below after I make sure they are not used in other projects.
//            /**
//             * Finds the LanguageRepresentations of a given concept sorted by language
//             *
//             * @param concept the concept
//             * @return A list of LanguageRepresentations
//             */
//            @Override
//            public List<LanguageRepresentation> getLanguageRepresentationsOfConceptSortedByLanguage(
//                    Concept concept) {
//
//                Query q = getEntityManager().createQuery(
//                        "SELECT e FROM Concept c, " +
//                        "IN(c.LanguageRepresentations) as lr, IN(lr.entries) e " +
//                        "where c=?1 order by e.lang"
//                );
//                q.setParameter(1, concept);
//                return q.getResultList();
//            }
//
//            /**
//             * Finds the LanguageRepresentations of a given concept without sorting
//             *
//             * @param c the concept
//             * @return A list of LanguageRepresentations
//             */
//            @Override
//            public List<LanguageRepresentation> getLanguageRepresentationsOfConcept(
//                    Concept concept) {
//                Query q = getEntityManager().createQuery(
//                        "SELECT e FROM Concept c, " +
//                        "IN(c.LanguageRepresentations) as lr, IN(lr.entries) e " +
//                        "where c=?1"
//                );
//                q.setParameter(1, concept);
//                return q.getResultList();
//            }
}
