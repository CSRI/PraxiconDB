/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.entities;

import csri.poeticon.praxicon.Constants;
import csri.poeticon.praxicon.Globals;
import csri.poeticon.praxicon.db.dao.LanguageRepresentationDao;
import csri.poeticon.praxicon.db.dao.implSQL.LanguageRepresentationDaoImpl;
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
 * @author Dimitris Mavroeidis
 * 
 */
@XmlRootElement()
@Entity
@Table(name="RelationChain")
public class RelationChain implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    @Column(name="RelationChainId")
    private Long Id;

    @Column(name="Name")
    String name="";

    // OK
    @ManyToMany(cascade=CascadeType.ALL, mappedBy="Relations")
    @JoinTable(
    name="Intersection_Relation",
    joinColumns={@JoinColumn(name="RelationId")},
    inverseJoinColumns={@JoinColumn(name="IntersectionId")}
    )
    private List<IntersectionOfRelationChains> IntersectionsOfRelationChains;

    // OK
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "RelationChain")
    private List<RelationChain_Relation> Relations;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="LanguageRepresentation_RelationChains")
    @JoinTable(
        name="LanguageRepresentation_RelationChain",
        joinColumns={@JoinColumn(name="ChainId")},
        inverseJoinColumns={@JoinColumn(name="LanguageRepresentationId")}
    )
    List<LanguageRepresentation> LanguageRepresentationNames;

    
    // Constructor
    public RelationChain()
    {
        IntersectionsOfRelationChains = new ArrayList<IntersectionOfRelationChains>();
        Relations = new ArrayList<RelationChain_Relation>();
        LanguageRepresentationNames = new ArrayList<LanguageRepresentation>();
    }

    @XmlAttribute
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @XmlTransient
    public List<LanguageRepresentation> getLanguageRepresentationNames()
    {
        return LanguageRepresentationNames;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;LRGroupNames&gt;"
     *     xmldescription="This tag defines the names of the LanguageRepresentationGroup that should be used to express this relation chain"
     */
    @XmlElement(name="LRGroupName")
    public List<String> getLanguageRepresentationNames_()
    {
       List<String> language_representation_names_ = new ArrayList<String>();
       for(int i = 0; i < LanguageRepresentationNames.size(); i++)
       {
           language_representation_names_.add(LanguageRepresentationNames.get(i).getText());
       }
        return language_representation_names_;
    }

    public void setLanguageRepresentationNames(List<LanguageRepresentation> language_representation_names)
    {
        this.LanguageRepresentationNames = language_representation_names;
    }

// TODO: Obsolete???
//    private void setLanguageRepresentationNames_(List<String> v) throws Exception
//    {
//        for (int i = 0; i < v.size(); i++)
//        {
//            if (Globals.ToMergeAfterUnMarshalling)
//            {
//                LanguageRepresentationGroupDao lrgDao = new LanguageRepresentationGroupDaoImpl();
//                List<LanguageRepresentationGroup> lrg = lrgDao.findAllByName(v.get(i).trim());
//                if(lrg!=null && lrg.isEmpty()&&Constants.globalConcepts.get(v.get(i).trim())!=null)
//                {
//                    lrg.add((LanguageRepresentationGroup)Constants.globalConcepts.get(v.get(i).trim()));
//                }
//                if (lrg!=null && !lrg.isEmpty())
//                {
//                    language_representation_names.addAll(lrg);
//                    for(int j  = 0; j < lrg.size(); j++)
//                    {
//                        lrg.get(j).getLRRelationChains().add(this);
//                    }
//                }
//                else
//                {
//                    LanguageRepresentationGroup c = new LanguageRepresentationGroup();
//
//                    c.setName(v.get(i));
//                    c.getLRRelationChains().add(this);
//                    lrgDao.persist(c);
//                    language_representation_names.add(c);
//                }
//
//             }
//             else
//             {
//                LanguageRepresentation c = new LanguageRepresentation();
//                c.setText(v.get(i));
//                c.getLanguageRepresentationRelationChains().add(this);
//                if (Constants.globalConcepts.contains(c))
//                {
//                    language_representation_names.add((LanguageRepresentation)Constants.globalConcepts.get(c.getText()));
//                }
//                else
//                {
//                    language_representation_names.add(c);
//                    Constants.globalConcepts.put(c.getText(), c);
//                }
//
//             }
//        }
//    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;relation&gt;"
     *     xmldescription="This tag defines the relations of the entity
     */
    @XmlElement(name="relation_order")
    public List<RelationChain_Relation> getRelations()
    {
        return Relations;
    }

    public List<Relation> getActualRelations()
    {
        List<Relation> rels = new ArrayList<Relation>(Relations.size());
        for (int i = 0; i < Relations.size(); i++)
        {
            rels.add(0, null);
        }
        for (int i = 0; i < Relations.size(); i++)
        {
            rels.add((int)Relations.get(i).getRelationOrder(), Relations.get(i).getRelation());
            rels.remove((int)Relations.get(i).getRelationOrder() + 1);
        }
        return rels;
    }

    public void setRelations(List<RelationChain_Relation> relations)
    {
        this.Relations = relations;
    }

    public void addRelation(Relation relation, long order)
    {
        RelationChain_Relation rcr = new RelationChain_Relation();
        rcr.setRelation(relation);
        rcr.setRelationChain(this);
        rcr.setRelationOrder(order);
        this.Relations.add(rcr);
    }
    
    @XmlTransient
    public List<IntersectionOfRelationChains> getIntersections()
    {
        return IntersectionsOfRelationChains;
    }

    public void setIntersections(List<IntersectionOfRelationChains> intersections)
    {
        this.IntersectionsOfRelationChains = intersections;
    }

    @XmlAttribute
    public Long getId()
    {
        return Id;
    }

    public void setId(Long id)
    {
        this.Id = id;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (Id != null ? Id.hashCode() : 0);
        return hash;
    }



    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RelationChain))
        {
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

        if ((this.Id == null && other.Id != null) || (this.Id != null && !this.Id.equals(other.Id))) {
            return false;
        }

        if (this.Id == null && other.Id == null)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        try
        {
            StringBuilder sb = new StringBuilder();
            
            Concept start = this.getIntersections().get(0).getConcept();
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
