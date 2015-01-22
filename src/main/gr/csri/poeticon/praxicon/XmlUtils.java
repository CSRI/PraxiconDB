/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon;

import static gr.csri.poeticon.praxicon.EntityMngFactory.getEntityManager;
import gr.csri.poeticon.praxicon.db.entities.CollectionOfConcepts;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Concepts;
import gr.csri.poeticon.praxicon.db.entities.RelationSet;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import org.hibernate.Session;

/**
 * A class that helps you to serialize any entity of the
 * gr.csri.poeticon.praxicon.db.entities as an XML
 *
 * @author dmavroeidis
 */
public class XmlUtils {

    /**
     * Saves all the entities as an XML file. The file should not exist, or
     * should be empty (the function appends the xml content in the end of the
     * file)
     *
     * @param objects      The entities of the
     *                     gr.csri.poeticon.praxicon.db.entities
     *                     that are going to be serialized as an XML
     * @param XML_FileName The name of the XML file (with or without a path)
     */
    public static void saveToXML(List<Concept> objects, String XML_FileName) {
        try {
            //Initialization
            JAXBContext context = JAXBContext.newInstance(
                    "gr.csri.poeticon.praxicon.db.entities");
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            for (Concept object : objects) {
                marshaller.marshal(object, new FileWriter(XML_FileName, true));
            }
        } catch (IOException | JAXBException ex) {
            Logger.getLogger(XmlUtils.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Saves all the entities as an XML file. The file should not exist, or
     * should be empty (the function appends the xml content in the end of the
     * file)
     *
     * @param collection   The entities of the
     *                     gr.csri.poeticon.praxicon.db.entities
     *                     that are going to be serialized as an XML
     * @param XML_FileName The name of the XML file (with or without a path)
     */
    public static void
            saveToXML(CollectionOfConcepts collection, String XML_FileName) {
        try {
            //Initialization
            JAXBContext context = JAXBContext.newInstance(
                    "gr.csri.poeticon.praxicon.db.entities");
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            try ( //Creating the xml file
                    FileWriter fWriter = new FileWriter(XML_FileName, true)) {
                marshaller.marshal(collection, fWriter);
            }
        } catch (IOException | JAXBException ex) {
            Logger.getLogger(XmlUtils.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    public static void exportConceptsToXML(List<Concept> conceptsList,
            String path) {
        EntityManager em = getEntityManager();
        Session session = em.unwrap(org.hibernate.Session.class);

        try {
            Concepts concepts = new Concepts();
            concepts.setConcepts(new ArrayList<Concept>());

            JAXBContext jaxbContext = JAXBContext.newInstance(Concepts.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            for (Concept item : conceptsList) {
                session.update(item);
            }

            concepts.getConcepts().addAll(conceptsList);

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.marshal(concepts, new File(path));

        } catch (JAXBException exp) {
            System.out.println("Cought exception: " + exp.toString());
        }
    }

    public static void exportRelationSetsToXML(List<RelationSet> relationSets,
            String path) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(RelationSet.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            for (RelationSet relationSet : relationSets) {
                JAXBElement<RelationSet> jaxbElement =
                        new JAXBElement<>(new QName(null, "relation_set"),
                                RelationSet.class, relationSet);
                marshaller.marshal(jaxbElement, System.out);
            }

        } catch (Exception exp) {
            System.out.println("Cought exception: " + exp.toString());
        }
    }
}
