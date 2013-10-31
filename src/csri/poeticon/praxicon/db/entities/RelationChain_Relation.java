/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.entities;

import csri.poeticon.praxicon.Globals;
import csri.poeticon.praxicon.db.dao.RelationDao;
import csri.poeticon.praxicon.db.dao.implSQL.RelationDaoImpl;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Erevodifwntas
 * @author Dimitris Mavroeidis
 * 
 */
@Entity
@Table(name="RelationChain_Relation")
public class RelationChain_Relation implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    @Column(name="RelationChain_RelationId")
    private Long Id;

    // OK
    // TODO: This could be a OneToMany and the corresponding a ManyToOne
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="RelationChainId")
    RelationChain RelationChain;

    // OK
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="RelationId")
    Relation Relation;

    @Column(name="RelationOrder")
    long RelationOrder;

    @XmlElement
    public Relation getRelation()
    {
        return Relation;
    }

    public void setRelation(Relation relation)
    {
        this.Relation = relation;
    }

    @XmlTransient
    public RelationChain getRelationChain()
    {
        return RelationChain;
    }

    public void setRelationChain(RelationChain relationChain)
    {
        this.RelationChain = relationChain;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="order"
     *     xmldescription="This attribute marks that the order of the relation
     *                      in the relation chain
     */
    @XmlAttribute(name="order")
    public long getRelationOrder()
    {
        return RelationOrder;
    }

    public void setRelationOrder(long relationOrder)
    {
        this.RelationOrder = relationOrder;
    }

    @XmlTransient
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
        if (!(object instanceof RelationChain_Relation)) {
            return false;
        }
        RelationChain_Relation other = (RelationChain_Relation) object;
        if (this.Relation.equals(other.Relation))
        {
            return true;
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
        return "csri.poeticon.praxicon.db.entities.RelationChain_Relation[id=" + Id + "]";
    }

    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        this.RelationChain = (RelationChain)parent;
        if (Globals.ToMergeAfterUnMarshalling)
        {
            RelationDao rDao = new RelationDaoImpl();
            this.Relation = rDao.getEntity(Relation);
        }
    }
}
