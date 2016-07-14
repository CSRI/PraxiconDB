/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon;

import static gr.csri.poeticon.praxicon.EntityMngFactory.getEntityManager;
import gr.csri.poeticon.praxicon.db.entities.CollectionOfObjects;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Concepts;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationSet;
import gr.csri.poeticon.praxicon.db.entities.RelationSets;
import gr.csri.poeticon.praxicon.db.entities.Relations;
import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
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
     * @param conceptsSet
     * @param xmlFileName
     */
    public static void exportConceptsToXML(Set<Concept> conceptsSet,
            String xmlFileName) {
        Concepts concepts = new Concepts();
        concepts.setConcepts(new LinkedHashSet<>());

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
            for (Concept item : conceptsSet) {
                if (!session.contains(item)) {
                    session.update(item);
                }
                concepts.getConcepts().add(item);
            }

            // Export concepts to the xml file
            marshaller.marshal(concepts, new File(xmlFileName));
        } catch (JAXBException ex) {
            Logger.getLogger(XmlUtils.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Exports an XML file given a list of relations. If the file exists, it is
     * overwritten.
     *
     * @param relationsSet
     * @param xmlFileName
     */
    public static void exportRelationsToXML(Set<Relation> relationsSet,
            String xmlFileName) {

        Relations relations = new Relations();
        relations.setRelations(new LinkedHashSet<Relation>());

        try {
            /*
             Get active session to update concepts. This way, we can update
             retrieved objects directly to avoid setting EAGER fetch which
             would criple performance during retrieval from the database.
             */
            EntityManager em = getEntityManager();
            Session session = em.unwrap(org.hibernate.Session.class);
            JAXBContext jaxbContext = JAXBContext.newInstance(Relations.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            for (Relation item : relationsSet) {
                if (!session.contains(item)) {
                    session.update(item);
                }
                relations.getRelations().add(item);
            }
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Export concepts to the xml file
            marshaller.marshal(relations, new File(xmlFileName));
        } catch (JAXBException ex) {
            Logger.getLogger(XmlUtils.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Exports an xml file given a list of relation sets. If the file exists,
     * it is overwritten.
     *
     * @param relationSetsSet
     * @param xmlFileName
     */
    public static void exportRelationSetsToXML(
            Set<RelationSet> relationSetsSet, String xmlFileName) {

        RelationSets relationSets = new RelationSets();
        relationSets.setRelationSets(new LinkedHashSet<>());

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

            for (RelationSet item : relationSetsSet) {
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

    /**
     * Exports an xml file given a list of relation sets, a list of Concepts
     * and a list of relations. If the file exists, it is overwritten.
     *
     * @param relationSetsSet
     * @param conceptsSet
     * @param relationsSet
     * @param xmlFileName
     */
    public static void exportAllObjectsToXML(Set<RelationSet> relationSetsSet,
            Set<Concept> conceptsSet, Set<Relation> relationsSet,
            String xmlFileName) {
        CollectionOfObjects collectionOfObjects = new CollectionOfObjects();
        RelationSets relationSets = new RelationSets();
        Concepts concepts = new Concepts();
        Relations relations = new Relations();
        relationSets.setRelationSets(new LinkedHashSet<>());
        concepts.setConcepts(new LinkedHashSet<>());
        relations.setRelations(new LinkedHashSet<>());

        try {
            /*
             Get active session to update relation sets. This way, we can
             update retrieved objects directly to avoid setting EAGER fetch
             which would criple performance during retrieval from the database.
             */
            EntityManager em = getEntityManager();
            Session session = em.unwrap(org.hibernate.Session.class);
            JAXBContext jaxbContext = JAXBContext.
                    newInstance(CollectionOfObjects.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            for (RelationSet item : relationSetsSet) {
                if (!session.contains(item)) {
                    session.update(item);
                }
                relationSets.getRelationSets().add(item);
            }
            for (Concept item : conceptsSet) {
                if (!session.contains(item)) {
                    session.update(item);
                }
                concepts.getConcepts().add(item);
            }
            for (Relation item : relationsSet) {
                if (!session.contains(item)) {
                    session.update(item);
                }
                relations.getRelations().add(item);
            }

            collectionOfObjects.getConcepts().add(concepts);
            collectionOfObjects.getRelations().add(relations);
            collectionOfObjects.getRelationSets().add(relationSets);

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Export all objects to the xml file
            marshaller.marshal(collectionOfObjects, new File(xmlFileName));

        } catch (JAXBException ex) {
            Logger.getLogger(XmlUtils.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    public static int importConceptsFromXml(String fullPathFileName) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Concepts.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            File xmlFile = new File(fullPathFileName);
            Concepts importedConcepts =
                    (Concepts)jaxbUnmarshaller.unmarshal(xmlFile);
            importedConcepts.storeConcepts();
        } catch (JAXBException ex) {
            Logger.getLogger(XmlUtils.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    public static int importRelationsFromXml(String fullPathFileName) {
        try {
            JAXBContext jaxbContext = JAXBContext.
                    newInstance(Relations.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            File xmlFile = new File(fullPathFileName);
            Relations importedRelations = (Relations) jaxbUnmarshaller.
                    unmarshal(xmlFile);
            importedRelations.storeRelations();
        } catch (JAXBException ex) {
            Logger.getLogger(XmlUtils.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    public static int importRelationSetsFromXml(String fullPathFileName) {
        try {
            JAXBContext jaxbContext = JAXBContext.
                    newInstance(RelationSets.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            File xmlFile = new File(fullPathFileName);
            RelationSets importedRelationSets = (RelationSets) jaxbUnmarshaller.
                    unmarshal(xmlFile);
            importedRelationSets.storeRelationSets();
        } catch (JAXBException ex) {
            Logger.getLogger(XmlUtils.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    public static int importObjectsFromXml(String fullPathFileName) {
        long startTime = System.nanoTime();
        try {
            JAXBContext jaxbContext = JAXBContext.
                    newInstance(CollectionOfObjects.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            File xmlFile = new File(fullPathFileName);
            CollectionOfObjects importedCollectionOfObjects =
                    (CollectionOfObjects) jaxbUnmarshaller.unmarshal(xmlFile);
            Set<Concepts> listOfConcepts = importedCollectionOfObjects.
                    getConcepts();
            for (Concepts concepts : listOfConcepts) {
                concepts.storeConcepts();
            }

            Set<RelationSets> listOfRelationSets = importedCollectionOfObjects.
                    getRelationSets();
            for (RelationSets relationSets : listOfRelationSets) {
                relationSets.storeRelationSets();
            }

            Set<Relations> listOfRelations = importedCollectionOfObjects.
                    getRelations();
            for (Relations relations : listOfRelations) {
                relations.storeRelations();
            }
        } catch (JAXBException ex) {
            Logger.getLogger(XmlUtils.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
//        catch (Exception e) {
//            System.err.println(e.getMessage());
//            return 1;
//        }
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        double minutes = (double)((elapsedTime / 1000000000.0)/60);
        System.out.print("XML import took: ");
        System.out.print(minutes);
        System.out.println(" minutes to run");
        return 0;
    }

}
