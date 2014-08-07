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

/**
 *
 * @author dmavroeidis
 *
 */
public class SimpleTest {

    public static void main(String args[]) {

        ConceptDao cDao = new ConceptDaoImpl();

        //String toSearch = "String";
        //String toSearch = "spoon%1:06:00::";
        //String toSearch = "eating_utensil%1:06:00::";

//        Query q = cDao.getEntityManager().createQuery("SELECT c FROM Concept c "
//                        + "where c.Name = ?1");
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Concepts");
//        EntityManager em = emf.createEntityManager();
        //List<Concept> concepts = cDao.findConceptsByLanguageRepresentationExact("spoon");
        List<Concept> concepts_all = cDao.findAllConcepts();
        //Concept concept_by_name = cDao.findConceptByNameExact("spoon");
        //List<Concept> concepts = query.getResultList();
        //query.setParameter(1, toSearch);
        //List<Concept> found = query.getResultList();
        //Concept startConcept = concepts.get(0);
        //System.out.println(concepts + " " + concepts.getSpecificityLevel());
//        for (Concept item : concepts) {
//            System.out.println(item.getName());
//        }
        for (Concept item : concepts_all) {
            System.out.println(item.getName());
        }

        HashSet<Concept> sisters = new HashSet<>();
//
        List<Concept> parents = cDao.getParentsOfConcept(concepts_all.get(0));
        for (Concept parent : parents) {
            sisters.addAll(cDao.getChildrenOfConcept(parent));
        }
        //sisters.remove(concepts_all.get(0));
//
        for (Concept con : sisters) {
            System.out.println(con + " " + con.getSpecificityLevel());
        }
    }
}
