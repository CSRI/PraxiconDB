/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.LanguageRepresentationDao;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public class LanguageRepresentationDaoImpl extends
        JpaDao<Long, LanguageRepresentation> implements
        LanguageRepresentationDao {

    /**
     * Finds the LanguageRepresentation that has the given language, text and
     * pos
     *
     * @param language the language to search
     * @param text     the text to search
     * @param pos      the pos to search
     * @return A LanguageRepresentation (null if not found)
     */
    @Override
    public LanguageRepresentation
            findLanguageRepresentation(String language, String text, String pos) {
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
     * pos Case insensitive search.
     *
     * @param language the language to search
     * @param text     the text to search
     * @param pos      the pos to search
     * @return A LanguageRepresentation (null if not found)
     */
    @Override
    public LanguageRepresentation
            findByLanguageRepresentation(String language, String text,
                    String pos) {
        Query q = getEntityManager().createQuery(
                "SELECT e FROM LanguageRepresentation e " +
                "where e.text = ?1 and e.lang = ?2 and UPPER(e.POS) = ?3"
        );
        q.setParameter(1, text);
        q.setParameter(2, language.toUpperCase());
        q.setParameter(3, pos.toUpperCase());
        List res = q.getResultList();
        if (res.size() > 0) {
            return (LanguageRepresentation)res.get(0);
        } else {
            return null;
        }
    }

    /**
     * Finds the LanguageRepresentations that have text field containing the
     * given string Case insensitive search.
     *
     * @param searchString the string to search
     * @return A list of LanguageRepresentations
     */
    @Override
    public List<LanguageRepresentation> find(String searchString) {
        Query q = getEntityManager().createQuery(
                "SELECT e FROM LanguageRepresentation e " +
                "where UPPER(e.text) like ?1"
        );
        q.setParameter(1, "%" + searchString.toUpperCase() + "%");
        return q.getResultList();
    }

    /**
     * Creates q query to search for a LanguageRepresentation using text, lang
     * and pos
     *
     * @param entity the LanguageRepresentation
     * @return a query to search for the LanguageRepresentation
     */
    @Override
    public Query getEntityQuery(LanguageRepresentation entity) {
        Query q = getEntityManager().createQuery(
                "SELECT e FROM LanguageRepresentation e " +
                "where UPPER(e.text) = ?1 and UPPER(e.lang) = ?2 " +
                "and UPPER(e.POS) = ?3"
        );
        q.setParameter(1, entity.getText().toUpperCase());
        q.setParameter(2, entity.getLanguage().name().toUpperCase());
        q.setParameter(3, entity.getPartOfSpeech().toString());

        return q;
    }

    /**
     * Finds the LanguageRepresentations of a given concept sorted by language
     *
     * @param c the concept
     * @return A list of LanguageRepresentations
     */
    @Override
    public List<LanguageRepresentation> getEntriesSorted(Concept c) {
        Query q = getEntityManager().createQuery(
                "SELECT e FROM Concept c, " +
                "IN(c.LanguageRepresentations) as lr, IN(lr.entries) e " +
                "where c=?1 order by e.lang"
        );
        q.setParameter(1, c);
        return q.getResultList();
    }

    /**
     * Finds the LanguageRepresentations of a given concept without sorting
     *
     * @param c the concept
     * @return A list of LanguageRepresentations
     */
    @Override
    public List<LanguageRepresentation> getEntries(Concept c) {
        Query q = getEntityManager().createQuery(
                "SELECT e FROM Concept c, " +
                "IN(c.LanguageRepresentations) as lr, IN(lr.entries) e " +
                "where c=?1"
        );
        q.setParameter(1, c);
        return q.getResultList();
    }
}
