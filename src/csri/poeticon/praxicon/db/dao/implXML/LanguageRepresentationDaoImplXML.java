/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao.implXML;

import csri.poeticon.praxicon.db.dao.implSQL.JpaDao;
import csri.poeticon.praxicon.Constants;
import csri.poeticon.praxicon.db.entities.comparators.LexicalEntryComparator;
import csri.poeticon.praxicon.db.dao.LanguageRepresentationDao;
import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.LanguageRepresentation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public class LanguageRepresentationDaoImplXML extends JpaDao<Long, LanguageRepresentation> implements LanguageRepresentationDao{

    @Override
    public LanguageRepresentation findLanguageRepresentation(String language, String text, String pos)
    {

        Enumeration en = Constants.globalConcepts.elements();
        while(en.hasMoreElements())
        {
            Concept con = (Concept)en.nextElement();
            for (int i = 0; i < con.getLanguageRepresentations().size(); i++)
            {
                for (int j = 0; j < con.getLanguageRepresentations().get(i).getEntries().size(); j++)
                {
                    LanguageRepresentation entry = con.getLanguageRepresentations().get(i).getEntries().get(j);
                    if (entry.getLanguage().name().equalsIgnoreCase(language) &&
                            entry.getText().equalsIgnoreCase(text) &&
                            entry.getPartOfSpeech() == LanguageRepresentation.PartOfSpeech.valueOf(pos))
                    {
                        return entry;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<LanguageRepresentation> find(String searchString)
    {
        List<LanguageRepresentation> res = new ArrayList<LanguageRepresentation>();
        Enumeration en = Constants.globalConcepts.elements();
        while(en.hasMoreElements())
        {
            Concept con = (Concept)en.nextElement();
            for (int i = 0; i < con.getLanguageRepresentations().size(); i++)
            {
                for (int j = 0; j < con.getLanguageRepresentations().get(i).getEntries().size(); j++)
                {
                    LanguageRepresentation entry = con.getLanguageRepresentations().get(i).getEntries().get(j);
                    if (entry.getText().toUpperCase().indexOf(searchString.toUpperCase()) >= 0)
                    {
                        res.add(entry);
                    }
                }
            }
        }
        return res;
    }

    @Override
    public List<LanguageRepresentation> findExact(String searchString)
    {
        List<LanguageRepresentation> res = new ArrayList<LanguageRepresentation>();
        Enumeration en = Constants.globalConcepts.elements();
        while(en.hasMoreElements())
        {
            Concept con = (Concept)en.nextElement();
            for (int i = 0; i < con.getLanguageRepresentations().size(); i++)
            {
                for (int j = 0; j < con.getLanguageRepresentations().get(i).getEntries().size(); j++)
                {
                    LanguageRepresentation entry = con.getLanguageRepresentations().get(i).getEntries().get(j);
                    if (entry.getText().equalsIgnoreCase(searchString))
                    {
                        res.add(entry);
                    }
                }
            }
        }
        return res;
    }
    
    @Override
    public Query getEntityQuery(LanguageRepresentation entity)
    {   
        return null;
    }

    @Override
    public List<LanguageRepresentation> getEntriesSorted(Concept c) {
        List<LanguageRepresentation> res = new ArrayList<LanguageRepresentation>();
        for (int i = 0; i < c.getLanguageRepresentations().size(); i++)
        {
            for (int j = 0; j < c.getLanguageRepresentations().get(i).getEntries().size(); j++)
            {
                res.add(c.getLanguageRepresentations().get(i).getEntries().get(j));
            }
        }

        Comparator<LanguageRepresentation> leCom = new LexicalEntryComparator();
        Collections.sort(res, leCom);

        return res;
    }

    @Override
    public List<LanguageRepresentation> getEntries(Concept c) {
        List<LanguageRepresentation> res = new ArrayList<LanguageRepresentation>();
        for (int i = 0; i < c.getLanguageRepresentations().size(); i++)
        {
            for (int j = 0; j < c.getLanguageRepresentations().get(i).getEntries().size(); j++)
            {
                res.add(c.getLanguageRepresentations().get(i).getEntries().get(j));
            }
        }

        return res;
    }

    @Override
    public LanguageRepresentation findByLanguageRepresentation(String language, String text, String pos) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
