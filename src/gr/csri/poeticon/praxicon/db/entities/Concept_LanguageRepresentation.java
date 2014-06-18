/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author dmavroeidis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "concept_languagerepresentation", namespace =
        "http://www.csri.gr/concept_languagerepresentation")
@XmlRootElement(name = "concept_languagerepresentation", namespace =
        "http://www.csri.gr/concept_languagerepresentation")
@Entity
@Table(name = "Concepts_LanguageRepresentations", indexes = {
    @Index(columnList = "Concept_LanguageRepresentationId")})

public class Concept_LanguageRepresentation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @XmlElement(required = true)
    @SequenceGenerator(name = "CUST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CUST_SEQ")
    @Column(name = "Concept_LanguageRepresentationId")
    private Long id;

    @Column(name = "RepresentativeLanguageRepresentation")
    @Min(0)
    @Max(10)
    private int representativeLanguageRepresentation;
    
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "ConceptId")
    private Concept concept;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "LanguageRepresentationId")
    private LanguageRepresentation languageRepresentation;

    /**
     * @return the language representation.
     */
    public LanguageRepresentation getLanguageRepresentation() {
        return languageRepresentation;
    }

    public void setLanguageRepresentation(
            LanguageRepresentation languageRepresentation) {
        this.languageRepresentation = languageRepresentation;
    }

    /**
     * @return the concept.
     */
    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }
}
