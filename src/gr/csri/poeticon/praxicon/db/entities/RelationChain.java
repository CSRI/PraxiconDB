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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
    @NamedQuery(name = "findRelationChainsByConcept", query = 
        "SELECT rc FROM RelationChain rc " +
        "JOIN rc.relations rc_rel " +
        "WHERE rc_rel.relation.object = :concept_id " +
        "OR rc_rel.relation.subject = :concept_id"
    ),
})
@Table(name = "RelationChains", indexes = {
    @Index(columnList = "Name"),
    @Index(columnList = "RelationChainId")})
public class RelationChain implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "RelationChainId", nullable = false)
    @SequenceGenerator(name = "CUST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CUST_SEQ")
    private Long id;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "relationChain")
    private List<RelationChain_Relation> relations;

    // Constructor
    public RelationChain() {
        relations = new ArrayList<>();
    }


    /**
     * @return the relations that participate in the relation chain.
     * @xmlcomments.args xmltag="&lt;relation_order&gt;" xmldescription="This
     * tag defines the relations of the entity
     */
    public List<RelationChain_Relation> getRelations() {
        return relations;
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
