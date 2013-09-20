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
import javax.persistence.SequenceGenerator;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.persistence.Table;

/**
 *
 * @author Erevodifwntas
 */
@XmlRootElement()
@Entity
@Table(name="LR_GROUP")
public class LanguageRepresentationGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    @Column(name="LR_GROUP_ID")
    private Long id;
    
    @Column(name="NAME")
    private String name;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="LRGROUP_LR",
        joinColumns={@JoinColumn(name="LR_GROUP_ID")},
        inverseJoinColumns={@JoinColumn(name="LANG_ENT_ID")}
    )
    private List<LanguageRepresentation> entries;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="LRGROUP_RELATIONSUBJECT",
        joinColumns={@JoinColumn(name="LRGROUP_ID")},
        inverseJoinColumns={@JoinColumn(name="RELATION_ID")}
    )
    private List<Relation> LRSubject;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="LRGROUP_RELATIONOBJECT",
        joinColumns={@JoinColumn(name="LRGROUP_ID")},
        inverseJoinColumns={@JoinColumn(name="RELATION_ID")}
    )
    private List<Relation> LRObject;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        joinColumns={@JoinColumn(name="CHAIN_ID")},
        inverseJoinColumns={@JoinColumn(name="LRGROUP_ID")}
    )
    private List<RelationChain> LRRelationChains;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        joinColumns={@JoinColumn(name="INTERSECTION_ID")},
        inverseJoinColumns={@JoinColumn(name="LRGROUP_ID")}
    )
    private List<IntersectionOfRelations> LRIntersections;

    @ManyToMany(mappedBy="LRs")
    private List<Concept> concepts;
    
    @XmlAttribute()
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<IntersectionOfRelations> getLRIntersections()
    {
        return LRIntersections;
    }

    public void setLRIntersections(List<IntersectionOfRelations> LRIntersections)
    {
        this.LRIntersections = LRIntersections;
    }

    @XmlTransient
    public List<RelationChain> getLRRelationChains()
    {
        return LRRelationChains;
    }

    public void setLRRelationChains(List<RelationChain> LRRelationChains)
    {
        this.LRRelationChains = LRRelationChains;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;entry&gt;"
     *     xmldescription="This tag defines a lexical entry"
     */
    @XmlElement(name="entry")
    public List<LanguageRepresentation> getEntries() {
        return entries;
    }

    @XmlTransient
    public List<Relation> getLRObject()
    {
        return LRObject;
    }

    public void setLRObject(List<Relation> LRObject)
    {
        this.LRObject = LRObject;
    }

    @XmlTransient
    public List<Relation> getLRSubject()
    {
        return LRSubject;
    }

    public void setLRSubject(List<Relation> LRSubject)
    {
        this.LRSubject = LRSubject;
    }

    public LanguageRepresentationGroup()
    {
        entries = new ArrayList<LanguageRepresentation>();
        concepts = new ArrayList<Concept>();
        LRObject = new ArrayList<Relation>();
        LRSubject = new ArrayList<Relation>();
        this.LRIntersections = new ArrayList<IntersectionOfRelations>();
        this.LRRelationChains = new ArrayList<RelationChain>();
    }

    @XmlTransient
    public List<Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<Concept> concepts) {
        this.concepts = concepts;
    }

    public void setEntries(List<LanguageRepresentation> entries) {
        this.entries = entries;
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
        if (!(object instanceof LanguageRepresentationGroup)) {
            return false;
        }
        LanguageRepresentationGroup other = (LanguageRepresentationGroup) object;
        if (this.getEntries().size() == other.getEntries().size())
        {
            boolean eq= true;
            for (int i = 0; i < this.getEntries().size(); i++)
            {
                if (!other.getEntries().contains(this.getEntries().get(i)))
                {
                    return false;
                }
            }
            if (eq)
            {
                return true;
            }
        }
        if (this.name !=null && other.name !=null && this.name.equalsIgnoreCase(other.name))
        {
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
        StringBuilder sb = new StringBuilder();
        sb.append("[id=" + id + "] ");
        for (int i = 0; i < this.getEntries().size(); i++)
        {
            sb.append(this.getEntries().get(i)+" ");
        }
        return sb.toString();
    }

    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        if (this.getName()!=null && Constants.globalConcepts.get(this.getName())!=null)
        {
            this.setId(((LanguageRepresentationGroup)Constants.globalConcepts.get(this.getName())).getId());
        }
        else
        {
            if(this.getName()!=null)
            {
                Constants.globalConcepts.put(this.getName(),this);
            }
        }

        if (Globals.ToMergeAfterUnMarshalling)
        {
            LanguageRepresentationDao leDao = new LanguageRepresentationDaoImpl();
            for (int i = 0; i<this.entries.size(); i++)
            {
                this.getEntries().set(i, leDao.getEntity(this.getEntries().get(i)));
            }
        }
        else
        {
            for (int i = 0; i<this.entries.size(); i++)
            {
                this.entries.get(i).getLRs().add(this);
            }
        }
    }
}
