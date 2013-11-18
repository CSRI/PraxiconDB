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
public class ConceptOfSentence implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    @Column(name="SentenceConceptId")
    private Long id;

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="SentenceId")
    SentenceToAnalyze owner;

    Long concept;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "owner")
    List<ValueOfVariable> values;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "ownersub")
    List<SubstituteOfConcept> substitutes;

    String relationName;

    String conceptName;

    public ConceptOfSentence(Concept c)
    {
        // normaly we shouldn't need the following if clause, but we are not sure
        // if we set the concept type everytime. This is why we use the name "hack"
        setConcept(c);
        if (c.getName().indexOf("dummy")<0)
        {
            this.setIsVariable(false);
        }
        else
        {
            this.setIsVariable(true);
        }

        setConceptName(c.getName());
        this.values = new ArrayList<ValueOfVariable>();
        this.substitutes = new ArrayList<SubstituteOfConcept>();
    }

    public String getConceptName()
    {
        return conceptName;
    }

    public void setConceptName(String conceptName)
    {
        this.conceptName = conceptName;
    }

    public String getRelationName()
    {
        return relationName;
    }

    public void setRelationName(String relationName)
    {
        this.relationName = relationName;
    }

    boolean isVariable;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public ConceptOfSentence()
    {
        this.values = new ArrayList<ValueOfVariable>();
        this.substitutes = new ArrayList<SubstituteOfConcept>();
    }

    public List<SubstituteOfConcept> getSubstitutes()
    {
        return substitutes;
    }

    public void setSubstitutes(List<SubstituteOfConcept> substitutes)
    {
        this.substitutes = substitutes;
    }

    public Long getConcept()
    {
        return concept;
    }

    public Concept getActualConcept()
    {
        if(this.getConcept()!=null &&
                this.getConcept()>0)
        {
            ConceptDao cDao = new ConceptDaoImpl();
            return cDao.findById(concept);
        }

        else
        {
            Concept res = new Concept();
            res.setName(conceptName);
            return res;
        }
    }

    public void setConcept(Concept concept)
    {
        this.concept = concept.getId();
        if (concept.getStatus() == Concept.status.VARIABLE)
        {
            this.setIsVariable(true);
        }
        else
        {
            this.setIsVariable(false);
        }
    }

    public boolean isIsVariable()
    {
        return isVariable;
    }

    public void setIsVariable(boolean isVariable)
    {
        this.isVariable = isVariable;
    }

    public SentenceToAnalyze getOwner()
    {
        return owner;
    }

    public void setOwner(SentenceToAnalyze owner)
    {
        this.owner = owner;
    }

    public List<ValueOfVariable> getValuesSingle()
    {
        List<ValueOfVariable> res = new ArrayList<ValueOfVariable>();
        for(ValueOfVariable v : values)
        {
            if(!res.contains(v))
            {
                res.add(v);
            }
        }
        return res;
    }

    public List<ValueOfVariable> getValues()
    {
        return values;
    }

    public void setValues(List<ValueOfVariable> values)
    {
        this.values = values;
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
        if (!(object instanceof ConceptOfSentence))
        {
            return false;
        }
        ConceptOfSentence other = (ConceptOfSentence) object;
        if(this.conceptName.equalsIgnoreCase(other.getConceptName()))
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
        return this.getConceptName();
    }
}
