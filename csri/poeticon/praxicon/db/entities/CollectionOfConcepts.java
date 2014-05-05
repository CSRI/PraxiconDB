/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csri.poeticon.praxicon.db.entities;

import csri.poeticon.praxicon.db.dao.ConceptDao;
import csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Erevodifwntas
 */
@XmlRootElement(name = "collection", namespace =
        "http://www.csri.gr/collection_of_concepts")
public class CollectionOfConcepts {

    List<Concept> concepts;

    @XmlElement(name = "entity")
    public List<Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<Concept> concepts) {
        this.concepts = concepts;
    }

    public CollectionOfConcepts() {
        concepts = new ArrayList<Concept>();
    }

    /**
     * Stores all concepts of the collection in the database updating same name entries
     */
    public void storeConcepts() {
        for (int i = 0; i < this.concepts.size(); i++) {
            ConceptDao cDao = new ConceptDaoImpl();
            Concept tmp = this.concepts.get(i);
            tmp = cDao.updatedConcept(tmp);

            System.out.println(i + " " + tmp.getName());
            cDao.merge(tmp);
        }
    }

    /**
     * Stores everything inside the collection
     */
    public void storeEverything() {
        storeConcepts();
    }
}
