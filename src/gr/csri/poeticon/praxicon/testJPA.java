package gr.csri.poeticon.praxicon;

import gr.csri.poeticon.praxicon.db.dao.ConceptDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.IntersectionOfRelationChains;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationChain;
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
 * @author Erevodifwntas
 * @author Dimitris Mavroeidis
 *
 * An XML of how we can create new concepts in the db, without using XMLs. Some
 * parts are obsolete
 */
public class testJPA {

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
         * Creating an entity
         */
        Concept concept1 = new Concept();

        concept1.setName("concept1");
        concept1.setConceptType(Concept.type.ABSTRACT);
        concept1.setStatus(Concept.status.CONSTANT);
        concept1.setPragmaticStatus(Concept.pragmatic_status.FIGURATIVE);
        concept1.setUniqueInstance(Concept.unique_instance.YES);
        System.out.println(concept1);

        //////////////////////
        // How to add an LR //
        //////////////////////
        //1.) create the LR
        List<LanguageRepresentation> lr;
        lr = new ArrayList<>();

        //2.) create the lexical entries (language representations)
        LanguageRepresentation le = new LanguageRepresentation();
        le.setLanguage(LanguageRepresentation.language.EN);
        le.setText("test");
        le.setPartOfSpeech(LanguageRepresentation.part_of_speech.NOUN);
        LanguageRepresentation le2 = new LanguageRepresentation();
        le2.setLanguage(LanguageRepresentation.language.EN);
        le2.setText("test");
        le2.setPartOfSpeech(LanguageRepresentation.part_of_speech.VERB);

        //3.) add the entries to the Langage Representation
        lr.add(le);
        lr.add(le2);

        //4.) add the Language Representation to the Concept
        for (int i = 0; i < 2; i++) {
            concept1.getLanguageRepresentations().add(lr.get(i));
        }

        //////////////////////////
        //How to add a Relation //
        //////////////////////////
        //1.) Create the IntersectionOfRelations
        IntersectionOfRelationChains inter = new IntersectionOfRelationChains();

        //2.) Create the Relation Chain of the relation
        RelationChain rChain = new RelationChain();

        //3.) Create the relation for this relation chain
        Relation r1 = new Relation();

        //4.a.) Create the type of the relation
        RelationType rType = new RelationType();
        rType.setForwardName(RelationType.relation_name_forward.HAS_PART);
        rType.setBackwardName(RelationType.relation_name_backward.PART_OF);

        //4.b) Set the type to the relation
        r1.setTypeSimple(rType);

        //4.c) set the object and the subject of the relation
        //(if the concepts do not exist you should create them -just set their name
        //the name should be unique -it works like their ID)
        Concept concept2 = new Concept();
        concept2.setName("concept2");
        concept2.setConceptType(Concept.type.MOVEMENT);
        concept2.setStatus(Concept.status.VARIABLE);
        concept2.setUniqueInstance(Concept.unique_instance.NO);
        concept2.setPragmaticStatus(Concept.pragmatic_status.LITERAL);
        r1.setObject(concept2);
        r1.setSubject(concept1);
        r1.setDerivation(Relation.derivation_supported.YES);

        //5.) Add the relation to the chain
        //the second argument is the order of the relation in the chain
        rChain.addRelation(r1, 0);

        //6.) add the chain to the Intersection
        inter.getRelationChains().add(rChain);

        //7.) add the intersection to the Union
        //union.getIntersections().add(inter);
        //8.) add the union to the Concept
        //con.addRelation(union);
        ////////////////////////////////////////
        // How to add a Visual Representation //
        ////////////////////////////////////////
        VisualRepresentation vr = new VisualRepresentation();
        vr.setRepresentation("this is a test VR");
        vr.setMediaType(VisualRepresentation.media_type.IMAGE);
        URI new_uri = null;
        try {
            new_uri = new URI("https://www.google.com/");
        } catch (URISyntaxException error_uri) {
            System.out.println("caught URI error");
            System.out.println(Arrays.toString(error_uri.getStackTrace()));
        };
        vr.setURI(new_uri);
        concept1.addVisualRepresentation(vr);

        //Adding a relation
        //1.) create the TypeOfRelation (or use an existing 1)
        RelationType type = new RelationType();
        type.setForwardName(
                RelationType.relation_name_forward.HAS_PARTIAL_INSTANCE);
        type.setBackwardName(RelationType.relation_name_backward.PART_OF);

        // 2.) create the relations (always there should be a type and an
        // Object for the relation)
        Relation rel = new Relation();
        rel.setType(type);
        rel.setObject(concept1);
        // here is an example for another relation
        Relation rel2 = new Relation();
        rel2.setType(type);
        rel2.setObject(concept2);

        //3.) create the relation chain
        RelationChain rc = new RelationChain();
        //there should be in the correct order (the order counter must start from 0)
        rc.addRelation(rel, 0);
        rc.addRelation(rel2, 1);

        // 4.) create the intersection
        IntersectionOfRelationChains inter1 = new IntersectionOfRelationChains();
        inter1.addRelationChain(rc);

        // 5.) add the intersection to the concept
        concept1.addIntersectionOfRelationChains(inter);
        // Causes java.lang.StackOverflowError
        //concept2.addIntersectionOfRelationChains(inter1);

        // 6.) create the Concept_UnionRelation
        // here you should determine if the union is describing the basic use of
        // the object or how important is this use for the entity
        concept1.setConceptType(Concept.type.ABSTRACT);
        concept1.setPragmaticStatus(Concept.pragmatic_status.FIGURATIVE);
        concept1.setStatus(Concept.status.VARIABLE);
        concept1.setUniqueInstance(Concept.unique_instance.YES);

        ConceptDao new_concept_dao = new ConceptDaoImpl();

        try {
            new_concept_dao.persist(concept1);
        } catch (javax.validation.ConstraintViolationException ee) {
            System.out.println("Size constraint violated.");
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        Set<ConstraintViolation<Concept>> violations = validator.validate(
                concept1);
        Set<ConstraintViolation<Concept>> violation = validator.validate(
                concept2);

        System.out.println(concept2.getName());
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
            em.close();
        }
    }

//    public void testViolations()
//    {
//        Concept con = new Concept();
//    }
}
