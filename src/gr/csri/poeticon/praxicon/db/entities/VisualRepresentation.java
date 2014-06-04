/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import gr.csri.poeticon.praxicon.Constants;
import java.io.Serializable;
import java.net.URI;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author dmavroeidis
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "visual_representation",
        namespace = "http://www.csri.gr/visual_representation")
@Entity
@Table(name = "VisualRepresentations", indexes = {
    @Index(columnList = "Name"),
    @Index(columnList = "Uri"),
    @Index(columnList = "VisualRepresentationId")})
public class VisualRepresentation implements Serializable {

    public static enum media_type {

        IMAGE, VIDEO;

        @Override
        public String toString() {
            return this.name();
        }
    }

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "CUST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CUST_SEQ")
    private Long Id;

    @Column(name = "MediaType")
    @NotNull(message = "Media type must be specified.")
    private media_type MediaType;

    @Column(name = "Name")
    private String Name;

    @ManyToOne(cascade = CascadeType.ALL)
    private Concept Concept;

    @Column(name = "Uri")
    @NotNull(message = "URI must be specified.")
    private URI Uri;

    @Column(name = "Comment")
    private String Comment;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "VisualRepresentation_RelationSubject",
            joinColumns = {
                @JoinColumn(name = "VisualRepresentationId")},
            inverseJoinColumns = {
                @JoinColumn(name = "RelationId")}
    )
    private List<Relation> RelationsWithVisualRepresentationAsSubject;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "VisualRepresentation_RelationObject",
            joinColumns = {
                @JoinColumn(name = "VisualRepresentationId")},
            inverseJoinColumns = {
                @JoinColumn(name = "RelationId")}
    )
    private List<Relation> RelationsWithVisualRepresentationAsObject;

    @ManyToOne(cascade = CascadeType.ALL)
    private MotoricRepresentation MotoricRepresentation;

    public VisualRepresentation(media_type media_type, String name) {
        this.MediaType = media_type;
        this.Name = name;
    }

    public VisualRepresentation() {
    }

    /**
     * @return the media type of this visual representation.
     * @xmlcomments.args xmltag="&lt;media_type&gt;" xmldescription="This tag
     * defines the type of the media that represents visually the entity
     */
    public media_type getMediaType() {
        return MediaType;
    }

    public void setMediaType(media_type mediaType) {
        this.MediaType = mediaType;
    }

    /**
     * @return the URI of the visual representation. Usually a URL or file path.
     * @xmlcomments.args xmltag="&lt;uri&gt;" xmldescription="This tag defines
     * the URI of the media."
     */
    public URI getURI() {
        return Uri;
    }

    public void setURI(URI uri) {
        this.Uri = uri;
    }

    public void setURI(String uri) {
        if (Uri.resolve(uri) != null) {
            this.Uri = Uri.resolve(uri);
        }
    }

    public String getRepresentation() {
        return Name;
    }

    public String getRepresentationWithPath() {
        if (Name.startsWith("http:")) {
            return Name;
        }
        if (Name.startsWith("file:")) {
            return Name;
        }
        if (this.MediaType.equals(media_type.IMAGE)) {
            return Constants.imagePath + Name;
        } else {
            return Constants.videoPath + Name;
        }
    }

    public void setRepresentation(String name) {
        this.Name = name;
    }

    @XmlAttribute
    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        this.Id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (Id != null ? Id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - method won't work in case the id fields are not set
        if (!(object instanceof VisualRepresentation)) {
            return false;
        }
        VisualRepresentation other = (VisualRepresentation) object;
        if (this.MediaType != null && this.Name != null &&
                this.MediaType.equals(other.MediaType) &&
                this.Name.equalsIgnoreCase(other.Name)) {
            return true;
        }
        if ((this.Id == null && other.Id != null) ||
                (this.Id != null && !this.Id.equals(other.Id))) {
            return false;
        }
        if (this.Id == null && other.Id == null) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[Id=" + Id + "] " + this.MediaType + ": " + this.Name;
    }

    public void afterUnmarshal(Unmarshaller u, Object parent) {
//        this.owner = (VisualRepresentation)parent;
    }
}
