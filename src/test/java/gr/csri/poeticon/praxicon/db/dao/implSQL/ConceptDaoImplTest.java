/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.java.gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.ConceptDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dmavroeidis
 */
public class ConceptDaoImplTest {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory(
            "PraxiconDBTestPU");
    private EntityManager em = emf.createEntityManager();

    UserTransaction utx;

    public ConceptDaoImplTest() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {

//        
//        clearData();
//        insertData();
//        startTransaction();
    }

    @After
    public void tearDown() throws RollbackException, HeuristicMixedException,
            HeuristicRollbackException, SecurityException, IllegalStateException,
            SystemException {
        utx.commit();
    }

    /**
     * Test of findAllConcepts method, of class ConceptDaoImpl.
     */
    @Test
    public void testFindAllConcepts() {
        System.out.println("findAllConcepts");

        Concept concept1 = new Concept();
        concept1.setName("concept1");
        concept1.setConceptType(Concept.type.ABSTRACT);
        concept1.setStatus(Concept.status.CONSTANT);
        concept1.setUniqueInstance(Concept.unique_instance.YES);
        concept1.setSource("myMind");

        Concept concept2 = new Concept();
        concept2.setName("concept2");
        concept2.setConceptType(Concept.type.MOVEMENT);
        concept2.setStatus(Concept.status.VARIABLE);
        concept2.setUniqueInstance(Concept.unique_instance.NO);
        concept2.setSource("myMind2");

        List<Concept> conceptsList = new ArrayList<>();
        conceptsList.add(concept1);
        conceptsList.add(concept2);

        ConceptDao instance = new ConceptDaoImpl();
        List<Concept> expResult = conceptsList;
        List<Concept> result = instance.findAllConcepts();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

//    /**
//     * Test of findConceptByConceptId method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testFindConceptByConceptId() {
//        System.out.println("findConceptByConceptId");
//        long conceptId = 0L;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        Concept expResult = null;
//        Concept result = instance.findConceptByConceptId(conceptId);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of findConceptsByName method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testFindConceptsByName() {
//        System.out.println("findConceptsByName");
//        String conceptName = "concept1";
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result = instance.findConceptsByName(conceptName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//    /**
//     * Test of findConceptByNameExact method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testFindConceptByNameExact() {
//        System.out.println("findConceptByNameExact");
//        String conceptName = "";
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        Concept expResult = null;
//        Concept result = instance.findConceptByNameExact(conceptName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of findConceptsByLanguageRepresentation method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testFindConceptsByLanguageRepresentation() {
//        System.out.println("findConceptsByLanguageRepresentation");
//        String languageRepresentationName = "";
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result =
//                instance.findConceptsByLanguageRepresentation(
//                        languageRepresentationName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of findConceptsByLanguageRepresentationExact method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testFindConceptsByLanguageRepresentationExact() {
//        System.out.println("findConceptsByLanguageRepresentationExact");
//        String languageRepresentationName = "";
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result =
//                instance.findConceptsByLanguageRepresentationExact(
//                        languageRepresentationName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of findConceptsByStatus method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testFindConceptsByStatus() {
//        System.out.println("findConceptsByStatus");
//        Concept.status status = null;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result = instance.findConceptsByStatus(status);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getConceptWithNameOrID method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetConceptWithNameOrID() {
//        System.out.println("getConceptWithNameOrID");
//        String v = "";
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        Concept expResult = null;
//        Concept result = instance.getConceptWithNameOrID(v);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of updatedConcept method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testUpdatedConcept() {
//        System.out.println("updatedConcept");
//        Concept newConcept = null;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        Concept expResult = null;
//        Concept result = instance.updatedConcept(newConcept);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of update method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testUpdate_Concept() {
//        System.out.println("update");
//        Concept newConcept = null;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        instance.update(newConcept);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of update method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testUpdate_Concept_Concept() {
//        System.out.println("update");
//        Concept oldConcept = null;
//        Concept newConcept = null;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        instance.update(oldConcept, newConcept);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getChildrenOfConcept method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetChildrenOfConcept() {
//        System.out.println("getChildrenOfConcept");
//        Concept concept = null;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result = instance.getChildrenOfConcept(concept);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getParentsOfConcept method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetParentsOfConcept() {
//        System.out.println("getParentsOfConcept");
//        Concept concept = null;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result = instance.getParentsOfConcept(concept);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getAllAncestors method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetAllAncestors() {
//        System.out.println("getAllAncestors");
//        Concept concept = null;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result = instance.getAllAncestors(concept);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getAllOffsprings method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetAllOffsprings() {
//        System.out.println("getAllOffsprings");
//        Concept concept = null;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result = instance.getAllOffsprings(concept);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getClassesOfInstance method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetClassesOfInstance() {
//        System.out.println("getClassesOfInstance");
//        Concept concept = null;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result = instance.getClassesOfInstance(concept);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getInstancesOf method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetInstancesOf() {
//        System.out.println("getInstancesOf");
//        Concept concept = null;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result = instance.getInstancesOf(concept);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getBasicLevel method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetBasicLevel() {
//        System.out.println("getBasicLevel");
//        Concept concept = null;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result = instance.getBasicLevel(concept);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getBasicLevelOfAnEntityConcept method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetBasicLevelOfAnEntityConcept() {
//        System.out.println("getBasicLevelOfAnEntityConcept");
//        Concept concept = null;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result = instance.getBasicLevelOfAnEntityConcept(concept);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getBasicLevelOfAnAbstractConcept method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetBasicLevelOfAnAbstractConcept() {
//        System.out.println("getBasicLevelOfAnAbstractConcept");
//        Concept concept = null;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result =
//                instance.getBasicLevelOfAnAbstractConcept(concept);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getConceptsRelatedWithByRelationType method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetConceptsRelatedWithByRelationType() {
//        System.out.println("getConceptsRelatedWithByRelationType");
//        Concept concept = null;
//        RelationType relationType = null;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result =
//                instance.getConceptsRelatedWithByRelationType(concept,
//                        relationType);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of clearManager method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testClearManager() {
//        System.out.println("clearManager");
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        instance.clearManager();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getEntityQuery method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetEntityQuery() {
//        System.out.println("getEntityQuery");
//        Concept concept = null;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        Query expResult = null;
//        Query result = instance.getEntityQuery(concept);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    public void persist(Object object) {
        EntityManagerFactory emf = javax.persistence.Persistence.
                createEntityManagerFactory("PraxiconDBTestPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("transaction error");
        } finally {
            em.flush();
            em.close();
        }
    }

    private void clearData() throws Exception {
        System.out.println(utx.toString());
        utx.begin();
        em.joinTransaction();
        System.out.println("Dumping old records...");
        //em.createQuery("delete from Game").executeUpdate();
        utx.commit();
    }

    private void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();

        System.out.println("Inserting records...");
        /*
         * Create Concept1
         */
        Concept concept1 = new Concept();
        concept1.setName("concept1");
        concept1.setConceptType(Concept.type.ABSTRACT);
        concept1.setStatus(Concept.status.CONSTANT);
        concept1.setUniqueInstance(Concept.unique_instance.YES);
        concept1.setSource("myMind");
        System.out.println(concept1.getName());

        /*
         * Create Concept2
         */
        Concept concept2 = new Concept();
        concept2.setName("concept2");
        concept2.setConceptType(Concept.type.MOVEMENT);
        concept2.setStatus(Concept.status.VARIABLE);
        concept2.setUniqueInstance(Concept.unique_instance.NO);
        concept2.setSource("myMind2");
        System.out.println(concept2.getName());

        /*
         * Add Language Representations to the concepts
         */
        List<LanguageRepresentation> languageRepresentations1;
        languageRepresentations1 = new ArrayList<>();

        LanguageRepresentation languageRepresentation1 =
                new LanguageRepresentation();
        languageRepresentation1.setLanguage(LanguageRepresentation.language.EN);
        languageRepresentation1.setText("LR1");
        languageRepresentation1.setPartOfSpeech(
                LanguageRepresentation.part_of_speech.NOUN);
        languageRepresentation1.setPragmaticStatus(
                LanguageRepresentation.pragmatic_status.FIGURATIVE);

        LanguageRepresentation languageRepresentation2 =
                new LanguageRepresentation();
        languageRepresentation2.setLanguage(LanguageRepresentation.language.EN);
        languageRepresentation2.setText("LR2");
        languageRepresentation2.setPartOfSpeech(
                LanguageRepresentation.part_of_speech.VERB);
        languageRepresentation2.setPragmaticStatus(
                LanguageRepresentation.pragmatic_status.FIGURATIVE);

        LanguageRepresentation languageRepresentation3 =
                new LanguageRepresentation();
        languageRepresentation3.setLanguage(LanguageRepresentation.language.EL);
        languageRepresentation3.setText("LR3");
        languageRepresentation3.setPartOfSpeech(
                LanguageRepresentation.part_of_speech.ADVERB);
        languageRepresentation3.setPragmaticStatus(
                LanguageRepresentation.pragmatic_status.LITERAL);

        concept1.addLanguageRepresentation(languageRepresentation1, false);
        concept1.addLanguageRepresentation(languageRepresentation2, true);
        concept2.addLanguageRepresentation(languageRepresentation3, true);

        em.persist(concept1);
        em.persist(concept2);
        utx.commit();
        // clear the persistence context (first-level cache)
        em.clear();
    }

    private void startTransaction() throws Exception {
        utx.begin();
        em.joinTransaction();
    }
}
