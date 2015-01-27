package gr.csri.poeticon.praxicon.db.entities;

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
@XmlRootElement(name = "collectionOfObjects")
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlSeeAlso(Concept.class)

public class CollectionOfObjects {

    @XmlElement(name = "concepts")
    List<Concepts> concepts = new ArrayList<>();

    @XmlElement(name = "relationSets")
    List<RelationSets> relationSets = new ArrayList<>();

    public List<Concepts> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<Concepts> concepts) {
        this.concepts = concepts;
    }

    public CollectionOfObjects() {
        concepts = new ArrayList<>();
        relationSets = new ArrayList<>();
    }

    public List<RelationSets> getRelationSets() {
        return relationSets;
    }

    public void setRelationSets(List<RelationSets> relationSets) {
        this.relationSets = relationSets;
    }

    /**
     * Stores all concepts of the collection in the database updating
     * same name entries
     */
    public void storeCollectionOfObjects() {
        for (Concepts conceptList : concepts) {
            conceptList.storeConcepts();
        }
        for (RelationSets relationSetList : relationSets) {
            relationSetList.storeRelationSets();
        }
    }

}
