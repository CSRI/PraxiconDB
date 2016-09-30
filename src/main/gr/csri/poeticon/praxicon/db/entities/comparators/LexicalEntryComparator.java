/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities.comparators;

import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation;
import java.util.Comparator;

/**
 *
 * @author Erevodifwntas
 */
public class LexicalEntryComparator implements
        Comparator<LanguageRepresentation> {

    /**
     * Compares languages of two Language representations
     *
     * @param a the first Language representation
     * @param b the second Language representation
     *
     * @return integer result of compare
     */
    @Override
    public int compare(LanguageRepresentation a, LanguageRepresentation b) {
        return a.getLanguage().name().
                compareToIgnoreCase(b.getLanguage().name());
    }
}
