/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon;

import csri.poeticon.praxicon.db.dao.ConceptDao;
import csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import csri.poeticon.praxicon.db.entities.Concept;
import java.util.HashSet;
import java.util.List;
import javax.persistence.Query;
//import csri.poeticon.praxicon.Constants;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Erevodifwntas
 * @author Dimitris Mavroeidis
 */
public class SimpleTest
{
    public static void main(String args[])
    {
        try
        {
            Constants.Constants();
        } //OK
        catch (FileNotFoundException ex)
        {
            System.out.println("File not found");
        }
        catch (IOException ex2)
        {
            System.out.println("Could not read from file");
        }
        ConceptDao cDao = new ConceptDaoImpl();

        String toSearch = "spoon%1:06:00::";
        //String toSearch = "eating_utensil%1:06:00::";

        Query q = cDao.getEntityManager().createQuery("SELECT c FROM Concept c "
                        + "where c.Name = ?1");
        q.setParameter(1, toSearch);
        List<Concept> found = q.getResultList();
        Concept startConcept = found.get(0);
        System.out.println(startConcept + " " + startConcept.getSpecificityLevel());

        HashSet<Concept> sisters = new HashSet<Concept>();

        List<Concept> parents = cDao.getParentsOf(startConcept);
        for (int i=0;i<parents.size();i++)
        {
            sisters.addAll(cDao.getChildrenOf(parents.get(i)));
        }
        sisters.remove(startConcept);

        for (Concept con: sisters)
        {
            System.out.println(con + " " + con.getSpecificityLevel());
        }
    }
}
