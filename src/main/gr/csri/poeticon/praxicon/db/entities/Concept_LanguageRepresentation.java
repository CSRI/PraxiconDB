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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author dmavroeidis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "concept_languagerepresentation", namespace =
        "http://www.csri.gr/concept_languagerepresentation")
@Entity
@Table(name = "Concepts_LanguageRepresentations")
//, indexes = {
//    @Index(columnList = "Concept_LanguageRepresentationId")
//})

public class Concept_LanguageRepresentation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CUST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CUST_SEQ")
    @Column(name = "Concept_LanguageRepresentationId")
//    @XmlAttribute
    @XmlTransient
    private Long id;

    @Column(name = "RepresentativeLanguageRepresentation")
    private boolean isRepresentativeLanguageRepresentation;

    @XmlTransient
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "ConceptId")
    private Concept concept;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "LanguageRepresentationId")
    private LanguageRepresentation languageRepresentation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    /**
     * @return whether the language representation is representative or not.
     */
    public boolean getIsRepresentative() {
        return isRepresentativeLanguageRepresentation;
    }

    public void setIsRepresentative(boolean isRepresentative) {
        this.isRepresentativeLanguageRepresentation = isRepresentative;
    }

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

    @Override
    public int hashCode() {
        int hash = 0;
        if (id != null) {
            hash += id.hashCode();
        } else {
            hash = 0;
        }
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - method won't work in case the id fields are not set
        if (!(object instanceof Concept_LanguageRepresentation)) {
            return false;
        }
        Concept_LanguageRepresentation other =
                (Concept_LanguageRepresentation)object;
        if (this.languageRepresentation.equals(other.languageRepresentation)) {
            return true;
        }

        if ((this.id == null && other.id != null) ||
                (this.id != null && !this.id.equals(other.id))) {
            return false;
        }

        if (this.id == null && other.id == null) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gr.csri.poeticon.praxicon.db.entities.Concept_LanguageRepre" +
                "sentation[ id=" + id + " ]";
    }

}
