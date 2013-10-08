/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.entities;

import csri.poeticon.praxicon.Constants;
import csri.poeticon.praxicon.Globals;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Dimitris Mavroeidis
 * 
 */
@XmlRootElement()
@Entity
@Table(name="Compositionality")
public class Compositionality implements Serializable {


    public static enum CompositionalityType {
        MULTIWORD, COMPOSITE_WORD, UNKNOWN;
        @Override
        public String toString()
        {
            return this.name();
        }
    }


    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    @Column(name="CompositionalityId")
    private Long id;

    @Column(name="CompositionalityType")
    @Enumerated(EnumType.STRING)
    private CompositionalityType compositionality_type;

    // Foreign keys
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "LanguageRepresentation")
    private LanguageRepresentation language_representation;
    
}
