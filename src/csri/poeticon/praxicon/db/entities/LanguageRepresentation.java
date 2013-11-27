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
import javax.persistence.ManyToOne;
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
 * @author Dimitris Mavroeidis
 * 
 */
@XmlRootElement()
@Entity
@Table(name="LanguageRepresentations")
public class LanguageRepresentation implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static List<LanguageRepresentation> language_representations;

    public static enum language
    {
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

    public static enum part_of_speech
    {
        ADJECTIVE, ADVERB, NOUN, PARTICIPLE, PROPER_NOUN, VERB;
        @Override
        public String toString()
        {
            return this.name();
        }
    }

    public static enum is_compositional
    {
        YES, NO, UNKNOWN;
        @Override
        public String toString()
        {
            return this.name();
        }
    }

    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    @Column(name="LanguageRepresentationId")
    private Long Id;

    @Column(name="Language")
    @Enumerated(EnumType.STRING)
    private language Language;

    @Column(name="PartOfSpeech")
    @Enumerated(EnumType.STRING)
    private part_of_speech PartOfSpeech;
    
    @Column(name="IsCompositional")
    private is_compositional IsCompositional;

    @Column(name="Text")
    private String Text;

    @Column(name="Comment")
    private String Comment;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="LanguageRepresentations")
    @JoinTable(
        name="Concept_LanguageRepresentation",
        joinColumns={@JoinColumn(name="ConceptId")},
        inverseJoinColumns={@JoinColumn(name="LanguageRepresentationId")}
    )
    private List<Concept> Concepts;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="LanguageRepresentation_RelationSubject",
        joinColumns={@JoinColumn(name="LanguageRepresentationId")},
        inverseJoinColumns={@JoinColumn(name="RelationId")}
    )
    private List<Relation> RelationsWithLanguageRepresentationAsSubject;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="LanguageRepresentation_RelationObject",
        joinColumns={@JoinColumn(name="LanguageRepresentationId")},
        inverseJoinColumns={@JoinColumn(name="RelationId")}
    )
    private List<Relation> RelationsWithLanguageRepresentationAsObject;

    // 
    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        joinColumns={@JoinColumn(name="RelationChainId")},
        inverseJoinColumns={@JoinColumn(name="LanguageRepresentationId")}
    )
    private List<RelationChain> LanguageRepresentationRelationChains;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="LanguageRepresentationNames")
    @JoinTable(
        name="LanguageRepresentation_IntersectionOfRelationChains",
        joinColumns={@JoinColumn(name="IntersectionOfRelationChainsId")},
        inverseJoinColumns={@JoinColumn(name="LanguageRepresentationId")}
    )
    private List<IntersectionOfRelationChains> LanguageRepresentationIntersections;

    // Foreign key
    @ManyToOne(cascade=CascadeType.ALL)
    private Constituents LanguageRepresentationConstituents;

    // Foreign key
    @ManyToOne(cascade=CascadeType.ALL)
    private Constituents Constituents;

    // Foreign key
    @ManyToOne(cascade=CascadeType.ALL)
    private Compositionality Compositionality;

    public LanguageRepresentation()
    {
        language_representations = new ArrayList<LanguageRepresentation>();
    }

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

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;lang&gt;"
     *     xmldescription="This tag defines the language of the entry"
     */
    @XmlElement()
    public language getLanguage()
    {
        return Language;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;part_of_speech&gt;"
     *     xmldescription="This tag defines the Part Of Speech of the entry"
     */
    @XmlElement(name="part_of_speech")
    public part_of_speech getPartOfSpeech()
    {
        return PartOfSpeech;
    }

    public void setPartOfSpeech(part_of_speech pos)
    {
        this.PartOfSpeech = pos;
    }

    public void setPartOfSpeech(String pos)
    {
        // TODO: Check if it returns the correct value.
        this.PartOfSpeech = part_of_speech.valueOf(pos.trim().toUpperCase());
    }

    @XmlTransient
    public List<LanguageRepresentation> getLanguageRepresentations()
    {
        List<LanguageRepresentation> language_representations_list = new ArrayList<LanguageRepresentation>();
        for (int i=0; i < this.language_representations.size(); i++)
        {
            language_representations_list.add(language_representations.get(i));
        }
        return language_representations_list;
    }

    @XmlTransient
    public List<Concept> getConcepts()
    {
        List<Concept> concepts = new ArrayList<Concept>();

        for (int i = 0; i < this.language_representations.   size(); i++)
        {
            for (int j = 0; j < this.language_representations.get(i).getConcepts().size(); j++)
            {
                if (!concepts.contains(this.language_representations.get(i).getConcepts().get(j)))
                {
                    concepts.add(this.language_representations.get(i).getConcepts().get(j));
                }
            }
        }
        return concepts;
    }

    public void setLanguageRepresentations(List<LanguageRepresentation> language_representations)
    {
        this.language_representations = language_representations;
    }

    public void setLanguage(language language)
    {
        this.Language = language;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;text&gt;"
     *     xmldescription="This tag defines the text of the entry"
     */
    @XmlElement()
    public String getText()
    {
        return Text;
    }

    public void setText(String text)
    {
        this.Text = text;
    }

    @XmlAttribute
    public Long getId()
    {
        return Id;
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
        if (!(object instanceof LanguageRepresentation))
        {
            return false;
        }
        LanguageRepresentation other = (LanguageRepresentation) object;
        if (this.Language!=null && this.Text!=null && this.PartOfSpeech!=null &&
                this.Language.name().equals(other.Language.name())
                && this.Text.equalsIgnoreCase(other.Text)
                && this.PartOfSpeech == other.PartOfSpeech)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public String toString()
    {
        return Text + "\\"+ this.PartOfSpeech+" (" + Language + ")";
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