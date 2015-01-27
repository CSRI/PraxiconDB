/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon;

import static gr.csri.poeticon.praxicon.EntityMngFactory.getEntityManager;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Concepts;
import gr.csri.poeticon.praxicon.db.entities.RelationSet;
import gr.csri.poeticon.praxicon.db.entities.RelationSets;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.hibernate.Session;

/**
 * A class that helps serialize any entity of the
 * gr.csri.poeticon.praxicon.db.entities as an XML
 *
 * @author dmavroeidis
 */
public class XmlUtils {

    /**
     * Exports an XML file given a list of concepts. If the file exists, it is
     * overwritten.
     *
     * @param conceptsList
     * @param xmlFileName
     */
    public static void exportConceptsToXML(List<Concept> conceptsList,
            String xmlFileName) {

        Concepts concepts = new Concepts();
        concepts.setConcepts(new ArrayList<Concept>());

        try {
            /* 
             Get active session to update concepts. This way, we can update
             retrieved objects directly to avoid setting EAGER fetch which
             would criple performance during retrieval from the database. 
             */
            EntityManager em = getEntityManager();
            Session session = em.unwrap(org.hibernate.Session.class);

            JAXBContext jaxbContext = JAXBContext.newInstance(Concepts.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            for (Concept item : conceptsList) {
                if (!session.contains(item)) {
                    session.update(item);
                }
                concepts.getConcepts().add(item);
            }

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Export concepts to the xml file
            marshaller.marshal(concepts, new File(xmlFileName));

        } catch (JAXBException ex) {
            Logger.getLogger(XmlUtils.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Exports an xml file given a list of relation sets. If the file exists,
     * it is overwritten.
     *
     * @param relationSetsList
     * @param xmlFileName
     */
    public static void exportRelationSetsToXML(
            List<RelationSet> relationSetsList, String xmlFileName) {

        RelationSets relationSets = new RelationSets();
        relationSets.setRelationSets(new ArrayList<RelationSet>());

        try {
            /* 
             Get active session to update relation sets. This way, we can 
             update retrieved objects directly to avoid setting EAGER fetch 
             which would criple performance during retrieval from the database. 
             */
            EntityManager em = getEntityManager();
            Session session = em.unwrap(org.hibernate.Session.class);

            JAXBContext jaxbContext = JAXBContext.
                    newInstance(RelationSets.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            for (RelationSet item : relationSetsList) {
                if (!session.contains(item)) {
                    session.update(item);
                }
                relationSets.getRelationSets().add(item);
            }

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Export relation sets to the xml file
            marshaller.marshal(relationSets, new File(xmlFileName));

        } catch (JAXBException ex) {
            Logger.getLogger(XmlUtils.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }
//
//    /**
//     * Exports an xml file given a list of relation sets. If the file exists,
//     * it is overwritten.
//     *
//     * @param relationSetsList
//     * @param conceptsList
//     * @param xmlFileName
//     */
//    public static void exportAllObjectsToXML(List<RelationSet> relationSetsList,
//            List<Concept> conceptsList, String xmlFileName) {
//
//        CollectionOfObjects collectionOfObjects = new CollectionOfObjects();
//        RelationSets relationSets = new RelationSets();
//        Concepts concepts = new Concepts();
//        relationSets.setRelationSets(new ArrayList<RelationSet>());
//        concepts.setConcepts(new ArrayList<Concept>());
//
//        try {
//            /* 
//             Get active session to update relation sets. This way, we can 
//             update retrieved objects directly to avoid setting EAGER fetch 
//             which would criple performance during retrieval from the database. 
//             */
//            EntityManager em = getEntityManager();
//            Session session = em.unwrap(org.hibernate.Session.class);
//
//            JAXBContext jaxbContext = JAXBContext.
//                    newInstance(CollectionOfObjects.class);
//            Marshaller marshaller = jaxbContext.createMarshaller();
//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//            for (RelationSet item : relationSetsList) {
//                if (!session.contains(item)) {
//                    session.update(item);
//                }
//                relationSets.getRelationSets().add(item);
//            }
//            for (Concept item : conceptsList) {
//                if (session.contains(item)) {
//                } else {
//                    session.update(item);
//                }
//                concepts.getConcepts().add(item);
//            }
//
//            collectionOfObjects.getConcepts().add(concepts);
//            collectionOfObjects.getRelationSets().add(relationSets);
//
//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//            // Export relation sets to the xml file
//            marshaller.marshal(collectionOfObjects, new File(xmlFileName));
//
//        } catch (JAXBException ex) {
//            Logger.getLogger(XmlUtils.class.getName()).
//                    log(Level.SEVERE, null, ex);
//        }
//    }

}
