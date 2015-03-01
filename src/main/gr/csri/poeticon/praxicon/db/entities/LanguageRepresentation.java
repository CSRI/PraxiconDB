/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

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
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author dmavroeidis
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "language_representation", namespace =
        "http://www.csri.gr/language_representation")
@Entity
@NamedQueries({
    @NamedQuery(name = "findAllLanguageRepresentations", query =
            "FROM LanguageRepresentation lr"),
    @NamedQuery(name =
            "findLanguageRepresentationsByTextLanguagePosPStatusCaseInsensitive",
            query =
            "FROM LanguageRepresentation lr " +
            "WHERE UPPER(lr.text) = :text " +
            "AND lr.language = :language " +
            "AND lr.partOfSpeech = :pos " +
            "AND lr.useStatus = :useStatus"),
    @NamedQuery(name =
            "findLanguageRepresentationsByTextLanguagePosPStatus", query =
            "FROM LanguageRepresentation lr " +
            "WHERE lr.text = :text " +
            "AND lr.language = :language " +
            "AND lr.partOfSpeech = :pos " +
            "AND lr.useStatus = :useStatus"),
    @NamedQuery(name = "getLanguageRepresentationEntityQuery", query =
            "FROM LanguageRepresentation lr " +
            "WHERE UPPER(lr.text) = :text " +
            "AND UPPER(lr.language) = :language " +
            "AND UPPER(lr.partOfSpeech) = :pos " +
            "AND UPPER(lr.useStatus) = :useStatus"),
    @NamedQuery(name = "findLanguageRepresentationsByText", query =
            "FROM LanguageRepresentation lr " +
            "WHERE UPPER(lr.text) = :text"),
    @NamedQuery(name = "getAllLanguageRepresentationTextByText", query =
            "SELECT DISTINCT lr.text FROM LanguageRepresentation lr"),})
@Table(name = "LanguageRepresentations",
        indexes = {
            @Index(columnList = "Text"),})
public class LanguageRepresentation implements Serializable {

    private static final long serialVersionUID = 1L;
    private static List<LanguageRepresentation> language_representations;

    /**
     * Enumeration of the languages.
     */
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
        public String toString() {
            return this.name();
        }
    }

    /**
     * Enumeration of the types of part of speech.
     */
    public static enum PartOfSpeech {

        ADJECTIVE, ADVERB, NOUN, PARTICIPLE, PROPER_NOUN, VERB, VERB_PHRASE;

        @Override
        public String toString() {
            return this.name();
        }
    }

    /**
     * Enumeration of the types of use status.
     */
    public static enum UseStatus {

        FIGURATIVE, LITERAL, UNKNOWN;

        @Override
        public String toString() {
            return this.name();
        }
    }

    /**
     * Enumeration of the types of productivity.
     * FULL: The word can produce other words, but is not a product itself.
     * PARTIAL: The word can both be a product and produce.
     * NONE: The word cannot produce other words.
     *
     */
    public static enum Productivity {

        FULL, PARTIAL, NONE, UNKNOWN;

        @Override
        public String toString() {
            return this.name();
        }
    }
    
    /**
     * Enumeration of the types of operator.
     * NONE: Default value; when there is no operator.
     * MORE: Denotes quantity estimation. Augmentative mechanism of language.
     * LESS: Denotes quantity estimation. Diminutive mechanism of language.
     * SAME: Canonical/frequent form. Frame of reference.
     * CLOSE: Body and Now frame of reference.
     *
     */
    public static enum Operator {

        NONE, MORE, LESS, SAME, CLOSE;

        @Override
        public String toString() {
            return this.name();
        }
    }
    

    @Id
    @SequenceGenerator(name = "CUST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CUST_SEQ")
    @Column(name = "LanguageRepresentationId")
//    @XmlAttribute
    @XmlTransient
    private Long id;

    @Column(name = "Language")
    @NotNull(message = "Language must be specified.")
    @Enumerated(EnumType.STRING)
    private Language language;

    @Column(name = "UseStatus")
    @NotNull(message = "Concept use status must be specified.")
    @Enumerated(EnumType.STRING)
    private UseStatus useStatus;

    @Column(name = "PartOfSpeech")
    @NotNull(message = "Part of speech must be specified.")
    @Enumerated(EnumType.STRING)
    private PartOfSpeech partOfSpeech;

    @Column(name = "Productivity")
    @Enumerated(EnumType.STRING)
    private Productivity productivity;

    @Column(name = "Negation")
    private String negation;

    @Column(name = "Operator")
    private Operator operator;
    
    @Column(name = "Text")
    @NotNull(message = "Text must be specified.")
    private String text;

    @Column(name = "Comment")
    private String comment;

    @XmlTransient
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "languageRepresentation")
    private List<Concept_LanguageRepresentation> concepts;

    @XmlTransient
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "LanguageRepresentation_RelationSet",
            joinColumns = {
                @JoinColumn(name = "LanguageRepresentationId")},
            inverseJoinColumns = {
                @JoinColumn(name = "RelationSetId")}
    )
    private List<RelationSet> RelationSets;

    public LanguageRepresentation() {
        language_representations = new ArrayList<>();
    }

    /**
     * @return the Language of the Language representation.
     */
    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     * @return the part of speech of the Language representation.
     */
    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(PartOfSpeech pos) {
        this.partOfSpeech = pos;
    }

    public void setPartOfSpeech(String pos) {
        // TODO: Check if it returns the correct value.
        this.partOfSpeech = PartOfSpeech.valueOf(pos.trim().toUpperCase());
    }

    /**
     * @return the part of speech of the Language representation.
     */
    public Productivity getProductivity() {
        return productivity;
    }

    public void setProductivity(Productivity productivity) {
        this.productivity = productivity;
    }

    public void setProductivity(String productivity) {
        this.productivity = Productivity.valueOf(productivity.trim().
                toUpperCase());
    }
    
    /**
     * 
     * @return The negation of this language representation
     */
    public String getNegation() {
        return negation;
    }

    public void setNegation(String negation) {
        this.negation = negation;
    }

    /**
     * @return the operator of this language representation
     */
    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public List<LanguageRepresentation> getLanguageRepresentations() {
        List<LanguageRepresentation> language_representations_list =
                new ArrayList<>();
        for (LanguageRepresentation language_representation
                : LanguageRepresentation.language_representations) {
            language_representations_list.add(language_representation);
        }
        return language_representations_list;
    }

    public List<Concept> getConcepts() {
        List<Concept> concepts = new ArrayList<>();
        for (LanguageRepresentation language_representation
                : LanguageRepresentation.language_representations) {
            for (int j = 0; j < language_representation.getConcepts().size();
                    j++) {
                if (!concepts.contains(language_representation.getConcepts().
                        get(j))) {
                    concepts.add(language_representation.getConcepts().get(j));
                }
            }
        }
        return concepts;
    }

    public void setLanguageRepresentations(
            List<LanguageRepresentation> languageRepresentations) {
        LanguageRepresentation.language_representations =
                languageRepresentations;
    }

    /**
     * @return the use status of the concept.
     */
    public UseStatus getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(UseStatus useStatus) {
        this.useStatus = useStatus;
    }

    public void setUseStatus(String useStatus) {
        String tmp = useStatus;
        tmp = tmp.replace(".", "_");
        tmp = tmp.replace(":", "_");
        // TODO: Check below if it returns the correct value.
        this.useStatus = UseStatus.
                valueOf(tmp.trim().toUpperCase());
    }

    /**
     * @return Text.
     */
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return Comment.
     */
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        if (id != null) {
            hash += id.hashCode();
        } else {
            hash = 0;
        }
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - method won't work in case the id fields are not set
        if (!(object instanceof LanguageRepresentation)) {
            return false;
        }
        LanguageRepresentation other = (LanguageRepresentation)object;
        if (this.language != null && this.text != null &&
                this.partOfSpeech != null &&
                this.language.name().equals(other.language.name()) &&
                this.text.equalsIgnoreCase(other.text) &&
                this.partOfSpeech == other.partOfSpeech &&
                this.useStatus == other.useStatus &&
                this.productivity == other.productivity) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return text + "\\" + this.partOfSpeech + " (" + language + ")";
    }

//    public void afterUnmarshal(Unmarshaller u, Object parent) {
//        if (!Globals.ToMergeAfterUnMarshalling) {
//            try {
//                String tmp = new String(this.getText().getBytes(), "UTF-8");
//                this.setText(tmp);
//            } catch (UnsupportedEncodingException ex) {
//                Logger.getLogger(LanguageRepresentation.class.getName()).log(
//                        Level.SEVERE,
//                        null, ex);
//            }
//        }
//    }
}
