/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao.implSQL;

import csri.poeticon.praxicon.db.dao.LanguageRepresentationGroupDao;
import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.LanguageRepresentationGroup;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public class LanguageRepresentationGroupDaoImpl extends JpaDao<Long, LanguageRepresentationGroup> implements LanguageRepresentationGroupDao{

    /**
     * Creates q query to search for a LanguageRepresentationGroup using name
     * @param entity the LanguageRepresentationGroup to be searched
     * @return a query to search for the LanguageRepresentationGroup
     */
    @Override
    public Query getEntityQuery(LanguageRepresentationGroup entity)
    {
        if (entity.getName().equalsIgnoreCase("null"))
        {
            return null;
        }
        Query q = getEntityManager().createQuery("SELECT e FROM LanguageRepresentation e " +
               "where UPPER(e.name) = ?1"
                );
        q.setParameter(1, entity.getName().toUpperCase());
        return q;
    }

    /**
     * Finds the concepts that have a list of strings as LanguageRepresentations for a specific language
     * @param names the list of texts to search
     * @param language the language to search
     * @return A list of concepts
     */
    @Override
    public List<Concept> findConceptsWithLanguageRepresentations(List<String> names, String language)
    {
        List<Concept> res = new ArrayList<Concept>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT e FROM LanguageRepresentation e, IN(e.entries) as entries " +
                "where entries.lang = ?1"
                );
        for (int i = 0; i < names.size(); i++)
        {
            sb.append(" and entries.text = ?"+(i+2));
        }
        Query q = getEntityManager().createQuery(sb.toString());
        q.setParameter(1, language);
        for (int i = 0; i < names.size(); i++)
        {
            q.setParameter(i+2, names.get(i));
        }

        List tmp = q.getResultList();
        for (int i = 0; i < tmp.size(); i++)
        {
            LanguageRepresentationGroup lr = (LanguageRepresentationGroup) tmp.get(i);
            List<Concept> tmpCon = lr.getConcepts();
            for (int j = 0; j < tmpCon.size(); j++)
            {
                if (!res.contains(tmpCon.get(j)))
                {
                    res.add(tmpCon.get(j));
                }
            }
        }
        return res;
    }

    /**
     * Updates the language representations of the Lr Group of the database that
     * has the same name as the given LanguageRepresentation Group, returning the updated LanguageRepresentation GRoup
     * @param newLanguageRepresentation LanguageRepresentation Group to use for the search and the update
     * @return The updated LanguageRepresentation Group
     */
    public LanguageRepresentationGroup updatedConcept(LanguageRepresentationGroup newLanguageRepresentation)
    {
        Query q = getEntityManager().createQuery("SELECT e FROM LanguageRepresentation e " +
                "where UPPER(e.name) = ?1"
                );
        q.setParameter(1, newLanguageRepresentation.getName().toUpperCase());
        List tmp = q.getResultList();

        LanguageRepresentationGroup oldLanguageRepresentation= null;
        if (tmp.size() == 0)
        {
            return newLanguageRepresentation;
        }
        else
        {
            oldLanguageRepresentation = (LanguageRepresentationGroup)tmp.get(0);
        }

        for (int i = 0; i <newLanguageRepresentation.getEntries().size(); i++)
        {
            if (!oldLanguageRepresentation.getEntries().contains(newLanguageRepresentation.getEntries().get(i)))
            {
                oldLanguageRepresentation.getEntries().add(newLanguageRepresentation.getEntries().get(i));
            }
        }

        return oldLanguageRepresentation;
    }

    /**
     * Finds all th LanguageRepresentation Groups with a given name
     * @param name the name to search
     * @return A List of LanguageRepresentation Groups
     */
    @Override
    public List<LanguageRepresentationGroup> findAllByName(String name) {
        List<LanguageRepresentationGroup> res;
        Query q = getEntityManager().createQuery("SELECT lrg FROM LanguageRepresentationGroup lrg "
                + "where lrg.name like ?1");
        q.setParameter(1, "%" + name + "%");
        res = q.getResultList();

        return res;
    }
}
