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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * gr.csri.poeticon.praxicon.db.entities to XML
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
        concepts.setConcepts(new ArrayList<>());
        List<Concept> newConceptsList = new ArrayList<>();

        try {
            /*
             * Get active session to update concepts. This way, we can update
             * retrieved objects directly to avoid setting EAGER fetch which
             * would criple performance during retrieval from the database.
             */
            EntityManager em = getEntityManager();
            Session session = em.unwrap(org.hibernate.Session.class);
            JAXBContext jaxbContext = JAXBContext.newInstance(Concepts.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            for (Concept item : conceptsList) {
                if (!newConceptsList.contains(item)) {
                    Concept tmpConcept = session.get(Concept.class, item.
                            getId());
                    // If the concept exists in the session, then don't
                    // add it, but rather add its found counterpart.
                    item = new Concept(tmpConcept, true, true, true);
                    newConceptsList.add(item);
                }
            }

            // Export concepts to the xml file
            concepts.setConcepts(newConceptsList);
            marshaller.marshal(concepts, new File(xmlFileName));
        } catch (JAXBException ex) {
            Logger.getLogger(XmlUtils.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println(Arrays.toString(e.getStackTrace()));
            System.exit(1);
        }
    }

    /**
     * Exports an XML file given a list of relations. If the file exists, it is
     * overwritten.
     *
     * @param relationsList
     * @param xmlFileName
     */
    public static void exportRelationsToXML(List<Relation> relationsList,
            String xmlFileName) {

        Relations relations = new Relations();
        relations.setRelations(new ArrayList<>());
        List<Relation> newRelationsList = new ArrayList<>();

        try {
            /*
             * Get active session to update concepts. This way, we can update
             * retrieved objects directly to avoid setting EAGER fetch which
             * would criple performance during retrieval from the database.
             */
            EntityManager em = getEntityManager();
            Session session = em.unwrap(org.hibernate.Session.class);
            JAXBContext jaxbContext = JAXBContext.newInstance(Relations.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            for (Relation item : relationsList) {
                if (!newRelationsList.contains(item)) {
                    Relation tmpRelation = session.get(Relation.class, item.
                            getId());
                    // If the relation exists in the session, then don't
                    // add it, but rather add its found counterpart.
                    item = new Relation(tmpRelation);
                    newRelationsList.add(item);
                }
            }
            relations.setRelations(newRelationsList);
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
     * @param relationSetsList
     * @param xmlFileName
     */
    public static void exportRelationSetsToXML(
            List<RelationSet> relationSetsList, String xmlFileName) {

        RelationSets relationSets = new RelationSets();
        relationSets.setRelationSets(new ArrayList<>());
        List<RelationSet> newRelationSetsList = new ArrayList<>();

        try {
            /*
             * Get active session to update relation sets. This way, we can
             * update retrieved objects directly to avoid setting EAGER fetch
             * which would criple performance during retrieval from the database.
             */
            EntityManager em = getEntityManager();
            Session session = em.unwrap(org.hibernate.Session.class);
            JAXBContext jaxbContext = JAXBContext.
                    newInstance(RelationSets.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            for (RelationSet item : relationSetsList) {
                if (!newRelationSetsList.contains(item)) {
                    RelationSet tmpRelationSet = session.
                            get(RelationSet.class, item.getId());
                    // If the relation set exists in the session, then don't
                    // add it, but rather add its found counterpart.
                    item = new RelationSet(tmpRelationSet);
                    newRelationSetsList.add(item);
                }
            }
            relationSets.setRelationSets(newRelationSetsList);
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
     * @param relationSetsList
     * @param conceptsList
     * @param relationsList
     * @param xmlFileName
     */
    public static void exportAllObjectsToXML(
            List<RelationSet> relationSetsList,
            List<Concept> conceptsList, List<Relation> relationsList,
            String xmlFileName) {
        CollectionOfObjects collectionOfObjects = new CollectionOfObjects();
        RelationSets relationSets = new RelationSets();
        List<RelationSet> newRelationSetsList = new ArrayList<>();
        Concepts concepts = new Concepts();
        List<Concept> newConceptsList = new ArrayList<>();
        Relations relations = new Relations();
        List<Relation> newRelationsList = new ArrayList<>();
        relationSets.setRelationSets(new ArrayList<>());
        concepts.setConcepts(new ArrayList<>());
        relations.setRelations(new ArrayList<>());

        try {
            /*
             * Get active session to update relation sets. This way, we can
             * update retrieved objects directly to avoid setting EAGER fetch
             * which would criple performance during retrieval from the database.
             */
            EntityManager em = getEntityManager();
            Session session = em.unwrap(org.hibernate.Session.class);
            JAXBContext jaxbContext = JAXBContext.
                    newInstance(CollectionOfObjects.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            for (RelationSet item : relationSetsList) {
                if (!newRelationSetsList.contains(item)) {
                    RelationSet tmpRelationSet = session.
                            get(RelationSet.class, item.getId());
                    // If the relation set exists in the session, then don't
                    // add it, but rather add its found counterpart.
                    item = new RelationSet(tmpRelationSet);
                    newRelationSetsList.add(item);
                }
            }

            for (Concept item : conceptsList) {
                if (!newConceptsList.contains(item)) {
                    Concept tmpConcept = session.get(Concept.class, item.
                            getId());
                    // If the concept exists in the session, then don't
                    // add it, but rather add its found counterpart.
                    item = new Concept(tmpConcept, true, true, true);
                    newConceptsList.add(item);
                }
            }

            for (Relation item : relationsList) {
                if (!newRelationsList.contains(item)) {
                    Relation tmpRelation = session.get(Relation.class, item.
                            getId());
                    // If the relation exists in the session, then don't
                    // add it, but rather add its found counterpart.
                    item = new Relation(tmpRelation);
                    newRelationsList.add(item);
                }
            }

            concepts.setConcepts(newConceptsList);
            relations.setRelations(newRelationsList);
            relationSets.setRelationSets(newRelationSetsList);

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
            System.err.println(Arrays.toString(e.getStackTrace()));
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
            Relations importedRelations = (Relations)jaxbUnmarshaller.
                    unmarshal(xmlFile);
            importedRelations.storeRelations();
        } catch (JAXBException ex) {
            Logger.getLogger(XmlUtils.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println(Arrays.toString(e.getStackTrace()));
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
            RelationSets importedRelationSets =
                    (RelationSets)jaxbUnmarshaller.
                    unmarshal(xmlFile);
            importedRelationSets.storeRelationSets();
        } catch (JAXBException ex) {
            Logger.getLogger(XmlUtils.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println(Arrays.toString(e.getStackTrace()));
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
                    (CollectionOfObjects)jaxbUnmarshaller.unmarshal(xmlFile);
            List<Concepts> listOfConcepts = importedCollectionOfObjects.
                    getConcepts();
            for (Concepts concepts : listOfConcepts) {
                concepts.storeConcepts();
            }

            List<RelationSets> listOfRelationSets =
                    importedCollectionOfObjects.
                    getRelationSets();
            for (RelationSets relationSets : listOfRelationSets) {
                relationSets.storeRelationSets();
            }

            List<Relations> listOfRelations = importedCollectionOfObjects.
                    getRelations();
            for (Relations relations : listOfRelations) {
                relations.storeRelations();
            }
        } catch (JAXBException ex) {
            Logger.getLogger(XmlUtils.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println(Arrays.toString(e.getStackTrace()));
            return 1;
        }

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        double minutes = (double)((elapsedTime / 1000000000.0) / 60);
        System.out.print("XML import took: ");
        System.out.print(minutes);
        System.out.println(" minutes to run");
        return 0;
    }

}
