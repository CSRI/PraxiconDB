/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.entities.comparators;

import csri.poeticon.praxicon.db.entities.LanguageRepresentation;
import java.util.Comparator;

/**
 *
 * @author Erevodifwntas
 */
public class LexicalEntryComparator implements Comparator<LanguageRepresentation>
{

    /**
     * Compares languages of two language representations
     * @param a the first language representation
     * @param b the second language representation
     * @return integer result of compare
     */
    @Override
    public int compare(LanguageRepresentation a, LanguageRepresentation b)
    {
        return a.getLanguage().name().compareToIgnoreCase(b.getLanguage().name());
    }
}
