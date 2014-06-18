/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author dmavroeidis
 */
@XmlType(name = "compositionality",
        namespace = "http://www.csri.gr/compositionality")
@Entity
@Table(name = "Compositionality", indexes = {@Index(columnList="CompositionalityId")})
public class Compositionality implements Serializable {

    public static enum CompositionalityType {

        MULTIWORD, COMPOSITE_WORD, UNKNOWN;

        @Override
        public String toString() {
            return this.name();
        }
    }

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "CUST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CUST_SEQ")
    @Column(name = "CompositionalityId")
    private Long id;

    @Column(name = "CompositionalityType")
    @Enumerated(EnumType.STRING)
    private CompositionalityType compositionalityType;

    // Foreign key
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "compositionality")
    private List<LanguageRepresentation> languageRepresentation;

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
        // TODO: Warning - method won't work in case the id fields are not set
        if (!(object instanceof Compositionality)) {
            return false;
        }
        Compositionality other = (Compositionality)object;
        if ((this.id == null && other.id != null) ||
                (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gr.csri.poeticon.praxicon.db.entities.Compositionality[id=" +
                id + "]";
    }
}
