
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import gr.csri.poeticon.praxicon.db.dao.RelationSetDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationSetDaoImpl;
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
@XmlRootElement(name = "relationSets")
@XmlAccessorType(XmlAccessType.FIELD)
public class RelationSets {

    @XmlElement(name = "relationSet")
    List<RelationSet> relationSets = new ArrayList<>();

    public List<RelationSet> getRelationSets() {
        return relationSets;
    }

    public void setRelationSets(List<RelationSet> relationSets) {
        this.relationSets = relationSets;
    }

    public RelationSets() {
        relationSets = new ArrayList<>();
    }

    /**
     * Stores all concepts of the collection in the database updating same name
     * entries
     */
    public void storeRelationSets() {
        if (!relationSets.isEmpty()) {
            RelationSetDao rsDao = new RelationSetDaoImpl();
            RelationSet newRelationSet;
            for (RelationSet relationSet : relationSets) {
                newRelationSet = storeRelationSet(relationSet);
                rsDao.getEntityManager().clear();
                System.out.println("Storing Relation Set: " + newRelationSet.
                        toString());
            }
        }
    }

    public RelationSet storeRelationSet(RelationSet relationSet) {

        /*
         Analyze relation set:
         0. Create a new Relation Set.
         1. Check if the Relation Set exists in the database.
         1.a. If it exists, merge it and return it.
         1.b. If it doesn't, go to step 2.
         2. For each relation in the relation set:
         2.1. Try to retrieve it from the database.
         2.1.a. If it exists, merge and add it to the new relation set.
         2.1.b. If it doesn't exist, store it and add it to the new
         relation set.
         3. Get Realtionset Candidates that have first relation of new 
         RelationSet.
         4. Compare each candidate to new RelationSet using contained Relations.
         4.1 If found same RelationSet, set new Relation Set to retrieved 
         candidate.
         5. Check LRs/VRs/MRs to update new RelationSet
         */
        Relations newRelationsObject = new Relations();
        RelationSet newRelationSet = new RelationSet();
        RelationSetDao rsDao = new RelationSetDaoImpl();

        // First, store relations and add them to new relation set
        for (Relation relation : relationSet.getRelationsList()) {
            Relation newRelation = newRelationsObject.storeRelation(relation);
            newRelationSet.addRelation(newRelation);
        }
        newRelationSet.setName(relationSet.getName());

        //RelationSet retrievedRelationSet = rsDao.getRelationSet(
        //        newRelationSet);
        List<RelationSet> relationSetCandidates
                = rsDao.getRelationSetsByRelation(
                        newRelationSet.getRelationsList().get(0));
        RelationSet retrievedRelationSet = null;
        for (RelationSet rsc : relationSetCandidates) {
            boolean foundMatch = false;
            //only check if they have same number of relations
            if (rsc.getRelationsList().size() == newRelationSet.
                    getRelationsList().size()) {
                foundMatch = true;
                //check if each relation of rscand also belongs to 
                //newRelationSet
                for (Relation relation : rsc.getRelationsList()) {
                    boolean foundRelation = false;
                    for (Relation newRelation :
                            newRelationSet.getRelationsList()) {
                        if (relation.equals(newRelation)) {
                            foundRelation = true;
                            break;
                        }
                    }
                    if (!foundRelation) {
                        foundMatch = false;
                    }
                }
            }
            if (foundMatch) {
                retrievedRelationSet = rsc;
                break;
            }
        }

        if (!isNull(retrievedRelationSet)) {
            newRelationSet = retrievedRelationSet;
        }

        if (!isNull(relationSet.getLanguageRepresentations())) {
            for (LanguageRepresentation lr : relationSet.
                    getLanguageRepresentations()) {
                //check if already assigned to relationset
                if (!newRelationSet.getLanguageRepresentations().contains(lr)) {
                    newRelationSet.addLanguageRepresentation(lr);
                }
            }
        }
        if (!isNull(relationSet.getMotoricRepresentations())) {

            for (MotoricRepresentation mr : relationSet.
                    getMotoricRepresentations()) {
                //check if already assigned to relationset
                if (!newRelationSet.getMotoricRepresentations().contains(mr)) {
                    newRelationSet.addMotoricRepresentation(mr);
                }
            }
        }
        if (!isNull(relationSet.getVisualRepresentations())) {
            for (VisualRepresentation vr : relationSet.
                    getVisualRepresentations()) {
                //check if already assigned to relationset
                if (!newRelationSet.getVisualRepresentations().contains(vr)) {
                    newRelationSet.addVisualRepresentation(vr);
                }
            }
        }

        if (retrievedRelationSet == null) {
            rsDao.persist(newRelationSet);
            return newRelationSet;
        } else {
            rsDao.merge(retrievedRelationSet);
            return retrievedRelationSet;
        }
    }
}
