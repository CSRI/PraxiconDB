/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Erevodifwntas
 */
@XmlRootElement()
@Entity
@Table(name="SimplifiedRelationNames")
public class SimplifiedRelationName implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    public Long getId()
    {
        return Id;
    }

    @Column(name="ComplexName")
    private String ComplexName;

    @Column(name="SimpleName")
    private String SimpleName;

    public String getComplexName()
    {
        return ComplexName;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;left-to-right_name&gt;"
     *     xmldescription="The name of the relation when the relational triplet
     *     is used from left to right"
     */
    @XmlAttribute(name="complex_name")
    public void setComplexName(String complex_name)
    {
        this.ComplexName = complex_name;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;left-to-right_name&gt;"
     *     xmldescription="The name of the relation when the relational triplet
     *     is used from left to right"
     */
    @XmlAttribute(name="simple_name")
    public String getSimpleName()
    {
        return SimpleName;
    }

    public void setSimpleName(String simple_name)
    {
        this.SimpleName = simple_name;
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
        if (!(object instanceof SimplifiedRelationName))
        {
            return false;
        }
        SimplifiedRelationName other = (SimplifiedRelationName) object;
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
        return this.getComplexName() + " - " + this.getSimpleName();
    }
}
