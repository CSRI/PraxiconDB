/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.entities;

import csri.poeticon.praxicon.Globals;
import csri.poeticon.praxicon.db.dao.RelationChainDao;
import csri.poeticon.praxicon.db.dao.implSQL.RelationChainDaoImpl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
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
 * @author Dimitris Mavroeidis
 * 
 */
@XmlRootElement()
@Entity
@Table(name="IntersectionsOfRelationChains")
public class IntersectionOfRelationChains implements Serializable
{

    public static enum inherent
    {
        YES, NO, UNKNOWN ;
        @Override
        public String toString()
        {
            return this.name();
        }
    }

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    @Column(name="IntersectionOfRelationChainsId")
    private Long Id;

    @Column(name="Name")
    private String Name;

    @Column(name="Inherent")
    @Enumerated(EnumType.STRING)
    protected inherent Inherent;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="Intersection_Relation",
        joinColumns={@JoinColumn(name="IntersectionId")},
        inverseJoinColumns={@JoinColumn(name="RelationId")}
    )
    private List<RelationChain> RelationChains;

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    //@JoinColumn(name="ConceptId")
    private Concept Concept;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="LanguageRepresentation_IntersectionOfRelationChains",
        joinColumns={@JoinColumn(name="LanguageRepresentationId")},
        inverseJoinColumns={@JoinColumn(name="IntersectionOfRelationChainsId")}
    )
    private List<LanguageRepresentation> LanguageRepresentationNames;


    public IntersectionOfRelationChains()
    {
        RelationChains = new ArrayList<RelationChain>();
//        unions = new ArrayList<UnionOfIntersections>();  //Obsolete
        LanguageRepresentationNames = new ArrayList<LanguageRepresentation>();
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;relation_chain&gt;"
     *     xmldescription="This tag defines a relation"
     */
    @XmlElement(name="relation_chain")
    public List<RelationChain> getRelations()
    {
        return RelationChains;
    }

    @XmlTransient
    public List<LanguageRepresentation> getLanguageRepresentationNames()
    {
        return LanguageRepresentationNames;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;LRGroupNames&gt;"
     *     xmldescription="This tag defines the names of the LanguageRepresentationGroup that should be used to express this intersection"
     */
   @XmlElement(name="LanguageRepresentationName")
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

    public void addRelationChain(RelationChain rc)
    {
        rc.getIntersections().add(this);
        RelationChains.add(rc);
    }

    public void addRelationChain1way(RelationChain rc)
    {
        RelationChains.add(rc);
    }

    public void setRelationChains(List<RelationChain> relation_chains)
    {
        this.RelationChains = relation_chains;
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

    @XmlAttribute()
    public String getName()
    {
        return Name;
    }

    public void setName(String name)
    {
        this.Name = name;
    }

    @XmlTransient
    public Concept getConcept()
    {
        return Concept;
    }

    public void setConcept(Concept concept)
    {
        this.Concept = concept;
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
        if (!(object instanceof IntersectionOfRelationChains))
        {
            return false;
        }
        IntersectionOfRelationChains other = (IntersectionOfRelationChains) object;
        if (this.RelationChains.size() == other.RelationChains.size())
        {
            boolean eq= true;
            for (int i = 0; i < this.RelationChains.size(); i++)
            {
                if (!other.RelationChains.contains(this.RelationChains.get(i)))
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
        if ((this.Id == null && other.Id != null) || (this.Id != null && !this.Id.equals(other.Id)))
        {
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
        return "csri.poeticon.praxicon.db.entities.IntersectionOfRelations[id=" + Id + "]";
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
