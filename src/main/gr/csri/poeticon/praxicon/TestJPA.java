package gr.csri.poeticon.praxicon;

import gr.csri.poeticon.praxicon.db.dao.ConceptDao;
import gr.csri.poeticon.praxicon.db.dao.LanguageRepresentationDao;
import gr.csri.poeticon.praxicon.db.dao.RelationArgumentDao;
import gr.csri.poeticon.praxicon.db.dao.RelationDao;
import gr.csri.poeticon.praxicon.db.dao.RelationSetDao;
import gr.csri.poeticon.praxicon.db.dao.RelationTypeDao;
import gr.csri.poeticon.praxicon.db.dao.VisualRepresentationDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.LanguageRepresentationDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationArgumentDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationSetDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationTypeDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.VisualRepresentationDaoImpl;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
import gr.csri.poeticon.praxicon.db.entities.RelationSet;
import gr.csri.poeticon.praxicon.db.entities.RelationType;
import gr.csri.poeticon.praxicon.db.entities.VisualRepresentation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.BeforeClass;

/**
 *
 * @author dmavroeidis
 *
 * An XML of how we can create new concepts in the db, without using XMLs. Some
 * parts are obsolete
 */
public class TestJPA {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private static final String PERSISTENCE_UNIT_NAME = "PraxiconDBPU";
    private EntityManagerFactory factory;

    @SuppressWarnings("empty-statement")
    public static void main(String args[]) {
        /*
         * Create Concept1
         */

        Concept concept1 = new Concept();
        concept1.setExternalSourceId("concept1");
        concept1.setConceptType(Concept.Type.ABSTRACT);
        concept1.setStatus(Concept.Status.CONSTANT);
        concept1.setUniqueInstance(Concept.UniqueInstance.YES);
        concept1.setSource("myMind");
        System.out.println(concept1.getExternalSourceId());

        /*
         * Create Concept2
         */
        Concept concept2 = new Concept();
        concept2.setExternalSourceId("concept2");
        concept2.setConceptType(Concept.Type.MOVEMENT);
        concept2.setStatus(Concept.Status.VARIABLE);
        concept2.setUniqueInstance(Concept.UniqueInstance.NO);
        concept2.setSource("myMind2");
        System.out.println(concept2.getExternalSourceId());

        /*
         * Add Language Representations to the concepts
         */
        List<LanguageRepresentation> languageRepresentations1;
        languageRepresentations1 = new ArrayList<>();

        LanguageRepresentation languageRepresentation1 =
                new LanguageRepresentation();
        languageRepresentation1.setLanguage(LanguageRepresentation.Language.EN);
        languageRepresentation1.setText("LR1");
        languageRepresentation1.setPartOfSpeech(
                LanguageRepresentation.PartOfSpeech.NOUN);
        languageRepresentation1.setPragmaticStatus(
                LanguageRepresentation.UseStatus.FIGURATIVE);

        LanguageRepresentation languageRepresentation2 =
                new LanguageRepresentation();
        languageRepresentation2.setLanguage(LanguageRepresentation.Language.EN);
        languageRepresentation2.setText("LR2");
        languageRepresentation2.setPartOfSpeech(
                LanguageRepresentation.PartOfSpeech.VERB);
        languageRepresentation2.setPragmaticStatus(
                LanguageRepresentation.UseStatus.FIGURATIVE);

        LanguageRepresentation languageRepresentation3 =
                new LanguageRepresentation();
        languageRepresentation3.setLanguage(LanguageRepresentation.Language.EL);
        languageRepresentation3.setText("LR3");
        languageRepresentation3.setPartOfSpeech(
                LanguageRepresentation.PartOfSpeech.ADVERB);
        languageRepresentation3.setPragmaticStatus(
                LanguageRepresentation.UseStatus.LITERAL);

        concept1.addLanguageRepresentation(languageRepresentation1, false);
        concept1.addLanguageRepresentation(languageRepresentation2, true);
        concept2.addLanguageRepresentation(languageRepresentation3, true);

        /* 
         * Add Visual Representations to the concepts
         */
        VisualRepresentation visualRepresentation1 = new VisualRepresentation();
        visualRepresentation1.setName("VR1");
        visualRepresentation1.
                setMediaType(VisualRepresentation.MediaType.IMAGE);
        URI new_uri = null;
        try {
            new_uri = new URI(
                    "http://iphonewallpaperhds.com/images/1810-lucky.jpg");
        } catch (URISyntaxException error_uri) {
            System.out.println("caught URI error");
            System.out.println(Arrays.toString(error_uri.getStackTrace()));
        };
        visualRepresentation1.setURI(new_uri);
        concept1.addVisualRepresentation(visualRepresentation1);

        VisualRepresentation visualRepresentation2 = new VisualRepresentation();
        visualRepresentation2.setName("VR2");
        visualRepresentation2.
                setMediaType(VisualRepresentation.MediaType.IMAGE);
        try {
            new_uri = new URI(
                    "http://www.picgifs.com/clip-art/cartoons/lucky-luke/" +
                    "clip-art-lucky-luke-240603.jpg");
        } catch (URISyntaxException error_uri) {
            System.out.println("caught URI error");
            System.out.println(Arrays.toString(error_uri.getStackTrace()));
        };
        visualRepresentation2.setURI(new_uri);
        concept2.addVisualRepresentation(visualRepresentation2);

        /* 
         * Create a relation with concept1 as leftArgument and concept2 as 
         * rightArgument.
         */
        Relation relation1 = new Relation();
        RelationType relationType1 = new RelationType();
        relationType1.
                setForwardName(RelationType.RelationNameForward.HAS_PART);
        relationType1.setBackwardName(
                RelationType.RelationNameBackward.PART_OF);
        relation1.setType(relationType1);

        RelationArgument relationArgument1 = new RelationArgument(concept1);
        RelationArgument relationArgument2 = new RelationArgument(concept2);
        relation1.setLeftArgument(relationArgument1);
        relation1.setRightArgument(relationArgument2);
        relation1.setLinguisticSupport(Relation.LinguisticallySupported.YES);

        /* 
         * Create a relation with concept2 as leftArgument and concept1 as 
         * rightArgument.
         */
        Relation relation2 = new Relation();
        RelationType relationType2 = new RelationType();
        relationType2.setForwardName(
                RelationType.RelationNameForward.HAS_INSTANCE);
        relationType2.setBackwardName(
                RelationType.RelationNameBackward.INSTANCE_OF);
        relation2.setType(relationType2);

        relation2.setLeftArgument(relationArgument2);
        relation2.setRightArgument(relationArgument1);
        relation2.setLinguisticSupport(Relation.LinguisticallySupported.NO);

        System.out.println("RELATION1 ID (pre-persist): " + relation1.getId());
        System.out.println("RELATION2 ID (pre-persist):" + relation2.getId());

        /* 
         * Create an unordered RelationSet 
         */
        RelationSet relationSet1 = new RelationSet();
        relationSet1.addLanguageRepresentation(languageRepresentation3);
        relationSet1.addLanguageRepresentation(languageRepresentation1);

        relationSet1.setName("NewRelationSet1");
        relationSet1.addRelation(relation1);
        relationSet1.addRelation(relation2);

        /* 
         * Create an ordered RelationSet 
         */
        RelationSet relationSet2 = new RelationSet();
        List<LanguageRepresentation> languageRepresentations2 =
                new ArrayList<>();
        languageRepresentations2.add(languageRepresentation3);
        relationSet2.setLanguageRepresentations(languageRepresentations2);
        relationSet2.setName("NewRelationSet2");
        relationSet2.addRelation(relation2);
        relationSet2.addRelation(relation1);

        ConceptDao newConceptDao = new ConceptDaoImpl();
        RelationArgumentDao newRelationArgumentDao =
                new RelationArgumentDaoImpl();
        RelationTypeDao newRelationTypeDao = new RelationTypeDaoImpl();
        LanguageRepresentationDao newLanguageRepresentationDao =
                new LanguageRepresentationDaoImpl();
        VisualRepresentationDao newVisualRepresentationDao =
                new VisualRepresentationDaoImpl();
        RelationSetDao newRelationSetDao = new RelationSetDaoImpl();
        RelationDao newRelationDao = new RelationDaoImpl();

        try {
            newRelationSetDao.persist(relationSet1);
            newRelationSetDao.persist(relationSet2);
        } catch (javax.validation.ConstraintViolationException ee) {
            System.out.println("Size constraint violated.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        Set<ConstraintViolation<Concept>> violations = validator.validate(
                concept1);
        Set<ConstraintViolation<Concept>> violation = validator.validate(
                concept2);

        System.out.println(concept2.getExternalSourceId());
//        constraintViolations = validator.validate( concept1 );
//        assertEquals( 1, constraintViolations.size() );
//        assertEquals( "may not be null", constraintViolations.iterator().next().getMessage() );

    }

    public void persist(Object object) {
        EntityManagerFactory emf = javax.persistence.Persistence.
                createEntityManagerFactory("PraxiconDBPU");
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

//    public void testViolations()
//    {
//        Concept con = new Concept();
//    }
}
