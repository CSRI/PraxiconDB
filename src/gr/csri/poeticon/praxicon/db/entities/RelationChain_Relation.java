/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author dmavroeidis
 *
 */
@XmlType(name = "relationchain_relation",
        namespace = "http://www.csri.gr/relationchain_relation")
@Entity
@Table(name = "RelationChains_Relations", indexes = {
    @Index(columnList = "RelationChain_RelationId")})
public class RelationChain_Relation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "CUST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CUST_SEQ")
    @Column(name = "RelationChain_RelationId")
    private Long id;

    // TODO: This could be a OneToMany and the corresponding a ManyToOne
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "RelationChainId")
    private RelationChain relationChain;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "RelationId")
    private Relation relation;

    @Column(name = "RelationOrder")
    private long relationOrder;

    @XmlElement(name = "relation")
    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    @XmlTransient
    public RelationChain getRelationChain() {
        return relationChain;
    }

    public void setRelationChain(RelationChain relationChain) {
        this.relationChain = relationChain;
    }

    /**
     * @return the order of the relation in the relation chain.
     * @xmlcomments.args xmltag="order" xmldescription="This attribute marks
     * that the order of the relation in the relation chain
     */
    @XmlElement(name = "relation_order")
    public long getRelationOrder() {
        return relationOrder;
    }

    public void setRelationOrder(long relationOrder) {
        this.relationOrder = relationOrder;
    }

    @XmlTransient
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
        if (!(object instanceof RelationChain_Relation)) {
            return false;
        }
        RelationChain_Relation other = (RelationChain_Relation)object;
        if (this.relation.equals(other.relation)) {
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
        return "gr.csri.poeticon.praxicon.db.entities.RelationChain_Relation[id=" +
                id + "]";
    }

//    public void afterUnmarshal(Unmarshaller u, Object parent)
//    {
//        this.RelationChain = (RelationChain)parent;
//        if (Globals.ToMergeAfterUnMarshalling)
//        {
//            RelationDao rDao = new RelationDaoImpl();
//            this.Relation = rDao.getEntity(Relation);
//        }
//    }
}
