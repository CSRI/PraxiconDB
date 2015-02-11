/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import gr.csri.poeticon.praxicon.Globals;
import gr.csri.poeticon.praxicon.db.dao.RelationDao;
import gr.csri.poeticon.praxicon.db.dao.RelationTypeDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationTypeDaoImpl;
import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author dmavroeidis
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "relation", namespace = "http://www.csri.gr/relation")
@XmlRootElement(name = "relation", namespace = "http://www.csri.gr/relation")
@Entity
@NamedQueries({
    @NamedQuery(name =
            "findRelationsByRelationArgumentRightArgumentOrLeftArgument",
            query =
            "SELECT r FROM Relation r " +
            "WHERE (r.rightArgument = :relationArgument " +
            "OR r.leftArgument = :relationArgument)"),
    @NamedQuery(name = "findRelationsByRelationArgumentRelationType", query =
            "SELECT r FROM Relation r " +
            "JOIN r.relationType rt " +
            "WHERE (r.leftArgument = :relationArgumentId " +
            "OR r.rightArgument = :relationArgumentId) " +
            "AND rt.forwardName = :relationType"),
    @NamedQuery(name = "findRelations", query =
            "SELECT r FROM Relation r " +
            "JOIN r.relationType rt " +
            "WHERE (r.leftArgument = :leftRelationArgumentId " +
            "OR r.rightArgument = :rightRelationArgumentId) " +
            "AND rt.forwardName = :relationType"),
    @NamedQuery(name = "findRelationsByRelationType", query =
            "SELECT r FROM Relation r " +
            "JOIN r.relationType rt " +
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
//    @XmlAttribute
    @XmlTransient
    private Long id;

    @Column(name = "Comment")
    private String comment;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    //@JoinColumn(name="Id")
    private RelationType relationType;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    //@JoinColumn(name = "RelationArgumentId")
    @NotNull(message = "LeftArgument of relation must be specified.")
    private RelationArgument leftArgument;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @NotNull(message = "RightArgument of relation must be specified.")
    private RelationArgument rightArgument;

    @XmlTransient
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "relation")
    private List<RelationSet_Relation> relationSet;

    @Column(name = "LinguisticallySupported")
    //@NotNull(message = "Linguistic support must be specified.")
    @Enumerated(EnumType.STRING)
    private LinguisticallySupported linguisticallySupported;

    public Relation() {
        leftArgument = new RelationArgument();
        rightArgument = new RelationArgument();
        relationType = new RelationType();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return RelationArgument as leftArgument
     */
    public RelationArgument getLeftArgument() {
        return leftArgument;
    }

    public void setLeftArgument(RelationArgument leftArgument) {
        this.leftArgument = leftArgument;
    }

    public RelationArgument getRightArgument() {
        return rightArgument;
    }

    public void setRightArgument(RelationArgument rightArgument) {
        this.rightArgument = rightArgument;
    }

    /**
     * @return whether linguistic support exists for this relation.
     */
    public LinguisticallySupported isLinguisticallySupported() {
        return linguisticallySupported;
    }

    public void setLinguisticSupport(
            LinguisticallySupported derivationSupported) {
        this.linguisticallySupported = derivationSupported;
    }

    /**
     * @return the relationType of the relation.
     */
    public RelationType getRelationType() {
        return relationType;
    }

    /**
     * Sets the relationType of the Relation but it doesn't check if there is the same
 relationType twice.
     *
     * @param type the relationType of relation
     */
    public void setRelationTypeSimple(RelationType type) {
        this.relationType = type;
    }

    public void setRelationType(RelationType type) {
        if (type.getId() == null) {
            RelationTypeDao tmp = new RelationTypeDaoImpl();
            RelationType res = tmp.getEntity(type);
            if (res != null) {
                type = res;
            }
        }
        this.relationType = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
            if ((this.relationType != null && this.rightArgument != null &&
                    this.leftArgument != null && this.relationType.equals(other.relationType) &&
                    this.rightArgument.equals(other.rightArgument) &&
                    this.leftArgument.equals(other.leftArgument)) ||
                    (this.relationType != null && this.rightArgument != null &&
                    this.leftArgument != null && this.relationType.equals(other.relationType) &&
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
        return this.getLeftArgument() + " " + this.getRelationType().getForwardName() +
                " " + this.getRightArgument();
    }

    public void afterUnmarshal(Unmarshaller u, Object parent) {
        if (Globals.ToMergeAfterUnMarshalling) {
            RelationDao rDao = new RelationDaoImpl();
            Relation tmpRelation = rDao.getRelation(this.leftArgument,
                    this.rightArgument, this.relationType);
            if (tmpRelation == null) {
                rDao.merge(this);
            }
        }
    }
}
