package csri.poeticon.praxicon.db.entities;

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
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Erevodifwntas
 */
@XmlRootElement()
@Entity
@Table(name="MotoricRepresentation")
public class MotoricRepresentation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    private Long id;

    @Column(name="Presentation")
    private String representation;

    public MotoricRepresentation()
    {
    }

    //@XmlElement()
    public String getVisualRepresentation()
    {
        return null;
    }

    public String getMediaType()
    {
        return "video";
    }

    public String getRepresentationWithPath() {
        //NOT IMPLEMENTED YET
        return "";
    }

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="MR_GROUP_ID")
    private MotoricRepresentationGroup owner;

//    @XmlTransient
//    public MotoricRepresentation getOwner() {
//        return owner;
//    }
//
//    public void setOwner(MotoricRepresentation owner) {
//        this.owner = owner;
//    }

    @XmlElement()
    public String getRepresentation() {
        return representation;
    }

    public void setRepresentation(String representation) {
        this.representation = representation;
    }

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
        if (!(object instanceof MotoricRepresentation)) {
            return false;
        }
        MotoricRepresentation other = (MotoricRepresentation) object;
        if (this.representation!=null&&this.representation.equalsIgnoreCase(other.representation))
        {
            return true;
        }
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        if (this.id == null && other.id == null)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "csri.poeticon.praxicon.db.entities.MotoricRepresentation[id=" + id + "]";
    }

    public void afterUnmarshal(Unmarshaller u, Object parent) {
        this.owner = (MotoricRepresentationGroup)parent;
    }
}
