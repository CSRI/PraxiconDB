/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csri.poeticon.praxicon;

import csri.poeticon.praxicon.db.entities.CollectionOfConcepts;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * A class that helps you to serialize any entity of the
 * csri.poeticon.praxicon.db.entities as an XML
 *
 * @author Erevodifwntas
 */
public class XML_Utils {

    /**
     * Saves all the entities as an XML file. The file should not exist, or
     * should be empty (the function appends the xml content in the end of the
     * file)
     *
     * @param objects      The entities of the csri.poeticon.praxicon.db.entities
     *                     that are going to be serialized as an XML
     * @param XML_FileName The name of the XML file (with or without a path)
     */
    public static void saveToXML(List objects, String XML_FileName) {
        try {
            //Initialization
            JAXBContext context = JAXBContext.newInstance(
                    "csri.poeticon.praxicon.db.entities");
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            for (Object object : objects) {
                marshaller.marshal(object, new FileWriter(XML_FileName, true));
            }
        } catch (IOException | JAXBException ex) {
            Logger.getLogger(XML_Utils.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Saves all the entities as an XML file. The file should not exist, or
     * should be empty (the function appends the xml content in the end of the
     * file)
     *
     * @param collection   The entities of the csri.poeticon.praxicon.db.entities
     *                     that are going to be serialized as an XML
     * @param XML_FileName The name of the XML file (with or without a path)
     */
    public static void
            saveToXML(CollectionOfConcepts collection, String XML_FileName) {
        try {
            //Initialization
            JAXBContext context = JAXBContext.newInstance(
                    "csri.poeticon.praxicon.db.entities");
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            try ( //Creating the xml file
                    FileWriter fWriter = new FileWriter(XML_FileName, true)) {
                marshaller.marshal(collection, fWriter);
            }
        } catch (IOException | JAXBException ex) {
            Logger.getLogger(XML_Utils.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }
}
