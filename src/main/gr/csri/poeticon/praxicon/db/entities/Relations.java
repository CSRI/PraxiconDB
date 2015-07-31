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

/**
 *
 * @author dmavroeidis
 */
@XmlRootElement(name = "relations")
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlSeeAlso(Concept.class)

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
         1.1. If it is a concept, find it and store it with Concepts.storeConcept
         1.2. If it is a relation set, find it and store it using RelationSets.storeRelationSet
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
        Concept leftConcept = new Concept();
        Concept rightConcept = new Concept();
        Concepts concepts = new Concepts();

        System.out.println("Left Concept: " + relation.getLeftArgument().
                getConcept());
        System.out.println("Left Concept Language Representation: " +
                relation.getLeftArgument().getConcept().
                getLanguageRepresentationsEntries());
        System.out.println("Right Concept: " + relation.getRightArgument().
                getConcept());
        System.out.println("Left Concept Language Representation: " +
                relation.getRightArgument().getConcept().
                getLanguageRepresentationsEntries());
        if (relation.getLeftArgument().isConcept()) {
            leftConcept = cDao.getConcept(relation.
                    getLeftArgument().getConcept());
            if (isNull(leftConcept)) {
                leftConcept = relation.getLeftArgument().getConcept();
                System.out.println("Language Representations: " + leftConcept.
                        getLanguageRepresentationsNames());
                System.out.println("Persist " + leftConcept);
                cDao.persist(leftConcept);
            } else {
                System.out.println("Merge " + leftConcept);
                System.out.println("Language Representations: " + leftConcept.
                        getLanguageRepresentationsNames());
                cDao.merge(leftConcept);
            }

//            System.out.println("Relation's Left Concept: " + relation.
//                    getLeftArgument().getConcept());
//            leftConcept = relation.getLeftArgument().getConcept();
//            Concept retrievedLeftConcept = concepts.storeConcept(leftConcept);
//            if (isNull(retrievedLeftConcept)) {
//                leftArgument = new RelationArgument(leftConcept);
//            } else {
//                leftArgument = raDao.getRelationArgument(leftConcept);
//            }
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
            rightConcept = cDao.getConcept(relation.
                    getRightArgument().getConcept());
            System.out.println("Right Concept: " + rightConcept);
            if (isNull(rightConcept)) {
                rightConcept = relation.getRightArgument().getConcept();
                System.out.println("Persist " + rightConcept);
                cDao.persist(rightConcept);
            } else {
                System.out.println("Merge " + rightConcept);
                cDao.merge(rightConcept);
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
        if (isNull(leftArgument)) {
            System.out.println("LeftArgument: " + leftArgument +
                    " doesn't exist and will be created");
            leftArgument = new RelationArgument(leftConcept);
            raDao.persist(leftArgument);
        } else {
            raDao.merge(leftArgument);
        }
        if (isNull(rightArgument)) {
            System.out.println("RightArgument: " + rightArgument +
                    " doesn't exist and will be created");
            rightArgument = new RelationArgument(rightConcept);
            System.out.println("Right Relation Argument Id: " + rightArgument.
                    getId());
            raDao.persist(rightArgument);
        } else {
            raDao.merge(rightArgument);
        }

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
        boolean areRelated = rDao.areRelated(leftArgument, rightArgument);
        if (!areRelated) {
            // Create a new relation with new components
            System.out.println("Relation: " + relation +
                    " doesn't exist and will be created");
            Relation newRelation = new Relation();
            newRelation.setLeftArgument(leftArgument);
            newRelation.setRightArgument(rightArgument);
            newRelation.setRelationType(relationType);
            System.out.println("New Relation: " + newRelation);
            rDao.persist(newRelation);
        }
    }

}
