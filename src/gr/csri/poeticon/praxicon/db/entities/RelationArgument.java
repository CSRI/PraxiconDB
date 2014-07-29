/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@XmlType(name = "relation_argument", namespace =
        "http://www.csri.gr/relation_argument")
@Entity
@Table(name = "RelationArguments", indexes = {
    @Index(columnList = "RelationArgumentId") })
public class RelationArgument implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "CUST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CUST_SEQ")
    @Column(name = "RelationArgumentId")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Concept concept;

    @OneToOne(cascade = CascadeType.ALL)
    private RelationSet relationSet;

    /*
     * Relations that have "this" RelationArgument as Object.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "object")
    private List<Relation> relationsContainingRelationArgumentAsObject;

    /*
     * Relations that have "this" RelationArgument as Subject.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subject")
    private List<Relation> relationsContainingRelationArgumentAsSubject;

    /**
     * Constructor #1. Both concept and relationSet are set to null.
     */
    public RelationArgument() {
        this.concept = null;
        this.relationSet = null;
    }

    /**
     * Constructor #2. concept is given and relationSet is set to null.
     *
     * @param concept
     */
    public RelationArgument(Concept concept) {
        this.concept = concept;
        this.relationSet = null;
    }

    /**
     * Constructor #3. relationSet is given and concept is set to null.
     *
     * @param relationSet
     */
    public RelationArgument(RelationSet relationSet) {
        this.concept = null;
        this.relationSet = relationSet;
    }

    /**
     * Gets the id of this RelationArgument.
     *
     * @return Long integer.
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * Sets the id of this RelationArgument.
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return the Concept connected with this RelationArgument (can be null)
     */
    public Concept getConcept() {
        return concept;
    }

    /**
     *
     * Sets the concept of this RelationArgument. It cannot be set if the
     * relationSet has already been set.
     *
     * @param concept
     */
    public void setConcept(Concept concept) {
        if (this.relationSet == null) {
            this.concept = concept;
        } else {
            System.err.println("Cannot set concept of the relation argument " +
                    "as a relation set is already present.");
        }
    }

    /**
     * Gets the RelationSet of this RelationArgument.
     *
     * @return the RelationSet connected with this RelationArgument (can be null)
     */
    public RelationSet getRelationSet() {
        return relationSet;
    }

    /**
     * Sets the RelationSet of this RelationArgument. It cannot be set if the
     * concept has already been set.
     *
     * @param relationSet
     */
    public void setRelationSet(RelationSet relationSet) {
        if (this.concept == null) {
            this.relationSet = relationSet;
        } else {
            System.err.println("Cannot set relation set of the " +
                    "relation argument as a concept is already present.");
        }
    }

    /**
     * Gets relations that contain this relationArgument as object.
     *
     * @return A list of relations
     */
    public List<Relation> getRelationsContainingRelationArgumentAsObject() {
        return relationsContainingRelationArgumentAsObject;
    }

    /**
     * Gets relations that contain this relationArgument as subject.
     *
     * @return A list of relations
     */
    public List<Relation> getRelationsContainingRelationArgumentAsSubject() {
        return relationsContainingRelationArgumentAsSubject;
    }

    /**
     * Gets relations that contain this relationArgument either as subject or
     * object. A set is used, so that there are no duplicate entries.
     *
     * @return a set of relations
     */
    public Set<Relation> getRelationsContainingRelationArgument() {
        List<Relation> relationList = new ArrayList();
        relationList.addAll(this.relationsContainingRelationArgumentAsObject);
        relationList.addAll(this.relationsContainingRelationArgumentAsSubject);
        HashSet<Relation> relationSet = new HashSet<>(relationList);
        return relationSet;
    }

    /**
     *
     * @return an Object that is either a Concept or RelationSet.
     */
    public Object getRelationArgumentAsObject() {
        if (concept != null) {
            return (Object)this.concept;
        } else if (relationSet != null) {
            return (Object)this.relationSet;
        }
        return null;
    }

    /**
     *
     * @return the class type of this RelationArgument.
     *         Can be either Concept or RelationSet.
     */
    public Class getRelationArgumentClassType() {
        if (concept != null) {
            return this.concept.getClass();
        } else if (relationSet != null) {
            return this.relationSet.getClass();
        }
        return null;
    }

    public boolean isConcept() {
        return this.getRelationArgumentClassType() == Concept.class;
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
        if (!(object instanceof RelationArgument)) {
            return false;
        }
        RelationArgument other = (RelationArgument)object;
        if ((this.id == null && other.id != null) ||
                (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gr.csri.poeticon.praxicon.db.entities.RelationArgument[ id=" +
                id + " ]";
    }
}
