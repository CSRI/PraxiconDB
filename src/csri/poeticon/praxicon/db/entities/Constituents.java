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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Dimitris Mavroeidis
 */
@XmlRootElement()
@Entity
@Table(name="Constituents")
public class Constituents implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    @Column(name="ConstituentId")
    private Long id;

    @Column(name="Language")
    @Enumerated(EnumType.STRING)
    private Language language;

    @Column(name="PartOfSpeech")
    @Enumerated(EnumType.STRING)
    private PartOfSpeech part_of_speech;
    
    @Column(name="IsCompositional")
    private IsCompositional is_compositional;

    @Column(name="Text")
    private String text;

    @Column(name="Comment")
    private String comment;

    //private List<LRGroup> LanguageResrouces;

}