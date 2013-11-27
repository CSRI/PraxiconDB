package csri.poeticon.praxicon.db.entities;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.ManyToMany;
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
 * @author Erevodifwntas
 *
 */
@XmlRootElement()
@Entity
@Table(name="MotoricRepresentations")
public class MotoricRepresentation implements Serializable
{

    public static enum performing_agent
    {
        ADULT, CHILD, ICUB, NAO, PR2;
        @Override
        public String toString()
        {
            return this.name();
        }
    }

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    private Long Id;

    @Column(name="Comment")
    private String Comment;

    @Column(name="PerformingAgent")
    private performing_agent PerformingAgent;

    @Column(name="URI")
    private URI URI;

    @ManyToOne(cascade=CascadeType.ALL)
    private Concept Concept;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="MotoricRepresentation_RelationSubject",
        joinColumns={@JoinColumn(name="MotoricRepresentationId")},
        inverseJoinColumns={@JoinColumn(name="RelationId")}
    )
    private List<Relation> RelationsWithMotoricRepresentationAsSubject;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="MotoricRepresentation_RelationObject",
        joinColumns={@JoinColumn(name="MotoricRepresentationId")},
        inverseJoinColumns={@JoinColumn(name="RelationId")}
    )
    private List<Relation> RelationsWithMotoricRepresentationAsObject;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="MotoricRepresentation")
    private List<VisualRepresentation> VisualRepresentations;

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

    public String getRepresentationWithPath()
    {
        //NOT IMPLEMENTED YET
        return "";
    }

//    @ManyToOne(optional=false, cascade=CascadeType.ALL)
//    @JoinColumn(name="MR_GROUP_ID")
//    private MotoricRepresentationGroup owner;

//    @XmlTransient
//    public MotoricRepresentation getOwner() {
//        return owner;
//    }
//
//    public void setOwner(MotoricRepresentation owner) {
//        this.owner = owner;
//    }

    @XmlElement()
    public String getComment()
    {
        return Comment;
    }

    public void setComment(String comment)
    {
        this.Comment = comment;
    }

    @XmlAttribute
    public Long getId()
    {
        return Id;
    }

    public void setId(Long id)
    {
        this.Id = id;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (Id != null ? Id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MotoricRepresentation))
        {
            return false;
        }
        MotoricRepresentation other = (MotoricRepresentation) object;
        if (this.Comment!=null && this.Comment.equalsIgnoreCase(other.Comment))
        {
            return true;
        }
        if ((this.Id == null && other.Id != null) || (this.Id != null && !this.Id.equals(other.Id)))
        {
            return false;
        }
        if (this.Id == null && other.Id == null)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "csri.poeticon.praxicon.db.entities.MotoricRepresentation[id=" + Id + "]";
    }

//    public void afterUnmarshal(Unmarshaller u, Object parent)
//    {
//        this.owner = (MotoricRepresentationGroup)parent;
//    }
}
