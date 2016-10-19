/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@XmlType(name = "ontological_domain",
        namespace = "http://www.csri.gr/ontological_domain")
@Entity
@Table(name = "OntologicalDomains")

public class OntologicalDomain implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @XmlTransient
    @Column(name = "OntologicalDomainId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "DomainName")
    private String domainName;

    @XmlTransient
    @ManyToMany
    @JoinTable(
            name = "OntologicalDomain_Concept",
            joinColumns = {
                @JoinColumn(name = "OntologicalDomainId")},
            inverseJoinColumns = {
                @JoinColumn(name = "ConceptId")}
    )
    private List<Concept> concepts;

    /**
     * Gets the id of the OntologicalDomain.
     *
     * @return long
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id of the OntologicalDomain.
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the OntologicalDomain.
     *
     * @return String
     */
    public String getDomainName() {
        return domainName;
    }

    /**
     * Sets the name of the OntologicalDomain.
     *
     * @param domainName
     */
    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    /**
     * Gets the concepts that belong to an ontological domain.
     *
     * @return a list containing concepts
     *
     */
    public final List<Concept> getConcepts() {
        return concepts;
    }

    /**
     * Sets the concepts that belong to this ontological domain.
     *
     * @param concepts a list of concepts
     *
     */
    public void setConcepts(List<Concept> concepts) {
        this.concepts = concepts;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.domainName);
        hash = 53 * hash + Objects.hashCode(this.concepts);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OntologicalDomain other = (OntologicalDomain)obj;
        if (!Objects.equals(this.domainName, other.domainName)) {
            return false;
        }
        if (!Objects.equals(this.concepts, other.concepts)) {
            return false;
        }
        return true;
    }

//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (id != null ? id.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof OntologicalDomain)) {
//            return false;
//        }
//        OntologicalDomain other = (OntologicalDomain)object;
//        if ((this.id == null && other.id != null) ||
//                (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
    @Override
    public String toString() {
        return "gr.csri.poeticon.praxicon.db.entities.OntologicalDomain[ id=" +
                id + " ]";
    }

}
