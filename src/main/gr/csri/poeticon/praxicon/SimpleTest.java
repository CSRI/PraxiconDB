/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon;

import gr.csri.poeticon.praxicon.db.dao.ConceptDao;
import gr.csri.poeticon.praxicon.db.dao.LanguageRepresentationDao;
import gr.csri.poeticon.praxicon.db.dao.RelationArgumentDao;
import gr.csri.poeticon.praxicon.db.dao.RelationDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.LanguageRepresentationDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationArgumentDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationDaoImpl;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
import gr.csri.poeticon.praxicon.db.entities.RelationType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author dmavroeidis
 *
 */
public class SimpleTest {

    public static void main(String args[]) {

        testConcepts();
        testLanguageRepresentations();
        testRelations();
        // Start by checking most of the methods.
        // Concepts:


//        for (Relation relation : hasInstanceRelations) {
//            //System.out.println(relation);
//            if (relation.getLeftArgument().isConcept() && relation.
//                    getRightArgument().isConcept()) {
//                System.out.println(
//                        relation.getLeftArgument().getConcept() + " " +
//                        relation.getType().getForwardNameString() + " " +
//                        relation.getRightArgument().getConcept());
//            } else {
//                System.out.println("One of the arguments is not a Concept");
//            }
//        }
        System.exit(0);
    }

    public static void testConcepts() {
        ConceptDao cDao = new ConceptDaoImpl();

        String toSearch = "spoon";

        // Get the number of all concepts
        List<Concept> concepts = cDao.getAllConcepts();
        System.out.println("\n\nNumber of all concepts: " + concepts.size());

        // Get children concepts and specificity level of the first concept 
        // in the list of concepts that have language representation spoon.
        System.out.println("\n\nChildren of the first occurence of a concept" +
                " having language representation spoon: ");
        System.out.println("------------------------------------------------" +
                "----------------------------------");
        List<Concept> conceptsSpoon = cDao.
                getConceptsByLanguageRepresentationExact(toSearch);
        List<Concept> childrenOfSpoon = cDao.getChildren(
                conceptsSpoon.get(0));
        for (Concept concept : childrenOfSpoon) {
            System.out.print(concept.getExternalSourceId());
            System.out.print(" - ");
            System.out.println(concept.getSpecificityLevel());
        }

        // Get parent concepts and specificity level of the first concept 
        // in the list of concepts that have language representation spoon.
        System.out.println("\n\nParent concepts of the first occurence of a " +
                "concept having language representation spoon: ");
        System.out.println("------------------------------------------------" +
                "-----------------------------------------");
        List<Concept> parents = new ArrayList<>();
        HashSet<Concept> sisters = new HashSet<>();
        parents = cDao.getParents(conceptsSpoon.get(0));
        for (Concept parent : parents) {
            System.out.println(parent + " - \t" + parent.getSpecificityLevel());
            sisters.addAll(cDao.getChildren(parent));
        }

        // Get sister concepts and specificity level of the first concept 
        // in the list of concepts that have language representation spoon.
        System.out.println("\n\nSister concepts of the first occurence of a " +
                "concept having language representation spoon: ");
        System.out.println("------------------------------------------------" +
                "-----------------------------------------");
        for (Concept sister : sisters) {
            System.out.println(sister + " - \t" + sister.getSpecificityLevel());
        }

        // Get all Basic Level Concepts.
        System.out.println("\n\nCount All Basic Level Concepts:");
        System.out.println("-------------------------------------------");
        List<Concept> basicLevelConcepts = cDao.getAllBasicLevelConcepts();
        System.out.println(basicLevelConcepts.size());
        
                String stringToSearch = "entity%1:03:00::";
        System.out.println("\n\nBasic Level of concept " + stringToSearch);
        System.out.println("-------------------------------------------");
        Concept concept = cDao.getConceptByExternalSourceIdExact(
                stringToSearch);
        long startTime = System.nanoTime();
        List<Concept> basicLevelOfConceptSoupSpoon = cDao.getBasicLevelConcepts(concept);
        long endTime = System.nanoTime();
        System.out.print(
                "Time of getBasicLevel() for concept: " + stringToSearch + " ");
        System.out.print((endTime - startTime) / 1000000000);
        System.out.println(" seconds");
//
        if (basicLevelOfConceptSoupSpoon.isEmpty()) {
            System.out.println("Concept " + stringToSearch +
                    " doesn't have a Basic Level Concept");
        } else {
            for (Concept item : basicLevelOfConceptSoupSpoon) {
                System.out.println(item);
            }
        }

    }

    public static void testLanguageRepresentations() {
        ConceptDao cDao = new ConceptDaoImpl();
        LanguageRepresentationDao lrDao = new LanguageRepresentationDaoImpl();

        // Get the language representations of all concepts having "spoon" as 
        // language representation.
        String toSearch = "spoon";
        List<Concept> conceptsSpoon = cDao.
                getConceptsByLanguageRepresentationExact(toSearch);
        System.out.println("\n\nLanguage Representations of spoon: ");
        System.out.println("---------------------------------");
        for (Concept concept : conceptsSpoon) {
            System.out.print(concept.getLanguageRepresentationName());
            System.out.print(" - ");
            System.out.print(concept.getExternalSourceId());
            System.out.print(" - ");
            System.out.println(concept.getSpecificityLevel());
        }

        // Get all Language Representation texts count.
        // Would be faster to write a NamedQuery for that, but wanted to
        // test the getAllLanguageRepresentationText() method.
        System.out.println("\n\nCount of all Language Representation Texts:");
        System.out.println("-------------------------------------------");
        List<String> languageRepresentationTexts = new ArrayList<>();
        languageRepresentationTexts = lrDao.getAllLanguageRepresentationText();
        System.out.println(languageRepresentationTexts.size());

    }

    public static void testRelations() {
        ConceptDao cDao = new ConceptDaoImpl();
        RelationDao rDao = new RelationDaoImpl();

        // Check whether concepts "shape" and "round_shape" are related.
        System.out.println("\n\nCheck whether two concepts are related: ");
        System.out.println("--------------------------------------- ");
        Concept conceptRoundShape = cDao.getConceptByExternalSourceIdExact(
                "round_shape%1:25:00::");
        Concept conceptShape = cDao.getConceptByExternalSourceIdExact(
                "shape%1:03:00::");
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
        System.out.println("-------------------------------");
        List<Relation> allRelationsOfConceptShape = rDao.
                getAllRelationsOfConcept(conceptShape);
        for (Relation relation : allRelationsOfConceptShape) {
            System.out.println(relation);
        }

        System.out.println("\n\nAll relations of concept substance: ");
        System.out.println("-------------------------------");
        Concept conceptSubstance = cDao.getConceptByExternalSourceIdExact(
                "substance%1:03:00::");
        List<Relation> allRelationsOfConceptSubstance = rDao.
                getAllRelationsOfConcept(conceptSubstance);
        for (Relation relation : allRelationsOfConceptSubstance) {
            System.out.println(
                    relation.getLeftArgument().getConcept() + " " +
                    relation.getType().getForwardNameString() + " " +
                    relation.getRightArgument().getConcept());
        }
        System.out.println(
                "\n\nCount of all relations with relation type: HAS_INSTANCE");
        System.out.println("-----------------------------------------------");
        List<Relation> hasInstanceRelations = rDao.getRelationsByRelationType(
                RelationType.RelationNameForward.HAS_INSTANCE);

        System.out.println(hasInstanceRelations.size());
    }

}
