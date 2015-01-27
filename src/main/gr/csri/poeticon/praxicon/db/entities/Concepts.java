package gr.csri.poeticon.praxicon.db.entities;

import gr.csri.poeticon.praxicon.db.dao.ConceptDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
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
@XmlRootElement(name = "concepts")
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlSeeAlso(Concept.class)

public class Concepts {

    @XmlElement(name = "concept")
    List<Concept> concepts = new ArrayList<>();

    public List<Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<Concept> concepts) {
        this.concepts = concepts;
    }

    public Concepts() {
        concepts = new ArrayList<>();
    }

    /**
     * Stores all concepts of the collection in the database updating
     * same name entries
     */
    public void storeConcepts() {
        for (Concept concept : concepts) {
            ConceptDao cDao = new ConceptDaoImpl();
            concept = cDao.updatedConcept(concept);

            System.out.println("External source Id: " + 
                    concept.getExternalSourceId());
            cDao.merge(concept);
        }
    }

}
