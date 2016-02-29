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

        // 1.1. If it is a concept, find it.
        if (relation.getLeftArgument().isConcept()) {
            leftConcept = relation.getLeftArgument().getConcept();
            Concept retrievedLeftConcept = cDao.getConcept(leftConcept);
            if (!isNull(retrievedLeftConcept)) {
                // 1.1.1. If the concept exists
                cDao.merge(retrievedLeftConcept);
                retrievedLeftRelationArgument = raDao.getRelationArgument(
                        retrievedLeftConcept);
                if (!isNull(retrievedLeftRelationArgument)) {
                    newLeftRelationArgument = retrievedLeftRelationArgument;
                    raDao.merge(newLeftRelationArgument);
                } else {
//                    cDao.persist(leftConcept);
                    newLeftRelationArgument = new RelationArgument(
                            retrievedLeftConcept);
                    raDao.persist(newLeftRelationArgument);
                }
            } else {
                cDao.persist(leftConcept);
                newLeftRelationArgument = new RelationArgument(leftConcept);
                raDao.persist(newLeftRelationArgument);
            }
        } else {
            RelationSet leftRelationSet = relation.getLeftArgument().
                    getRelationSet();
            RelationSets newRelationSetsObject = new RelationSets();
            newRelationSetsObject.storeRelationSet(leftRelationSet);
        }

        if (relation.getRightArgument().isConcept()) {
            rightConcept = relation.getRightArgument().getConcept();
            Concept retrievedRightConcept = cDao.getConcept(rightConcept);
            if (!isNull(retrievedRightConcept)) {
                // 1.1.1. If the concept exists
                cDao.merge(retrievedRightConcept);
                retrievedRightRelationArgument = raDao.getRelationArgument(
                        retrievedRightConcept);
                if (!isNull(retrievedRightRelationArgument)) {
                    newRightRelationArgument = retrievedRightRelationArgument;
                    raDao.merge(newRightRelationArgument);
                } else {
                    newRightRelationArgument =
                            new RelationArgument(retrievedRightConcept);
                    raDao.persist(newRightRelationArgument);
                }
            } else {
                cDao.persist(rightConcept);
                newRightRelationArgument = new RelationArgument(rightConcept);
                raDao.persist(newRightRelationArgument);
            }
        } else if (relation.getRightArgument().isRelationSet()) {
            RelationSet rightRelationSet = relation.getRightArgument().
                    getRelationSet();
            RelationSets newRelationSetsObject = new RelationSets();
            newRelationSetsObject.storeRelationSet(rightRelationSet);
        }

        // 2. Get relation type
        RelationType relationType = new RelationType();
        relationType = rtDao.getRelationTypeByForwardName(
                relation.getRelationType().getForwardName());

        // 2.1 & 2.2
        if (relationType == null) {
            relationType = relation.getRelationType();
            rtDao.persist(relationType);
        } else {
            rtDao.merge(relationType);
        }
        Relation newRelation = new Relation();
        // 3. Check if related
        boolean areRelated = rDao.areRelated(newLeftRelationArgument,
                newRightRelationArgument);
        // Create a new relation with new components
        newRelation.setLeftArgument(newLeftRelationArgument);
        newRelation.setRightArgument(newRightRelationArgument);
        newRelation.setRelationType(relationType);
        if (!areRelated) {
            rDao.persist(newRelation);
        } else {
            Relation retrievedRelation = rDao.getRelation(
                    newLeftRelationArgument, newRightRelationArgument,
                    relationType.getForwardName());
            rDao.merge(retrievedRelation);
            return retrievedRelation;
        }
        return newRelation;

    }

}
