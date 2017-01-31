/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon;

import gr.csri.poeticon.praxicon.db.entities.CollectionOfObjects;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationSet;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;

/**
 *
 * @author dmavroeidis
 */
public class GeneratePraxiconXSD {

    public static SchemaOutputResolver scoure;
    JAXBContext jaxbContextConcept;
    JAXBContext jaxbContextRelation;
    JAXBContext jaxbContextRelationSet;
    JAXBContext jaxbContextCollectionOfObjects;

    public GeneratePraxiconXSD() throws JAXBException {
        this.jaxbContextConcept = JAXBContext.newInstance(Concept.class);
        this.jaxbContextRelation = JAXBContext.newInstance(Relation.class);
        this.jaxbContextRelationSet = JAXBContext.newInstance(
                RelationSet.class);
        this.jaxbContextCollectionOfObjects = JAXBContext.newInstance(
                CollectionOfObjects.class);

    }

    public static void main(String args[]) throws JAXBException, IOException {
        SchemaOutputResolver sour;
        JAXBContext jaxbContextConcept;
        JAXBContext jaxbContextRelation;
        JAXBContext jaxbContextRelationSet;
        JAXBContext jaxbContextCollectionOfObjects;
        sour = new PraxiconDBOutputResolver();
        jaxbContextConcept = JAXBContext.newInstance(Concept.class);
        jaxbContextRelation = JAXBContext.newInstance(Relation.class);
        jaxbContextRelationSet = JAXBContext.newInstance(RelationSet.class);
        jaxbContextCollectionOfObjects = JAXBContext.newInstance(
                CollectionOfObjects.class);
        jaxbContextConcept.generateSchema(sour);
        jaxbContextRelation.generateSchema(sour);
        jaxbContextRelationSet.generateSchema(sour);
        jaxbContextCollectionOfObjects.generateSchema(sour);
        sour.createOutput("http://www.csri.gr/concept", "misc/xsd/Concept.xsd");
        sour.createOutput("http://www.csri.gr/relation", "misc/xsd/Relation.xsd");
        sour.createOutput("http://www.csri.gr/relationSet",
                "misc/xsd/RelationSet.xsd");
        sour.createOutput("http://www.csri.gr/collectionOfObjects",
                "misc/xsd/CollectionOfObjects.xsd");
    }
}
