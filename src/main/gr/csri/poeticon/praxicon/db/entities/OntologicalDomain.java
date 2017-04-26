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
import org.apache.commons.lang.builder.EqualsBuilder;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
     * @return a set containing concepts
     *
     */
    public final List<Concept> getConcepts() {
        return concepts;
    }

    /**
     * Sets the concepts that belong to this ontological domain.
     *
     * @param concepts a set of concepts
     *
     */
    public void setConcepts(List<Concept> concepts) {
        this.concepts = concepts;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.domainName);
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
                EqualsBuilder eb = new EqualsBuilder();
        eb.append(this.domainName, other.getDomainName());
        return eb.isEquals();
    }

    @Override
    public String toString() {
        return "gr.csri.poeticon.praxicon.db.entities.OntologicalDomain[ id=" +
                id + " ]";
    }

}
