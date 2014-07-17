/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import gr.csri.poeticon.praxicon.Globals;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
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
    @NamedQuery(name = "findLanguageRepresentationsByTextLanguagePos", query =
            "FROM LanguageRepresentation lr " +
            "WHERE UPPER(lr.text) = :text " +
            "AND lr.language = :language " +
            "AND lr.partOfSpeech = :pos"),
    @NamedQuery(name =
            "findLanguageRepresentationsByTextLanguagePosCaseInsensitive",
            query =
            "FROM LanguageRepresentation lr " +
            "WHERE lr.text = :text " +
            "AND lr.language = :language " +
            "AND lr.partOfSpeech = :pos"),
    @NamedQuery(name = "getLanguageRepresentationEntityQuery", query =
            "FROM LanguageRepresentation lr " +
            "WHERE UPPER(lr.text) = :text " +
            "AND UPPER(lr.language) = :language " +
            "AND UPPER(lr.partOfSpeech) = :pos " +
            "AND UPPER(lr.pragmaticStatus) = :pragmatic_status"),
    @NamedQuery(name = "findLanguageRepresentationsByText", query =
            "FROM LanguageRepresentation lr " +
            "WHERE UPPER(lr.text) = :text"), })
@Table(name = "LanguageRepresentations", indexes = {
    @Index(columnList = "Text"),
    @Index(columnList = "LanguageRepresentationId")})
public class LanguageRepresentation implements Serializable {

    private static final long serialVersionUID = 1L;
    private static List<LanguageRepresentation> language_representations;

    public static enum language {

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
     * Enumeration of the types of pragmatic status.
     */
    public static enum pragmatic_status {

        FIGURATIVE, LITERAL, UNKNOWN;

        @Override
        public String toString() {
            return this.name();
        }
    }

    public static enum part_of_speech {

        ADJECTIVE, ADVERB, NOUN, PARTICIPLE, PROPER_NOUN, VERB;

        @Override
        public String toString() {
            return this.name();
        }
    }

    public static enum is_compositional {

        YES, NO, UNKNOWN;

        @Override
        public String toString() {
            return this.name();
        }
    }

    @Id
    @SequenceGenerator(name = "CUST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CUST_SEQ")
    @Column(name = "LanguageRepresentationId")
    private Long id;

    /*  The name of the column was truncated because "Language" is a 
     reserved SQL-99 keyword. */
    @Column(name = "Language")
    @NotNull(message = "Language must be specified.")
    @Enumerated(EnumType.STRING)
    private language language;

    @Column(name = "PragmaticStatus")
    //@XmlElement(required = false)
    @NotNull(message = "Concept pragmatic status must be specified.")
    @Enumerated(EnumType.STRING)
    private pragmatic_status pragmaticStatus;

    @Column(name = "PartOfSpeech")
    @NotNull(message = "Part of speech must be specified.")
    @Enumerated(EnumType.STRING)
    private part_of_speech partOfSpeech;

    @Column(name = "IsCompositional")
    private is_compositional isCompositional;

    @Column(name = "Text")
    @NotNull(message = "Text must be specified.")
    private String text;

    @Column(name = "Comment")
    private String comment;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "languageRepresentation")
    private List<Concept_LanguageRepresentation> concepts;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "LanguageRepresentation_RelationSubject",
            joinColumns = {
                @JoinColumn(name = "LanguageRepresentationId")},
            inverseJoinColumns = {
                @JoinColumn(name = "RelationId")}
    )
    private List<Relation> relationsWithLanguageRepresentationAsSubject;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "LanguageRepresentation_RelationObject",
            joinColumns = {
                @JoinColumn(name = "LanguageRepresentationId")},
            inverseJoinColumns = {
                @JoinColumn(name = "RelationId")}
    )
    private List<Relation> relationsWithLanguageRepresentationAsObject;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "LanguageRepresentation_RelationSet",
            joinColumns = {
                @JoinColumn(name = "LanguageRepresentationId")},
            inverseJoinColumns = {
                @JoinColumn(name = "RelationSetId")}
    )
    private List<RelationSet> RelationSets;

    // Foreign key
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "constituentLanguageRepresentation")
    private List<Constituents> languageRepresentationConstituents;

    // Foreign key
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "languageRepresentation")
    private List<Constituents> constituents;

    // Foreign key
    @ManyToOne(cascade = CascadeType.ALL)
    private Compositionality compositionality;

    public LanguageRepresentation() {
        language_representations = new ArrayList<>();
    }

    /**
     * @return the constituents of the language representation.
     * @xmlcomments.args xmltag="&lt;constituents&gt;" xmldescription="This tag
     * defines the constituents of a composite word or multiword"
     */
    @XmlElement(name = "constituents")
    public List<Constituents> getConstituents() {
        List<Constituents> constituents = new ArrayList<>();
        if (this.isCompositional == is_compositional.YES) {
            for (gr.csri.poeticon.praxicon.db.entities.Constituents constituent
                    : constituents) {
                constituents.add(constituent);
            }
        } else {
            constituents = null;
        }
        return constituents;
    }

    public void setConstituents(List<Constituents> constituents) {
        this.constituents = constituents;
    }

    /**
     * @return whether the language representation consists of more than one
     *         constituents.
     * @xmlcomments.args xmltag="&lt;is_compositional&gt;" xmldescription="This
     * tag defines if the LanguageRepresentation is compositional or not"
     */
    @XmlElement(name = "is_compositional")
    public is_compositional isCompositional() {
        return isCompositional;
    }

    public void setCompositional(is_compositional isCompositional) {
        this.isCompositional = isCompositional;
    }

    /**
     * @return the language of the language representation.
     * @xmlcomments.args xmltag="&lt;language&gt;" xmldescription="This tag
     * defines the language of the entry"
     */
    @XmlElement(name = "language")
    public language getLanguage() {
        return language;
    }

    /**
     * @return the part of speech of the language representation.
     * @xmlcomments.args xmltag="&lt;part_of_speech&gt;" xmldescription="This
     * tag defines the Part Of Speech of the entry"
     */
    @XmlElement(name = "part_of_speech")
    public part_of_speech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(part_of_speech pos) {
        this.partOfSpeech = pos;
    }

    public void setPartOfSpeech(String pos) {
        // TODO: Check if it returns the correct value.
        this.partOfSpeech = part_of_speech.valueOf(pos.trim().toUpperCase());
    }

    @XmlTransient
    public List<LanguageRepresentation> getLanguageRepresentations() {
        List<LanguageRepresentation> language_representations_list =
                new ArrayList<>();
        for (LanguageRepresentation language_representation
                : LanguageRepresentation.language_representations) {
            language_representations_list.add(language_representation);
        }
        return language_representations_list;
    }

    @XmlTransient
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

    public void setLanguage(language language) {
        this.language = language;
    }

    /**
     * @return the pragmatic status of the concept.
     * @xmlcomments.args xmltag="&lt;pragmatic_status&gt;" xmldescription="This
     * tag defines if the entity is literal or figurative"
     */
    public pragmatic_status getPragmaticStatus() {
        return pragmaticStatus;
    }

    public void setPragmaticStatus(pragmatic_status pragmaticStatus) {
        this.pragmaticStatus = pragmaticStatus;
    }

    public void setPragmaticStatus(String pragmaticStatus) {
        String tmp = pragmaticStatus;
        tmp = tmp.replace(".", "_");
        tmp = tmp.replace(":", "_");
        // TODO: Check below if it returns the correct value.
        this.pragmaticStatus = pragmatic_status.
                valueOf(tmp.trim().toUpperCase());
    }

    /**
     * @return Text.
     * @xmlcomments.args xmltag="&lt;text&gt;" xmldescription="This tag defines
     * the text of the entry"
     */
    @XmlElement(name = "text")
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
                this.pragmaticStatus == other.pragmaticStatus) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return text + "\\" + this.partOfSpeech + " (" + language + ")";
    }

    public void afterUnmarshal(Unmarshaller u, Object parent) {
        if (!Globals.ToMergeAfterUnMarshalling) {
            try {
                String tmp = new String(this.getText().getBytes(), "UTF-8");
                this.setText(tmp);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(LanguageRepresentation.class.getName()).log(
                        Level.SEVERE,
                        null, ex);
            }
        }
    }
}
