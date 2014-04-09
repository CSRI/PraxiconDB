/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon;

import csri.poeticon.praxicon.db.entities.Concept;
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
    JAXBContext jaxbContext;
    
    public GeneratePraxiconXSD() throws JAXBException 
    {
        this.jaxbContext = JAXBContext.newInstance(Concept.class);
    }

    public static void main() throws JAXBException, IOException
    {
        JAXBContext jaxbContext;
        scoure = new PraxiconDBOutputResolver();
        jaxbContext = JAXBContext.newInstance(Concept.class);
        jaxbContext.generateSchema(scoure);
        scoure.createOutput("http://www.csri.gr/concept", "/home/dmavroeidis/output.xsd");
    }

}
