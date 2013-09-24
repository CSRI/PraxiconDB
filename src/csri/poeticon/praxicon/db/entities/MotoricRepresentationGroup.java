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
@Table(name="MotoricRepresentationGROUP")
public class MotoricRepresentationGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    @Column(name="MotoricRepresentation_GROUP_ID")
    private Long id;

    @Column(name="NAME")
    private String name;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "owner")
    private List<MotoricRepresentation> entries;

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="CONCEPT_ID")
    private Concept owner;


    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="MotoricRepresentationGROUP_RELATIONOBJECT",
        joinColumns={@JoinColumn(name="MotoricRepresentationGROUP_ID")},
        inverseJoinColumns={@JoinColumn(name="RELATION_ID")}
    )
    private List<Relation> MotoricRepresentationObject;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="MotoricRepresentationGROUP_RELATIONSUBJECT",
        joinColumns={@JoinColumn(name="MotoricRepresentationGROUP_ID")},
        inverseJoinColumns={@JoinColumn(name="RELATION_ID")}
    )
    private List<Relation> MotoricRepresentationSubject;

    @XmlTransient
    public List<Relation> getMotoricRepresentationObject()
    {
        return MotoricRepresentationObject;
    }

    public void setMotoricRepresentationObject(List<Relation> MotoricRepresentationObject)
    {
        this.MotoricRepresentationObject = MotoricRepresentationObject;
    }

    @XmlTransient
    public List<Relation> getMotoricRepresentationSubject()
    {
        return MotoricRepresentationSubject;
    }

    public void setMotoricRepresentationSubject(List<Relation> MotoricRepresentationSubject)
    {
        this.MotoricRepresentationSubject = MotoricRepresentationSubject;
    }

    @XmlElement(name="mr_entry")
    public List<MotoricRepresentation> getEntries() {
        return entries;
    }

    public void setEntries(List<MotoricRepresentation> entries) {
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

    public MotoricRepresentationGroup()
    {
        this.entries = new ArrayList<MotoricRepresentation>();
        this.MotoricRepresentationObject = new ArrayList<Relation>();
        this.MotoricRepresentationSubject = new ArrayList<Relation>();
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
        if (!(object instanceof MotoricRepresentationGroup)) {
            return false;
        }
        MotoricRepresentationGroup other = (MotoricRepresentationGroup) object;
        
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
        return "csri.poeticon.praxicon.db.entities.MotoricRepresentationGroup[id=" + id + "]";
    }

    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        if (this.getName()!=null && Constants.globalConcepts.get(this.getName())!=null)
        {
            this.setId(((MotoricRepresentationGroup)Constants.globalConcepts.get(this.getName())).getId());
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
