
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
         * Analyze relation set:
         * 0. Create a new Relation Set.
         * 1. Check if the Relation Set exists in the database.
         * 1.a. If it exists, merge it and return it.
         * 1.b. If it doesn't, go to step 2.
         * 2. For each relation in the relation set:
         * 2.1. Try to retrieve it from the database.
         * 2.1.a. If it exists, merge and add it to the new relation set.
         * 2.1.b. If it doesn't exist, store it and add it to the new
         * relation set.
         * 3. Get Realtionset Candidates that have first relation of new
         * RelationSet.
         * 4. Compare each candidate to new RelationSet using contained Relations.
         * 4.1 If found same RelationSet, set new Relation Set to retrieved
         * candidate.
         * 5. Check LRs/VRs/MRs to update new RelationSet
         */
        boolean foundRelations = true;
        boolean foundLanguageRepresentations = true;
        Relations newRelationsObject = new Relations();
        RelationSet newRelationSet = new RelationSet();
        RelationSetDao rsDao = new RelationSetDaoImpl();

        // First, store relations and add them to new relation set
        for (Relation relation : relationSet.getRelationsList()) {
            Relation newRelation = newRelationsObject.storeRelation(relation);
            newRelationSet.addRelation(newRelation);
        }
        newRelationSet.setName(relationSet.getName());
        RelationSet retrievedRelationSet = null;

        if (!newRelationSet.getRelations().isEmpty()) {
            foundRelations = true;
            //RelationSet retrievedRelationSet = rsDao.getRelationSet(
            //        newRelationSet);
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
        } else {
            foundRelations = false;
        }

        if (!isNull(retrievedRelationSet)) {
            newRelationSet = retrievedRelationSet;
        }

        // For each language representation, find it in the DB.
        // If it exists, attach it to the RelationSet.
        // If it doesn't exist, create it.
        LanguageRepresentationDao lrDao =
                new LanguageRepresentationDaoImpl();

        if (!relationSet.getLanguageRepresentations().isEmpty()) {
            foundLanguageRepresentations = true;
            for (LanguageRepresentation languageRepresentation
                    : relationSet.getLanguageRepresentations()) {
                LanguageRepresentation retrievedLanguageRepresentation =
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
                    if (!newRelationSet.getLanguageRepresentations().contains(
                            retrievedLanguageRepresentation)) {
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
        } else // If relationSet doesn't have LanguageRepresentations
        {
            foundLanguageRepresentations = false;
            if (!foundRelations) {
                return null;
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

        if (foundLanguageRepresentations && !foundRelations) {
            rsDao.merge(newRelationSet);
            return newRelationSet;
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
