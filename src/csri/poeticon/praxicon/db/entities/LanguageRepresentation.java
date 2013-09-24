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
@Table(name="LanguageRepresentation")
public class LanguageRepresentation implements Serializable {

    public static enum Language {
        // ISO-639-1 standard
        AB, AA, AF, AK, SQ, AM, AR, AN, HY, AS, AV, AE, AY, BM, BA, EU, BE, BN, 
        BH, BI, BS, BR, BG, MY, CA, CH, CE, NY, ZH, CV, KW, CO, CR, HR, CS, DA,
        DV, NL, DZ, EN, EO, ET, EE, FO, FJ, FI, FR, FF, GL, KA, DE, EL, GN, GU,
        HT, HA, HE, HZ, HI, HO, HU, IA, ID, IE, GA, IG, IK, IO, IS, IT, IU, JA,
        JV, KL, KN, KR, KS, KK, KM, KI, RW, KY, KV, KG, KO, KU, KJ, LA, LB, LG,
        LI, LN, LO, LT, LU, LV, GV, MK, MG, MS, ML, MT, MI, MR, MH, MN, NA, NV,
        NB, ND, NE, NG, NN, NO, II, NR, OC, OJ, CU, OM, OR, OS, PA, PI, FA, PL,
        PS, PT, QU, RM, RN, RO, RU, SA, SC, SD, SE, SM, SG, SR, GD, SN, SI, SK,
        SL, SO, ST, AZ, ES, SU, SW, SS, SV, TA, TE, TG, TH, TI, BO, TK, TL, TN,
        TO, TR, TS, TT, TW, TY, UG, UK, UR, UZ, VE, VI, VO, WA, CY, WO, FY, XH,
        YI, YO, ZA, ZU;
        @Override
        public String toString()
        {
            return this.name();
        }
    }

    public static enum PartOfSpeech {
        ADJECTIVE, ADVERB, NOUN, PARTICIPLE, PROPER_NOUN, VERB;
        @Override
        public String toString()
        {
            return this.name();
        }
    }

    public static enum IsCompositional {
        YES, NO, UNKNOWN;
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
    @Column(name="LanguageRepresentationId")
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

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="LanguageRepresentation_RelationSubject",
        joinColumns={@JoinColumn(name="LanguageRepresentationId")},
        inverseJoinColumns={@JoinColumn(name="RelationId")}
    )
    private List<Relation> LanguageRepresentationSubject;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="LanguageRepresentation_RelationObject",
        joinColumns={@JoinColumn(name="LanguageRepresentationId")},
        inverseJoinColumns={@JoinColumn(name="RelationId")}
    )
    private List<Relation> LanguageRepresentationObject;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        joinColumns={@JoinColumn(name="RelationChainId")},
        inverseJoinColumns={@JoinColumn(name="LanguageRepresentationId")}
    )
    private List<RelationChain> LanguageRepresentationRelationChains;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        joinColumns={@JoinColumn(name="IntersectionId")},
        inverseJoinColumns={@JoinColumn(name="LanguagerepresentationId")}
    )
    private List<IntersectionOfRelations> LanguageRepresentationIntersections;



//    /**
//     * @xmlcomments.args
//     *	   xmltag="&lt;compositeWords&gt;"
//     *     xmldescription="This tag defines the composite words"
//     */
//
// Commented until the new structure becomes effective
//    @XmlElement()
//  public String getCompositeWords()
//    {
//        return composite_words;
//    }

//    public void setCompositeWords(String composite_words)
//    {
//        this.composite_words = composite_words;
//    }
//
//        /**
//     * @xmlcomments.args
//     *	   xmltag="&lt;multiword&gt;"
//     *     xmldescription="This tag defines if the LanguageRepresentation is multiword or not"
//     */
//    @XmlElement()
//    public boolean isMultiword()
//    {
//        return multiword;
//    }
//
//    public void setMultiword(boolean multiword)
//    {
//        this.multiword = multiword;
//    }
//
//    /**
//     * @xmlcomments.args
//     *	   xmltag="&lt;sound&gt;"
//     *     xmldescription="This tag defines the url to a sound file (a recond of the
//     * entry being said)"
//     */
//    @XmlElement()
//    public String getSound() {
//        return sound;
//    }
//
//    public String getSoundWithPath() {
//        return Constants.soundPath + sound;
//    }
//
//    public void setSound(String sound) {
//        this.sound = sound;
//    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;lang&gt;"
     *     xmldescription="This tag defines the language of the entry"
     */
    @XmlElement()
    public Language getLanguage() {
        return language;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;part_of_speech&gt;"
     *     xmldescription="This tag defines the Part Of Speech of the entry"
     */
    @XmlElement(name="part_of_speech")
    public PartOfSpeech getPartOfSpeech() {
        return part_of_speech;
    }

    public void setPartOfSpeech(PartOfSpeech p) {
        this.part_of_speech = p;
    }

    public void setPartOfSpeech(String p) {
        this.part_of_speech = PartOfSpeech.valueOf(p.trim().toUpperCase());
    }


//    @XmlTransient
//    public List<LanguageRepresentationGroup> getLanguageRepresentations() {
//        return LanguageRepresentations;
//    }

//Temporarily commented until LanguageRepresentation structure is ready
//    @XmlTransient
//    public List<Concept> getConcepts() {
//        List<Concept> concepts = new ArrayList<Concept>();
//
//        for (int i = 0; i < this.LanguageRepresentations.size(); i++)
//        {
//            for (int j = 0; j < this.LanguageRepresentations.get(i).getConcepts().size(); j++)
//            {
//                if (!concepts.contains(this.LanguageRepresentations.get(i).getConcepts().get(j)))
//                {
//                    concepts.add(this.LanguageRepresentations.get(i).getConcepts().get(j));
//                }
//            }
//        }
//        return concepts;
//    }
//
//    public void setLanguageRepresentations(List<LanguageRepresentationGroup> LanguageRepresentations) {
//        this.LanguageRepresentations = LanguageRepresentations;
//    }
//
    public LanguageRepresentation()
    {
//        LanguageRepresentation = new LanguageRepresentation();
    }

    public void setLanguage(Language language) {
        this.language = language;
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
        if (this.language!=null && this.text!=null && this.part_of_speech!=null &&
                this.language.name().equals(other.language.name())
                && this.text.equalsIgnoreCase(other.text)
                && this.part_of_speech == other.part_of_speech)
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
        return text + "\\"+ this.part_of_speech+" (" + language + ")";
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
