/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.entities;

import csri.poeticon.praxicon.Constants;
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
import javax.persistence.OneToMany;
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
@Table(name="VisualRepresentationGROUP")
public class VisualRepresentationGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    @Column(name="VisualRepresentation_GROUP_ID")
    private Long id;

    @Column(name="NAME")
    private String name;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "owner")
    private List<VisualRepresentation> entries;

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="CONCEPT_ID")
    private Concept owner;
    
    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="VisualRepresentationGROUP_RELATIONSUBJECT",
        joinColumns={@JoinColumn(name="VisualRepresentationGROUP_ID")},
        inverseJoinColumns={@JoinColumn(name="RELATION_ID")}
    )
    private List<Relation> VisualRepresentationSubject;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="VisualRepresentationGROUP_RELATIONOBJECT",
        joinColumns={@JoinColumn(name="VisualRepresentationGROUP_ID")},
        inverseJoinColumns={@JoinColumn(name="RELATION_ID")}
    )
    private List<Relation> VisualRepresentationObject;

    @XmlTransient
    public List<Relation> getVisualRepresentationObject()
    {
        return VisualRepresentationObject;
    }

    public void setVisualRepresentationObject(List<Relation> VisualRepresentationObject)
    {
        this.VisualRepresentationObject = VisualRepresentationObject;
    }

    @XmlTransient
    public List<Relation> getVisualRepresentationSubject()
    {
        return VisualRepresentationSubject;
    }

    public void setVisualRepresentationSubject(List<Relation> VisualRepresentationSubject)
    {
        this.VisualRepresentationSubject = VisualRepresentationSubject;
    }

    @XmlElement(name="vr_entry")
    public List<VisualRepresentation> getEntries() {
        return entries;
    }

    public void setEntries(List<VisualRepresentation> entries) {
        this.entries = entries;
    }

    @XmlAttribute()
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Concept getOwner() {
        return owner;
    }

    public void setOwner(Concept owner) {
        this.owner = owner;
    }

    @XmlTransient
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VisualRepresentationGroup()
    {
        this.entries = new ArrayList<VisualRepresentation>();
        this.VisualRepresentationObject = new ArrayList<Relation>();
        this.VisualRepresentationSubject = new ArrayList<Relation>();
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
        if (!(object instanceof VisualRepresentationGroup)) {
            return false;
        }
        VisualRepresentationGroup other = (VisualRepresentationGroup) object;
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
        return "csri.poeticon.praxicon.db.entities.VisualRepresentationGroup[id=" + id + "]";
    }

    public void add(VisualRepresentation vr)
    {
        vr.setOwner(this);
        getEntries().add(vr);
    }

    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        if (this.getName()!=null && Constants.globalConcepts.get(this.getName())!=null)
        {
            this.setId(((VisualRepresentationGroup)Constants.globalConcepts.get(this.getName())).getId());
        }
        else
        {
            if(this.getName()!=null)
            {
                Constants.globalConcepts.put(this.getName(),this);
            }
        }
    }
    
}
