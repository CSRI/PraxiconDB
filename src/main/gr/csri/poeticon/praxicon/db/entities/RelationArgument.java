/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author dmavroeidis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "relation_argument", namespace =
        "http://www.csri.gr/relation_argument")
@Entity
@NamedQueries({
    @NamedQuery(name = "findAllRelationArguments", query =
            "FROM RelationArgument ra"),
    @NamedQuery(name = "findRelationArgumentByConcept", query =
            "SELECT ra FROM RelationArgument ra " +
            "JOIN ra.concept rac " +
            "WHERE rac = :concept"),
    @NamedQuery(name = "findRelationArgumentByRelationSet", query =
            "SELECT ra FROM RelationArgument ra " +
            "JOIN ra.relationSet rars " +
            "WHERE rars = :relationSet"),})
@Table(name = "RelationArguments")
public class RelationArgument implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @XmlTransient
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RelationArgumentId")
    private Long id;

    @Basic(optional = true)
    @OneToOne(cascade = CascadeType.ALL)
    private Concept concept;

    @Basic(optional = true)
    @OneToOne(cascade = CascadeType.ALL)
    private RelationSet relationSet;

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
     * Constructor #4. Checks whether the argument is Concept or RelationSet
     * and stores it accordingly.
     *
     * @param relationArgument
     */
    public RelationArgument(RelationArgument relationArgument) {
        if (relationArgument.isConcept()) {
            this.concept = relationArgument.getConcept();
            this.relationSet = null;
        }
        if (relationArgument.isRelationSet()) {
            this.relationSet = relationArgument.getRelationSet();
            this.concept = null;
        }
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
            System.err.println(
                    "Cannot set concept of the relation argument " +
                    "as a relation set is already present.");
        }
    }

    /**
     * Gets the RelationSet of this RelationArgument.
     *
     * @return the RelationSet connected with this RelationArgument
     *         (can be null)
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
     *
     * @return an Object structure that is either a Concept or RelationSet.
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

    public boolean isRelationSet() {
        return this.getRelationArgumentClassType() == RelationSet.class;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        if (this.isConcept()) {
            hash = 89 * hash + Objects.hashCode(this.getConcept());
        } else {
            hash = 89 * hash + Objects.hashCode(this.getRelationSet());
        }
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
        final RelationArgument other = (RelationArgument)obj;
        if (this.isConcept()) {
            if (other.isRelationSet()) {
                return false;
            } else if (!this.getConcept().equals(other.getConcept())) {
                return false;
            }
        } else if (this.isRelationSet()) {
            if (other.isConcept()) {
                return false;
            } else if (!this.getRelationSet().equals(other.getRelationSet())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "gr.csri.poeticon.praxicon.db.entities.RelationArgument[ id=" +
                id + " ]";
    }
}
