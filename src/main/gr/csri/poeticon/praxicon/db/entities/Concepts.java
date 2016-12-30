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
     * Stores all concepts of the collection in the database checking if they
     * already exist in the database.
     */
    public void storeConcepts() {
        if (!concepts.isEmpty()) {
            ConceptDao cDao = new ConceptDaoImpl();
            for (Concept concept : concepts) {
                storeConcept(concept);
                cDao.getEntityManager().clear();
                System.out.println("Storing Concept: " + concept.toString());
            }
        }
    }

    /**
     * Stores a concept in the database checking if it already exists.
     *
     * @param concept
     *
     * @return a concept
     */
    public Concept storeConcept(Concept concept) {
        ConceptDao cDao = new ConceptDaoImpl();
        Concept oldConcept = new Concept(concept, false, false, false);

        Concept retrievedConcept = cDao.getConcept(concept);
        Concept newConcept;
        // If concept does not exist in the database, store it.
        if (!isNull(retrievedConcept)) {
            // Set new concept to retrieved one so as to update it
            newConcept = retrievedConcept;
        } else {
            newConcept = oldConcept;
        }

        // For each language representation, find it in the DB.
        // If it exists, attach it to the concept.
        // If it doesn't exist, create it.
        LanguageRepresentationDao lrDao =
                new LanguageRepresentationDaoImpl();

        if (!concept.getLanguageRepresentations().isEmpty()) {
            for (LanguageRepresentation languageRepresentation
                    : concept.getLanguageRepresentations()) {
                LanguageRepresentation retrievedLanguageRepresentation =
                        lrDao.getSingleLanguageRepresentation(
                                languageRepresentation.getLanguage(),
                                languageRepresentation.getText(),
                                languageRepresentation.getPartOfSpeech(),
                                languageRepresentation.getUseStatus(),
                                languageRepresentation.getProductivity(),
                                languageRepresentation.getNegation(),
                                languageRepresentation.getOperator());
                // if Language Representation exists add the retrieved,
                // otherwise, add the new one.
                if (!isNull(retrievedLanguageRepresentation)) {
                    //check if already assigned to concept
                    if (!newConcept.getLanguageRepresentations().contains(
                            retrievedLanguageRepresentation)) {
                        newConcept.addLanguageRepresentation(
                                retrievedLanguageRepresentation,
                                retrievedLanguageRepresentation.
                                getIsRepresentative(newConcept));
                    }
                } else {
                    LanguageRepresentation newLanguageRepresentation =
                            new LanguageRepresentation(languageRepresentation);
                    //check if already assigned to concept
                    if (!newConcept.getLanguageRepresentations().
                            contains(newLanguageRepresentation)) {
                        newConcept.addLanguageRepresentation(
                                newLanguageRepresentation,
                                languageRepresentation.getIsRepresentative(
                                        concept));
                    }
                }
            }
        }

        // If Motoric Representations exist, add them to new concept
        if (!concept.getMotoricRepresentations().isEmpty()) {
            for (MotoricRepresentation mr : concept.
                    getMotoricRepresentations()) {
                mr.setConcept(newConcept);
                //check if already assigned to concept
                if (!newConcept.getMotoricRepresentations().contains(mr)) {
                    newConcept.addMotoricRepresentation(mr);
                }
            }
        }

        // If Visual Representations exist, add them to new concept
        if (!concept.getVisualRepresentations().isEmpty()) {
            for (VisualRepresentation vr : concept.getVisualRepresentations()) {
                vr.setConcept(newConcept);
                //check if already assigned to concept
                if (!newConcept.getVisualRepresentations().contains(vr)) {
                    newConcept.addVisualRepresentation(vr);
                }
            }
        }

        // If Ontological Domains exist, add them to new concept
        if (!concept.getOntologicalDomains().isEmpty()) {
            for (OntologicalDomain od : concept.getOntologicalDomains()) {
                //check if already assigned to concept
                if (!newConcept.getOntologicalDomains().contains(od)) {
                    newConcept.addOntologicalDomain(od);
                }
            }
        }

        // If Concept doesn't exist, add it
        if (isNull(retrievedConcept)) {
            cDao.persist(newConcept);
            return newConcept;
        } else {
            cDao.merge(retrievedConcept);
            return retrievedConcept;
        }

    }
}
