package gr.csri.poeticon.praxicon.db.entities;

import java.util.LinkedHashSet;
import java.util.Set;
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
    Set<Concepts> concepts = new LinkedHashSet<>();

    @XmlElement(name = "relations")
    Set<Relations> relations = new LinkedHashSet<>();

    @XmlElement(name = "relationSets")
    Set<RelationSets> relationSets = new LinkedHashSet<>();

    public Set<Concepts> getConcepts() {
        return concepts;
    }

    public void setConcepts(Set<Concepts> concepts) {
        this.concepts = concepts;
    }

    public Set<Relations> getRelations() {
        return relations;
    }

    public void setRelations(Set<Relations> relations) {
        this.relations = relations;
    }

    public Set<RelationSets> getRelationSets() {
        return relationSets;
    }

    public void setRelationSets(Set<RelationSets> relationSets) {
        this.relationSets = relationSets;
    }

    public CollectionOfObjects() {
        concepts = new LinkedHashSet<>();
        relations = new LinkedHashSet<>();
        relationSets = new LinkedHashSet<>();
    }

    /**
     * Stores all concepts of the collection in the database updating
     * same-name entries
     */
    public void storeCollectionOfObjects() {
        for (Concepts conceptList : concepts) {
            conceptList.storeConcepts();
        }
        for (Relations relationList : relations) {
            relationList.storeRelations();
        }
        for (RelationSets relationSetList : relationSets) {
            relationSetList.storeRelationSets();
        }
    }

}
