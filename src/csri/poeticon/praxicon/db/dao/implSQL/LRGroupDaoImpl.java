/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao.implSQL;

import csri.poeticon.praxicon.db.dao.LRGroupDao;
import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.LRGroup;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Erevodifwntas
 */
public class LRGroupDaoImpl extends JpaDao<Long, LRGroup> implements LRGroupDao{

    /**
     * Creates q query to search for a LRGroup using name
     * @param entity the LRGroup to be searched
     * @return a query to search for the LRGroup
     */
    @Override
    public Query getEntityQuery(LRGroup entity)
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
     * Finds the concepts that have a list of strings as LRs for a specific language
     * @param names the list of texts to search
     * @param language the language to search
     * @return A list of concepts
     */
    @Override
    public List<Concept> findConceptsWithLRs(List<String> names, String language)
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
            LRGroup lr = (LRGroup) tmp.get(i);
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
     * has the same name as the given LR Group, returning the updated LR GRoup
     * @param newLR LR Group to use for the search and the update
     * @return The updated LR Group
     */
    public LRGroup updatedConcept(LRGroup newLR)
    {
        Query q = getEntityManager().createQuery("SELECT e FROM LanguageRepresentation e " +
                "where UPPER(e.name) = ?1"
                );
        q.setParameter(1, newLR.getName().toUpperCase());
        List tmp = q.getResultList();

        LRGroup oldLR= null;
        if (tmp.size() == 0)
        {
            return newLR;
        }
        else
        {
            oldLR = (LRGroup)tmp.get(0);
        }

        for (int i = 0; i <newLR.getEntries().size(); i++)
        {
            if (!oldLR.getEntries().contains(newLR.getEntries().get(i)))
            {
                oldLR.getEntries().add(newLR.getEntries().get(i));
            }
        }

        return oldLR;
    }

    /**
     * Finds all th LR Groups with a given name
     * @param name the name to search
     * @return A List of LR Groups
     */
    @Override
    public List<LRGroup> findAllByName(String name) {
        List<LRGroup> res;
        Query q = getEntityManager().createQuery("SELECT lrg FROM LRGroup lrg "
                + "where lrg.name like ?1");
        q.setParameter(1, "%" + name + "%");
        res = q.getResultList();

        return res;
    }
}
