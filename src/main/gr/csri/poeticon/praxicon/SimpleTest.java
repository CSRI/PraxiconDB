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
import gr.csri.poeticon.praxicon.db.entities.Concepts;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
import gr.csri.poeticon.praxicon.db.entities.RelationSet;
import gr.csri.poeticon.praxicon.db.entities.RelationSets;
import gr.csri.poeticon.praxicon.db.entities.RelationType;
import static gr.csri.poeticon.praxicon.db.entities.RelationType.RelationNameForward.TYPE_TOKEN;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.hibernate.Session;
import org.junit.BeforeClass;

/**
 *
 * @author dmavroeidis
 *
 */
public class SimpleTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public static void main(String args[]) {

        Scanner user_input = new Scanner(System.in);
        String test_choice = "a";

        while (test_choice != "q") {
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
            System.out.println("6. Test concepts equality method");
            System.out.println("7. Test relation sets equality method");
            System.out.println("8. Test relation sets");
            System.out.println("9. Custom test");
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
                case "6":
                    testConceptEquals();
                    continue;
                case "7":
                    testRelationSetEquals();
                    continue;
                case "8":
                    testRelationSets();
                    continue;
                case "9":
                    testCustom();
                    continue;
                case "q":
                    System.exit(0);
            }
        }
        System.exit(0);
    }

    public static void testConcepts() {
        /*
         * Get active session to update concepts. This way, we can
         * update retrieved objects directly to avoid setting EAGER fetch
         * which would criple performance during retrieval from the database.
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
        System.out.println(
                "\n\nChildren of the first occurence of a concept" +
                " having language representation spoon: ");
        System.out.println(
                "------------------------------------------------" +
                "----------------------------------");
        List<Concept> conceptsSpoon = cDao.
                getConceptsByLanguageRepresentationExact(toSearch);
        List<Concept> childrenOfSpoon = cDao.getChildren(
                conceptsSpoon.iterator().next());
        for (Concept concept : childrenOfSpoon) {
            /*
             * Check if concept is contained in the active session and
             * add it if it's not.
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
        System.out.println(
                "\n\nParent concepts of the first occurence of a " +
                "concept having language representation spoon: ");
        System.out.println(
                "------------------------------------------------" +
                "-----------------------------------------");
        List<Concept> parents = cDao.
                getParents(conceptsSpoon.iterator().next());
        List<Concept> sisters = new ArrayList<>();
        for (Concept parent : parents) {
            System.out.println("Parent: " + parent + " - \t" + parent.
                    getSpecificityLevel());
            sisters.addAll(cDao.getChildren(parent));
        }

        System.out.println(
                "\n\nOffspring concepts of the first occurence of a " +
                "concept having language representation spoon: ");
        System.out.println(
                "------------------------------------------------" +
                "-----------------------------------------");
        long startTime = System.nanoTime();
        List<Concept> offsprings = cDao.getAllOffsprings(conceptsSpoon.
                iterator().next());
        long endTime = System.nanoTime();
        System.out.print("getAllOffsprings() took: ");
        System.out.print((endTime - startTime) / 1000000000);
        System.out.println(" seconds to run");
        for (Concept offspring : offsprings) {
            System.out.println("Offspring: " + offspring + " - \t" +
                    offspring.
                    getSpecificityLevel());
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();

        XmlUtils.exportConceptsToXML(childrenOfSpoon, String.
                format("misc/export/Concepts_%s.xml",
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
        List<Concept> basicLevelOfConcept = cDao.
                getBasicLevelConcepts(concept);
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
        System.out.println("\n\nPress Enter to continue...");
        try {
            System.in.read();
        } catch (Exception e) {
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
        Relation relationFound = rDao.
                getRelation(relationArgumentConceptShape,
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
                format("misc/exports/Relations_%s.xml",
                        dateFormat.format(date)));

        XmlUtils.exportRelationSetsToXML(relationSets, String.
                format("misc/exports/RelationSets_%s.xml",
                        dateFormat.format(date)));

        // Create a list of concepts to create XML with both concepts and
        // relation sets.
        List<Concept> basicLevelConcepts = cDao.getAllBasicLevelConcepts();

        XmlUtils.exportAllObjectsToXML(relationSets, basicLevelConcepts,
                allRelationsOfConceptSubstance,
                String.format("misc/exports/Objects_%s.xml",
                        dateFormat.format(date)));
    }

    public static void exportAllPraxicon() {
        ConceptDao cDao = new ConceptDaoImpl();
        RelationDao rDao = new RelationDaoImpl();
        RelationSetDao rsDao = new RelationSetDaoImpl();

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();

        // Get all concepts
        List<Concept> concepts = cDao.getAllConcepts();
        List<Relation> relations = rDao.findAll();
        List<RelationSet> relationSets = rsDao.findAll();
        XmlUtils.exportAllObjectsToXML(relationSets, concepts, relations,
                String.format("misc/exports/ObjectsAll_%s.xml",
                        dateFormat.format(date)));
    }

    public static void testXmlImport() {
        /*
         * Currently commented-out all import tests.
         * User can uncomment accordingly.
         */

        Scanner userInput = new Scanner(System.in);
        String testChoice = "a";

        while (testChoice != "q") {
            for (int i = 0; i < 50; ++i) {
                System.out.println();
            }
            System.out.println("Please, make your choice:");
            System.out.println("-------------------------");
            System.out.println("1. Import from test fixtures");
            System.out.println("2. Import from file");
            System.out.println("q. Return to the previous menu");
            System.out.println();
            System.out.println("Please enter your choice: ");
            testChoice = userInput.next();

            switch (testChoice) {
                case "1":
                    ImportXmlFromFixtures();
                    continue;
                case "2":
                    ImportXmlFromFile();
                    continue;
                case "q":
                    return;
            }
        }
    }

    public static void ImportXmlFromFixtures() {
        Scanner userInput = new Scanner(System.in);
        String testChoice = "a";

        while (testChoice != "q") {
            for (int i = 0; i < 50; ++i) {
                System.out.println();
            }
            System.out.println("Select fixture to import from:");
            System.out.println("-------------------------");
            System.out.println("1. Import Concepts");
            System.out.println("2. Import Relations");
            System.out.println("3. Import Relation Sets");
            System.out.println("4. Import Objects");
            System.out.println("q. Return to the previous menu");
            System.out.println();
            System.out.println("Please enter your choice: ");
            testChoice = userInput.next();

            switch (testChoice) {
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

    public static void ImportXmlFromFile() {
        Scanner userInput = new Scanner(System.in);
        Scanner fileInput = new Scanner(System.in);
        String testChoice = "a";
        String fileName = "";

        while (testChoice != "q") {
            for (int i = 0; i < 50; ++i) {
                System.out.println();
            }
            System.out.println("What do you want to import?");
            System.out.println("-------------------------");
            System.out.println("1. Import Concepts");
            System.out.println("2. Import Relations");
            System.out.println("3. Import Relation Sets");
            System.out.println("4. Import Objects (all or some of the above" +
                    " in a single file)");
            System.out.println("q. Return to the previous menu");
            System.out.println();
            System.out.println("Please enter your choice: ");
            testChoice = userInput.next();
            int result = 0;

            switch (testChoice) {
                case "1":
                    System.out.println("Please, provide the path to the " +
                            "Concepts XML file:");
                    fileName = fileInput.next();
                    result = XmlUtils.importConceptsFromXml(fileName);
                    if (result == 0) {
                        System.out.println("Imported from Concepts XML file");
                    } else if (result == 1) {
                        System.err.println(
                                "Could not import all Concepts from provided file.");
                    }
                    continue;
                case "2":
                    System.out.println("Please, provide the path to the " +
                            "Concepts XML file:");
                    fileInput = new Scanner(System.in);
                    fileName = fileInput.next();
                    result = XmlUtils.importRelationsFromXml(fileName);
                    if (result == 0) {
                        System.out.println("Imported from Relations XML file");
                    } else if (result == 1) {
                        System.err.println(
                                "Could not import all Relations from provided file.");
                    }
                    continue;
                case "3":
                    System.out.println("Please, provide the path to the " +
                            "Concepts XML file:");
                    fileInput = new Scanner(System.in);
                    fileName = fileInput.next();
                    result = XmlUtils.importRelationSetsFromXml(fileName);
                    if (result == 0) {
                        System.out.
                                println("Imported from Relation Sets XML file");
                    } else if (result == 1) {
                        System.err.println(
                                "Could not import Relation Sets from provided file.");
                    }
                    continue;
                case "4":
                    System.out.println("Please, provide the path to the " +
                            "Objects XML file:");
                    fileInput = new Scanner(System.in);
                    fileName = fileInput.next();
                    result = XmlUtils.importObjectsFromXml(fileName);
                    if (result == 0) {
                        System.out.println("Imported from Objects XML file");
                    } else if (result == 1) {
                        System.err.println(
                                "Could not import Objects from provided file.");
                    }
                    continue;
                case "q":
                    return;
            }
        }
    }

    private static void testConceptEquals() {
        System.out.println(
                "\nTesting the validity of equals method in Concept");
        // Import single concept from file.
        int result = 0;
        Concepts importedConcepts = new Concepts();

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Concepts.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            String fileName =
                    "misc/test-fixtures/" +
                    "Concepts_colour_abstract_20160712172921.xml";
            File xmlFile = new File(fileName);
            importedConcepts =
                    (Concepts)jaxbUnmarshaller.unmarshal(xmlFile);
        } catch (JAXBException ex) {
            Logger.getLogger(XmlUtils.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        ConceptDao cDao = new ConceptDaoImpl();
        Concept dbConcept = cDao.getConceptByNameExact("color%1:07:01::");
        if (!importedConcepts.getConcepts().isEmpty()) {
            for (Concept item : importedConcepts.getConcepts()) {
                System.out.println("\nConcept's " + item + " hash code is  " +
                        item.hashCode());
                System.out.println("\nLanguageRepresentation's " +
                        item.getConcept_LanguageRepresentation().toString() +
                        " hash code is  " +
                        item.getConcept_LanguageRepresentation().
                        toString().hashCode());

                System.out.println(
                        "\nDBConcept's " + item + " hash code is  " +
                        dbConcept.hashCode());
                System.out.println("\nDBLanguageRepresentation's " +
                        item.getConcept_LanguageRepresentation().toString() +
                        " hash code is  " +
                        dbConcept.getConcept_LanguageRepresentation().
                        toString().hashCode());

                if (item.equals(dbConcept)) {
                    System.out.println("\nConcept " + item + " is equal to " +
                            dbConcept);
                } else {
                    System.out.println("\nConcept " + item +
                            " NOT equal to " + dbConcept);
                }
                if (item.getConcept_LanguageRepresentation().toString().
                        equals(
                                dbConcept.getConcept_LanguageRepresentation().
                                toString())) {
                    System.out.println("\nLR " + item.
                            getConcept_LanguageRepresentation().toString() +
                            " is equal to " +
                            dbConcept.getConcept_LanguageRepresentation().
                            toString());
                } else {
                    System.out.println("\nLR " + item.
                            getConcept_LanguageRepresentation().toString() +
                            " NOT equal to " +
                            dbConcept.getConcept_LanguageRepresentation().
                            toString());
                }

            }
        }
    }

    private static void testRelationSetEquals() {
        System.out.println(
                "\nTesting the validity of equals method in RelationSet\n\n");

        int result = 0;
        RelationSets importedRelationSets = new RelationSets();

        try {
            JAXBContext jaxbContext = JAXBContext.
                    newInstance(RelationSets.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            String fileName = "misc/test-fixtures/RelationSet_352.xml";
            File xmlFile = new File(fileName);
            importedRelationSets =
                    (RelationSets)jaxbUnmarshaller.unmarshal(xmlFile);
            System.out.println("RelationSets: " + importedRelationSets);
        } catch (JAXBException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(XmlUtils.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        RelationSetDao rsDao = new RelationSetDaoImpl();

        RelationSet dbRelationSet = rsDao.getRelationSetByName("RelSet#352");

        if (!importedRelationSets.getRelationSets().isEmpty()) {
            for (RelationSet item : importedRelationSets.getRelationSets()) {
                System.out.println("RelationSet's " + item + item.getName() +
                        " hash code is  " +
                        item.hashCode());
                System.out.println("DBRelationSet's " + dbRelationSet +
                        dbRelationSet.getName() + " hash code is  " +
                        dbRelationSet.hashCode());
                if (item.equals(dbRelationSet)) {
                    System.out.println("DBRelationSet's " + item + item.
                            getName() + " is equal to " +
                            dbRelationSet);
                } else {
                    System.out.println("DBRelationSet's " + item + item.
                            getName() + " NOT equal to " +
                            dbRelationSet);
                }
            }
        }
    }

    public static void exportConcept() {

        ConceptDao cDao = new ConceptDaoImpl();

        List<Concept> retrievedConcepts = cDao.
                getConceptsByName("color%1:07:01::");
        System.out.println("\n\nRetrieved Concept: " + retrievedConcepts);
        System.out.println("\n\nLanguage Representations: " +
                retrievedConcepts.
                iterator().next().getLanguageRepresentationsNames());

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();

        XmlUtils.exportConceptsToXML(retrievedConcepts, String.
                format("misc/export/Concepts_colour_abstract_%s.xml",
                        dateFormat.format(date)));
    }

    private static void testRelationSets() {
        System.out.println("Get all Relation Sets and print them:");
        RelationSetDao rsDao = new RelationSetDaoImpl();
        List<RelationSet> allRelationSets = rsDao.findAll();
        for (RelationSet item : allRelationSets){
            System.out.println(item.toString());
        }

//        // Get all Concepts with name like βεντάλια
//        ConceptDao cDao = new ConceptDaoImpl();
//        List<Concept> fanConcepts = cDao.getConceptsByName("βεντάλια");
//        for (Concept item : fanConcepts) {
//            System.out.println(item);
//            for (LanguageRepresentation lr : item.getLanguageRepresentations()) {
//                System.out.println(lr);
//            }
//
//        }
//        Concept concept1 = cDao.getConceptByNameExact(
//                "fdummy-shape1-βεντάλια1-shape#feature");
//        List<LanguageRepresentation> lr1 = concept1.getLanguageRepresentations();
//        LanguageRepresentation lrc1 = new LanguageRepresentation(lr1.get(0));
//        lrc1.setLanguage(LanguageRepresentation.Language.EN);
//        lrc1.setText("shape fan");
//        concept1.addLanguageRepresentation(lrc1, true);
//        cDao.persist(concept1);
//
//        Concept concept2 = cDao.getConceptByNameExact(
//                "fdummy-sizewidth1-βεντάλια1-sizewidth#feature");
//        List<LanguageRepresentation> lr2 = concept2.getLanguageRepresentations();
//        LanguageRepresentation lrc2 = new LanguageRepresentation(lr2.get(0));
//        lrc2.setLanguage(LanguageRepresentation.Language.EN);
//        lrc2.setText("size fan");
//        concept2.addLanguageRepresentation(lrc2, true);
//        cDao.persist(concept2);
//
//        Concept concept3 = cDao.getConceptByNameExact(
//                "ένα1_βεντάλια1-size#feature");
//        List<LanguageRepresentation> lr3 = concept1.getLanguageRepresentations();
//        LanguageRepresentation lrc3 = new LanguageRepresentation(lr3.get(0));
//        lrc3.setLanguage(LanguageRepresentation.Language.EN);
//        lrc3.setText("one fan");
//        concept3.addLanguageRepresentation(lrc3, true);
//        cDao.persist(concept3);
//
//        Concept concept4 = cDao.getConceptByNameExact(
//                "βεντάλια1#entity");
//        List<LanguageRepresentation> lr4 = concept1.getLanguageRepresentations();
//        LanguageRepresentation lrc4 = new LanguageRepresentation(lr4.get(0));
//        lrc4.setLanguage(LanguageRepresentation.Language.EN);
//        lrc4.setText("fan");
//        concept4.addLanguageRepresentation(lrc4, true);
//        cDao.persist(concept4);
//
//        Concept concept5 = cDao.getConceptByNameExact(
//                "κρατώ1~with~χέρι1~the~βεντάλια1#movement");
//        List<LanguageRepresentation> lr5 = concept1.getLanguageRepresentations();
//        LanguageRepresentation lrc5 = new LanguageRepresentation(lr5.get(0));
//        lrc5.setLanguage(LanguageRepresentation.Language.EN);
//        lrc5.setText("hold fan with hand");
//        concept5.addLanguageRepresentation(lrc5, true);
//        cDao.persist(concept5);

    }

    private static void testCustom() {
        ConceptDao cDao = new ConceptDaoImpl();
        RelationDao rDao = new RelationDaoImpl();
        List<RelationSet> allTheRelationsets = new ArrayList<>();
        List<Relation> allTheRelations = new ArrayList<>();
        List<Concept> allTheConcepts = new ArrayList<>();

        List<Concept> coffeeConcepts = cDao.getConceptsByName("coffee");
        List<Concept> poppyConcepts = cDao.getConceptsByName("poppy");
        List<Concept> redConcepts = cDao.getConceptsByName("red");
        List<Concept> brownConcepts = cDao.getConceptsByName("brown");
        List<Concept> cupConcepts = cDao.getConceptsByName("cup");
        List<Concept> spoonConcepts = cDao.getConceptsByName("spoon");

        System.out.println("\nConcepts containing string coffee");
        for (Concept concept : coffeeConcepts) {
            System.out.println(concept);
        }
        System.out.println("\nConcepts containing string poppy");
        for (Concept concept : poppyConcepts) {
            System.out.println(concept);
        }
        System.out.println("\nConcepts containing string red");
        for (Concept concept : redConcepts) {
            System.out.println(concept);
        }
        System.out.println("\nConcepts containing string brown");
        for (Concept concept : brownConcepts) {
            System.out.println(concept);
        }
        System.out.println("\nConcepts containing string cup");
        for (Concept concept : cupConcepts) {
            System.out.println(concept);
        }
        System.out.println("\nConcepts containing string spoon");
        for (Concept concept : spoonConcepts) {
            System.out.println(concept);
        }

        Concept coffeeConcept = cDao.getConceptByNameExact("coffee%1:13:00::");
        List<Relation> coffeeRelations = rDao.getAllRelationsOfConcept(
                coffeeConcept);
        System.out.println("\nRelations of concept coffee%1:13:00::");
        for (Relation relation : coffeeRelations) {
            System.out.println(relation);
        }
        Concept poppyConcept = cDao.getConceptByNameExact("poppy%1:20:00::");
        List<Relation> poppyRelations = rDao.getAllRelationsOfConcept(
                poppyConcept);
        System.out.println("\nRelations of concept poppy%1:20:00::");
        for (Relation relation : poppyRelations) {
            System.out.println(relation);
        }
        Concept redConcept = cDao.getConceptByNameExact("red%1:07:00::");
        List<Relation> redRelations = rDao.getAllRelationsOfConcept(
                redConcept);
        System.out.println("\nRelations of concept red%1:07:00::");
        for (Relation relation : redRelations) {
            System.out.println(relation);
        }
        Concept brownConcept = cDao.getConceptByNameExact("brown%2:30:00::");
        List<Relation> brownRelations = rDao.getAllRelationsOfConcept(
                brownConcept);
        System.out.println("\nRelations of concept brown%2:30:00::");
        for (Relation relation : brownRelations) {
            System.out.println(relation);
        }
        Concept coffeCupConcept = cDao.getConceptByNameExact(
                "coffee_cup%1:06:00::");
        List<Relation> coffeCupRelations = rDao.getAllRelationsOfConcept(
                coffeCupConcept);
        System.out.println("\nRelations of concept coffee_cup%1:06:00::");
        for (Relation relation : coffeCupRelations) {
            System.out.println(relation);
        }
        Concept spoonConcept = cDao.getConceptByNameExact(
                "spoon%1:06:00::");
        List<Relation> spoonRelations = rDao.getAllRelationsOfConcept(
                spoonConcept);
        System.out.println("\nRelations of concept spoon%1:06:00::");
        for (Relation relation : spoonRelations) {
            System.out.println(relation);
        }

        
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        RelationArgument poppyRelationArgument = raDao.getRelationArgument(
                poppyConcept);
        System.out.println("poppyRelationArgument: " + poppyRelationArgument.
                getId());
        RelationArgument redRelationArgument = raDao.getRelationArgument(
                redConcept);
        System.out.
                println("redRelationArgument: " + redRelationArgument.getId());
        RelationArgument coffeeRelationArgument = raDao.getRelationArgument(
                coffeeConcept);
        System.out.println("coffeeRelationArgument: " + coffeeRelationArgument.
                getId());
        RelationArgument brownRelationArgument = raDao.getRelationArgument(
                brownConcept);
        System.out.println("brownRelationArgument: " + brownRelationArgument.
                getId());
        

        allTheConcepts.add(redConcept);
        allTheConcepts.add(brownConcept);
        allTheConcepts.add(coffeeConcept);
        allTheConcepts.add(poppyConcept);
        allTheConcepts.add(coffeCupConcept);
        allTheConcepts.add(spoonConcept);

        allTheRelations.addAll(poppyRelations);
        allTheRelations.addAll(redRelations);
        allTheRelations.addAll(brownRelations);
        allTheRelations.addAll(coffeeRelations);
        allTheRelations.addAll(coffeCupRelations);
        allTheRelations.addAll(spoonRelations);

        XmlUtils.exportAllObjectsToXML(allTheRelationsets, allTheConcepts,
                allTheRelations, "misc/exports/praxicon_sample.xml");
    }

}
