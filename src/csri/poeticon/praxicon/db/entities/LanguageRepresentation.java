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
 * @author Erevodifwntas
 */
@XmlRootElement()
@Entity
@Table(name="LANGUAGE_REPRESENTATION")
public class LanguageRepresentation implements Serializable {

    public static enum POS {
        NOUN, VERB, ADJECTIVE;
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
    @Column(name="LANG_ENT_ID")
    private Long id;

    @Column(name="LANG")
    private String lang;

    @Column(name="IS_MULTIWORD")
    private boolean multiword;

    @Column(name="COMPOSITE_WORDS")
    private String compositeWords;

    @Column(name="TEXT")
    private String text;

    @Column(name="PART_OF_SPEECH")
    @Enumerated(EnumType.STRING)
    private POS POS;

    @Column(name="SOUND")
    private String sound;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="entries")
     @JoinTable(
        name="LRGROUP_LR",
        joinColumns={@JoinColumn(name="LANG_ENT_ID")},
        inverseJoinColumns={@JoinColumn(name="LR_GROUP_ID")}
    )
    private List<LRGroup> LRs;

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;compositeWords&gt;"
     *     xmldescription="This tag defines the composite words"
     */
    @XmlElement()
    public String getCompositeWords()
    {
        return compositeWords;
    }

    public void setCompositeWords(String compositeWords)
    {
        this.compositeWords = compositeWords;
    }

        /**
     * @xmlcomments.args
     *	   xmltag="&lt;multiword&gt;"
     *     xmldescription="This tag defines if the LR is multiword or not"
     */
    @XmlElement()
    public boolean isMultiword()
    {
        return multiword;
    }

    public void setMultiword(boolean multiword)
    {
        this.multiword = multiword;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;sound&gt;"
     *     xmldescription="This tag defines the url to a sound file (a recond of the
     * entry being said)"
     */
    @XmlElement()
    public String getSound() {
        return sound;
    }

    public String getSoundWithPath() {
        return Constants.SoundPath + sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;lang&gt;"
     *     xmldescription="This tag defines the language of the entry"
     */
    @XmlElement()
    public String getLang() {
        return lang;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;part_of_speech&gt;"
     *     xmldescription="This tag defines the Part Of Speech of the entry"
     */
    @XmlElement(name="part_of_speech")
    public POS getPOS() {
        return POS;
    }

    public void setPOS(POS p) {
        this.POS = p;
    }

    public void setPOS(String p) {
        this.POS = POS.valueOf(p.trim().toUpperCase());
    }


    @XmlTransient
    public List<LRGroup> getLRs() {
        return LRs;
    }

    @XmlTransient
    public List<Concept> getConcepts() {
        List<Concept> concepts = new ArrayList<Concept>();

        for (int i = 0; i < this.LRs.size(); i++)
        {
            for (int j = 0; j < this.LRs.get(i).getConcepts().size(); j++)
            {
                if (!concepts.contains(this.LRs.get(i).getConcepts().get(j)))
                {
                    concepts.add(this.LRs.get(i).getConcepts().get(j));
                }
            }
        }
        return concepts;
    }

    public void setLRs(List<LRGroup> LRs) {
        this.LRs = LRs;
    }

    public LanguageRepresentation()
    {
        LRs = new ArrayList<LRGroup>();
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;text&gt;"
     *     xmldescription="This tag defines the text of the entry"
     */
    @XmlElement()
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @XmlAttribute
    public Long getId() {
        return id;
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
        if (!(object instanceof LanguageRepresentation)) {
            return false;
        }
        LanguageRepresentation other = (LanguageRepresentation) object;
        if (this.lang!=null && this.text!=null && this.POS!=null &&
                this.lang.equalsIgnoreCase(other.lang)
                && this.text.equalsIgnoreCase(other.text)
                && this.POS == other.POS)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public String toString() {
        return text + "\\"+ this.POS+" (" + lang + ")";
    }

    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        if (!Globals.ToMergeAfterUnMarshalling)
        {
          /*  try {
                String tmp = new String(this.getText().getBytes(), "UTF-8");
                this.setText(tmp);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(LanguageEntry.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }
    }

}
