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
import javax.persistence.OneToMany;
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
    @NamedQuery(name =
            "findRelationsByRelationArgumentRightArgumentOrLeftArgument",
            query =
            "SELECT r FROM Relation r " +
            "WHERE (r.rightArgument = :relationArgument " +
            "OR r.leftArgument = :relationArgument)"),
    @NamedQuery(name = "findRelationsByRelationArgumentRelationType", query =
            "SELECT r FROM Relation r, RelationType rt " +
            "WHERE (r.leftArgument = :relationArgumentId " +
            "OR r.rightArgument = :relationArgumentId) " +
            "AND r.type = rt " +
            "AND rt.forwardName = :relationType"),
    @NamedQuery(name = "findRelationsByRightRelationArgumentRelationType",
            query =
            "SELECT r FROM Relation r, RelationType rt " +
            "WHERE (r.rightArgument = :relationArgumentId) " +
            "AND r.type = rt " +
            "AND rt.forwardName = :relationType"),
    @NamedQuery(name = "findRelationsByLeftRelationArgumentRelationType",
            query =
            "SELECT r FROM Relation r, RelationType rt " +
            "WHERE (r.leftArgument = :relationArgumentId) " +
            "AND r.type = rt " +
            "AND rt.forwardName = :relationType"),
    @NamedQuery(name = "findRelationsByRelationType", query =
            "SELECT r FROM Relation r " +
            "JOIN r.type rt " +
            "WHERE rt.forwardName = :relationType"),
    @NamedQuery(name = "findRelationsByRelationArgumentRightArgument", query =
            "SELECT r FROM Relation r " +
            "WHERE r.rightArgument = :relationArgumentId"),
    @NamedQuery(name = "findRelationsByRelationArgumentLeftArgument", query =
            "SELECT r FROM Relation r " +
            "WHERE r.leftArgument = :relationArgumentId"),
    @NamedQuery(name = "areRelated", query =
            "SELECT r FROM Relation r " +
            "WHERE (r.leftArgument = :relationArgumentId1 " +
            "AND r.rightArgument = :relationArgumentId2) " +
            "OR (r.leftArgument = :relationArgumentId2 " +
            "AND r.rightArgument = :relationArgumentId1)")
})
@Table(name = "Relations", indexes = {
    @Index(columnList = "Comment"),
    @Index(columnList = "RelationId")})
public class Relation implements Serializable {

    public static enum LinguisticallySupported {

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
    @NotNull(message = "LeftArgument of relation must be specified.")
    private RelationArgument leftArgument;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    //@JoinColumn(name = "RelationArgumentId")
    @NotNull(message = "RightArgument of relation must be specified.")
    private RelationArgument rightArgument;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "relation")
    private List<RelationSet_Relation> relationSet;

    @Column(name = "LinguisticallySupported")
    @NotNull(message = "Linguistic support must be specified.")
    @Enumerated(EnumType.STRING)
    private LinguisticallySupported linguisticallySupported;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "LanguageRepresentation_RelationLeftArgument",
            joinColumns = {
                @JoinColumn(name = "RelationId")},
            inverseJoinColumns = {
                @JoinColumn(name = "LanguageRepresentationId")}
    )
    private List<LanguageRepresentation> languageRepresentationLeftArgument;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "LanguageRepresentation_RelationRightArgument",
            joinColumns = {
                @JoinColumn(name = "RelationId")},
            inverseJoinColumns = {
                @JoinColumn(name = "LanguageRepresentationId")}
    )
    private List<LanguageRepresentation> languageRepresentationRightArgument;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "MotoricRepresentation_RelationLeftArgument",
            joinColumns = {
                @JoinColumn(name = "RelationId")},
            inverseJoinColumns = {
                @JoinColumn(name = "MotoricRepresentationId")}
    )
    private List<MotoricRepresentation> motoricRepresentationLeftArgument;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "MotoricRepresentation_RelationRightArgument",
            joinColumns = {
                @JoinColumn(name = "RelationId")},
            inverseJoinColumns = {
                @JoinColumn(name = "MotoricRepresentationId")}
    )
    private List<MotoricRepresentation> motoricRepresentationRightArgument;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "VisualRepresentation_RelationLeftArgument",
            joinColumns = {
                @JoinColumn(name = "RelationId")},
            inverseJoinColumns = {
                @JoinColumn(name = "VisualRepresentationId")}
    )
    private List<VisualRepresentation> visualRepresentationLeftArgument;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "VisualRepresentation_RelationRightArgument",
            joinColumns = {
                @JoinColumn(name = "RelationId")},
            inverseJoinColumns = {
                @JoinColumn(name = "VisualRepresentationId")}
    )
    private List<VisualRepresentation> visualRepresentationRightArgument;

    public Relation() {
        leftArgument = new RelationArgument();
        rightArgument = new RelationArgument();
        visualRepresentationRightArgument = new ArrayList<>();
        visualRepresentationLeftArgument = new ArrayList<>();
        languageRepresentationRightArgument = new ArrayList<>();
        languageRepresentationLeftArgument = new ArrayList<>();
        motoricRepresentationRightArgument = new ArrayList<>();
        motoricRepresentationLeftArgument = new ArrayList<>();
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
     * @return RelationArgument as leftArgument
     * @xmlcomments.args xmltag="leftArgument" xmldescription="This attribute defines
     * the rightArgument that the relation is related to"
     */
    //@XmlAttribute(name="leftArgument")
    public RelationArgument getLeftArgument() {
        return leftArgument;
    }

    public void setLeftArgument(RelationArgument leftArgument) {
        this.leftArgument = leftArgument;
    }

    /**
     * @xmlcomments.args xmltag="rightArgument" xmldescription="This attribute defines
     * the rightArgument that the relation is related to"
     */
    public RelationArgument getRightArgument() {
        return rightArgument;
    }

    public void setRightArgument(RelationArgument rightArgument) {
        this.rightArgument = rightArgument;
    }

    /**
     * @return whether derivation is supported for this relation.
     * @xmlcomments.args xmltag="derivation_supported" xmldescription="This
     * attribute defines if the relation supports derivation or not"
     */
    //@XmlAttribute(name = "LinguisticallySupported")
    public LinguisticallySupported isLinguisticallySupported() {
        return linguisticallySupported;
    }

    public void setLinguisticSupport(LinguisticallySupported derivationSupported) {
        this.linguisticallySupported = derivationSupported;
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
    public List<LanguageRepresentation> getLanguageRepresentationRightArgument() {
        return languageRepresentationRightArgument;
    }

    /**
     * @return the language representation of the rightArgument side of the relation.
     *         The rightArgument can be a Concept or a RelationSet.
     * @xmlcomments.args xmltag="&lt;language_representation_rightArgument&gt;"
     * xmldescription="This tag defines the LanguageRepresentation that should
     * be used to express the RightArgument in this relation"
     */
    public String getLanguageRepresentationRightArgument_() {
        String languageΡepresentationΟbject_;
        languageΡepresentationΟbject_ = new String();
        languageΡepresentationΟbject_ = languageRepresentationRightArgument.
                toString();
        return languageΡepresentationΟbject_;
    }

    public void setLanguageRepresentationRightArgument(
            List<LanguageRepresentation> languageRepresentationRightArgument) {
        this.languageRepresentationRightArgument =
                languageRepresentationRightArgument;
    }

    @XmlTransient
    public List<LanguageRepresentation> getLanguageRepresentationLeftArgument() {
        return languageRepresentationLeftArgument;
    }

    @XmlTransient
    public List<MotoricRepresentation> getMotoricRepresentationRightArgument() {
        return motoricRepresentationRightArgument;
    }

    /**
     * @return the motoric representation of the concept that is on the rightArgument
     *         side of the relation.
     * @xmlcomments.args xmltag="&lt;motoric_representation_rightArgument&gt;"
     * xmldescription="This tag defines the MotoricRepresentation that should be
     * used to express the RightArgument in this relation"
     */
    public List<String> getMotoricRepresentationRightArgument_() {
        List<String> motoricRepresentationRightArgument_ = new ArrayList<>();
        for (MotoricRepresentation motoricRepresentationRightArgument1
                : motoricRepresentationRightArgument) {
            motoricRepresentationRightArgument_.add(
                    motoricRepresentationRightArgument1.toString());
        }
        return motoricRepresentationRightArgument_;
    }

    public void setMotoricRepresentationRightArgument(
            List<MotoricRepresentation> motoricRepresentationRightArgument) {
        this.motoricRepresentationRightArgument =
                motoricRepresentationRightArgument;
    }

    @XmlTransient
    public List<MotoricRepresentation> getMotoricRepresentationLeftArgument() {
        return motoricRepresentationLeftArgument;
    }

    @XmlTransient
    public List<VisualRepresentation> getVisualRepresentationRightArgument() {
        return visualRepresentationRightArgument;
    }

    public void setVisualRepresentationRightArgument(
            List<VisualRepresentation> visualRepresentationRightArgument) {
        this.visualRepresentationRightArgument =
                visualRepresentationRightArgument;
    }

    @XmlTransient
    public List<VisualRepresentation> getVisualRepresentationLeftArgument() {
        return visualRepresentationLeftArgument;
    }

    public void setVisualRepresentationLeftArgument(
            List<VisualRepresentation> visualRepresentationLeftArgument) {
        this.visualRepresentationLeftArgument = visualRepresentationLeftArgument;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object rightArgument) {
        // TODO: Warning - method won't work in case the id fields are not set
        if (!(rightArgument instanceof Relation)) {
            return false;
        }
        Relation other = (Relation)rightArgument;
        try {
            if ((this.type != null && this.rightArgument != null &&
                    this.leftArgument != null && this.type.equals(other.type) &&
                    this.rightArgument.equals(other.rightArgument) &&
                    this.leftArgument.equals(other.leftArgument)) ||
                    (this.type != null && this.rightArgument != null &&
                    this.leftArgument != null && this.type.equals(other.type) &&
                    this.rightArgument.equals(other.leftArgument) &&
                    this.leftArgument.equals(other.rightArgument))) {
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
        return this.getLeftArgument() + " " + this.getType().getForwardName() +
                " " + this.getRightArgument();
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
//            this.LeftArgument = cDao.getEntity(LeftArgument);
//        }
//    }
}
