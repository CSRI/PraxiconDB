/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.Globals;
import gr.csri.poeticon.praxicon.XmlUtils;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static javax.persistence.Persistence.createEntityManagerFactory;
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

    private static EntityManagerFactory emf;
    private static EntityManager em;
//    private static ConceptDao cDao = new ConceptDaoImpl();

    public ConceptDaoImplTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        // Get the entity manager for testing.
        emf = createEntityManagerFactory(Globals.JpaPUDerbyTest);
        em = emf.createEntityManager();
    }

    @AfterClass
    public static void tearDownClass() {
        // Close the entity manager for testing.
        em.close();
        emf.close();
    }

    @Before
    public void setUp() {
        XmlUtils.importConceptsFromXml("misc/test-fixtures/ConceptsSample.xml");
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getAllConcepts method, of class ConceptDaoImpl.
     */
    @Test
    public void testGetAllConcepts() {
        System.out.println("getAllConcepts");
        ConceptDaoImpl instance = new ConceptDaoImpl();
        int expResult = 51;
        int result = instance.getAllConcepts().size();
        assertEquals(expResult, result);
    }

    /**
     * Test of getConcept method, of class ConceptDaoImpl.
     */
    @Test
    public void testGetConcept() {
        System.out.println("getConcept");
        Concept concept = new Concept();
        concept.setExternalSourceId("107695965");
        concept.setName("sandwich%1:13:00::");
        concept.setConceptType(Concept.ConceptType.ENTITY);
        concept.setPragmaticStatus(Concept.PragmaticStatus.CONCRETE);
        concept.setSpecificityLevel(Concept.SpecificityLevel.SUBORDINATE);
        concept.setStatus(Concept.Status.CONSTANT);
        concept.setUniqueInstance(Concept.UniqueInstance.NO);
        ConceptDaoImpl instance = new ConceptDaoImpl();
        String expResult = "sandwich";
        String result = instance.getConcept(concept).
                getFirstLanguageRepresentationName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllBasicLevelConcepts method, of class ConceptDaoImpl.
     */
    @Test
    public void testGetAllBasicLevelConcepts() {
        System.out.println("getAllBasicLevelConcepts");
        ConceptDaoImpl instance = new ConceptDaoImpl();
        int expResult = 10;
        int result = instance.getAllBasicLevelConcepts().size();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllNonBasicLevelConcepts method, of class ConceptDaoImpl.
     */
    @Test
    public void testGetAllNonBasicLevelConcepts() {
        System.out.println("getAllNonBasicLevelConcepts");
        ConceptDaoImpl instance = new ConceptDaoImpl();
        int expResult = 41;
        int result = instance.getAllNonBasicLevelConcepts().size();
        assertEquals(expResult, result);
    }
//
//    /**
//     * Test of getConceptByConceptId method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetConceptByConceptId() {
//        System.out.println("getConceptByConceptId");
//        long conceptId = 0L;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        Concept expResult = null;
//        Concept result = instance.getConceptByConceptId(conceptId);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getConceptsByName method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetConceptsByName() {
//        System.out.println("getConceptsByName");
//        String conceptName = "";
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result = instance.getConceptsByName(conceptName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getConceptByNameExact method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetConceptByNameExact() {
//        System.out.println("getConceptByNameExact");
//        String conceptName = "";
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        Concept expResult = null;
//        Concept result = instance.getConceptByNameExact(conceptName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getConceptsByNameAndStatus method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetConceptsByNameAndStatus() {
//        System.out.println("getConceptsByNameAndStatus");
//        String conceptName = "";
//        Concept.Status status = null;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result =
//                instance.getConceptsByNameAndStatus(conceptName, status);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getConceptsByExternalSourceId method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetConceptsByExternalSourceId() {
//        System.out.println("getConceptsByExternalSourceId");
//        String conceptExternalSourceId = "";
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result =
//                instance.
//                getConceptsByExternalSourceId(conceptExternalSourceId);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getConceptByExternalSourceIdExact method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetConceptByExternalSourceIdExact() {
//        System.out.println("getConceptByExternalSourceIdExact");
//        String conceptExternalSourceId = "";
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        Concept expResult = null;
//        Concept result =
//                instance.getConceptByExternalSourceIdExact(
//                        conceptExternalSourceId);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getConceptsByLanguageRepresentation method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetConceptsByLanguageRepresentation() {
//        System.out.println("getConceptsByLanguageRepresentation");
//        String languageRepresentationName = "";
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result =
//                instance.getConceptsByLanguageRepresentation(
//                        languageRepresentationName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getConceptsByLanguageRepresentationExact method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetConceptsByLanguageRepresentationExact() {
//        System.out.println("getConceptsByLanguageRepresentationExact");
//        String languageRepresentationName = "";
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result =
//                instance.getConceptsByLanguageRepresentationExact(
//                        languageRepresentationName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getConceptsByLanguageRepresentationStartsWith method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetConceptsByLanguageRepresentationStartsWith() {
//        System.out.println("getConceptsByLanguageRepresentationStartsWith");
//        String languageRepresentationNameStart = "";
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result =
//                instance.getConceptsByLanguageRepresentationStartsWith(
//                        languageRepresentationNameStart);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getConceptsByLanguageRepresentationEndsWith method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetConceptsByLanguageRepresentationEndsWith() {
//        System.out.println("getConceptsByLanguageRepresentationEndsWith");
//        String languageRepresentationNameEnd = "";
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result =
//                instance.getConceptsByLanguageRepresentationEndsWith(
//                        languageRepresentationNameEnd);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getConceptsByStatus method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetConceptsByStatus() {
//        System.out.println("getConceptsByStatus");
//        Concept.Status status = null;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result = instance.getConceptsByStatus(status);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getConceptWithExternalSourceIdOrId method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetConceptWithExternalSourceIdOrId() {
//        System.out.println("getConceptWithExternalSourceIdOrId");
//        String v = "";
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        Concept expResult = null;
//        Concept result = instance.getConceptWithExternalSourceIdOrId(v);
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
//     * Test of getChildren method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetChildren() {
//        System.out.println("getChildren");
//        Concept concept = null;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result = instance.getChildren(concept);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getParents method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetParents() {
//        System.out.println("getParents");
//        Concept concept = null;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result = instance.getParents(concept);
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
//     * Test of getBasicLevelConcepts method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetBasicLevelConcepts() {
//        System.out.println("getBasicLevelConcepts");
//        Concept concept = null;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Concept> expResult = null;
//        List<Concept> result = instance.getBasicLevelConcepts(concept);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getBasicLevelConceptsOld method, of class ConceptDaoImpl.
//     */
//    @Test
//    public void testGetBasicLevelConceptsOld() {
//        System.out.println("getBasicLevelConceptsOld");
//        Concept concept = null;
//        ConceptDaoImpl instance = new ConceptDaoImpl();
//        List<Map.Entry<Concept, ConceptDaoImpl.Direction>> expResult = null;
//        List<Map.Entry<Concept, ConceptDaoImpl.Direction>> result =
//                instance.getBasicLevelConceptsOld(concept);
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

}
