package gr.csri.poeticon.praxicon.db.entities;

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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.Unmarshaller;
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
@XmlType(name = "motoric_representation",
        namespace = "http://www.csri.gr/motoric_representation")
@Entity
@NamedQueries({
    @NamedQuery(name = "getMotoricRepresentationEntityQuery", query =
            "FROM MotoricRepresentation mr " +
            "WHERE UPPER(mr.comment) = :comment"),})
@Table(name = "MotoricRepresentations", indexes = {
    @Index(columnList = "MotoricRepresentationId")})
public class MotoricRepresentation implements Serializable {

    public static enum PerformingAgent {

        ADULT, CHILD, ICUB, NAO, PR2;

        @Override
        public String toString() {
            return this.name();
        }
    }

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "CUST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CUST_SEQ")
    @Column(name = "MotoricRepresentationId")
//    @XmlAttribute
    @XmlTransient    
    private Long id;

    @Column(name = "PerformingAgent")
    @NotNull(message = "Performing agent must be specified.")
    private PerformingAgent performingAgent;

    @Column(name = "Source")
    private String source;

    @Column(name = "URI")
    @NotNull(message = "URI must be specified.")
    private URI uri;

    @Column(name = "Comment")
    private String comment;

    @XmlTransient
    @ManyToOne(cascade = CascadeType.ALL)
    private Concept concept;

    @XmlTransient
    @ManyToOne(cascade = CascadeType.ALL)
    private RelationSet relationSet;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "motoricRepresentation")
    private List<VisualRepresentation> visualRepresentations;

    public MotoricRepresentation() {
    }

    public String getVisualRepresentation() {
        return null;
    }

    public String getMediaType() {
        return "video";
    }

    /**
     * @return the source of the visual representation.
     *         This can be ImageNet, GoogleImages, etc.
     */
    public PerformingAgent getPerformingAgent() {
        return performingAgent;
    }

    public void setSource(PerformingAgent performingAgent) {
        this.performingAgent = performingAgent;
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
     * @return the source of the visual representation.
     *         This can be ImageNet, GoogleImages, etc.
     */
    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public void setUri(String uri) {
        if (this.uri.resolve(uri) != null) {
            this.uri = this.uri.resolve(uri);
        }
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        if (id != null) {
            hash += id.hashCode();
        } else {
            hash = 0;
        }
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - method won't work in case the id fields are not set
        if (!(object instanceof MotoricRepresentation)) {
            return false;
        }
        MotoricRepresentation other = (MotoricRepresentation)object;
        if (this.comment != null &&
                this.comment.equalsIgnoreCase(other.comment)) {
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
        return "gr.csri.poeticon.praxicon.db.entities.MotoricRepresentation" +
                "[id=" + id + "]";
    }

    public void afterUnmarshal(Unmarshaller u, Object parent) {
        //this.owner = (MotoricRepresentationGroup)parent;
    }
}
