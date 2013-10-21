/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.entities;

import csri.poeticon.praxicon.Constants;
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
@Table(name="VisualRepresentation")
public class VisualRepresentation implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    private Long Id;

    @Column(name="MediaType")
    private String MediaType;

    @Column(name="Type")
    private String Type;

    @Column(name="Representation")
    private String Representation;

    public VisualRepresentation(String media_type, String representation)
    {
        this.MediaType = media_type;
        this.Representation = representation;
    }

    public VisualRepresentation()
    {
    }



  //  @Column(name = "CONCEPT_ID", insertable=false, updatable=false)
 //   private long ownerID;

//    @ManyToOne(optional=false, cascade=CascadeType.ALL)
//    @JoinColumn(name="VR_GROUP_ID")
//    private VisualRepresentationGroup owner;

    @Column(name="Prototype")
    private boolean Prototype;

//    @XmlTransient
//    public VisualRepresentationGroup getOwner()
//    {
//        return owner;
//    }

    /**
     * @xmlcomments.args
     *	   xmltag="prototype"
     *     xmldescription="This attributes defines that this visual representation
     *     is a prototypical instance of the concept
     */
    @XmlAttribute
    public boolean isPrototype()
    {
        return Prototype;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="vrtype"
     *     xmldescription="This attributes defines more info about the visual representation
     *  (for example, if it is an immitation of the actual action)
     */
    @XmlAttribute
    public String getVisualRepresentationType()
    {
        return Type;
    }

    public void setVisualRepresentationType(String visual_representation_type)
    {
        this.Type = visual_representation_type;
    }

    public void setPrototype(boolean prototype)
    {
        this.Prototype = prototype;
    }
//
//    public void setOwner(VisualRepresentationGroup owner)
//    {
//        this.owner = owner;
//    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;mediaType&gt;"
     *     xmldescription="This tag defines the type of the media that represents
     *     visually the entity
     */
    @XmlElement()
    public String getMediaType()
    {
        return MediaType;
    }

    public void setMediaType(String media_type)
    {
        this.MediaType = media_type;
    }

//    @XmlAttribute(name="owner")
//    public long getOwnerID() {
//        return ownerID;
//    }


    @XmlElement()
    public String getRepresentation()
    {
        return Representation;
    }

    public String getRepresentationWithPath()
    {
        if (Representation.startsWith("http:"))
        {
            return Representation;
        }
        if(Representation.startsWith("file:"))
        {
            return Representation;
        }
        if (this.MediaType.equalsIgnoreCase("image"))
        {
            return Constants.imagePath + Representation;
        }
        else
        {
            return Constants.videoPath + Representation;
        }
    }

    public void setRepresentation(String representation)
    {
        this.Representation = representation;
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
        if (!(object instanceof VisualRepresentation))
        {
            return false;
        }
        VisualRepresentation other = (VisualRepresentation) object;
        if (this.MediaType!=null&& this.Representation!=null&&this.MediaType.equalsIgnoreCase(other.MediaType) && this.Representation.equalsIgnoreCase(other.Representation))
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
        return "[Id=" + Id + "] "+this.MediaType+ ": "+ this.Representation;
    }

//    public void afterUnmarshal(Unmarshaller u, Object parent)
//    {
//        this.owner = (VisualRepresentation)parent;
//    }
}
