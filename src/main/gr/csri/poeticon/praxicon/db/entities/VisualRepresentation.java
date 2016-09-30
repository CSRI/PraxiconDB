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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
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
    @SequenceGenerator(name = "CUST_SEQ", allocationSize = 1)
    @Column(name = "VisualRepresentationId")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CUST_SEQ")
    private Long id;

    @Column(name = "MediaType")
    @NotNull(message = "Media type must be specified.")
    private MediaType mediaType;

    @Column(name = "Name")
    private String name;

    @XmlTransient
    @ManyToOne(cascade = CascadeType.ALL)
    private Concept concept;

    @XmlTransient
    @ManyToOne(cascade = CascadeType.ALL)
    private RelationSet relationSet;

    @Column(name = "Source")
    private String source;

    @Column(name = "Uri")
    @NotNull(message = "URI must be specified.")
    private URI uri;

    @Column(name = "Comment")
    private String comment;

    @XmlTransient
    @ManyToOne(cascade = CascadeType.ALL)
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
    public URI getURI() {
        return uri;
    }

    public void setURI(URI uri) {
        this.uri = uri;
    }

    public void setURI(String uri) throws URISyntaxException,
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.mediaType);
        hash = 47 * hash + Objects.hashCode(this.name);
        hash = 47 * hash + Objects.hashCode(this.source);
        hash = 47 * hash + Objects.hashCode(this.uri);
        hash = 47 * hash + Objects.hashCode(this.motoricRepresentation);
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
        if (this.mediaType != other.mediaType) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.source, other.source)) {
            return false;
        }
        if (!Objects.equals(this.uri, other.uri)) {
            return false;
        }
        if (!Objects.equals(this.motoricRepresentation,
                other.motoricRepresentation)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[Id=" + id + "] " + this.mediaType + " : " + this.name + " : " +
                this.uri;
    }
}
