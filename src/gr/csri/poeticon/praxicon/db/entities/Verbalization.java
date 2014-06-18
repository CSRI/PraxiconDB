/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author dmavroeidis
 */
@XmlType(name = "verbalization", namespace = "http://www.csri.gr/verbalization")
@Entity
@Table(name = "Verbalizations", indexes = {
    @Index(columnList = "VerbalizationId")})
public class Verbalization implements Serializable {

    public static enum allowed {

        YES, NO, UNKNOWN;

        @Override
        public String toString() {
            return this.name();
        }
    }

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "CUST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CUST_SEQ")
    @Column(name = "VerbalizationId")
    private Long id;

    @Column(name = "Allowed")
    @Enumerated(EnumType.STRING)
    private allowed allowed;

    @OneToOne(cascade = CascadeType.ALL)
    private Concept concept;

    @OneToOne(cascade = CascadeType.ALL)
    private LanguageRepresentation languageRepresentation;

    @OneToOne(cascade = CascadeType.ALL)
    private Relation relation;

    @XmlAttribute
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Verbalization)) {
            return false;
        }
        Verbalization other = (Verbalization)object;
        if ((this.id == null && other.id != null) ||
                (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gr.csri.poeticon.praxicon.db.entities.Verbalization[id=" + id +
                "]";
    }
}
