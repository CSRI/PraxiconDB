/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.logs;

import csri.poeticon.praxicon.db.dao.ConceptDao;
import csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import csri.poeticon.praxicon.db.entities.Concept;
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

/**
 *
 * @author Erevodifwntas
 */
@Entity
public class SubstituteOfConcept implements Serializable, Comparable
{
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    @Column(name="SubstitudeId")
    private Long id;

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="ValueId")
    ValueOfVariable owner;

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="SentenceConceptId")
    ConceptOfSentence ownersub;

    Long concept;

    boolean used;

    double orderValue;

    public SubstituteOfConcept()
    {
        used = false;
    }

    public double getOrderValue()
    {
        return orderValue;
    }

    public void setOrderValue(double orderValue)
    {
        this.orderValue = orderValue;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public boolean isUsed()
    {
        return used;
    }

    public ConceptOfSentence getOwnersub()
    {
        return ownersub;
    }

    public void setOwnersub(ConceptOfSentence ownersub)
    {
        this.ownersub = ownersub;
    }

    public void setUsed(boolean used)
    {
        this.used = used;
    }

    public Long getConcept()
    {
        return concept;
    }

    public void setConcept(Concept concept)
    {
        this.concept = concept.getId();
    }

    public Concept getActualConcept()
    {
        ConceptDao cDao = new ConceptDaoImpl();
        return cDao.findById(concept);
    }

    public ValueOfVariable getOwner()
    {
        return owner;
    }

    public void setOwner(ValueOfVariable owner)
    {
        this.owner = owner;
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
        if (!(object instanceof SubstituteOfConcept))
        {
            return false;
        }
        SubstituteOfConcept other = (SubstituteOfConcept) object;
        if (this.getConcept().equals(other.getConcept()))
        {
            return true;
        }
        if (this.getConcept().equals(other.getConcept()))
        {
            return true;
        }
        return false;
    }

    @Override
    public String toString()
    {
        if (this.isUsed())
        {
            return this.getActualConcept().toString() + " (selected)";
        }
        else
        {
            return this.getActualConcept().toString() + " (not selected)";
        }
    }

    @Override
    public int compareTo(Object o)
    {
        if (o instanceof SubstituteOfConcept)
        {
            SubstituteOfConcept other = (SubstituteOfConcept)o;
            if(this.getOrderValue()>other.getOrderValue())
            {
                return 1;
            }
            return -1;
        }
        return -1;
    }
}
