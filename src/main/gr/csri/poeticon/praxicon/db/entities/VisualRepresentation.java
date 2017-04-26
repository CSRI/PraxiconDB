/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import gr.csri.poeticon.praxicon.Constants;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import static java.util.Objects.isNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author dmavroeidis
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "visual_representation",
        namespace = "http://www.csri.gr/visual_representation")
@Entity
@Table(name = "VisualRepresentations",
        indexes = {
            @Index(columnList = "Name")}//,
//                uniqueConstraints = {
//            @UniqueConstraint(columnNames = {"MediaType", "Name", "Source" ,
//                "Uri"
//            }),}

)
public class VisualRepresentation implements Serializable {

    public static enum MediaType {

        IMAGE, VIDEO;

        @Override
        public String toString() {
            return this.name();
        }
    }

    private static final long serialVersionUID = 1L;
    @Id
    @XmlTransient
    @Column(name = "VisualRepresentationId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MediaType")
    @NotNull(message = "Media type must be specified.")
    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @Column(name = "Name")
    private String name;

    @XmlTransient
    @ManyToOne
    private Concept concept;

    @XmlTransient
    @ManyToOne
    private RelationSet relationSet;

    @Column(name = "Source")
    private String source;

    @Column(name = "Uri", columnDefinition = "BLOB")
    @NotNull(message = "URI must be specified.")
    private URI uri;

    @Column(name = "Comment")
    private String comment;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "MotoricRepresentation_motoricRepresentationId")
    private MotoricRepresentation motoricRepresentation;

    public VisualRepresentation(MediaType mediaType, String name) {
        this.mediaType = mediaType;
        this.name = name;
    }

    public VisualRepresentation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the media type of this visual representation.
     */
    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    /**
     * @return the concept connected to the visual representation.
     */
    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }

    /**
     * @return the relation set connected to the visual representation.
     */
    public RelationSet getRelationSet() {
        return relationSet;
    }

    public void setRelationSet(RelationSet relationSet) {
        this.relationSet = relationSet;
    }

    /**
     * @return the source of the visual representation.
     *         This can be ImageNet, GoogleImages, etc.
     */
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the URI of the visual representation. Usually a URL or file path.
     */
    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public void setUri(String uri) throws URISyntaxException,
            MalformedURLException {
        String[] schemes = {"http", "https"};
        URL url = null;
        try {
            url = new URL(uri);
            this.uri = url.toURI();
        } catch (MalformedURLException em) {
            System.err.println("URL " + uri + " is malformed.");
        } catch (URISyntaxException eu) {
            System.err.println("URI syntax exception " + url);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRepresentationWithPath() {
        if (uri.toString().startsWith("http:")) {
            return uri.toString();
        }
        if (uri.toString().startsWith("file:")) {
            return uri.toString();
        }
        if (this.mediaType.equals(MediaType.IMAGE)) {
            return Constants.imagePath + uri.toString();
        } else {
            return Constants.videoPath + uri.toString();
        }
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public MotoricRepresentation getMotoricRepresentation() {
        return motoricRepresentation;
    }

    public void setMotoricRepresentation(
            MotoricRepresentation motoricRepresentation) {
        this.motoricRepresentation = motoricRepresentation;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.mediaType);
        hash = 47 * hash + Objects.hashCode(this.name);
        hash = 47 * hash + Objects.hashCode(this.source);
        hash = 47 * hash + Objects.hashCode(this.uri);
//        hash = 47 * hash + Objects.hashCode(this.motoricRepresentation);
        return hash;
    }

    @Override
    /*
     * TODO: Create a realistic equals() method
     */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VisualRepresentation other = (VisualRepresentation)obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(this.mediaType, other.getMediaType());
        eb.append(this.name, other.getName());
        eb.append(this.source, other.getSource());
        eb.append(this.uri, other.getUri());
        return eb.isEquals();
    }

    @Override
    public String toString() {
        if (isNull(this.name) || this.name.equals("")) {
            return this.mediaType + "\\" + this.uri;
        } else {
            return this.name + "\\" + this.mediaType + "\\" + this.uri;
        }
    }
}
