/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon;

import gr.csri.poeticon.praxicon.db.dao.ConceptDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import java.util.HashSet;
import java.util.List;

//import csri.poeticon.praxicon.Constants;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Dimitris Mavroeidis
 *
 */
public class SimpleTest {

    public static void main(String args[]) {
//        try
//        {
//            Constants.Constants();
//        } //OK
//        catch (FileNotFoundException ex)
//        {
//            System.out.println("File not found");
//        }
//        catch (IOException ex2)
//        {
//            System.out.println("Could not read from file");
//        }
        ConceptDao cDao = new ConceptDaoImpl();

        String toSearch = "concept1";
        //String toSearch = "spoon%1:06:00::";
        //String toSearch = "eating_utensil%1:06:00::";

//        Query q = cDao.getEntityManager().createQuery("SELECT c FROM Concept c "
//                        + "where c.Name = ?1");
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Concepts");
//        EntityManager em = emf.createEntityManager();
        List<Concept> concepts = cDao.findAll();
        //List<Concept> concepts = query.getResultList();
        //query.setParameter(1, toSearch);
        //List<Concept> found = query.getResultList();
        //Concept startConcept = concepts.get(0);
        //System.out.println(concepts + " " + concepts.getSpecificityLevel());
        for (Concept item : concepts) {
            System.out.println(item.getName());
        }

        HashSet<Concept> sisters = new HashSet<>();

        List<Concept> parents = cDao.getParentsOf(concepts.get(0));
        for (Concept parent : parents) {
            sisters.addAll(cDao.getChildrenOf(parent));
        }
        sisters.remove(concepts.get(0));

        for (Concept con : sisters) {
            System.out.println(con + " " + con.getSpecificityLevel());
        }
    }
}
