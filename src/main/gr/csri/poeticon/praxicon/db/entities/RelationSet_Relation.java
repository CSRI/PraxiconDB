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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author dmavroeidis
 */
@Entity
@Table(name = "RelationSets_Relations", indexes = {
    @Index(columnList = "RelationSet_RelationId")
})
public class RelationSet_Relation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RelationSet_RelationId")
//    @XmlAttribute
    @XmlTransient
    private Long id;

    @XmlTransient
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "RelationSetId")
    private RelationSet relationSet;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "RelationId")
    private Relation relation;

    public RelationSet_Relation() {
        this.relationSet = new RelationSet();
        this.relation = new Relation();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (id != null) {
            hash += id.hashCode();
        } else {
            hash = 0;
        }
        return hash;
    }

    @Override
    public boolean equals(Object object) {
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
        return "gr.csri.poeticon.praxicon.db.entities.RelationSet_Relation" +
                "[ id=" + id + " ]";
    }
}
