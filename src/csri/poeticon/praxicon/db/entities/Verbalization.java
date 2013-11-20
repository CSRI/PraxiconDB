/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author dmavroeidis
 */
@XmlRootElement()
@Entity
@Table(name="Verbalization")
public class Verbalization implements Serializable {

    public static enum allowed
    {
        YES, NO, UNKNOWN ;
        @Override
        public String toString()
        {
            return this.name();
        }
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="Allowed")
    @Enumerated(EnumType.STRING)
    private allowed Allowed;

    @OneToOne(cascade=CascadeType.ALL)
    private Concept Concept;

    @OneToOne(cascade=CascadeType.ALL)
    private LanguageRepresentation LanguageRepresentation;

    @OneToOne(cascade=CascadeType.ALL)
    private Relation Relation;



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
        Verbalization other = (Verbalization) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "csri.poeticon.praxicon.db.entities.Verbalization[id=" + id + "]";
    }

}
