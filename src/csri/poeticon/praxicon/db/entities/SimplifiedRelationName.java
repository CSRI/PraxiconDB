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
@Table(name="SIMPLIFIED_RELATION_NAME")
public class SimplifiedRelationName implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    @Column(name="COMPLEX_NAME")
    private String complexName;

    @Column(name="SIMPLE_NAME")
    private String simpleName;

    public String getComplexName() {
        return complexName;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;left-to-right_name&gt;"
     *     xmldescription="The name of the relation when the relational triplet
     *     is used from left to right"
     */
    @XmlAttribute(name="complex_name")
    public void setComplexName(String complexName) {
        this.complexName = complexName;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;left-to-right_name&gt;"
     *     xmldescription="The name of the relation when the relational triplet
     *     is used from left to right"
     */
    @XmlAttribute(name="simple_name")
    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
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
        if (!(object instanceof SimplifiedRelationName)) {
            return false;
        }
        SimplifiedRelationName other = (SimplifiedRelationName) object;
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
        return this.getComplexName() + " - " + this.getSimpleName();
    }

}
