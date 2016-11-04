
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import gr.csri.poeticon.praxicon.db.dao.LanguageRepresentationDao;
import gr.csri.poeticon.praxicon.db.dao.RelationSetDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.LanguageRepresentationDaoImpl;
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
     * Stores all relation sets of the collection in the database.
     */
    public void storeRelationSets() {
        if (!relationSets.isEmpty()) {
            RelationSetDao rsDao = new RelationSetDaoImpl();
            RelationSet newRelationSet;
            for (RelationSet relationSet : relationSets) {
                newRelationSet = storeRelationSet(relationSet);
                rsDao.getEntityManager().clear();
                if (!isNull(newRelationSet)) {
                    System.out.println("Storing Relation Set: " +
                            newRelationSet.toString());
                } else {
                    System.out.println(
                            newRelationSet.toString() + " neither " +
                            "has relations or language representations, " +
                            "so is not stored");
                }
            }
        }
    }

    /**
     * Stores a single relation set checking for the existence of equal items in
     * the database.
     */
    public RelationSet storeRelationSet(RelationSet relationSet) {

        /*
         * Algorithm for storing relation sets:
         * ------------------------------------
         * 0. Set both hasRelations and hasLanguageRepresentations flags 
         *    to FALSE.
         * 1. Check if relation set contains relations. If yes, 
         *    set hasRelations flag to TRUE.
         * 2. Check if relation set contains language representations. 
         *    If yes, set hasLanguageRepresentations flag to TRUE.
         * 3. If hasRelations==TRUE:
         *     3.1. Create a new Relation Set.
         *     3.2. Check if the Relation Set exists in the database.
         *         3.2.1. If it exists, merge it and return it.
         *         3.2.2. If it doesn't:
         *             3.2.2.1. For each relation in the relation set:
         *                 3.2.2.1.1. Try to retrieve it from the database.
         *                 3.2.2.1.2. If it exists, merge and add it to the 
         *                            new relation set.
         *                 3.2.2.1.3. If it doesn't exist, store it and 
         *                            add it to the new relation set.
         *             3.2.2.2. Get Relation Set Candidates that have 
         *                      first relation of new RelationSet.
         *             3.2.2.3. Compare each candidate to new RelationSet 
         *                      using contained Relations.
         *             3.2.2.4. If found same RelationSet, 
         *                      set new Relation Set to retrieved candidate.
         * 4. Check LRs/VRs/MRs to update new RelationSet.
         * 5. If hasRelations==FALSE:
         *     5.1. If hasLanguageRepresentations==TRUE:
         *         5.1.1. Find them in the DB.
         *         5.1.2. Check if all of them belong to an existing 
         *                relation set.
         *             5.1.2.1. If they belong, then the relation set 
         *                      already exists. Exit.
         *             5.1.2.2. If they don't belong, just connect them 
         *                      to the relation set.
         *     5.2. If hasLanguageRepresentations==FALSE:
         *         5.2.1. Check for name.
         *             5.2.1.1. If name exists, save it.
         *             5.2.1.2. Otherwise, don't save it.
         */
        
        boolean foundRelations = false;
        boolean foundLanguageRepresentations = false;
        Relations newRelationsObject = new Relations();
        RelationSet newRelationSet = new RelationSet();
        RelationSet retrievedRelationSet = null;
        LanguageRepresentation retrievedLanguageRepresentation =
                new LanguageRepresentation();
        RelationSetDao rsDao = new RelationSetDaoImpl();

        if (!relationSet.getRelationsList().isEmpty()) {
            foundRelations = true;
        }
        if (!relationSet.getLanguageRepresentations().isEmpty()) {
            foundLanguageRepresentations = true;
        }

        if (foundRelations) {
            // First, store relations and add them to new relation set
            for (Relation relation : relationSet.getRelationsList()) {
                Relation newRelation = newRelationsObject.storeRelation(
                        relation);
                newRelationSet.addRelation(newRelation);
            }
            newRelationSet.setName(relationSet.getName());

            List<RelationSet> relationSetCandidates =
                    rsDao.getRelationSetsByRelation(
                            newRelationSet.getRelationsList().get(0));

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
                        for (Relation newRelation : newRelationSet.
                                getRelationsList()) {
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
        }

        if (foundLanguageRepresentations) {
            // For each language representation, find it in the DB.
            // If it exists, attach it to the RelationSet.
            // If it doesn't exist, create it.
            LanguageRepresentationDao lrDao =
                    new LanguageRepresentationDaoImpl();
            for (LanguageRepresentation languageRepresentation
                    : relationSet.getLanguageRepresentations()) {
                retrievedLanguageRepresentation =
                        lrDao.getSingleLanguageRepresentation(
                                languageRepresentation.getLanguage(),
                                languageRepresentation.getText(),
                                languageRepresentation.getPartOfSpeech(),
                                languageRepresentation.getUseStatus(),
                                languageRepresentation.getProductivity(),
                                languageRepresentation.getNegation(),
                                languageRepresentation.getOperator());
                // if Language Representation exists add the retrieved,
                // otherwise, add the new one.
                if (!isNull(retrievedLanguageRepresentation)) {
                    //check if already assigned to relationSet
                    if (!newRelationSet.getLanguageRepresentations().
                            contains(retrievedLanguageRepresentation)) {
                        newRelationSet.addLanguageRepresentation(
                                retrievedLanguageRepresentation);
                    }
                } else {
                    LanguageRepresentation newLanguageRepresentation =
                            new LanguageRepresentation(languageRepresentation);
                    //check if already assigned to relationSet
                    if (!newRelationSet.getLanguageRepresentations().
                            contains(newLanguageRepresentation)) {
                        newRelationSet.addLanguageRepresentation(
                                newLanguageRepresentation);
                    }
                }
            }
        }

        if (!isNull(relationSet.getMotoricRepresentations())) {
            for (MotoricRepresentation mr : relationSet.
                    getMotoricRepresentations()) {
                mr.setRelationSet(newRelationSet);
                //check if already assigned to relationset
                if (!newRelationSet.getMotoricRepresentations().contains(mr)) {
                    newRelationSet.addMotoricRepresentation(mr);
                }
            }
        }

        if (!isNull(relationSet.getVisualRepresentations())) {
            for (VisualRepresentation vr : relationSet.
                    getVisualRepresentations()) {
                vr.setRelationSet(newRelationSet);
                //check if already assigned to relationset
                if (!newRelationSet.getVisualRepresentations().contains(vr)) {
                    newRelationSet.addVisualRepresentation(vr);
                }
            }
        }

        if (foundRelations) {
            if (isNull(retrievedRelationSet)) {
                rsDao.persist(newRelationSet);
                return newRelationSet;
            } else {
                rsDao.merge(retrievedRelationSet);
                return retrievedRelationSet;
            }
        } else if (!foundRelations && foundLanguageRepresentations) {
            if (isNull(retrievedLanguageRepresentation)) {
                rsDao.merge(newRelationSet);
                return newRelationSet;
            }
        }
        return new RelationSet();
    }
}
