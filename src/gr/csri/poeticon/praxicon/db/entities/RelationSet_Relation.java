/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author dmavroeidis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "relationset_relation", namespace =
        "http://www.csri.gr/relationset_relation")
@Entity
@Table(name = "RelationSets_Relations", indexes = {
    @Index(columnList = "RelationSet_RelationId")
})
public class RelationSet_Relation implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name = "CUST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CUST_SEQ")
    @Column(name = "RelationSet_RelationId")
    private Long id;

    @Column(name = "RelationOrder")
    private short relationOrder;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "RelationSetId")
    private RelationSet relationSet;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "RelationId")
    private Relation relation;

    /**
     * Constructor #1.
     */
    public RelationSet_Relation() {
        this.relation = null;
        //this.relationOrder = 0;
        this.relationSet = null;
    }

    /**
     * Constructor #2.
     */
    public RelationSet_Relation(short order) {
        this.relation = null;
        this.relationOrder = order;
        this.relationSet = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getRelationOrder() {
        return relationOrder;
    }

    public void setRelationOrder(short relationOrder) {
        this.relationOrder = relationOrder;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public RelationSet getRelationSet() {
        return relationSet;
    }

    public void setRelationSet(RelationSet relationSet) {
        this.relationSet = relationSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RelationSet_Relation)) {
            return false;
        }
        RelationSet_Relation other = (RelationSet_Relation)object;
        if ((this.id == null && other.id != null) ||
                (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gr.csri.poeticon.praxicon.db.entities.RelationSet_Relation[ " +
                "id=" + id + " ]";
    }

}
