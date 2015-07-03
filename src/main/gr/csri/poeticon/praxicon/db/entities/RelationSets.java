
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
            for (RelationSet relationSet : relationSets) {
                storeRelationSet(relationSet);
            }
        }
    }

    private void storeRelationSet(RelationSet relationSet) {
        RelationSetDao rsDao = new RelationSetDaoImpl();
        relationSet = rsDao.updatedRelationSet(relationSet);
        rsDao.merge(relationSet);
    }
}
