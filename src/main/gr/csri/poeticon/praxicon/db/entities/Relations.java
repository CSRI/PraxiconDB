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
                System.out.println("\n\n\nStoring: " + relation.toString() +
                        "\n\n\n");
                storeRelation(relation);
            }
        }
    }

    public Relation storeRelation(Relation relation) {
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        ConceptDao cDao = new ConceptDaoImpl();
        RelationSetDao rsDao = new RelationSetDaoImpl();
        RelationTypeDao rtDao = new RelationTypeDaoImpl();
        RelationDao rDao = new RelationDaoImpl();

        /*
         Analyze relation:
         1. Get left and right argument and check if they exist in the DB.
         1.1. If it is a concept, find it.
         1.1.1. If the concept exists
         1.1.1.1. Get the concept from the DB.
         1.1.1.2. Get the relation argument from the DB. If it doesn't exist,
         create it
         1.1.2. If the concept doesn't exist, store the concept in the DB,
         create a relation argument with this concept and store that in
         the DB also.
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
        RelationArgument retrievedLeftRelationArgument = new RelationArgument();
        RelationArgument retrievedRightRelationArgument = new RelationArgument();
        RelationArgument newLeftRelationArgument = new RelationArgument();
        RelationArgument newRightRelationArgument = new RelationArgument();
        RelationSet retrievedLeftRelationSet = new RelationSet();
        RelationSet retrievedRightRelationSet = new RelationSet();
        RelationSet newLeftRelationSet = new RelationSet();
        RelationSet newRightRelationSet = new RelationSet();

        Concept leftConcept = new Concept();
        Concept rightConcept = new Concept();
//        Concepts concepts = new Concepts();

        System.out.println("Left Concept: " + relation.getLeftArgument().
                getConcept());

        // 1.1. If it is a concept, find it.
        if (relation.getLeftArgument().isConcept()) {
            leftConcept = relation.getLeftArgument().getConcept();
            Concept retrievedLeftConcept = cDao.getConcept(leftConcept);
            System.out.println("\nRetrieved Left Concept: " +
                    retrievedLeftConcept + "\n\n");
            if (!isNull(retrievedLeftConcept)) {
                // 1.1.1. If the concept exists
                System.out.println("Left Concept Found!");
                System.out.println("Merge " + retrievedLeftConcept);
                cDao.merge(retrievedLeftConcept);
                retrievedLeftRelationArgument = raDao.getRelationArgument(
                        retrievedLeftConcept);
                if (!isNull(retrievedLeftRelationArgument)) {
                    System.out.println("Left Argument Found!");
                    newLeftRelationArgument = retrievedLeftRelationArgument;
                    raDao.merge(newLeftRelationArgument);
                } else {
//                    cDao.persist(leftConcept);
                    newLeftRelationArgument = new RelationArgument(
                            retrievedLeftConcept);
                    System.out.println("New Left Relation Argument: " +
                            newLeftRelationArgument.getConcept().getId());
                    raDao.persist(newLeftRelationArgument);
                }
            } else {
//                System.out.println("Left Concept " + leftConcept + " not found");
                cDao.persist(leftConcept);
                newLeftRelationArgument = new RelationArgument(leftConcept);
                raDao.persist(newLeftRelationArgument);
            }
        } else {
            RelationSet leftRelationSet = relation.getLeftArgument().
                    getRelationSet();
            RelationSets newRelationSetsObject = new RelationSets();
            newRelationSetsObject.storeRelationSet(leftRelationSet);

//            retrievedLeftRelationSet = rsDao.getRelationSet(leftRelationSet);
//            if (!isNull(retrievedLeftRelationSet)) {
//                System.out.println("Left Relation Set Found!");
//                newLeftRelationSet = retrievedLeftRelationSet;
//                rsDao.merge(newLeftRelationSet);
//            } else {
//                newLeftRelationSet = leftRelationSet;
//                rsDao.persist(newLeftRelationSet);
//            }
        }

        if (relation.getRightArgument().isConcept()) {
            rightConcept = relation.getRightArgument().getConcept();
            Concept retrievedRightConcept = cDao.getConcept(rightConcept);
            if (!isNull(retrievedRightConcept)) {
                // 1.1.1. If the concept exists
                System.out.println("Right Concept Found!");
                System.out.println("Merge " + retrievedRightConcept);
                cDao.merge(retrievedRightConcept);
                retrievedRightRelationArgument = raDao.getRelationArgument(
                        retrievedRightConcept);
                if (!isNull(retrievedRightRelationArgument)) {
                    System.out.println("Right Argument Found!");
                    newRightRelationArgument = retrievedRightRelationArgument;
                    raDao.merge(newRightRelationArgument);
                } else {
                    newRightRelationArgument =
                            new RelationArgument(retrievedRightConcept);
                    raDao.persist(newRightRelationArgument);
                }
            } else {
                System.out.println("Right Concept " + rightConcept +
                        " not found");
                cDao.persist(rightConcept);
                newRightRelationArgument = new RelationArgument(rightConcept);
                raDao.persist(newRightRelationArgument);
            }
        } else if (relation.getRightArgument().isRelationSet()) {
            RelationSet rightRelationSet = relation.getRightArgument().
                    getRelationSet();
            RelationSets newRelationSetsObject = new RelationSets();
            newRelationSetsObject.storeRelationSet(rightRelationSet);

//            // TODO: Do the same for relations sets
//            RelationSet rightRelationSet = relation.getRightArgument().
//                    getRelationSet();
//            retrievedRightRelationSet = rsDao.getRelationSet(rightRelationSet);
//            if (!isNull(retrievedRightRelationSet)) {
//                System.out.println("Right Relation Set Found!");
//                newRightRelationSet = retrievedRightRelationSet;
//                rsDao.merge(newRightRelationSet);
//            } else {
//                newRightRelationSet = rightRelationSet;
//                rsDao.persist(newRightRelationSet);
//            }
//
        }

        // 2. Get relation type
        RelationType relationType = new RelationType();
        relationType = rtDao.getRelationTypeByForwardName(
                relation.getRelationType().getForwardName());
        // 2.1 & 2.2
        if (relationType == null) {
            System.out.println("RelationType: " + relation.getRelationType() +
                    " doesn't exist and will be created");
            relationType = relation.getRelationType();
            rtDao.persist(relationType);
        } else {
            rtDao.merge(relationType);
        }
        Relation newRelation = new Relation();
        // 3. Check if related
        System.out.println("New Left Relation Argument in areRelated: " +
                newLeftRelationArgument.getId());
        System.out.println("New Right Relation Argument in areRelated: " +
                newRightRelationArgument.getId());
        boolean areRelated = rDao.areRelated(newLeftRelationArgument,
                newRightRelationArgument);
        // Create a new relation with new components
        System.out.println("Relation: " + relation +
                " doesn't exist and will be created");
        System.out.println("New Left Argument from Relations: " +
                newLeftRelationArgument.getConcept());
        newRelation.setLeftArgument(newLeftRelationArgument);
        newRelation.setRightArgument(newRightRelationArgument);
        System.out.println(relationType);
        newRelation.setRelationType(relationType);
        System.out.println("New Relation from Relations: " + newRelation);
        if (!areRelated) {
            rDao.persist(newRelation);
        } else {
//            System.out.println("Relation " + newRelation + " exists.");
//            Relation retrievedRelation = rDao.getRelation(newRelation);
//            System.out.println("Retrieved relation from StoreRelation: " +
//                    retrievedRelation);
            rDao.merge(newRelation);
            return newRelation;
        }
        System.out.println("NEW RELATION from StoreRelation --> " + newRelation);
        return newRelation;

    }

}
