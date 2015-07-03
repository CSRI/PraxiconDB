package gr.csri.poeticon.praxicon.db.entities;

import gr.csri.poeticon.praxicon.db.dao.RelationDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationDaoImpl;
import java.util.ArrayList;
import java.util.List;
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
        RelationDao rDao = new RelationDaoImpl();
//        Relation retrievedRelation = rDao.getRelation(relation);
        
        
        
//        if (isNull(retrievedRelation)) {
//            System.out.println(retrievedRelation);
            Relation newRelation = rDao.updatedRelation(relation);
            rDao.merge(newRelation);
//        }
    }

//    public void storeRelation(Relation relation) {
//
//        RelationDao rDao = new RelationDaoImpl();
//        Relation oldRelation = new Relation(relation);
//        Relation newRelation = new Relation();
//
////        Relation retrievedRelation = rDao.
////                getRelation(new RelationArgument(relation.getLeftArgument()), new RelationArgument(relation.
////                        getRightArgument()), new RelationType(relation.getRelationType()).getForwardName());
////
////        // If relation does not exist in the database, store it.
////        if (!isNull(retrievedRelation)) {
////            // Create a new relation with the
////            newRelation = new Relation(retrievedRelation);
////        } else {
////            newRelation = oldRelation;
////        }
//        // Find both Relation Arugments in the DB.
//        // If they exist, attach them to the Relation.
//        // If they don't exist, create them.
//        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
//        ConceptDao cDao = new ConceptDaoImpl();
//        RelationSetDao rsDao = new RelationSetDaoImpl();
//        RelationArgument retrievedLeftRelationArgument = new RelationArgument();
//        RelationArgument retrievedRightRelationArgument = new RelationArgument();
//        RelationArgument newLeftRelationArgument = new RelationArgument();
//        RelationArgument newRightRelationArgument = new RelationArgument();
//        Concept retrievedRightConcept = new Concept();
//        Concept retrievedLeftConcept = new Concept();
//        // First, check the relation arguments are concept or relation set.
//        // And search for them in the database
//        // Left argument
//        if (oldRelation.getLeftArgument().isConcept()) {
//            retrievedLeftConcept = cDao.getConcept(
//                    oldRelation.getLeftArgument().getConcept());
//            if (!isNull(retrievedLeftConcept)) {
//                retrievedLeftRelationArgument = raDao.getRelationArgument(
//                        retrievedLeftConcept);
//            } else {
//                retrievedLeftRelationArgument = null;
//            }
//        } else {
//            RelationSet retrievedRelationSet = rsDao.getRelationSet(oldRelation.
//                    getLeftArgument().getRelationSet());
//            if (!isNull(retrievedRelationSet)) {
//                retrievedLeftRelationArgument = raDao.getRelationArgument(
//                        oldRelation.getLeftArgument().getRelationSet());
//            }
//        }
//        // Right argument
//        if (oldRelation.getRightArgument().isConcept()) {
//            retrievedRightConcept = cDao.getConcept(oldRelation.
//                    getRightArgument().getConcept());
//            if (!isNull(retrievedRightConcept)) {
//                retrievedRightRelationArgument = raDao.getRelationArgument(
//                        retrievedRightConcept);
//            }
//        } else {
//            RelationSet retrievedRelationSet = rsDao.getRelationSet(oldRelation.
//                    getLeftArgument().getRelationSet());
//            if (!isNull(retrievedRelationSet)) {
//                retrievedRightRelationArgument = raDao.getRelationArgument(
//                        oldRelation.getRightArgument().getRelationSet());
//            }
//        }
////
////        if (isNull(retrievedLeftRelationArgument)) {
////            System.out.println("Couldn't find left argument.");
////            newLeftRelationArgument = new RelationArgument(oldRelation.
////                    getLeftArgument());
////        } else {
////            System.out.println("Found left argument.");
////            newLeftRelationArgument = retrievedLeftRelationArgument;
////        }
////
////        if (isNull(retrievedRightRelationArgument)) {
////            System.out.println("Couldn't find right argument.");
////            newRightRelationArgument = new RelationArgument(oldRelation.
////                    getRightArgument());
////        } else {
////            System.out.println("Found right argument.");
////            newRightRelationArgument = retrievedRightRelationArgument;
////        }
//
//        // Check if two relation arguments are related
//        if (isNull(retrievedRightRelationArgument) &&
//                isNull(retrievedLeftRelationArgument)) {
//            // Relation arguments don't exist in the DB
//            // If concepts were found, then add the ids in the relation
//            if (isNull(retrievedLeftConcept) && isNull(retrievedRightConcept)) {
//                // Store concepts first and then the relation. WHY?
//                newRelation.setLeftArgument(newLeftRelationArgument);
//                newRelation.setRightArgument(newRightRelationArgument);
//                newRelation.setRelationType(relation.getRelationType());
//            }
//            if (!isNull(retrievedLeftConcept)) {
//                newLeftRelationArgument = raDao.getRelationArgument(
//                        retrievedLeftConcept);
//                if (isNull(retrievedLeftRelationArgument)) {
//                    newRelation.setLeftArgument(relation.getLeftArgument());
//                }
//            }
//            if (!isNull(retrievedRightConcept)) {
//                newRightRelationArgument = raDao.getRelationArgument(
//                        retrievedRightConcept);
//                if (isNull(retrievedRightRelationArgument)) {
//                    newRelation.setRightArgument(relation.getRightArgument());
//                }
//            }
//
//
//        } else {
//
//        }
//
//        rDao.merge(newRelation);
//
//        RelationType newRelationType = new RelationType();
//        newRelationType.setForwardName(
//                oldRelation.getRelationType().getForwardName());
//        newRelationType.setBackwardName(
//                oldRelation.getRelationType().getBackwardName());
//
////        raDao.merge(newLeftRelationArgument);
////        raDao.merge(newRightRelationArgument);
////        EntityMngFactory.getEntityManager().refresh(newLeftRelationArgument);
////        EntityMngFactory.getEntityManager().refresh(newRightRelationArgument);
//        newRelation.setLeftArgument(newLeftRelationArgument);
//        newRelation.setRightArgument(newRightRelationArgument);
//        newRelation.setRelationType(newRelationType);
////        rDao.merge(newRelation);
//
//        Relation retrievedRelation = rDao.getRelation(newRelation);
//
//        if (isNull(retrievedRelation)) {
//            newRelation = relation;
//        } else {
//            newRelation = retrievedRelation;
//        }
////
////        newRelation.setLeftArgument(newLeftRelationArgument);
////        newRelation.setRightArgument(newRightRelationArgument);
////        newRelation.setRelationType(relation.getRelationType());
////        newRelation.setLinguisticSupport(relation.getLinguisticallySupported());
//        rDao.merge(newRelation);
//    }
}
