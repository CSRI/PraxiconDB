/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author dmavroeidis
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "relationset_relation", namespace = "http://www.csri.gr/relationset_relation")
@Entity
@Table(name = "RelationSets_Relations")
public class RelationSet_Relation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RelationSet_RelationId")
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

    @XmlTransient
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

    @XmlTransient
    public RelationSet getRelationSet() {
        return relationSet;
    }

    public void setRelationSet(RelationSet relationSet) {
        this.relationSet = relationSet;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.relation);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RelationSet_Relation other = (RelationSet_Relation)obj;
        if (!Objects.equals(this.relation, other.relation)) {
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
