/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author dmavroeidis
 */
@Entity
public class RelationArgument implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Concept concept;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }

    public RelationSet getRelationSet() {
        return relationSet;
    }

    public void setRelationSet(RelationSet relationSet) {
        this.relationSet = relationSet;
    }

    public Object getArgumentAsObject() {
        if (concept != null) {
            return (Object)this.concept;
        } else if (relationSet != null) {
            return (Object)this.relationSet;
        }
        return null;
    }

    public Class getArgumentClassType() {
        if (concept != null) {
            return this.concept.getClass();
        } else if (relationSet != null) {
            return this.relationSet.getClass();
        }
        return null;
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
