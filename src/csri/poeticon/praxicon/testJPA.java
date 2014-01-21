package csri.poeticon.praxicon;

import csri.poeticon.praxicon.db.dao.ConceptDao;
import csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.IntersectionOfRelationChains;
import csri.poeticon.praxicon.db.entities.LanguageRepresentation;
import csri.poeticon.praxicon.db.entities.MotoricRepresentation;
import csri.poeticon.praxicon.db.entities.Relation;
import csri.poeticon.praxicon.db.entities.RelationChain;
import csri.poeticon.praxicon.db.entities.RelationType;
import csri.poeticon.praxicon.db.entities.VisualRepresentation;
import java.util.List;
import java.util.ArrayList;
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

    
    private static final String PERSISTENCE_UNIT_NAME = "testingJPAwithXMLPU";
    private EntityManagerFactory factory;

    public static void main(String args[])
    {

        /*
         * Creating an entity
         */
        Concept con = new Concept();
        //All concepts should have a name
        con.setName("testConcept2");

        //////////////////////
        // How to add an LR //
        //////////////////////
        //1.) create the LR
        List<LanguageRepresentation> lr = new ArrayList<LanguageRepresentation>();
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
        for (int i=0; i<2; i++){
            con.getLanguageRepresentations().add(lr.get(i));
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
        rType.setForwardName(RelationType.relation_name.PART_OF);
        rType.setBackwardName(RelationType.relation_name.HAS_PART);
        //4.b) Set the type to the relation
        r1.setTypeSimple(rType);
        //4.c) set the object and the subject of the relation
        //(if the concepts do not exist you should create them -just set their name
        //the name should be unique -it works like their ID)
        Concept objectTest = new Concept();
        objectTest.setName("objectTestConcept");

        r1.setObject(objectTest);
        r1.setSubject(con);
        //5.) Add the relation to the chain
        //the second argument is the order of the relation in the chain
        rChain.addRelation(r1, 0);
        //6.) add the chain to the Intersection
        inter.getRelations().add(rChain);
        //7.) add the intersection to the Union
        //union.getIntersections().add(inter);
        //8.) add the union to the Concept
        //con.addRelation(union);

        ////////////////////////////////////////
        // How to add a Visual Representation //
        ////////////////////////////////////////
        VisualRepresentation vr =new VisualRepresentation();
        vr.setRepresentation("this is a test VR");
        vr.setMediaType(VisualRepresentation.media_type.IMAGE);
        con.addVisualRepresentation(vr);
        //Adding a relation
        //1.) create the TypeOfRelation (or use an existing 1)
        RelationType type = new RelationType();
        type.setForwardName(RelationType.relation_name.PART_OF);
        type.setBackwardName(RelationType.relation_name.PARTIAL_INSTANCE_OF);
        // 2.) create the relations (always there should be a type and an
        // Object for the relation)
        Relation rel = new Relation();
        rel.setType(type);
        rel.setObject(con);
        // here is an example for another relation
        Relation rel2 = new Relation();
        rel2.setType(type);
        rel2.setObject(objectTest);
        //3.) create the relation chain
        RelationChain rc = new RelationChain();
        //there should be in the correct order (the order counter must start from 0)
        rc.addRelation(rel, 0);
        rc.addRelation(rel2, 1);
        // 4.) create the intersection
        IntersectionOfRelationChains inter1 = new IntersectionOfRelationChains();
        inter1.addRelationChain(rc);
        // 5.) add the intersection to the concept
        con.addIntersectionOfRelationChains(inter);
        // Causes java.lang.StackOverflowError
        //objectTest.addIntersectionOfRelationChains(inter1);
        try{
            Set<ConstraintViolation<Concept>> constraintViolations = validator.validate(con);
            System.out.println("Size constraint violated.");
        }
        catch (java.lang.NullPointerException e)
        {
            System.out.println("No violation.");
        }
        // 6.) create the Concept_UnionRelation
        // here you should determine if the union is describing the basic use of
        // the object or how important is this use for the entity
        ConceptDao conceptDaoDao = new ConceptDaoImpl();
        conceptDaoDao.merge(con);
    }


    public void persist(Object object)
    {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("testingJPAwithXMLPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try
        {
            em.merge(object);
            em.getTransaction().commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
        finally
        {
            em.close();
        }
    }

//    public void testViolations()
//    {
//        Concept con = new Concept();
//    }
}
