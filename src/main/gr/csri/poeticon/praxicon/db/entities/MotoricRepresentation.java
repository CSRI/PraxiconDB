package gr.csri.poeticon.praxicon.db.entities;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@XmlType(name = "motoric_representation",
        namespace = "http://www.csri.gr/motoric_representation")
@Entity
@NamedQueries({
    @NamedQuery(name = "getMotoricRepresentationEntityQuery", query =
            "FROM MotoricRepresentation mr " +
            "WHERE UPPER(mr.comment) = :comment"),})
@Table(name = "MotoricRepresentations"//,
//        uniqueConstraints = {
//            @UniqueConstraint(columnNames = {"PerformingAgent", "Source", "URI"
//        }),}
)
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
    @XmlTransient
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MotoricRepresentationId")
    private Long id;

    @Column(name = "PerformingAgent")
    @NotNull(message = "Performing agent must be specified.")
    @Enumerated(EnumType.STRING)
    private PerformingAgent performingAgent;

    @Column(name = "Source")
    private String source;

    @Column(name = "URI")
    @NotNull(message = "URI must be specified.")
    private URI uri;

    @Column(name = "Comment")
    private String comment;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "concept_ConceptId")
    private Concept concept;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "relationSet_RelationSetId")
    private RelationSet relationSet;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "motoricRepresentation")
    private List<VisualRepresentation> visualRepresentations;

    public MotoricRepresentation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the source of the visual representation.
     *         This can be ImageNet, GoogleImages, etc.
     */
    public PerformingAgent getPerformingAgent() {
        return performingAgent;
    }

    public void setPerformingAgent(PerformingAgent performingAgent) {
        this.performingAgent = performingAgent;
    }

    /**
     * @return the concept related to this Motoric Representation
     */
    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }

    /**
     * @return the relation set related to this Motoric Representation
     */
    public RelationSet getRelationSet() {
        return relationSet;
    }

    public void setRelationSet(RelationSet relationSet) {
        this.relationSet = relationSet;
    }

    public List<VisualRepresentation> getVisualRepresentation() {
        return visualRepresentations;
    }

    public void setVisualRepresentation(
            List<VisualRepresentation> visualRepresentations) {
        this.visualRepresentations = visualRepresentations;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.performingAgent);
        hash = 17 * hash + Objects.hashCode(this.source);
        hash = 17 * hash + Objects.hashCode(this.uri);
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
        final MotoricRepresentation other = (MotoricRepresentation)obj;
        if (!this.performingAgent.equals(other.getPerformingAgent())) {
            return false;
        }
        if (!this.source.equals(other.getSource())) {
            return false;
        }
        if (!this.uri.equals(other.getUri())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gr.csri.poeticon.praxicon.db.entities.MotoricRepresentation" +
                "[id=" + id + "]";
    }

}
