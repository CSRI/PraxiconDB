/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.LanguageRepresentationDao;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation.Language;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation.Operator;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation.PartOfSpeech;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation.Productivity;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation.UseStatus;
import java.util.ArrayList;
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
     * Finds all the Language Representations.
     *
     * @return a list of all Language Representations in the database
     */
    @Override
    public List<LanguageRepresentation> getAllLanguageRepresentations() {
        Query query = getEntityManager().createNamedQuery(
                "findAllLanguageRepresentations");
        List<LanguageRepresentation> retrievedLanguageRepresentationsList =
                new ArrayList<>(query.getResultList());
        return retrievedLanguageRepresentationsList;
    }

    /**
     * Finds the LanguageRepresentation that has the given Language, Text, Part
     * of Speech, Use Status, Productivity, Negation and Operator.
     * Overloaded.
     *
     * @param language     the Language to search
     * @param text         the text to search
     * @param pos          the part of speech to search
     * @param useStatus    the use status to search
     * @param productivity the productivity to search
     * @param negation     the negation to search
     * @param operator     the operator to search
     *
     * @return A LanguageRepresentation (null if not found)
     */
    @Override
    public LanguageRepresentation getSingleLanguageRepresentation(
            Language language, String text, PartOfSpeech pos,
            UseStatus useStatus, Productivity productivity, String negation,
            Operator operator) {
        Query query = getEntityManager().createNamedQuery(
                "findLanguageRepresentation").
                setParameter("text", text).
                setParameter("language", language).
                setParameter("pos", pos).
                setParameter("useStatus", useStatus).
                setParameter("productivity", productivity).
                setParameter("negation", negation).
                setParameter("operator", operator);
        List<LanguageRepresentation> retrievedLanguageRepresentationList =
                new ArrayList<>(query.getResultList());
        if (retrievedLanguageRepresentationList.isEmpty()) {
            return null;
        }
        return retrievedLanguageRepresentationList.iterator().next();
    }

    /**
     * Finds the LanguageRepresentation that has the given Language, Text and
     * Part of Speech.
     * Overloaded.
     *
     * @param language  the Language to search
     * @param text      the text to search
     * @param pos       the part of speech to search
     * @param useStatus the use status to search
     *
     * @return A LanguageRepresentation (null if not found)
     */
    @Override
    public List<LanguageRepresentation> getLanguageRepresentations(
            Language language, String text, PartOfSpeech pos,
            UseStatus useStatus) {
        Query query = getEntityManager().createNamedQuery(
                "findLanguageRepresentationsByTextLanguagePosPStatus").
                setParameter("text", text).
                setParameter("language", language).
                setParameter("pos", pos).
                setParameter("useStatus", useStatus);
        List<LanguageRepresentation> retrievedLanguageRepresentationList =
                new ArrayList<>(query.getResultList());
        if (retrievedLanguageRepresentationList.size() > 0) {
            return retrievedLanguageRepresentationList;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Finds the LanguageRepresentations that have text field containing the
     * given string Case insensitive search.
     * Overloaded.
     *
     * @param text the Language representation text
     *
     * @return A list of LanguageRepresentations
     */
    @Override
    public List<LanguageRepresentation> getLanguageRepresentations(
            String text) {
        Query query = getEntityManager().createNamedQuery(
                "findLanguageRepresentationsByText").
                setParameter("text", text);
        return new ArrayList<>(query.getResultList());
    }

    /**
     * Finds the LanguageRepresentation that has the given Language, text and
     * part of speech
     * Case insensitive search.
     *
     * @param language  the Language to search
     * @param text      the text to search
     * @param pos       the pos to search
     * @param useStatus
     *
     * @return A LanguageRepresentation (null if not found)
     */
    @Override
    public LanguageRepresentation getLanguageRepresentationsCaseInsensitive(
            Language language, String text, PartOfSpeech pos,
            UseStatus useStatus) {
        Query query = getEntityManager().createNamedQuery(
                "findLanguageRepresentationsBy" +
                "TextLanguagePosPStatusCaseInsensitive").
                setParameter("text", text).
                setParameter("language", language).
                setParameter("pos", pos).
                setParameter("useStatus", useStatus);
        List<LanguageRepresentation> retrievedLanguageRepresentationList =
                new ArrayList<>(query.getResultList());
        if (retrievedLanguageRepresentationList.size() > 0) {
            return retrievedLanguageRepresentationList.iterator().next();
        } else {
            return new LanguageRepresentation();
        }
    }

    /**
     * Gets all LanguageRepresentation Texts.
     * Overloaded.
     *
     * @return A list of LanguageRepresentation Texts
     */
    @Override
    public List<String> getAllLanguageRepresentationText() {
        Query query = getEntityManager().createNamedQuery(
                "getAllLanguageRepresentationTextByText");
        return new ArrayList<>(query.getResultList());
    }

    /**
     * Creates q query to search for a LanguageRepresentation using text, lang
     * and pos
     *
     * @param languageRepresentation The language representation
     *
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
                setParameter("pos", languageRepresentation.
                        getPartOfSpeech().toString().toUpperCase()).
                setParameter("useStatus",
                        languageRepresentation.getUseStatus().toString().
                        toUpperCase()).
                setParameter("productivity", languageRepresentation.
                        getProductivity().toString().toUpperCase()).
                setParameter("negation", languageRepresentation.getNegation().
                        toString().toUpperCase()).
                setParameter("operator", languageRepresentation.getOperator().
                        toString().toUpperCase());
        return query;
    }
}
