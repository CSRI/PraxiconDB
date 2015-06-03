package gr.csri.poeticon.praxicon.db.entities;

import gr.csri.poeticon.praxicon.db.dao.ConceptDao;
import gr.csri.poeticon.praxicon.db.dao.LanguageRepresentationDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.LanguageRepresentationDaoImpl;
import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.isNull;
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
     * Stores all concepts of the collection in the database checking if
     * they already exist in the database.
     */
    public void storeConcepts() {
        if (!concepts.isEmpty()) {
            for (Concept concept : concepts) {
                ConceptDao cDao = new ConceptDaoImpl();
                Concept oldConcept = new Concept(concept, false, false, false);

                Concept retrievedConcept = cDao.getConcept(concept);

                // If concept does not exist in the database, store it.
                if (!isNull(retrievedConcept)) {
                    // Create a new concept without the language representation info
                    Concept newConcept = new Concept(retrievedConcept, false,
                            false, false);

                    // For each language representation, find it in the DB.
                    // If it exists, attach it to the concept, removing the old one.
                    // If it doesn't exist, create it.
                    LanguageRepresentationDao lrDao =
                            new LanguageRepresentationDaoImpl();
                    for (LanguageRepresentation languageRepresentation
                            : concept.getLanguageRepresentations()) {
                        System.out.
                                print("Language Representation of " + concept);
                        System.out.println(" is " + languageRepresentation);
                        LanguageRepresentation retrievedLanguageRepresentation =
                                lrDao.getSingleLanguageRepresentation(
                                        languageRepresentation.getLanguage(),
                                        languageRepresentation.getText(),
                                        languageRepresentation.getPartOfSpeech(),
                                        languageRepresentation.getUseStatus(),
                                        languageRepresentation.getProductivity(),
                                        languageRepresentation.getNegation(),
                                        languageRepresentation.getOperator());
                        // if Language Representation exists, add the retrieved,
                        // otherwise, add the new one.
                        if (!isNull(retrievedLanguageRepresentation)) {
                            System.out.print("Found Language Representation ");
                            System.out.println(languageRepresentation);
                            newConcept.addLanguageRepresentation(
                                    retrievedLanguageRepresentation,
                                    retrievedLanguageRepresentation.
                                    getIsRepresentative(newConcept));
                        } else {
                            System.out.print(
                                    "Could not find Language Representation ");
                            System.out.println(languageRepresentation);
                            newConcept.addLanguageRepresentation(
                                    new LanguageRepresentation(
                                            languageRepresentation), true);
                        }
                    }

                    cDao.merge(newConcept);
                } else {
                    cDao.merge(oldConcept);
                    System.out.println("");
                }
            }
        }
    }

}
