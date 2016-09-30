package gr.csri.poeticon.praxicon.db.entities;

import gr.csri.poeticon.praxicon.db.dao.RelationDao;
import gr.csri.poeticon.praxicon.db.dao.RelationTypeDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationDaoImpl;
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
            RelationDao rDao = new RelationDaoImpl();
            Relation newRelation;
            for (Relation relation : relations) {
                newRelation = storeRelation(relation);
                rDao.getEntityManager().clear();
                if (!isNull(newRelation)) {
                    System.out.println("Storing Relation: " + newRelation.
                            toString());
                }
            }
        }
    }

    public Relation storeRelation(Relation relation) {
        /*
         * Analyze relation:
         * 1. Get left and right argument and check if they exist in the DB.
         * 1.1. If it is a concept, find it.
         * 1.1.1. If the concept exists
         * 1.1.1.1. Get the concept from the DB.
         * 1.1.1.2. Get the relation argument from the DB. If it doesn't exist,
         * create it
         * 1.1.2. If the concept doesn't exist, store the concept in the DB,
         * create a relation argument with this concept and store that in
         * the DB also.
         * 1.2. If it is a relation set, find it and store it using
         * RelationSets.storeRelationSet
         * 2. Get relation type and check if it exists
         * 2.1. If it exists, merge it
         * 2.2. If it doesn't exist, persist it
         * 3. Now, check if the relation arguments are related
         * 3.1. If they are, don't do anything
         * 3.2. If they aren't, persist the relation
         */
        RelationTypeDao rtDao = new RelationTypeDaoImpl();
        RelationDao rDao = new RelationDaoImpl();
        RelationArgument newLeftRelationArgument;
        RelationArgument newRightRelationArgument;
        RelationSet leftRelationSet;
        RelationSet rightRelationSet;
        RelationSet retrievedLeftRelationSet;
        RelationSet retrievedRightRelationSet;
        Concepts newConceptsObject = new Concepts();
        RelationArguments newRelationArgumentsObject = new RelationArguments();
        RelationSets newRelationSetsObject = new RelationSets();
        Concept newLeftConcept;
        Concept newRightConcept;
        Concept leftConcept;
        Concept rightConcept;
        boolean isConcept;

        // 1.1. If it is a concept, find it.
        isConcept = relation.getLeftArgument().isConcept();
        if (isConcept) {
            leftConcept = relation.getLeftArgument().getConcept();
            newLeftConcept = newConceptsObject.storeConcept(leftConcept);
            newLeftRelationArgument = newRelationArgumentsObject.
                    storeRelationArgument(new RelationArgument(newLeftConcept));
        } else {
            leftRelationSet = relation.getLeftArgument().
                    getRelationSet();
            retrievedLeftRelationSet = newRelationSetsObject.storeRelationSet(
                    leftRelationSet);
            RelationArgument leftRelationArgument = new RelationArgument(
                    retrievedLeftRelationSet);
            newLeftRelationArgument = newRelationArgumentsObject.
                    storeRelationArgument(leftRelationArgument);
        }

        isConcept = relation.getRightArgument().isConcept();
        if (isConcept) {
            rightConcept = relation.getRightArgument().getConcept();
            newRightConcept = newConceptsObject.storeConcept(rightConcept);
            newRightRelationArgument = newRelationArgumentsObject.
                    storeRelationArgument(
                            new RelationArgument(newRightConcept));
        } else {
            rightRelationSet = relation.getRightArgument().
                    getRelationSet();
            retrievedRightRelationSet = newRelationSetsObject.
                    storeRelationSet(rightRelationSet);
            RelationArgument rightRelationArgument = new RelationArgument(
                    retrievedRightRelationSet);
            newRightRelationArgument = newRelationArgumentsObject.
                    storeRelationArgument(rightRelationArgument);
        }

        // 2. Get relation type
        RelationType retrievedRelationType = rtDao.
                getRelationTypeByForwardName(
                        relation.getRelationType().getForwardName());
        RelationType relationType = new RelationType();
        // 2.1 & 2.2
        if (!isNull(retrievedRelationType)) {
            rtDao.merge(retrievedRelationType);
            relationType = retrievedRelationType;
        } else {
            RelationType newRelationType = relation.getRelationType();
            for (RelationType.RelationNameForward rt
                    : RelationType.RelationNameForward.values()) {
                if (rt.equals(newRelationType.getForwardName())) {
                    rtDao.persist(newRelationType);
                    relationType = newRelationType;
                }
            }
        }
        Relation newRelation = new Relation();
        // 3. Check if related
        boolean areRelated = rDao.areRelated(newLeftRelationArgument,
                newRightRelationArgument);
        // Create a new relation with new components
        newRelation.setLeftArgument(newLeftRelationArgument);
        newRelation.setRightArgument(newRightRelationArgument);
        newRelation.setRelationType(relationType);
        newRelation.setInferred(relation.getInferred());
        newRelation.setLinguisticSupport(
                relation.getLinguisticallySupported());
        if (!areRelated) {
            rDao.persist(newRelation);
            return newRelation;
        } else {
            Relation retrievedRelation = rDao.getRelation(
                    newLeftRelationArgument, newRightRelationArgument,
                    relationType.getForwardName());
            // Check for existence
            if (!isNull(retrievedRelation)) {
                rDao.merge(retrievedRelation);
                return retrievedRelation;
            } else {
                rDao.persist(newRelation);
                return newRelation;
            }
        }
    }

}
