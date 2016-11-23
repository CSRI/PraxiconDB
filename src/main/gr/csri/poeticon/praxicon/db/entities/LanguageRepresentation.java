/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static java.util.Objects.isNull;
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
    @NamedQuery(name =
            "findLanguageRepresentation", query =
            "FROM LanguageRepresentation lr " +
            "WHERE lr.text = :text " +
            "AND lr.language = :language " +
            "AND lr.partOfSpeech = :pos " +
            "AND lr.useStatus = :useStatus " +
            "AND lr.productivity = :productivity " +
            "AND lr.negation = :negation " +
            "AND lr.operator = :operator"),
    @NamedQuery(name = "getLanguageRepresentationEntityQuery", query =
            "FROM LanguageRepresentation lr " +
            "WHERE UPPER(lr.text) = :text " +
            "AND UPPER(lr.language) = :language " +
            "AND UPPER(lr.partOfSpeech) = :pos " +
            "AND UPPER(lr.useStatus) = :useStatus " +
            "AND UPPER(lr.productivity) = :productivity " +
            "AND UPPER(lr.negation) = :negation " +
            "AND UPPER(lr.operator) = :operator"),
    @NamedQuery(name = "findLanguageRepresentationsByText", query =
            "FROM LanguageRepresentation lr " +
            "WHERE UPPER(lr.text) = :text"),
    // TODO: This is not finished yet
    @NamedQuery(name = "findLanguageRepresentationsByConcept", query =
            "FROM LanguageRepresentation lr " +
            "WHERE UPPER(lr.text) = :text"),
    @NamedQuery(name = "getAllLanguageRepresentationTextByText", query =
            "SELECT DISTINCT lr.text FROM LanguageRepresentation lr"),})
@Table(name = "LanguageRepresentations",
        indexes = {
            @Index(columnList = "Text"),}//,
//        uniqueConstraints = {
//            @UniqueConstraint(columnNames = {"Language", "UseStatus",
//                "PartOfSpeech", "Productivity", "Negation", "Operator", "Text"
//            }),}
)
public class LanguageRepresentation implements Serializable {

    private static final long serialVersionUID = 1L;
    private static List<LanguageRepresentation> languageRepresentations;

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

        ADJECTIVE, ADVERB, NOUN, PARTICIPLE, PROPER_NOUN, VERB, VERB_PHRASE,
        UNKNOWN;

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

        AGAIN, BAD, CLOSE, COLLECTIVE, DIFFERENT, DIFFICULT, EASY, FAR, FULL,
        GOOD, LESS, MORE, NONE, ONE, PLURAL, SAME;

        @Override
        public String toString() {
            return this.name();
        }
    }

    @Id
    @XmlTransient
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LanguageRepresentationId")
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
    @Enumerated(EnumType.STRING)
    private Operator operator;

    @Column(name = "Text")
    @NotNull(message = "Text must be specified.")
    private String text;

    @Column(name = "Comment")
    private String comment;

    @XmlTransient
    @OneToMany(mappedBy = "languageRepresentation")
    private List<Concept_LanguageRepresentation> concepts;

    @XmlTransient
    @ManyToMany()
    @JoinTable(
            name = "LanguageRepresentation_RelationSet",
            joinColumns = {
                @JoinColumn(name = "LanguageRepresentationId")},
            inverseJoinColumns = {
                @JoinColumn(name = "RelationSetId")}
    )
    private List<RelationSet> relationSets;

    public LanguageRepresentation() {
        language = Language.EN;
        useStatus = UseStatus.UNKNOWN;
        partOfSpeech = PartOfSpeech.UNKNOWN;
        productivity = Productivity.UNKNOWN;
        negation = "";
        operator = Operator.NONE;
        text = "";
        comment = "";
    }

    public LanguageRepresentation(
            LanguageRepresentation newLanguageRepresentation) {
        language = newLanguageRepresentation.language;
        useStatus = newLanguageRepresentation.useStatus;
        partOfSpeech = newLanguageRepresentation.partOfSpeech;
        productivity = newLanguageRepresentation.productivity;
        negation = newLanguageRepresentation.negation;
        operator = newLanguageRepresentation.operator;
        text = newLanguageRepresentation.text;
        comment = newLanguageRepresentation.comment;
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

    public boolean getIsRepresentative(Concept concept) {
        if (!isNull(concepts)) {
            for (Concept_LanguageRepresentation tmpConceptLanguageRepresentation
                    : concepts) {
                if (tmpConceptLanguageRepresentation.getConcept().
                        equals(concept)) {
                    if (tmpConceptLanguageRepresentation.getIsRepresentative()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<LanguageRepresentation> getLanguageRepresentations() {
        List<LanguageRepresentation> languageRepresentationsList =
                new ArrayList<>();
        for (LanguageRepresentation languageRepresentation
                : LanguageRepresentation.languageRepresentations) {
            languageRepresentationsList.add(languageRepresentation);
        }
        return languageRepresentationsList;
    }

    public void setLanguageRepresentations(
            List<LanguageRepresentation> languageRepresentations) {
        LanguageRepresentation.languageRepresentations =
                languageRepresentations;
    }

    public List<Concept> getConcepts() {
        List<Concept> languageRepresentationConcepts = new ArrayList<>();
        for (LanguageRepresentation languageRepresentation
                : LanguageRepresentation.languageRepresentations) {
            for (Concept concept : languageRepresentation.getConcepts()) {
                if (!languageRepresentationConcepts.contains(concept)) {
                    languageRepresentationConcepts.add(concept);
                }
            }
        }
        return languageRepresentationConcepts;
    }

    public List<RelationSet> getRelationSets() {
        List<RelationSet> languageRepresentationRelationSets =
                new ArrayList<>();
        if (!isNull(relationSets)) {
            languageRepresentationRelationSets.addAll(relationSets);
        }
        return languageRepresentationRelationSets;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.language);
        hash = 67 * hash + Objects.hashCode(this.useStatus);
        hash = 67 * hash + Objects.hashCode(this.partOfSpeech);
        hash = 67 * hash + Objects.hashCode(this.productivity);
        hash = 67 * hash + Objects.hashCode(this.negation);
        hash = 67 * hash + Objects.hashCode(this.operator);
        hash = 67 * hash + Objects.hashCode(this.text);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LanguageRepresentation other = (LanguageRepresentation)obj;
        if (this.language != other.getLanguage()) {
            return false;
        }
        if (this.useStatus != other.getUseStatus()) {
            return false;
        }
        if (this.partOfSpeech != other.getPartOfSpeech()) {
            return false;
        }
        if (this.productivity != other.getProductivity()) {
            return false;
        }
        if (!Objects.equals(this.negation, other.getNegation())) {
            return false;
        }
        if (this.operator != other.getOperator()) {
            return false;
        }
        if (!Objects.equals(this.text, other.getText())) {
            return false;
        }
        return true;
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
//                        Level.SEVERE, null, ex);
//            }
//        }
//    }
}
