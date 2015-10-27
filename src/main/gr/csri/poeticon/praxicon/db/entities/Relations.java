package gr.csri.poeticon.praxicon.db.entities;

import gr.csri.poeticon.praxicon.db.dao.ConceptDao;
import gr.csri.poeticon.praxicon.db.dao.RelationArgumentDao;
import gr.csri.poeticon.praxicon.db.dao.RelationDao;
import gr.csri.poeticon.praxicon.db.dao.RelationSetDao;
import gr.csri.poeticon.praxicon.db.dao.RelationTypeDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationArgumentDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationSetDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationTypeDaoImpl;
import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.isNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author dmavroeidis
 */
@XmlRootElement(name = "relations")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(Concept.class)

public class Relations {

    @XmlElement(name = "relation")
    List<Relation> relations = new ArrayList<>();

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    public Relations() {
        relations = new ArrayList<>();
    }

    /**
     * Stores all relations of the collection in the database updating
     * same name entries
     */
    public void storeRelations() {
        if (!relations.isEmpty()) {
            for (Relation relation : relations) {
                System.out.println("\n\n\n" + relation.toString() + "\n\n\n");
//                System.out.println("\nLanguage Representation of Left Concept from storeRelations: " + relation.getLeftArgument().getConcept().getLanguageRepresentationName() + "\n\n\n");
//                System.out.println("\nLanguage Representation of Right Concept from storeRelations: " + relation.getRightArgument().getConcept().getLanguageRepresentationName() + "\n\n\n");
                storeRelation(relation);
            }
        }
    }

    public void storeRelation(Relation relation) {
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        ConceptDao cDao = new ConceptDaoImpl();
        RelationSetDao rsDao = new RelationSetDaoImpl();
        RelationTypeDao rtDao = new RelationTypeDaoImpl();
        RelationDao rDao = new RelationDaoImpl();

        /*
         Analyze relation:
         1. Get left and right argument and check if they exist in the DB.
         1.1. If it is a concept, find it.
         1.1.1. If it exists, get the object from the DB.
         1.1.2. If it doesn't, store the concept in the DB, create a
         relation argument with this concept and store that in the DB
         also.
         1.2. If it is a relation set, find it and store it using
         RelationSets.storeRelationSet
         2. Get relation type and check if it exists
         2.1. If it exists, merge it
         2.2. If it doesn't exist, persist it
         3. Now, check if the relation arguments are related
         3.1. If they are, don't do anything
         3.2. If they aren't, persist the relation
         */
        // 1. Get left & right argument
        RelationArgument leftArgument = new RelationArgument();
        RelationArgument rightArgument = new RelationArgument();
        RelationArgument newLeftRelationArgument = new RelationArgument();
        RelationArgument newRightRelationArgument = new RelationArgument();

        Concept leftConcept = new Concept();
        Concept rightConcept = new Concept();
        Concepts concepts = new Concepts();

        System.out.println("Left Concept: " + relation.getLeftArgument().
                getConcept());
//        System.out.println("Left Concept Language Representation: " +
//                relation.getLeftArgument().getConcept().
//                getLanguageRepresentationsEntries());
//        System.out.println("Right Concept: " + relation.getRightArgument().
//                getConcept());
//        System.out.println("Right Concept Language Representation: " +
//                relation.getRightArgument().getConcept().
//                getLanguageRepresentationsEntries());

        if (relation.getLeftArgument().isConcept()) {
//            Concept newLeftConcept = concepts.storeConcept(leftConcept);
            leftConcept = relation.getLeftArgument().getConcept();
            Concept retrievedLeftConcept = cDao.getConcept(relation.
                    getLeftArgument().getConcept());
            if (isNull(retrievedLeftConcept)) {
//                System.out.println("Language Representations: " + leftConcept.
//                        getLanguageRepresentationsNames());
                System.out.println("Persist " + leftConcept);
                cDao.persist(leftConcept);

                newLeftRelationArgument = new RelationArgument(leftConcept);
                raDao.persist(newLeftRelationArgument);
            } else {
                System.out.println("Merge " + leftConcept);
                newLeftRelationArgument = raDao.getRelationArgument(leftConcept);
                cDao.merge(leftConcept);
            }
        } else {
            System.out.println("Right Concept: " + relation.getRightArgument().
                    getConcept());
            leftArgument = raDao.getRelationArgument(relation.
                    getLeftArgument().getRelationSet());
            System.out.println("Right Concept: " + relation.getRightArgument().
                    getConcept());
        }

        if (relation.getRightArgument().isConcept()) {
            System.out.println("Right Concept: " + relation.getRightArgument().
                    getConcept());
//            Concept newRightConcept = concepts.storeConcept(rightConcept);
            rightConcept = relation.getRightArgument().getConcept();
            Concept retrievedRightConcept = cDao.getConcept(rightConcept);
            System.out.println("Right Concept: " + rightConcept);
            if (isNull(retrievedRightConcept)) {
//                rightConcept = relation.getRightArgument().getConcept();
                System.out.println("Persist " + rightConcept);
//                cDao.persist(rightConcept);
                concepts.storeConcept(rightConcept);
                newRightRelationArgument = new RelationArgument(rightConcept);
                raDao.persist(newRightRelationArgument);
            } else {
                System.out.println("Get right concept's relation argument, " +
                        "as concept " + rightConcept + " already exists.");
                newRightRelationArgument = raDao.getRelationArgument(
                        retrievedRightConcept);
            }
//
//             System.out.println("Relation's Right Concept: " + relation.
//                    getRightArgument().getConcept());
//            rightConcept = relation.getRightArgument().getConcept();
//            Concept retrievedRightConcept = concepts.storeConcept(rightConcept);
//            if (isNull(retrievedRightConcept)) {
//                rightArgument = new RelationArgument(rightConcept);
//            } else {
//                rightArgument = raDao.getRelationArgument(rightConcept);
//            }
        } else {
            rightArgument = raDao.getRelationArgument(relation.
                    getRightArgument().getRelationSet());
        }

        // 1.1 & 1.2
//        if (isNull(newLeftRelationArgument)) {
//            System.out.println("LeftArgument: " + leftArgument +
//                    " doesn't exist and will be created");
//            leftArgument = new RelationArgument(leftConcept);
//            raDao.persist(leftArgument);
//        } else {
//            raDao.merge(leftArgument);
//        }
//        if (isNull(newRightRelationArgument)) {
//            System.out.println("RightArgument: " + rightArgument +
//                    " doesn't exist and will be created");
//            rightArgument = new RelationArgument(rightConcept);
//            System.out.println("Right Relation Argument Id: " + rightArgument.
//                    getId());
//            raDao.persist(rightArgument);
//        } else {
//            raDao.merge(rightArgument);
//        }
        // 2. Get relation type
        RelationType relationType = new RelationType();
        relationType = rtDao.getRelationTypeByForwardName(
                relation.getRelationType().getForwardName());
        // 2.1 & 2.2
        if (isNull(relationType)) {
            System.out.println("RelationType: " + relation.getRelationType() +
                    " doesn't exist and will be created");
            rtDao.persist(relation.getRelationType());
        } else {
            rtDao.merge(relationType);
        }

        // 3. Check if related
        boolean areRelated = rDao.areRelated(newLeftRelationArgument,
                newRightRelationArgument);
        if (!areRelated) {
            // Create a new relation with new components
            System.out.println("Relation: " + relation +
                    " doesn't exist and will be created");
            Relation newRelation = new Relation();
            newRelation.setLeftArgument(newLeftRelationArgument);
            newRelation.setRightArgument(newRightRelationArgument);
            newRelation.setRelationType(relationType);
            System.out.println("New Relation: " + newRelation);
            rDao.persist(newRelation);
        } else {
            System.out.println("Relation " + relation + " exists.");
        }
    }

}
