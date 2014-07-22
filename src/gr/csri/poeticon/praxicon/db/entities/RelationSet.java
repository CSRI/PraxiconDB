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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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
@XmlType(name = "relation_set", namespace = "http://www.csri.gr/relation_set")
@Entity
@Table(name = "RelationSets", indexes = {
    @Index(columnList = "RelationSetId"),
    @Index(columnList = "Name")})
public class RelationSet implements Serializable {

    public static enum inherent {

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
    @Column(name = "RelationSetId")
    private Long id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Inherent")
    @Enumerated(EnumType.STRING)
    private inherent inherent;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "relationSet")
    private List<RelationSet_Relation> relations;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "LanguageRepresentation_RelationSet",
            joinColumns = {
                @JoinColumn(name = "RelationSetId")},
            inverseJoinColumns = {
                @JoinColumn(name = "LanguageRepresentationId")}
    )
    List<LanguageRepresentation> languageRepresentations;

    /**
     * Constructor #1.
     */
    public RelationSet() {
        this.name = null;
        this.relations = null;
        this.inherent = this.inherent.UNKNOWN;
        this.languageRepresentations = null;
    }
    
    /**
     * Constructor #2.
     */
    public RelationSet(String name, List<RelationSet_Relation> relationSetRelationsList, inherent isInherent, List<LanguageRepresentation> languageRepresentation) {
        this.name = name;
        this.relations = relationSetRelationsList;
        this.inherent = isInherent;
        this.languageRepresentations = null;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public inherent getIsInherent() {
        return inherent;
    }

    public void setIsInherent(inherent inherent) {
        this.inherent = inherent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets a list of all Relations contained in this RelationSet.
     *
     * @return a list of relations
     */
    public List<Relation> getRelationsList() {
        List<RelationSet_Relation> relationSet_RelationList = new ArrayList();
        List<Relation> relationList = new ArrayList();
        relationSet_RelationList = this.relations;
        for (RelationSet_Relation relationSetRelation : relationSet_RelationList) {
            relationList.add(relationSetRelation.getRelation());
        }
        return relationList;
    }

    /**
     * Gets all Relations contained in this RelationSet in the form of Set,
     * which means that there are no duplicate entries.
     *
     * @return a set of relations
     */
    public Set<Relation> getRelationsSet() {
        HashSet<Relation> relationSet = new HashSet<>(this.getRelationsList());
        return relationSet;
    }

    public List<LanguageRepresentation> getLanguageRepresentations() {
        return languageRepresentations;
    }

    public List<RelationSet_Relation> getRelations() {
        return relations;
    }

    public List<Relation> getActualRelations() {
        List<Relation> rels;
        rels = new ArrayList<>(relations.size());
        for (RelationSet_Relation relation : relations) {
            rels.add(0, null);
        }
        for (RelationSet_Relation Relation : relations) {
            rels.add((int)Relation.getRelationOrder(), Relation.getRelation());
            rels.remove((int)Relation.getRelationOrder() + 1);
        }
        return rels;
    }

    public void setRelations(List<RelationSet_Relation> relations) {
        this.relations = relations;
    }
//
//    public void addRelation(Relation relation, int order) {
//        RelationSet_Relation rsr = new RelationSet_Relation();
//        rsr.setRelation(relation);
//        rsr.setRelationSet(this);
//        rsr.setRelationOrder(order);
//        System.out.println("RELATION: " + relation.toString());
//        this.relations.add(rsr);
//    }

    public void addRelation(Relation relation) {
        RelationSet_Relation rsr = new RelationSet_Relation();
        rsr.setRelation(relation);
        this.relations.add(rsr);
    }

    public void addRelation(Relation relation, int order) {
        RelationSet_Relation rsr = new RelationSet_Relation(order);
        rsr.setRelation(relation);
        rsr.setRelationSet(this);

        try {
            this.relations.add(rsr);
        } catch (Exception ex) {
            System.err.println("THE ERROR MESSAGE:");
            ex.printStackTrace();
        }

    }

    /**
     * @return the names of the language representations of the concepts that
     *         participate in the relation chain.
     */
    public List<String> getLanguageRepresentationNames() {
        List<String> languageRepresentationNames = new ArrayList<>();
        for (LanguageRepresentation languageRepresentation
                : languageRepresentations) {
            languageRepresentationNames.add(
                    languageRepresentation.getText());
        }
        return languageRepresentationNames;
    }

    public void setLanguageRepresentationNames(
            List<LanguageRepresentation> languageRepresentationNames) {
        this.languageRepresentations = languageRepresentationNames;
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
        if (!(object instanceof RelationSet)) {
            return false;
        }
        RelationSet other = (RelationSet)object;
        if ((this.id == null && other.id != null) ||
                (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gr.csri.poeticon.praxicon.db.entities.RelationSet[ id=" + id +
                " ]";
    }

}
