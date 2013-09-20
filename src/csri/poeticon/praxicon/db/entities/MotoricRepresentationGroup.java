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
@Table(name="MRGROUP")
public class MotoricRepresentationGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    @Column(name="MR_GROUP_ID")
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
        name="MRGROUP_RELATIONOBJECT",
        joinColumns={@JoinColumn(name="MRGROUP_ID")},
        inverseJoinColumns={@JoinColumn(name="RELATION_ID")}
    )
    private List<Relation> MRObject;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="MRGROUP_RELATIONSUBJECT",
        joinColumns={@JoinColumn(name="MRGROUP_ID")},
        inverseJoinColumns={@JoinColumn(name="RELATION_ID")}
    )
    private List<Relation> MRSubject;

    @XmlTransient
    public List<Relation> getMRObject()
    {
        return MRObject;
    }

    public void setMRObject(List<Relation> MRObject)
    {
        this.MRObject = MRObject;
    }

    @XmlTransient
    public List<Relation> getMRSubject()
    {
        return MRSubject;
    }

    public void setMRSubject(List<Relation> MRSubject)
    {
        this.MRSubject = MRSubject;
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
        this.MRObject = new ArrayList<Relation>();
        this.MRSubject = new ArrayList<Relation>();
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
        return "csri.poeticon.praxicon.db.entities.MRGroup[id=" + id + "]";
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
