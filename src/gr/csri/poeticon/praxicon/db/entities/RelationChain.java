/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
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
@XmlType(name = "relation_chain",
        namespace = "http://www.csri.gr/relation_chain")
@Entity
@NamedQueries({
    @NamedQuery(name = "getRelationChainsByConcept", query = 
        "SELECT rc FROM RelationChain rc " +
        "JOIN rc.Relations rc_rel " +
        "WHERE rc_rel.Relation.Object = :concept_id " +
        "OR rc_rel.Relation.Subject = :concept_id"
    ),
})
@Table(name = "RelationChains", indexes = {
    @Index(columnList = "Name"),
    @Index(columnList = "RelationChainId")})
public class RelationChain implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "RelationChainId", nullable = false)
    @SequenceGenerator(name = "CUST_SEQ", sequenceName = "RelationChainIdSeq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CUST_SEQ")
    private Long id;

    @Column(name = "Name")
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "Intersection_Relation",
            joinColumns = {
                @JoinColumn(name = "RelationId")},
            inverseJoinColumns = {
                @JoinColumn(name = "IntersectionId")}
    )
    private List<IntersectionOfRelationChains> IntersectionsOfRelationChains;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "RelationChain")
    private List<RelationChain_Relation> relations;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "LanguageRepresentation_RelationChain",
            joinColumns = {
                @JoinColumn(name = "RelationChainId")},
            inverseJoinColumns = {
                @JoinColumn(name = "LanguageRepresentationId")}
    )
    List<LanguageRepresentation> languageRepresentationNames;

    // Constructor
    public RelationChain() {
        IntersectionsOfRelationChains = new ArrayList<>();
        relations = new ArrayList<>();
        languageRepresentationNames = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<LanguageRepresentation> getLanguageRepresentationNames() {
        return languageRepresentationNames;
    }

    /**
     * @return the names of the language representations of the concepts that
     *         participate in the relation chain.
     * @xmlcomments.args xmltag="&lt;language_representation_names&gt;"
     * xmldescription="This tag defines the names of the LanguageRepresentation
     * that should be used to express this relation chain"
     */
    public List<String> getLanguageRepresentationNames_() {
        List<String> language_representation_names_ = new ArrayList<>();
        for (LanguageRepresentation languageRepresentationName
                : languageRepresentationNames) {
            language_representation_names_.add(
                    languageRepresentationName.getText());
        }
        return language_representation_names_;
    }

    public void setLanguageRepresentationNames(
            List<LanguageRepresentation> languageRepresentationNames) {
        this.languageRepresentationNames = languageRepresentationNames;
    }

    /**
     * @return the relations that participate in the relation chain.
     * @xmlcomments.args xmltag="&lt;relation_order&gt;" xmldescription="This
     * tag defines the relations of the entity
     */
    public List<RelationChain_Relation> getRelations() {
        return relations;
    }

    public List<Relation> getActualRelations() {
        List<Relation> rels;
        rels = new ArrayList<>(relations.size());
        for (RelationChain_Relation Relation : relations) {
            rels.add(0, null);
        }
        for (RelationChain_Relation Relation : relations) {
            rels.add((int)Relation.getRelationOrder(), Relation.getRelation());
            rels.remove((int)Relation.getRelationOrder() + 1);
        }
        return rels;
    }

    public void setRelations(List<RelationChain_Relation> relations) {
        this.relations = relations;
    }

    public void addRelation(Relation relation, long order) {
        RelationChain_Relation rcr = new RelationChain_Relation();
        rcr.setRelation(relation);
        rcr.setRelationChain(this);
        rcr.setRelationOrder(order);
        this.relations.add(rcr);
    }

    @XmlTransient
    public List<IntersectionOfRelationChains> getIntersections() {
        return IntersectionsOfRelationChains;
    }

    public void setIntersections(
            List<IntersectionOfRelationChains> intersections) {
        this.IntersectionsOfRelationChains = intersections;
    }

    @XmlAttribute
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
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
        if (!(object instanceof RelationChain)) {
            return false;
        }
        RelationChain other = (RelationChain)object;

        if (this.getRelations().size() == other.getRelations().size()) {
            for (int i = 0; i < this.getRelations().size(); i++) {
                if (!other.getRelations().contains(
                        this.getRelations().get(i))) {
                    return false;
                }
            }
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
        try {
            StringBuilder sb = new StringBuilder();
            Concept start = this.getIntersections().get(0).getConcept();
            sb.append(start.getName()).append(" (");

            for (int i = 0; i < this.getRelations().size(); i++) {
                for (int j = 0; j < this.getRelations().size(); j++) {
                    if (this.getRelations().get(j).getRelationOrder() == i) {
                        if (this.getRelations().get(j).getRelation().
                                getSubject().equals(start)) {
                            if (i == 0) {
                                sb.append(this.getRelations().get(j).
                                        getRelation().getSubject().getName()).
                                        append(" ").append(this.getRelations().
                                                get(j).getRelation().getType().
                                                getForwardName().name().
                                                toUpperCase()).append(" ").
                                        append(this.getRelations().get(j).
                                                getRelation().getObject().
                                                getName());
                            } else {
                                sb.append(", ").append(this.getRelations().
                                        get(j).getRelation().getSubject().
                                        getName()).append(" ").
                                        append(this.getRelations().get(j).
                                                getRelation().getType().
                                                getBackwardName().name().
                                                toUpperCase()).append(" ").
                                        append(this.getRelations().get(j).
                                                getRelation().getObject().
                                                getName());
                            }
                            start = this.getRelations().get(j).getRelation().
                                    getObject();
                        } else {
                            if (i == 0) {
                                sb.append(this.getRelations().get(j).
                                        getRelation().getSubject().getName()).
                                        append(" ").append(this.getRelations().
                                                get(j).getRelation().getType().
                                                getForwardName().name().
                                                toUpperCase()).append(" ").
                                        append(this.getRelations().get(j).
                                                getRelation().getObject().
                                                getName());
                            } else {
                                sb.append(", ").append(this.getRelations().
                                        get(j).getRelation().getObject().
                                        getName()).append(" ").append(this.
                                                getRelations().get(j).
                                                getRelation().getType().
                                                getBackwardName().name().
                                                toUpperCase()).append(" ").
                                        append(this.getRelations().get(j).
                                                getRelation().getSubject().
                                                getName());
                            }
                            start = this.getRelations().get(j).getRelation().
                                    getSubject();
                        }
                        break;
                    }
                }
            }
            sb.append(")");
            return sb.toString();
        } catch (Exception e) {
            return "There is a problem with the database (Relations)";
        }
    }
}
