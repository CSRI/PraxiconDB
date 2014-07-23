/*
 * To change this template, choose Tools | Templates
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
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author dmavroeidis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "constituent", namespace = "http://www.csri.gr/constituent")
@Entity
@Table(name = "Constituents", indexes = {
    @Index(columnList = "ConstituentId")})
public class Constituent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "CUST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CUST_SEQ")
    @Column(name = "ConstituentId")
    private Long id;

    @Column(name = "ConstituentOrder")
    private short order;

    // Foreign key
    @ManyToOne(cascade = CascadeType.ALL)
    private LanguageRepresentation constituentLanguageRepresentation;

    // Foreign key
    @ManyToOne(cascade = CascadeType.ALL)
    private LanguageRepresentation languageRepresentation;

    @XmlAttribute
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlAttribute
    public short getOrder() {
        return order;
    }

    public void setOrder(short order) {
        this.order = order;
    }

    @XmlAttribute
    public LanguageRepresentation getConstituentLanguageRepresentation() {
        return constituentLanguageRepresentation;
    }

    public void setConstituentLanguageRepresentation(LanguageRepresentation clr) {
        this.constituentLanguageRepresentation = clr;
    }

    @XmlAttribute
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - method won't work in case the id fields are not set
        if (!(object instanceof Constituent)) {
            return false;
        }
        Constituent other = (Constituent)object;
        if ((this.id == null && other.id != null) ||
                (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gr.csri.poeticon.praxicon.db.entities.Constituents[id=" + id +
                "]";
    }
}
