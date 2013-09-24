/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.entities;

import csri.poeticon.praxicon.Constants;
import csri.poeticon.praxicon.Globals;
import csri.poeticon.praxicon.db.dao.LanguageRepresentationGroupDao;
import csri.poeticon.praxicon.db.dao.RelationChainDao;
import csri.poeticon.praxicon.db.dao.implSQL.LanguageRepresentationGroupDaoImpl;
import csri.poeticon.praxicon.db.dao.implSQL.RelationChainDaoImpl;
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
@XmlRootElement()
@Entity
@Table(name="INTERSECTION_OF_RELATIONS")
public class IntersectionOfRelations implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    @Column(name="INTERSECTION_ID")
    private Long id;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
    name="INTERSECTION_RELATION",
    joinColumns={@JoinColumn(name="INTERSECTION_ID")},
    inverseJoinColumns={@JoinColumn(name="RELATION_ID")}
    )
    List<RelationChain> relations;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="intersections")
    @JoinTable(
    name="UNION_INTERSECTIONRELATION",
    joinColumns={@JoinColumn(name="INTERSECTION_ID")},
    inverseJoinColumns={@JoinColumn(name="UNION_ID")}
    )
    List<UnionOfIntersections> unions;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="LRIntersections")
    @JoinTable(
        name="LRGROUP_INTERSECTION",
        joinColumns={@JoinColumn(name="INTERSECTION_ID")},
        inverseJoinColumns={@JoinColumn(name="LRGROUP_ID")}
    )
    List<LanguageRepresentationGroup> LRGroupNames;

    @Column(name="NAME")
    String name;


    public IntersectionOfRelations()
    {
        relations = new ArrayList<RelationChain>();
        unions = new ArrayList<UnionOfIntersections>();
        LRGroupNames = new ArrayList<LanguageRepresentationGroup>();
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;relation_chain&gt;"
     *     xmldescription="This tag defines a relation"
     */
    @XmlElement(name="relation_chain")
    public List<RelationChain> getRelations() {
        return relations;
    }

    @XmlTransient
    public List<LanguageRepresentationGroup> getLRGroupNames()
    {
        return LRGroupNames;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;LRGroupNames&gt;"
     *     xmldescription="This tag defines the names of the LanguageRepresentationGroup that should be used to express this intersection"
     */
   @XmlElement(name="LRGroupName")
    public List<String> getLRGroupNames_()
    {
       List<String> LRGroupNames_ = new ArrayList<String>();
       for(int i = 0; i < LRGroupNames.size(); i++)
       {
           LRGroupNames_.add(LRGroupNames.get(i).getName());
       }
        return LRGroupNames_;
    }

    public void setLRGroupNames(List<LanguageRepresentationGroup> LRGroupNames)
    {
        this.LRGroupNames = LRGroupNames;
    }

    private void setLRGroupNames_(List<String> v) throws Exception
    {
        for (int i = 0; i < v.size(); i++)
        {
            if (Globals.ToMergeAfterUnMarshalling)
            {
                LanguageRepresentationGroupDao lrgDao = new LanguageRepresentationGroupDaoImpl();
                List<LanguageRepresentationGroup> lrg = lrgDao.findAllByName(v.get(i).trim());
                if(lrg!=null && lrg.isEmpty()&&Constants.globalConcepts.get(v.get(i).trim())!=null)
                {
                    lrg.add((LanguageRepresentationGroup)Constants.globalConcepts.get(v.get(i).trim()));
                }
                if (lrg!=null && !lrg.isEmpty())
                {
                    LRGroupNames.addAll(lrg);
                    for(int j  = 0; j < lrg.size(); j++)
                    {
                        lrg.get(j).getLanguageRepresentationIntersections().add(this);
                    }
                }
                else
                {
                    LanguageRepresentationGroup c = new LanguageRepresentationGroup();

                    c.setName(v.get(i));
                    c.getLanguageRepresentationIntersections().add(this);
                    lrgDao.persist(c);
                    LRGroupNames.add(c);
                }

             }
             else
             {
                LanguageRepresentationGroup c = new LanguageRepresentationGroup();
                c.setName(v.get(i));
                c.getLanguageRepresentationIntersections().add(this);
                if (Constants.globalConcepts.contains(c))
                {
                    LRGroupNames.add((LanguageRepresentationGroup)Constants.globalConcepts.get(c.getName()));
                }
                else
                {
                    LRGroupNames.add(c);
                    Constants.globalConcepts.put(c.getName(), c);
                }

             }
        }
    }

    public void addRelationChain(RelationChain rc)
    {
        rc.getIntersections().add(this);
        relations.add(rc);
    }

    public void addRelationChain1way(RelationChain rc)
    {
        relations.add(rc);
    }

    public void setRelations(List<RelationChain> relations) {
        this.relations = relations;
    }

    @XmlTransient
    public List<UnionOfIntersections> getUnions() {
        return unions;
    }

    public void setUnions(List<UnionOfIntersections> unions) {
        this.unions = unions;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IntersectionOfRelations)) {
            return false;
        }
        IntersectionOfRelations other = (IntersectionOfRelations) object;
        if (this.relations.size() == other.relations.size())
        {
            boolean eq= true;
            for (int i = 0; i < this.relations.size(); i++)
            {
                if (!other.relations.contains(this.relations.get(i)))
                {
                    eq = false;
                    break;
                }
            }
            if (eq)
            {
                return true;
            }
        }
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
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
        return "csri.poeticon.praxicon.db.entities.IntersectionOfRelations[id=" + id + "]";
    }

    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        if (Globals.ToMergeAfterUnMarshalling)
        {
            RelationChainDao rcDao = new RelationChainDaoImpl();
            for (int i = 0; i<this.getRelations().size(); i++)
            {
                RelationChain rc =  rcDao.getEntity(this.getRelations().get(i));
                this.getRelations().set(i,rc);
            }
        }
        else
        {
            for (int i = 0; i<this.getRelations().size(); i++)
            {
                this.getRelations().get(i).getIntersections().add(this);

            }
        }
    }
}
