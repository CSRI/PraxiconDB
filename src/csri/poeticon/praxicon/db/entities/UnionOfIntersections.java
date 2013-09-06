/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.entities;

import csri.poeticon.praxicon.Globals;
import csri.poeticon.praxicon.db.dao.IntersectionOfRelationsDao;
import csri.poeticon.praxicon.db.dao.implSQL.IntersectionOfRelationsDaoImpl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Erevodifwntas
 */
@XmlRootElement(name="union_of_intersections_of_relations")
@Entity
@Table(name="UNION_OF_INTERSECTIONS")
public class UnionOfIntersections implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    @Column(name="UNION_ID")
    private Long id;

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="CONCEPT_ID")
    Concept concept;

    @Column(name="PERCENTAGE")
    double percentage;

    @Column(name="INHERENT")
    boolean inherent;

    @XmlAttribute(name="inherent")
    public boolean isInherent() {
        return inherent;
    }

    public void setInherent(boolean basic) {
        this.inherent = basic;
    }

    @XmlAttribute()
    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;intersections_of_relations&gt;"
     *     xmldescription="This tag defines the intersections of some relations"
     */
    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
    name="UNION_INTERSECTIONRELATION",
    joinColumns={@JoinColumn(name="UNION_ID")},
    inverseJoinColumns={@JoinColumn(name="INTERSECTION_ID")}
    )
    List<IntersectionOfRelations> intersections;

    @Column(name="NAME")
    String name;

    public UnionOfIntersections()
    {
        intersections = new ArrayList<IntersectionOfRelations>();
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;intersections_of_relations&gt;"
     *     xmldescription="This tag defines the intersection of relations for the entity
     */
    @XmlElement(name="intersections_of_relations")
    public List<IntersectionOfRelations> getIntersections() {
        return intersections;
    }

    public void addIntersection(IntersectionOfRelations inter)
    {
        inter.getUnions().add(this);
        intersections.add(inter);
    }

    public void addIntersection1way(IntersectionOfRelations inter)
    {
        //inter.getUnions().add(this);
        intersections.add(inter);
    }

    public void setIntersections(List<IntersectionOfRelations>
            intersections) {
        this.intersections = intersections;
    }

    @XmlTransient
    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }

    @XmlAttribute
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlAttribute()
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UnionOfIntersections)) {
            return false;
        }
        UnionOfIntersections other = (UnionOfIntersections) object;

        if (this.getIntersections().size() == other.getIntersections().size())
        {
            for (int i = 0; i < this.getIntersections().size(); i++)
            {
                if (!other.getIntersections().contains(this.getIntersections().get(i)))
                {
                    return false;
                }
            }
            return true;
        }
        
        if ((this.id == null && other.id != null) || (this.id != null &&
                !this.id.equals(other.id))) {
            return false;
        }

        if (this.id == null && other.id == null)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "csri.poeticon.praxicon.db.entities.UnionOfRelations[id=" +
                id + "]";
    }

    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        if (Globals.ToMergeAfterUnMarshalling)
        {
            IntersectionOfRelationsDao leDao = new IntersectionOfRelationsDaoImpl();
        }
        else
        {
            for (int i = 0; i<this.getIntersections().size(); i++)
            {
                this.getIntersections().get(i).getUnions().add(this);
            }
        }
    }


    public void addRelationAtTheBeginning(Relation relation)
    {
        List<IntersectionOfRelations> inters = this.getIntersections();
        for (int i = 0; i < inters.size(); i++)
        {
            IntersectionOfRelations inter = inters.get(i);
            List<RelationChain> chains = inter.getRelations();
            for (int j = 0; j < chains.size(); j++)
            {
                RelationChain chain = chains.get(j);
                for(int k = 0; k < chain.getRelations().size(); k++)
                {
                    chain.getRelations().get(k).setRelationOrder(chain.getRelations().get(k).getRelationOrder()+1);
                }
                chain.addRelation(relation, 1);
            }
        }
    }

    public void addRelationAtTheBeginningDynamicaly(Relation relation)
    {
        List<IntersectionOfRelations> inters = this.getIntersections();
        for (int i = 0; i < inters.size(); i++)
        {
            IntersectionOfRelations inter = inters.get(i);
            List<RelationChain> chains = inter.getRelations();
            for (int j = 0; j < chains.size(); j++)
            {
                RelationChain chain = chains.get(j);
                int min = Integer.MAX_VALUE;
                Concept c = null;
                for(int k = 0; k < chain.getRelations().size(); k++)
                {
                    if (min > chain.getRelations().get(k).getRelationOrder())
                    {
                        min = (int)chain.getRelations().get(k).getRelationOrder();
                        c = chain.getRelations().get(k).getRelation().getSubject();
                    }
                    chain.getRelations().get(k).setRelationOrder(chain.getRelations().get(k).getRelationOrder()+1);
                }
                relation.setObj(c);
                chain.addRelation(relation, 1);
            }
        }
    }
}