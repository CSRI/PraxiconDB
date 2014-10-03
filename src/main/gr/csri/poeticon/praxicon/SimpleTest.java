/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon;

import gr.csri.poeticon.praxicon.db.dao.ConceptDao;
import gr.csri.poeticon.praxicon.db.dao.RelationArgumentDao;
import gr.csri.poeticon.praxicon.db.dao.RelationDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationArgumentDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationDaoImpl;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
import java.util.List;

/**
 *
 * @author dmavroeidis
 *
 */
public class SimpleTest {

    public static void main(String args[]) {

        // Start by checking most of the methods.
        // Concepts:
        ConceptDao cDao = new ConceptDaoImpl();
        RelationDao rDao = new RelationDaoImpl();

        // Get the number of all concepts
        List<Concept> concepts = cDao.findAllConcepts();
        System.out.println("\n\nNumber of all concepts: " + concepts.size());

        // Get the language representations of all concepts having "spoon" as 
        // language representation.
        String toSearch = "spoon";
        List<Concept> concepts_spoon = cDao.
                findConceptsByLanguageRepresentationExact(toSearch);
        System.out.println("\n\nLanguage Representations of spoon: ");
        System.out.println("---------------------------------");
        for (Concept concept : concepts_spoon) {
            System.out.print(concept.getLanguageRepresentationName());
            System.out.print(" - ");
            System.out.println(concept.getName());
        }

        // Check whether concepts "shape" and "round_shape" are related.
        System.out.println("\n\nCheck whether two concepts are related: ");
        System.out.println("--------------------------------------- ");
        Concept conceptRoundShape = cDao.findConceptByNameExact(
                "round_shape%1:25:00::");
        Concept conceptShape = cDao.findConceptByNameExact("shape%1:03:00::");
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        RelationArgument relationArgumentConceptShape = raDao.
                getRelationArgumentByConcept(conceptShape);
        RelationArgument relationArgumentConceptRoundShape = raDao.
                getRelationArgumentByConcept(conceptRoundShape);
        boolean areRelated;
        areRelated = rDao.areRelated(relationArgumentConceptShape,
                relationArgumentConceptRoundShape);
        System.out.print("shape and round_shape are ");
        System.out.println((areRelated) ? "related" : "not related");

        // Get all relations of a Concept
        System.out.println("\n\nAll relations of concept shape: ");
        System.out.println("------------------------------- ");
        List<Relation> allRelationsOfConceptShape = rDao.
                getAllRelationsOfConcept(conceptShape);
        for (Relation relation : allRelationsOfConceptShape) {
            System.out.println(relation);
        }

        // Get children concepts of the first concept in the list of concepts
        // that have language representation spoon.
        System.out.println("\n\nChildren of the first occurence of a concept" +
                " having language representation spoon: ");
        System.out.println("------------------------------------------------" +
                "---------------------------------- ");
        List<Concept> children_of_spoon = cDao.getChildrenOfConcept(
                concepts_spoon.get(0));
        //System.out.println(concepts_spoon.get(0));
        for (Concept concept : children_of_spoon) {

            System.out.println(concept.getName());
        }
//System.out.println(concepts + " " + concepts.getSpecificityLevel());
        //        for (Concept item : concepts) {
        //            System.out.println(item.getName());
        //        }
        //        List<Concept> concepts_1 = cDao.findConceptsByName("spoon");
        //        List<Concept> concepts_by_LR = cDao.findConceptsByLanguageRepresentationExact("fork");
        //        for (Concept item : concepts_by_LR) {
        //            System.out.println(item.getName());
        //        }
        //
        //        
        ////        for (Concept item : concepts_all) {
        ////            System.out.println(item.getName());
        ////        }
        //
        //        HashSet<Concept> sisters = new HashSet<>();
        //        List<Concept> parents = new ArrayList<>();
        ////        Concept concept1 = concepts_all.get(0);
        ////
        ////        cDao.persist(concept1);
        //        System.out.println("ANCESTORS: " + cDao.getAllAncestors(concepts_by_LR.get(0)));
        ////        
        //        
        //        //System.out.println("Concept0: " + concepts_1.get(0));
        //
        //        parents = cDao.getParentsOfConcept(concepts_by_LR.get(0));
        //        for (Concept parent : parents) {
        //            System.out.println("PARENT: " + parent);
        //            sisters.addAll(cDao.getChildrenOfConcept(parent));
        //        }
        //        sisters.remove(concepts_by_LR.get(0));
        //
        //        System.out.println("\n\nSISTERS: \n");
        //        
        //        for (Concept con : sisters) {
        //            System.out.println(con + " " + con.getSpecificityLevel());
        //        }
    }
}
