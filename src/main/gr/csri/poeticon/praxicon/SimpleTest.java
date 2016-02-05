/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon;

import static gr.csri.poeticon.praxicon.EntityMngFactory.getEntityManager;
import gr.csri.poeticon.praxicon.db.dao.ConceptDao;
import gr.csri.poeticon.praxicon.db.dao.LanguageRepresentationDao;
import gr.csri.poeticon.praxicon.db.dao.RelationArgumentDao;
import gr.csri.poeticon.praxicon.db.dao.RelationDao;
import gr.csri.poeticon.praxicon.db.dao.RelationSetDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.LanguageRepresentationDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationArgumentDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationSetDaoImpl;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
import gr.csri.poeticon.praxicon.db.entities.RelationSet;
import gr.csri.poeticon.praxicon.db.entities.RelationType;
import static gr.csri.poeticon.praxicon.db.entities.RelationType.RelationNameForward.TYPE_TOKEN;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManager;
import org.hibernate.Session;

/**
 *
 * @author dmavroeidis
 *
 */
public class SimpleTest {

    public static void main(String args[]) {

//        // Get the OS to set the clear command.
//        String os = System.getProperty("os.name");
        Scanner user_input = new Scanner(System.in);
        String test_choice = "a";

        while (test_choice != "q") {
            if ((test_choice != "5" && test_choice != "a") || (test_choice != "q")) {
                System.out.println("\n\nPress Enter to continue...");
                try {
                    System.in.read();
                } catch (Exception e) {
                }
            }
            for (int i = 0; i < 50; ++i) {
                System.out.println();
            }
            System.out.println(
                    "██████╗ ██████╗  █████╗ ██╗  ██╗██╗ ██████╗ ██████╗ ███╗   ██╗");
            System.out.println(
                    "██╔══██╗██╔══██╗██╔══██╗╚██╗██╔╝██║██╔════╝██╔═══██╗████╗  ██║");
            System.out.println(
                    "██████╔╝██████╔╝███████║ ╚███╔╝ ██║██║     ██║   ██║██╔██╗ ██║");
            System.out.println(
                    "██╔═══╝ ██╔══██╗██╔══██║ ██╔██╗ ██║██║     ██║   ██║██║╚██╗██║");
            System.out.println(
                    "██║     ██║  ██║██║  ██║██╔╝ ██╗██║╚██████╗╚██████╔╝██║ ╚████║");
            System.out.println(
                    "╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝ ╚═════╝ ╚═════╝ ╚═╝  ╚═══╝");
            System.out.println("");
            System.out.println(
                    "       ___ ____ ____ ___    ____ _  _ _ ___ ____");
            System.out.println(
                    "        |  |___ [__   |     [__  |  | |  |  |___ ");
            System.out.println(
                    "        |  |___ ___]  |     ___] |__| |  |  |___ ");

            System.out.println("");
            System.out.println("Please, make your choice:");
            System.out.println("-------------------------");
            System.out.println("1. Concepts");
            System.out.println("2. Language Representations");
            System.out.println("3. Relations & Relation Sets");
            System.out.println("4. Export everything to XML");
            System.out.println("5. Import from XML");
            System.out.println("q. Exit");
            System.out.println();
            System.out.println("Please enter your choice: ");
            test_choice = user_input.next();
            switch (test_choice) {
                case "1":
                    testConcepts();
                    continue;
                case "2":
                    testLanguageRepresentations();
                    continue;
                case "3":
                    testRelations();
                    continue;
                case "4":
                    exportAllPraxicon();
                    continue;
                case "5":
                    testXmlImport();
                    continue;
                case "q":
                    System.exit(0);
            }
        }
        System.exit(0);
    }

    public static void testConcepts() {
        /*
         Get active session to update concepts. This way, we can
         update retrieved objects directly to avoid setting EAGER fetch
         which would criple performance during retrieval from the database.
         */
        EntityManager em = getEntityManager();
        Session session = em.unwrap(org.hibernate.Session.class);

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
            /* Check if concept is contained in the active session and
             add it if it's not.
             */
            if (!session.contains(concept)) {
                session.update(concept);
            }
            System.out.print(concept.getName());
            System.out.print(" - \t");
            System.out.print(concept.getLanguageRepresentationsNames().
                    toString());
            System.out.print(" - \t");
            System.out.println(concept.getSpecificityLevel());
        }

        // Get parent concepts and specificity level of the first concept
        // in the list of concepts that have language representation spoon.
        System.out.println("\n\nParent concepts of the first occurence of a " +
                "concept having language representation spoon: ");
        System.out.println("------------------------------------------------" +
                "-----------------------------------------");
        List<Concept> parents = cDao.getParents(conceptsSpoon.get(0));
        List<Concept> sisters = new ArrayList<>();
        for (Concept parent : parents) {
            System.out.println("Parent: " + parent + " - \t" + parent.
                    getSpecificityLevel());
            sisters.addAll(cDao.getChildren(parent));
        }

        System.out.println(
                "\n\nOffspring concepts of the first occurence of a " +
                "concept having language representation spoon: ");
        System.out.println("------------------------------------------------" +
                "-----------------------------------------");
        long startTime = System.nanoTime();
        List<Concept> offsprings = cDao.getAllOffsprings(conceptsSpoon.get(0));
        long endTime = System.nanoTime();
        System.out.print("getAllOffsprings() took: ");
        System.out.print((endTime - startTime) / 1000000000);
        System.out.println(" seconds to run");
        for (Concept offspring : offsprings) {
            System.out.println("Offspring: " + offspring + " - \t" + offspring.
                    getSpecificityLevel());
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();

        XmlUtils.exportConceptsToXML(childrenOfSpoon, String.
                format("/home/dmavroeidis/Concepts_%s.xml",
                        dateFormat.format(date)));

        // Get all Basic Level Concepts.
        System.out.println("\n\nCount All Basic Level Concepts:");
        System.out.println("-------------------------------------------");
        List<Concept> basicLevelConcepts = cDao.getAllBasicLevelConcepts();
        System.out.println(basicLevelConcepts.size());

        String stringToSearch = "substance%1:03:00::";
        System.out.println("\n\nBasic Level of concept " + stringToSearch);
        System.out.println("-------------------------------------------");
        Concept concept = cDao.getConceptByNameExact(
                stringToSearch);
        startTime = System.nanoTime();
        List<Concept> basicLevelOfConcept = cDao.getBasicLevelConcepts(concept);
        endTime = System.nanoTime();
        System.out.print(
                "Time of getBasicLevel() for concept: " + stringToSearch + " ");
        System.out.print((endTime - startTime) / 1000000000);
        System.out.println(" seconds");

        if (basicLevelOfConcept.isEmpty()) {
            System.out.println("Concept " + stringToSearch +
                    " doesn't have a Basic Level Concept");
        } else {
            for (Concept item : basicLevelOfConcept) {
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
            System.out.print(concept.getLanguageRepresentationsNames());
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
        List<String> languageRepresentationTexts = lrDao.
                getAllLanguageRepresentationText();
        System.out.println(languageRepresentationTexts.size());

    }

    public static void testRelations() {
        ConceptDao cDao = new ConceptDaoImpl();
        RelationDao rDao = new RelationDaoImpl();
        RelationSetDao rsDao = new RelationSetDaoImpl();

        // Check whether concepts "shape" and "round_shape" are related.
        System.out.println("\n\nCheck whether two concepts are related: ");
        System.out.println("--------------------------------------- ");
        Concept conceptRoundShape = cDao.getConceptByNameExact(
                "round_shape%1:25:00::");
        Concept conceptShape = cDao.getConceptByNameExact(
                "shape%1:03:00::");
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        RelationArgument relationArgumentConceptShape = raDao.
                getRelationArgument(conceptShape);
        RelationArgument relationArgumentConceptRoundShape = raDao.
                getRelationArgument(conceptRoundShape);
        boolean areRelated;
        areRelated = rDao.areRelated(conceptShape, conceptRoundShape);
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

        // Get all relations of concept spoon%1:06:00::
        System.out.println("\n\nAll relations of concept spoon%1:06:00::: ");
        System.out.println("-------------------------------");
        Concept conceptspoon6 = cDao.getConceptByNameExact(
                "spoon%1:06:00::");
        List<Relation> allRelationsOfConceptSpoon6 = rDao.
                getAllRelationsOfConcept(conceptspoon6);
        for (Relation relation : allRelationsOfConceptSpoon6) {
            System.out.println(relation);
        }

        System.out.println("\n\nAll relations of concept substance: ");
        System.out.println("-------------------------------");
        Concept conceptSubstance = cDao.getConceptByNameExact(
                "substance%1:03:00::");
        List<Relation> allRelationsOfConceptSubstance = rDao.
                getAllRelationsOfConcept(conceptSubstance);
        for (Relation relation : allRelationsOfConceptSubstance) {
            System.out.println(
                    relation.getLeftArgument().getConcept() + " " +
                    relation.getRelationType().getForwardNameString() + " " +
                    relation.getRightArgument().getConcept());
        }
        System.out.println(
                "\n\nCount of all relations with relation type: HAS_INSTANCE");
        System.out.println("-----------------------------------------------");
        List<Relation> hasInstanceRelations = rDao.getRelationsByRelationType(
                RelationType.RelationNameForward.HAS_INSTANCE);

        System.out.println(hasInstanceRelations.size());

        // Create a relation set
        // Test the XML export functionality for relation set
        Concept conceptForRelationSet = cDao.getConceptByNameExact(
                "dummy_object_brooch%2:35:00::_brooch%1:06:00::");

        List<RelationSet> relationSets = rsDao.getRelationSetsByConcept(
                conceptForRelationSet);

        // Find a specific relation in the database
        System.out.println("ConceptShape: " + conceptShape);
        System.out.println("ConceptRoundShape: " + conceptRoundShape);
        System.out.println("relationArgumentConceptShape id: " +
                relationArgumentConceptShape);
        System.out.println("relationArgumentConceptRoundShape id: " +
                relationArgumentConceptRoundShape);
        Relation relationFound = rDao.getRelation(relationArgumentConceptShape,
                relationArgumentConceptRoundShape, TYPE_TOKEN);
        System.out.println("\n\nCheck if the relation exists:");
        System.out.println("-----------------------------");
        System.out.println(relationFound);
        if (relationFound != null) {
            System.out.println("YES\n\n");
        } else {
            System.out.println("NO\n\n");
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();

        XmlUtils.exportRelationsToXML(allRelationsOfConceptSubstance, String.
                format("/home/dmavroeidis/Relations_%s.xml",
                        dateFormat.format(date)));

        XmlUtils.exportRelationSetsToXML(relationSets, String.
                format("/home/dmavroeidis/RelationSets_%s.xml",
                        dateFormat.format(date)));

        // Create a list of concepts to create XML with both concepts and
        // relation sets.
        List<Concept> basicLevelConcepts = cDao.getAllBasicLevelConcepts();

        XmlUtils.exportAllObjectsToXML(relationSets, basicLevelConcepts,
                allRelationsOfConceptSubstance,
                String.format("/home/dmavroeidis/Objects_%s.xml",
                        dateFormat.format(date)));
    }

    public static void exportAllPraxicon() {
        ConceptDao cDao = new ConceptDaoImpl();
        RelationDao rDao = new RelationDaoImpl();
        RelationSetDao rsDao = new RelationSetDaoImpl();

        // Get all concepts
        List<Concept> concepts = cDao.getAllConcepts();
        List<Relation> relations = rDao.findAll();
        List<RelationSet> relationSets = rsDao.findAll();
        XmlUtils.exportAllObjectsToXML(relationSets, concepts, relations,
                "/home/dmavroeidis/Objects_all.xml");
    }

    public static void testXmlImport() {
        /*
         Currently commented-out all import tests.
         User can uncomment accordingly.
         */

        Scanner user_input = new Scanner(System.in);
        String test_choice = "a";

        while (test_choice != "q") {
            for (int i = 0; i < 50; ++i) {
                System.out.println();
            }
            System.out.println("Please, make your choice:");
            System.out.println("-------------------------");
            System.out.println("1. Import Concepts from test fixture");
            System.out.println("2. Import Relations from test fixture");
            System.out.println("3. Import Relation Sets from test fixture");
            System.out.println("4. Import All Objects from test fixture");
            System.out.println("q. Return to the previous menu");
            System.out.println();
            System.out.println("Please enter your choice: ");
            test_choice = user_input.next();

            switch (test_choice) {
                case "1":
                    XmlUtils.importConceptsFromXml(
                            "misc/test-fixtures/Concepts.xml");
                    System.out.println("Imported from Concepts");
                    continue;
                case "2":
                    XmlUtils.importRelationsFromXml(
                            "misc/test-fixtures/Relations.xml");
                    System.out.println("Imported from Relations");
                    continue;
                case "3":
                    XmlUtils.importRelationSetsFromXml(
                            "misc/test-fixtures/RelationSets.xml");
                    System.out.println("Imported from Relation Sets");
                    continue;
                case "4":
                    XmlUtils.importObjectsFromXml(
                            "misc/test-fixtures/Objects.xml");
                    System.out.println("Imported from Objects");
                    continue;
                case "q":
                    return;
            }
        }
    }
}
