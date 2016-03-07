
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
     * Stores all concepts of the collection in the database updating
     * same name entries
     */
    public void storeRelationSets() {
        if (!relationSets.isEmpty()) {
            RelationSetDao rsDao = new RelationSetDaoImpl();
            RelationSet newRelationSet;
            for (RelationSet relationSet : relationSets) {
                newRelationSet = storeRelationSet(relationSet);
                rsDao.getEntityManager().clear();
                System.out.println("Storing Relation Set: " + newRelationSet.toString());
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
         3. Store new relation set in the database and return it.
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
        if (!isNull(relationSet.getLanguageRepresentations())) {
            for (LanguageRepresentation lr : relationSet.
                    getLanguageRepresentations()) {
                newRelationSet.addLanguageRepresentation(lr);
            }
        }
        if (!isNull(relationSet.getMotoricRepresentations())) {

            for (MotoricRepresentation mr : relationSet.
                    getMotoricRepresentations()) {
                newRelationSet.addMotoricRepresentation(mr);
            }
        }
        if (!isNull(relationSet.getVisualRepresentations())) {
            for (VisualRepresentation vr : relationSet.
                    getVisualRepresentations()) {
                newRelationSet.addVisualRepresentation(vr);
            }
        }

        RelationSet retrievedRelationSet = rsDao.getRelationSet(
                newRelationSet);
        if (!isNull(retrievedRelationSet)) {
            rsDao.merge(retrievedRelationSet);
            return retrievedRelationSet;
        } else {
            rsDao.persist(newRelationSet);
            return newRelationSet;
        }
    }
}
