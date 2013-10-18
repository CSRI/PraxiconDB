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
 */
@Entity
@Table(name="RELATIONCHAIN_RELATION")
public class RelationChain_Relation implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    @Column(name="ID")
    private Long id;

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="CHAIN_ID")
    RelationChain relationChain;

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="RELATION_ID")
    Relation relation;

    @Column(name="ORDER_IN_RELATION")
    long relationOrder;

    @XmlElement
    public Relation getRelation()
    {
        return relation;
    }

    public void setRelation(Relation relation)
    {
        this.relation = relation;
    }

    @XmlTransient
    public RelationChain getRelationChain()
    {
        return relationChain;
    }

    public void setRelationChain(RelationChain relationChain)
    {
        this.relationChain = relationChain;
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
        return relationOrder;
    }

    public void setRelationOrder(long relationOrder)
    {
        this.relationOrder = relationOrder;
    }

    @XmlTransient
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
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
        if (this.relation.equals(other.relation))
        {
            return true;
        }
            
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }

        if (this.id == null && other.id == null)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "csri.poeticon.praxicon.db.entities.RelationChain_Relation[id=" + id + "]";
    }

    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        this.relationChain = (RelationChain)parent;
        if (Globals.ToMergeAfterUnMarshalling)
        {

        RelationDao rDao = new RelationDaoImpl();
        this.relation = rDao.getEntity(relation);
        }
    }
}
