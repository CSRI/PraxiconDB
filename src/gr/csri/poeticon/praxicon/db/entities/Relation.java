/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import gr.csri.poeticon.praxicon.db.dao.RelationTypeDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationTypeDaoImpl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author dmavroeidis
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "relation", namespace = "http://www.csri.gr/relation")
@Entity
@NamedQueries({
    @NamedQuery(name = "findRelationsByRelationTypeConcept", query =
            "SELECT r FROM Relation r, RelationType rt " +
            "WHERE ((r.subject = :subject_id " +
            "OR r.object = :object_id) " +
            "AND rt.id = :relation_type_id)"),
    @NamedQuery(name = "findRelationsByConceptObjectOrSubject", query =
            "SELECT r FROM Relation r " +
            "WHERE r.object = :concept OR r.subject = :concept"),
    @NamedQuery(name = "findRelationsByConceptRelationType", query =
            "SELECT r FROM Relation r, RelationType rt " +
            "WHERE (r.subject = :concept_id OR r.object = :concept_id) " +
            "AND r.type = rt " +
            "AND rt.forwardName = :relation_type"),
    @NamedQuery(name = "findRelationsByConceptObject", query =
            "SELECT r FROM Relation r " +
            "WHERE r.object = :concept_id"),
    @NamedQuery(name = "findRelationsByConceptSubject", query =
            "SELECT r FROM Relation r " +
            "WHERE r.subject = :concept_id"),
    @NamedQuery(name = "areRelated", query =
            "SELECT r FROM Relation r " +
            "WHERE (r.subject = :concept_id_subject " +
            "AND r.object = :concept_id_object) " +
            "OR (r.subject = :concept_id_object " +
            "AND r.object = :concept_id_subject)"),})
@Table(name = "Relations", indexes = {
    @Index(columnList = "Comment"),
    @Index(columnList = "RelationId")})
public class Relation implements Serializable {

    public static enum derivation_supported {

        YES, NO, UNKNOWN;

        @Override
        public String toString() {
            return this.name();
        }
    }

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "CUST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CUST_SEQ")
    @Column(name = "RelationId")
    private Long id;

    @Column(name = "Comment")
    private String comment;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    //@JoinColumn(name="Id")
    private RelationType type;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    //@JoinColumn(name = "RelationArgumentId")
    @NotNull(message = "Object of relation must be specified.")
    private RelationArgument object;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    //@JoinColumn(name = "RelationArgumentId")
    @NotNull(message = "Subject of relation must be specified.")
    private RelationArgument subject;

    @Column(name = "DerivationSupported")
    @NotNull(message = "Derivation support must be specified.")
    @Enumerated(EnumType.STRING)
    private derivation_supported derivationSupported;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "LanguageRepresentation_RelationSubject",
            joinColumns = {
                @JoinColumn(name = "RelationId")},
            inverseJoinColumns = {
                @JoinColumn(name = "LanguageRepresentationId")}
    )
    private List<LanguageRepresentation> languageRepresentationSubject;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "LanguageRepresentation_RelationObject",
            joinColumns = {
                @JoinColumn(name = "RelationId")},
            inverseJoinColumns = {
                @JoinColumn(name = "LanguageRepresentationId")}
    )
    private List<LanguageRepresentation> languageRepresentationObject;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "MotoricRepresentation_RelationSubject",
            joinColumns = {
                @JoinColumn(name = "RelationId")},
            inverseJoinColumns = {
                @JoinColumn(name = "MotoricRepresentationId")}
    )
    private List<MotoricRepresentation> motoricRepresentationSubject;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "MotoricRepresentation_RelationObject",
            joinColumns = {
                @JoinColumn(name = "RelationId")},
            inverseJoinColumns = {
                @JoinColumn(name = "MotoricRepresentationId")}
    )
    private List<MotoricRepresentation> motoricRepresentationObject;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "VisualRepresentation_RelationSubject",
            joinColumns = {
                @JoinColumn(name = "RelationId")},
            inverseJoinColumns = {
                @JoinColumn(name = "VisualRepresentationId")}
    )
    private List<VisualRepresentation> visualRepresentationSubject;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "VisualRepresentation_RelationObject",
            joinColumns = {
                @JoinColumn(name = "RelationId")},
            inverseJoinColumns = {
                @JoinColumn(name = "VisualRepresentationId")}
    )
    private List<VisualRepresentation> visualRepresentationObject;

// TODO: Uncomment each method after checking it first
    public Relation() {
        subject = new RelationArgument();
        object = new RelationArgument();
        visualRepresentationObject = new ArrayList<>();
        visualRepresentationSubject = new ArrayList<>();
        languageRepresentationObject = new ArrayList<>();
        languageRepresentationSubject = new ArrayList<>();
        motoricRepresentationObject = new ArrayList<>();
        motoricRepresentationSubject = new ArrayList<>();
        type = new RelationType();
    }

    @XmlAttribute
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return RelationArgument as subject
     * @xmlcomments.args xmltag="subject" xmldescription="This attribute defines
     * the object that the relation is related to"
     */
    //@XmlAttribute(name="subject")
    public RelationArgument getSubject() {
        return subject;
    }

    public void setSubject(RelationArgument subject) {
        this.subject = subject;
    }

    /**
     * @xmlcomments.args xmltag="object" xmldescription="This attribute defines
     * the object that the relation is related to"
     */
    public RelationArgument getObject() {
        return object;
    }

    public void setObject(RelationArgument object) {
        this.object = object;
    }

    /**
     * @return whether derivation is supported for this relation.
     * @xmlcomments.args xmltag="derivation_supported" xmldescription="This
     * attribute defines if the relation supports derivation or not"
     */
    //@XmlAttribute(name = "derivation_supported")
    public derivation_supported DerivationSupported() {
        return derivationSupported;
    }

    public void setDerivation(derivation_supported derivationSupported) {
        this.derivationSupported = derivationSupported;
    }

    /**
     * @return the type of the relation.
     * @xmlcomments.args xmltag="&lt;relation_type&gt;" xmldescription="This tag
     * defines the type of the relation"
     */
    public RelationType getType() {
        return type;
    }

    /**
     * Sets the type of the Relation but it doesn't check if there is the same
     * type twice.
     *
     * @param type the type of relation
     */
    public void setTypeSimple(RelationType type) {
        this.type = type;
    }

    public void setType(RelationType type) {
        if (type.getId() == null) {
            RelationTypeDao tmp = new RelationTypeDaoImpl();
            RelationType res = tmp.getEntity(type);
            if (res != null) {
                type = res;
            }
        }
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @XmlTransient
    public List<LanguageRepresentation> getLanguageRepresentationObject() {
        return languageRepresentationObject;
    }

    /**
     * @return the language representation of the object side of the relation.
     *         The object can be a Concept or a RelationSet.
     * @xmlcomments.args xmltag="&lt;language_representation_object&gt;"
     * xmldescription="This tag defines the LanguageRepresentation that should
     * be used to express the Object in this relation"
     */
    public String getLanguageRepresentationObject_() {
        String languageΡepresentationΟbject_;
        languageΡepresentationΟbject_ = new String();
        languageΡepresentationΟbject_ = languageRepresentationObject.toString();
        return languageΡepresentationΟbject_;
    }

    public void setLanguageRepresentationObject(
            List<LanguageRepresentation> languageRepresentationObject) {
        this.languageRepresentationObject = languageRepresentationObject;
    }

    @XmlTransient
    public List<LanguageRepresentation> getLanguageRepresentationSubject() {
        return languageRepresentationSubject;
    }

    @XmlTransient
    public List<MotoricRepresentation> getMotoricRepresentationObject() {
        return motoricRepresentationObject;
    }

    /**
     * @return the motoric representation of the concept that is on the object
     *         side of the relation.
     * @xmlcomments.args xmltag="&lt;motoric_representation_object&gt;"
     * xmldescription="This tag defines the MotoricRepresentation that should be
     * used to express the Object in this relation"
     */
    public List<String> getMotoricRepresentationObject_() {
        List<String> motoricRepresentationObject_ = new ArrayList<>();
        for (MotoricRepresentation motoricRepresentationObject1
                : motoricRepresentationObject) {
            motoricRepresentationObject_.add(
                    motoricRepresentationObject1.toString());
        }
        return motoricRepresentationObject_;
    }

    public void setMotoricRepresentationObject(
            List<MotoricRepresentation> motoricRepresentationObject) {
        this.motoricRepresentationObject = motoricRepresentationObject;
    }

    @XmlTransient
    public List<MotoricRepresentation> getMotoricRepresentationSubject() {
        return motoricRepresentationSubject;
    }

    @XmlTransient
    public List<VisualRepresentation> getVisualRepresentationObject() {
        return visualRepresentationObject;
    }

    public void setVisualRepresentationObject(
            List<VisualRepresentation> visualRepresentationObject) {
        this.visualRepresentationObject = visualRepresentationObject;
    }

    @XmlTransient
    public List<VisualRepresentation> getVisualRepresentationSubject() {
        return visualRepresentationSubject;
    }

    public void setVisualRepresentationSubject(
            List<VisualRepresentation> visualRepresentationSubject) {
        this.visualRepresentationSubject = visualRepresentationSubject;
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
        if (!(object instanceof Relation)) {
            return false;
        }
        Relation other = (Relation)object;
        try {
            if ((this.type != null && this.object != null &&
                    this.subject != null && this.type.equals(other.type) &&
                    this.object.equals(other.object) &&
                    this.subject.equals(other.subject)) ||
                    (this.type != null && this.object != null &&
                    this.subject != null && this.type.equals(other.type) &&
                    this.object.equals(other.subject) &&
                    this.subject.equals(other.object))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String toString() {
        return this.getSubject() + " " + this.getType().getForwardName() +
                " " + this.getObject();
    }

//    public void afterUnmarshal(Unmarshaller u, Object parent) 
//    {
//        if (Globals.ToMergeAfterUnMarshalling)
//        {
//            RelationTypeDao rDao = new RelationTypeDaoImpl();
//            this.Type = rDao.getEntity(Type);
//            ConceptDao cDao = new ConceptDaoImpl();
//            this.Object = cDao.getEntity(Object);
//            Object.getRelationsContainingConceptAsObject().add(this);
//            this.Subject = cDao.getEntity(Subject);
//        }
//    }
}
