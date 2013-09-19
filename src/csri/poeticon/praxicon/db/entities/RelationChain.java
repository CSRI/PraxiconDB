/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.entities;

import csri.poeticon.praxicon.Constants;
import csri.poeticon.praxicon.Globals;
import csri.poeticon.praxicon.db.dao.LRGroupDao;
import csri.poeticon.praxicon.db.dao.implSQL.LRGroupDaoImpl;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
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
@Table(name="RELATION_CHAIN")
public class RelationChain implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    @Column(name="CHAIN_ID")
    private Long id;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="relations")
    @JoinTable(
    name="INRESECTION_RELATION",
    joinColumns={@JoinColumn(name="RELATION_ID")},
    inverseJoinColumns={@JoinColumn(name="INTERSECTION_ID")}
    )
    private List<IntersectionOfRelations> intersections;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "relationChain")
    private List<RelationChain_Relation> relations;

    @Column(name="NAME")
    String name="";

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="LRRelationChains")
    @JoinTable(
        name="LRGROUP_RELATIONCHAIN",
        joinColumns={@JoinColumn(name="CHAIN_ID")},
        inverseJoinColumns={@JoinColumn(name="LRGROUP_ID")}
    )
    List<LRGroup> LRGroupNames;

    public RelationChain()
    {
        intersections = new ArrayList<IntersectionOfRelations>();
        relations = new ArrayList<RelationChain_Relation>();
        LRGroupNames = new ArrayList<LRGroup>();
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<LRGroup> getLRGroupNames()
    {
        return LRGroupNames;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;LRGroupNames&gt;"
     *     xmldescription="This tag defines the names of the LRGroup that should be used to express this relation chain"
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

    public void setLRGroupNames(List<LRGroup> LRGroupNames)
    {
        this.LRGroupNames = LRGroupNames;
    }

    private void setLRGroupNames_(List<String> v) throws Exception
    {
        for (int i = 0; i < v.size(); i++)
        {
            if (Globals.ToMergeAfterUnMarshalling)
            {
                LRGroupDao lrgDao = new LRGroupDaoImpl();
                List<LRGroup> lrg = lrgDao.findAllByName(v.get(i).trim());
                if(lrg!=null && lrg.isEmpty()&&Constants.globalConcepts.get(v.get(i).trim())!=null)
                {
                    lrg.add((LRGroup)Constants.globalConcepts.get(v.get(i).trim()));
                }
                if (lrg!=null && !lrg.isEmpty())
                {
                    LRGroupNames.addAll(lrg);
                    for(int j  = 0; j < lrg.size(); j++)
                    {
                        lrg.get(j).getLRRelationChains().add(this);
                    }
                }
                else
                {
                    LRGroup c = new LRGroup();

                    c.setName(v.get(i));
                    c.getLRRelationChains().add(this);
                    lrgDao.persist(c);
                    LRGroupNames.add(c);
                }

             }
             else
             {
                LRGroup c = new LRGroup();
                c.setName(v.get(i));
                c.getLRRelationChains().add(this);
                if (Constants.globalConcepts.contains(c))
                {
                    LRGroupNames.add((LRGroup)Constants.globalConcepts.get(c.getName()));
                }
                else
                {
                    LRGroupNames.add(c);
                    Constants.globalConcepts.put(c.getName(), c);
                }

             }
        }
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;relation&gt;"
     *     xmldescription="This tag defines the relations of the entity
     */
    @XmlElement(name="relation_order")
    public List<RelationChain_Relation> getRelations() {
        return relations;
    }

    public List<Relation> getActualRelations() {
        List<Relation> rels = new ArrayList<Relation>(relations.size());
        for (int i = 0; i < relations.size(); i++)
        {
            rels.add(0, null);
        }
        for (int i = 0; i < relations.size(); i++)
        {
            rels.add((int)relations.get(i).getRelationOrder(), relations.get(i).getRelation());
            rels.remove((int)relations.get(i).getRelationOrder() + 1);
        }
        return rels;
    }

    public void setRelations(List<RelationChain_Relation> relations) {
        this.relations = relations;
    }

    public void addRelation(Relation relation, long order)
    {
        RelationChain_Relation rcr = new RelationChain_Relation();
        rcr.setRelation(relation);
        rcr.setRelationChain(this);
        rcr.setRelationOrder(order);

        this.relations.add(rcr);
    }
    
    @XmlTransient
    public List<IntersectionOfRelations> getIntersections() {
        return intersections;
    }

    public void setIntersections(List<IntersectionOfRelations> intersections) {
        this.intersections = intersections;
    }

    @XmlAttribute
    public Long getId() {
        return id;
    }

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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RelationChain)) {
            return false;
        }
        RelationChain other = (RelationChain) object;
        
        if (this.getRelations().size() == other.getRelations().size())
        {
            for (int i = 0; i < this.getRelations().size(); i++)
            {
                if (!other.getRelations().contains(this.getRelations().get(i)))
                {
                    return false;
                }
            }
            return true;
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
        try
        {
            StringBuilder sb = new StringBuilder();
            
            Concept start = this.getIntersections().get(0).getUnions().get(0).getConcept();
            sb.append(start.getName()).append(" (");

            for (int i = 0; i < this.getRelations().size(); i++)
            {
                for (int j = 0; j < this.getRelations().size(); j++)
                {
                    if (this.getRelations().get(j).getRelationOrder() == i)
                    {
                        if (this.getRelations().get(j).getRelation().getSubject().equals(start))
                        {
                            if( i == 0)
                            {
                                sb.append(this.getRelations().get(j).getRelation().getSubject().getName()).append(" ").append(this.getRelations().get(j).getRelation().getType().getForwardName().name().toUpperCase()).append(" ").append(this.getRelations().get(j).getRelation().getObject().getName());
                            }
                            else
                            {
                                sb.append(", ").append(this.getRelations().get(j).getRelation().getSubject().getName()).append(" ").append(this.getRelations().get(j).getRelation().getType().getBackwardName().name().toUpperCase()).append(" ").append(this.getRelations().get(j).getRelation().getObject().getName());
                            }
                            start = this.getRelations().get(j).getRelation().getObject();
                        }
                        else
                        {
                            if( i == 0)
                            {
                                sb.append(this.getRelations().get(j).getRelation().getSubject().getName()).append(" ").append(this.getRelations().get(j).getRelation().getType().getForwardName().name().toUpperCase()).append(" ").append(this.getRelations().get(j).getRelation().getObject().getName());
                            }
                            else
                            {
                                sb.append(", ").append(this.getRelations().get(j).getRelation().getObject().getName()).append(" ").append(this.getRelations().get(j).getRelation().getType().getBackwardName().name().toUpperCase()).append(" ").append(this.getRelations().get(j).getRelation().getSubject().getName());
                            }
                            start = this.getRelations().get(j).getRelation().getSubject();
                        }
                        break;
                    }
                }
            }
            sb.append(")");
            return sb.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "There is a problem with the database (Relations)";
        }
    }


}
