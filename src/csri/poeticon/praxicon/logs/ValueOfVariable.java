/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.logs;

import csri.poeticon.praxicon.db.dao.ConceptDao;
import csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import csri.poeticon.praxicon.db.entities.Concept;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

/**
 *
 * @author Erevodifwntas
 */
@Entity
public class ValueOfVariable implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    @Column(name="VALUE_ID")
    private Long id;

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="CS_ID")
    ConceptOfSentence owner;

    Long variableValue;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "owner")
    List<SubstituteOfConcept> substitutes;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public ValueOfVariable()
    {
        this.substitutes = new ArrayList<SubstituteOfConcept>();
    }

    public ConceptOfSentence getOwner()
    {
        return owner;
    }

    public void setOwner(ConceptOfSentence owner)
    {
        this.owner = owner;
    }

    public List<SubstituteOfConcept> getSubstitutes()
    {
        return substitutes;
    }

    public void setSubstitutes(List<SubstituteOfConcept> substitutes)
    {
        this.substitutes = substitutes;
    }

    public Long getVariableValue()
    {
        return variableValue;
    }

    public Concept getActualVariableValue()
    {
        ConceptDao cDao = new ConceptDaoImpl();

        return cDao.findById(variableValue);
    }

    public void setVariableValue(Concept variableValue)
    {
        this.variableValue = variableValue.getId();
    }

    public boolean isSelected()
    {
        for (SubstituteOfConcept sub:this.getSubstitutes())
        {
            if (sub.isUsed())
            {
                return true;
            }
        }
        return false;
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
        if (!(object instanceof ValueOfVariable))
        {
            return false;
        }
        ValueOfVariable other = (ValueOfVariable) object;
        if(this.getActualVariableValue().toString().equalsIgnoreCase(other.getActualVariableValue().toString()))
        {
            return true;
        }
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return this.getActualVariableValue().toString();
    }

}
