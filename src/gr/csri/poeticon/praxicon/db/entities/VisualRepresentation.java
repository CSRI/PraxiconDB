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
    //@Index(columnList = "Uri"),
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
    @Column(name = "VisualRepresentationId")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CUST_SEQ")
    private Long id;

    @Column(name = "MediaType")
    @NotNull(message = "Media type must be specified.")
    private media_type mediaType;

    @Column(name = "Name")
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    private Concept concept;

    @Column(name = "Uri")
    @NotNull(message = "URI must be specified.")
    private URI uri;

    @Column(name = "Comment")
    private String comment;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "VisualRepresentation_RelationSubject",
            joinColumns = {
                @JoinColumn(name = "VisualRepresentationId")},
            inverseJoinColumns = {
                @JoinColumn(name = "RelationId")}
    )
    private List<Relation> relationsWithVisualRepresentationAsSubject;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "VisualRepresentation_RelationObject",
            joinColumns = {
                @JoinColumn(name = "VisualRepresentationId")},
            inverseJoinColumns = {
                @JoinColumn(name = "RelationId")}
    )
    private List<Relation> relationsWithVisualRepresentationAsObject;

    @ManyToOne(cascade = CascadeType.ALL)
    private MotoricRepresentation motoricRepresentation;

    public VisualRepresentation(media_type media_type, String name) {
        this.mediaType = media_type;
        this.name = name;
    }

    public VisualRepresentation() {
    }

    /**
     * @return the media type of this visual representation.
     * @xmlcomments.args xmltag="&lt;media_type&gt;" xmldescription="This tag
     * defines the type of the media that represents visually the entity
     */
    public media_type getMediaType() {
        return mediaType;
    }

    public void setMediaType(media_type mediaType) {
        this.mediaType = mediaType;
    }

    /**
     * @return the URI of the visual representation. Usually a URL or file path.
     * @xmlcomments.args xmltag="&lt;uri&gt;" xmldescription="This tag defines
     * the URI of the media."
     */
    public URI getURI() {
        return uri;
    }

    public void setURI(URI uri) {
        this.uri = uri;
    }

    public void setURI(String uri) {
        if (this.uri.resolve(uri) != null) {
            this.uri = this.uri.resolve(uri);
        }
    }

    public String getRepresentation() {
        return name;
    }

    public String getRepresentationWithPath() {
        if (name.startsWith("http:")) {
            return name;
        }
        if (name.startsWith("file:")) {
            return name;
        }
        if (this.mediaType.equals(media_type.IMAGE)) {
            return Constants.imagePath + name;
        } else {
            return Constants.videoPath + name;
        }
    }

    public void setRepresentation(String name) {
        this.name = name;
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
        // TODO: Warning - method won't work in case the id fields are not set
        if (!(object instanceof VisualRepresentation)) {
            return false;
        }
        VisualRepresentation other = (VisualRepresentation)object;
        if (this.mediaType != null && this.name != null &&
                this.mediaType.equals(other.mediaType) &&
                this.name.equalsIgnoreCase(other.name)) {
            return true;
        }
        if ((this.id == null && other.id != null) ||
                (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        if (this.id == null && other.id == null) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[Id=" + id + "] " + this.mediaType + ": " + this.name;
    }

    public void afterUnmarshal(Unmarshaller u, Object parent) {
//        this.owner = (VisualRepresentation)parent;
    }
}
