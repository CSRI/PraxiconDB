/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author dmavroeidis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "concept", namespace = "http://www.csri.gr/concept")
@XmlRootElement(name = "concept", namespace = "http://www.csri.gr/concept")
@Entity
//@EntityListeners(ConceptListener.class)
@NamedQueries({
    @NamedQuery(name = "findAllConcepts", query = "FROM Concept c"),
    @NamedQuery(name = "findConceptsByConceptId", query =
            "FROM Concept c WHERE c.id = :conceptId"),
    @NamedQuery(name = "findAllBasicLevelConcepts", query =
            "FROM Concept c WHERE c.specificityLevel = 'BASIC_LEVEL'"),
    @NamedQuery(name = "findAllNonBasicLevelConcepts", query =
            "FROM Concept c WHERE c.specificityLevel != 'BASIC_LEVEL'"),
    @NamedQuery(name = "findConceptsByExternalSourceId", query =
            "FROM Concept c " +
            "WHERE c.externalSourceId LIKE :conceptExternalSourceId"),
    @NamedQuery(name = "findConceptByExternalSourceIdExact", query =
            "FROM Concept c " +
            "WHERE c.externalSourceId = :conceptExternalSourceId"),
    @NamedQuery(name = "findConceptsByLanguageRepresentation", query =
            "SELECT c FROM Concept c " +
            "JOIN c.languageRepresentations clr " +
            "JOIN clr.languageRepresentation lr " +
            "WHERE lr.text LIKE :languageRepresentationName"),
    @NamedQuery(name = "findConceptsByLanguageRepresentationExact", query =
            "SELECT c FROM Concept c " +
            "JOIN c.languageRepresentations clr " +
            "JOIN clr.languageRepresentation lr " +
            "WHERE lr.text = :languageRepresentationName"),
    @NamedQuery(name = "findConceptsByStatusExact", query =
            "SELECT c FROM Concept c " +
            "WHERE c.status = :status"),
    @NamedQuery(name = "findConcept", query =
            "SELECT c FROM Concept c " +
            "WHERE c.externalSourceId = :externalSourceId " +
            "AND c.conceptType = :type " +
            "AND c.specificityLevel = :specificityLevel " +
            "AND c.status = :status " +
            "AND c.pragmaticStatus = :pragmaticStatus " +
            "AND c.uniqueInstance = :uniqueInstance " +
            "AND c.ontologicalDomain = :ontologicalDomain " +
            "AND c.source = :source "
    //            "AND c.languageRepresentations = :languageRepresentations "
    ),
    @NamedQuery(name = "getConceptEntityQuery", query =
            "SELECT c FROM Concept c " +
            "WHERE c.status = :status " +
            "AND c.externalSourceId = :externalSourceId " +
            "AND c.conceptType = :type " +
            "AND c.pragmaticStatus = :pragmaticStatus"),})
@Table(name = "Concepts",
        indexes = {
            @Index(columnList = "ExternalSourceId"),})
//@ConceptConstraint(groups=ConceptGroup.class)
public class Concept implements Serializable {

    /**
     * Enumeration for the concept type of a concept.
     */
    public static enum ConceptType {

        ENTITY, FEATURE, MOVEMENT, UNKNOWN;

        @Override
        public String toString() {
            return this.name();
        }
    }

    /**
     * Enumeration for the specificity level of a concept.
     */
    public static enum SpecificityLevel {

        BASIC_LEVEL, BASIC_LEVEL_EXTENDED, SUPERORDINATE, SUBORDINATE, UNKNOWN;

        @Override
        public String toString() {
            return this.name();
        }
    }

    /**
     * Enumeration for the concept type of a concept.
     *
     */
    public static enum Status {

        CONSTANT, VARIABLE;

        @Override
        public String toString() {
            return this.name();
        }
    }

    /**
     * Enumeration for the pragmatic status of a concept.
     *
     */
    public static enum PragmaticStatus {

        CONCRETE, ABSTRACT, UNKNOWN;

        @Override
        public String toString() {
            return this.name();
        }
    }

    /**
     * Enumeration for the unique instance of a concept.
     */
    public static enum UniqueInstance {

        YES, NO, UNKNOWN;

        @Override
        public String toString() {
            return this.name();
        }
    }

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CUST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CUST_SEQ")
    @Column(name = "ConceptId")
    @XmlTransient
//    @XmlAttribute
    private Long id;

    @Column(name = "ExternalSourceId", nullable = false)
    //@Size(min = 5, max = 14)
    @NotNull(message = "Concept externalSourceId must be specified.")
    private String externalSourceId;

    @Column(name = "Type", nullable = false)
    @NotNull(message = "Concept type must be specified.")
    @Enumerated(EnumType.STRING)
    private ConceptType conceptType;

    @Column(name = "SpecificityLevel", nullable = false)
    @NotNull(message = "Specificity level must be specified.")
    @Enumerated(EnumType.STRING)
    private SpecificityLevel specificityLevel;

    @Column(name = "Status", nullable = false)
    @NotNull(message = "Concept status must be specified.")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "PragmaticStatus", nullable = false)
    @NotNull(message = "Concept pragmatic status must be specified")
    @Enumerated(EnumType.STRING)
    private PragmaticStatus pragmaticStatus;

    @Column(name = "UniqueInstance", nullable = false)
    @NotNull(message = "Concept unique instance must be specified.")
    @Enumerated(EnumType.STRING)
    private UniqueInstance uniqueInstance;

    @Column(name = "OntologicalDomain")
    private String ontologicalDomain;

    @Column(name = "Source")
    private String source;

    @Column(name = "Comment")
    private String comment;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "concept")
    private List<Concept_LanguageRepresentation> languageRepresentations;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "concept")
    private List<VisualRepresentation> visualRepresentations;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "concept")
    private List<MotoricRepresentation> motoricRepresentations;

    /**
     * Public Constructor.
     */
    public Concept() {
        externalSourceId = "";
        conceptType = Concept.ConceptType.UNKNOWN;
        specificityLevel = Concept.SpecificityLevel.UNKNOWN;
        status = Concept.Status.CONSTANT;
        pragmaticStatus = Concept.PragmaticStatus.UNKNOWN;
        uniqueInstance = Concept.UniqueInstance.UNKNOWN;
        ontologicalDomain = "";
        source = "";
        comment = "";
        languageRepresentations = new ArrayList<>();
        visualRepresentations = new ArrayList<>();
        motoricRepresentations = new ArrayList<>();
    }

    /**
     * Public Constructor with argument.
     *
     * @param newConcept
     */
    public Concept(Concept newConcept) {
        this.comment = newConcept.getComment();
        this.externalSourceId = newConcept.externalSourceId;
        this.conceptType = newConcept.getConceptType();
        this.ontologicalDomain = newConcept.getOntologicalDomain();
        this.pragmaticStatus = newConcept.getPragmaticStatus();
        this.source = newConcept.getSource();
        this.specificityLevel = newConcept.getSpecificityLevel();
        this.status = newConcept.getStatus();
        this.uniqueInstance = newConcept.getUniqueInstance();
        languageRepresentations = new ArrayList<>();
        visualRepresentations = new ArrayList<>();
        motoricRepresentations = new ArrayList<>();
        for (LanguageRepresentation lr : newConcept.
                getLanguageRepresentations()) {
            if (!this.getLanguageRepresentations().contains(lr)) {
//                lr.getConcepts().remove(newConcept);

                LanguageRepresentation newLr = new LanguageRepresentation(lr);
                System.out.println("NEW LANGUAGE REPRESENTATION: " + newLr);
                this.addLanguageRepresentation(newLr, false);
                //getLanguageRepresentations().add(newLr);
            }
            System.out.println("Concept's LanguageRepresenations: " + this.
                    getLanguageRepresentations());
        }

        for (VisualRepresentation tmpVisualRepresentation : newConcept.
                getVisualRepresentationsEntries()) {
            if (!this.getVisualRepresentationsEntries().contains(
                    tmpVisualRepresentation)) {
                this.getVisualRepresentationsEntries().add(
                        tmpVisualRepresentation);
            }
        }

        for (MotoricRepresentation tmpMotoricRepresentation : newConcept.
                getMotoricRepresentations()) {
            if (!this.getMotoricRepresentations().contains(
                    tmpMotoricRepresentation)) {
                this.getMotoricRepresentations().add(tmpMotoricRepresentation);
            }
        }
    }

    /**
     * Gets the id of the concept.
     *
     * @return long
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id of the concept.
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the external source id of the concept. This is usually the id
     * of the resource that was used to populate the Praxicon.
     *
     * @return String - The externalSourceId of the concept.
     */
    public String getExternalSourceId() {
        if (externalSourceId != null) {
            return externalSourceId;
        } else {
            return id + "";
        }
    }

    /**
     * Sets the external source id.
     *
     * @param externalSourceId
     */
    public void setExternalSourceId(String externalSourceId) {
        this.externalSourceId = externalSourceId.trim();
    }

    /**
     * Gets the type of the concept.
     *
     * @return ConceptType - The ConceptType of the concept.
     */
    public ConceptType getConceptType() {
        return conceptType;
    }

    /**
     * Sets the type of the concept. Permitted values:
     * ABSTRACT, ENTITY, FEATURE, MOVEMENT, UNKNOWN;
     *
     * @param conceptType - ConceptType
     */
    public void setConceptType(ConceptType conceptType) {
        this.conceptType = conceptType;
    }

    /**
     * Sets the type of the concept. Overloaded.
     *
     * @param conceptType - String
     */
    public void setConceptType(String conceptType) {
        this.conceptType = ConceptType.valueOf(conceptType.trim().toUpperCase());
    }

    /**
     * Gets the specificity level of the concept.
     *
     * @return the specificity level of the concept.
     *
     */
    public SpecificityLevel getSpecificityLevel() {
        return specificityLevel;
    }

    /**
     * Sets the specificity level of the concept. Permitted values:
     * SUBORDINATE, BASIC_LEVEL, BASIC_LEVEL_EXTENDED, SUPERORDINATE, UNKNOWN
     *
     * @param levelType
     */
    public void setSpecificityLevel(SpecificityLevel levelType) {
        this.specificityLevel = levelType;
    }

    /**
     * Sets the specificity level of the concept. Overloaded.
     *
     * @param levelType - String
     */
    public void setSpecificityLevel(String levelType) {
        if (levelType.equalsIgnoreCase("BASIC_LEVEL")) {
            this.specificityLevel = Concept.SpecificityLevel.BASIC_LEVEL;
        } else if (levelType.equalsIgnoreCase("BASIC_LEVEL_EXTENDED")) {
            this.specificityLevel =
                    Concept.SpecificityLevel.BASIC_LEVEL_EXTENDED;
        } else if (levelType.equalsIgnoreCase("SUPERORDINATE")) {
            this.specificityLevel = Concept.SpecificityLevel.SUPERORDINATE;
        } else if (levelType.equalsIgnoreCase("SUBORDINATE")) {
            this.specificityLevel = Concept.SpecificityLevel.SUBORDINATE;
        } else {
            this.specificityLevel = Concept.SpecificityLevel.UNKNOWN;
        }
    }

    /**
     * Gets the status of the concept.
     *
     * @return the Status of the concept.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the status of the concept. Permitted values:
     * CONSTANT, VARIABLE
     *
     * @param varType - Status
     */
    public void setStatus(Status varType) {
        this.status = varType;
    }

    /**
     * Sets the status of the concept. Overloaded
     *
     * @param varType - String
     */
    public void setStatus(String varType) {
        this.status = status.valueOf(varType.trim().toUpperCase());
    }

    /**
     * Gets the pragmatic status of the concept.
     *
     * @return the Pragmatic Status of the concept.
     */
    public PragmaticStatus getPragmaticStatus() {
        return pragmaticStatus;
    }

    /**
     * Sets the pragmatic status of the concept. Permitted values:
     * CONCRETE, ABSTRACT, UNKNOWN
     *
     * @param pragmaticStatus - PragmaticStatus
     */
    public void setPragmaticStatus(PragmaticStatus pragmaticStatus) {
        this.pragmaticStatus = pragmaticStatus;
    }

    /**
     * Sets the pragmatic status of the concept. Overloaded
     *
     * @param varPragmaticStatus - String
     */
    public void setPragmaticStatus(String varPragmaticStatus) {
        this.pragmaticStatus = pragmaticStatus.valueOf(
                varPragmaticStatus.trim().toUpperCase());
    }

    /**
     * Gets whether the concept is a unique instance.
     *
     * @return The unique instance of the concept
     */
    public UniqueInstance getUniqueInstance() {
        return uniqueInstance;
    }

    /**
     * Sets the unique instance of the concept. Permitted values:
     * YES, NO, UNKNOWN
     *
     * @param uniqueInstance
     */
    public void setUniqueInstance(UniqueInstance uniqueInstance) {
        this.uniqueInstance = uniqueInstance;
    }

    /**
     * Gets the ontological domain of the concept. It defines the domain in
     * terms of ontology that the concept belongs to
     * (for example: natural event, activity or physical process)
     *
     * @return The ontological domain of the concept
     */
    public String getOntologicalDomain() {
        return ontologicalDomain;
    }

    /**
     * Sets the ontological domain of the concept.
     *
     * @param ontologicalDomain
     */
    public void setOntologicalDomain(String ontologicalDomain) {
        this.ontologicalDomain = ontologicalDomain;
    }

    /**
     * Gets the source of the concept. Where the concept came from
     * (e.g. WordNew).
     *
     * @return the source of the concept
     *
     *
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the source of the concept.
     *
     * @param source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Gets the comment.
     *
     * @return a string containing additional information about the concept.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment.
     *
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Gets the language representations of the concept.
     *
     * @return a list containing the concept's language representations
     *
     */
    public final List<LanguageRepresentation> getLanguageRepresentations() {
        List<LanguageRepresentation> lrs;
        lrs = new ArrayList();
        for (Concept_LanguageRepresentation clr : this.
                getConceptLanguageRepresentation()) {
            lrs.add(clr.getLanguageRepresentation());
        }
        return lrs;
    }

    /**
     * Gets the Concept_LanguageRepresentation construct which helps get
     * the language representations.
     *
     * @return the Concept_LanguageRepresentation instance of the concept
     *
     */
    public final List<Concept_LanguageRepresentation>
            getConceptLanguageRepresentation() {
        return languageRepresentations;
    }

    /**
     * Gets a list of Concept_LanguageRepresentation instances.
     *
     * @return a list of Concept_LanguageRepresentation instances for the
     *         concept
     */
    public List<Concept_LanguageRepresentation>
            getLanguageRepresentationsEntries() {
        List<Concept_LanguageRepresentation> language_representation_entries =
                new ArrayList<>();
        for (Concept_LanguageRepresentation languageRepresentation
                : this.languageRepresentations) {
            language_representation_entries.add(languageRepresentation);
        }
        return language_representation_entries;
    }

    /**
     * Adds a Concept_LanguageRepresentation instance to the concept.
     *
     * @param conceptLanguageRepresentation A structure that contains the
     *                                      Language representation with
     *                                      information about its
     *                                      representativeness.
     */
    public void addConceptLanguageRepresentation(
            Concept_LanguageRepresentation conceptLanguageRepresentation) {
        this.languageRepresentations.add(conceptLanguageRepresentation);
    }

    /**
     * Adds a LanguageRepresentation instance to the concept.
     *
     * @param languageRepresentation a Language representation.
     * @param isRepresentative       whether the Language representation is
     *                               representative of the concept or not.
     *                               There can be more than one representative
     *                               Language representations.
     */
    public final void addLanguageRepresentation(
            LanguageRepresentation languageRepresentation,
            boolean isRepresentative) {
        Concept_LanguageRepresentation clr =
                new Concept_LanguageRepresentation();
        clr.setConcept(this);
        clr.setLanguageRepresentation(languageRepresentation);
        this.languageRepresentations.add(clr);
    }

    public void setLanguageRepresentations(
            List<Concept_LanguageRepresentation> languageRepresentations) {
        this.languageRepresentations = languageRepresentations;
    }

    /**
     * Gets text of the first Language representation of Language "en" for this
     * concept.
     *
     * @return the externalSourceId of the first Language Representation
     *         of the concept.
     */
    public String getLanguageRepresentationName() {
        List<LanguageRepresentation> lrs = this.getLanguageRepresentations();
        for (LanguageRepresentation lr : lrs) {
            if (lr.getLanguage().name().equalsIgnoreCase("en")) {
                return lr.getText();
            }
        }
        if (lrs.size() > 0) {
            return lrs.get(0).getText();
        }
        return "noname";
    }

    /**
     * Gets a list of Language representation texts for this concept.
     *
     * @return list of strings with all the texts of the Language
     *         Representations of the Concept.
     */
    public List<String> getLanguageRepresentationsNames() {
        List<LanguageRepresentation> lrs = this.getLanguageRepresentations();
        List<String> lrNames = new ArrayList<>();
        if (lrs.isEmpty()) {
            lrNames.add("There are no Language Representations for Concept " +
                    this.getExternalSourceId());
        } else {
            for (LanguageRepresentation lr : lrs) {
                lrNames.add(lr.getText());
            }
        }
        return lrNames;
    }

    /**
     * Updates LanguageRepresentation of a concept using this concept's
     * LanguageRepresentation
     *
     * @param oldConcept the concept to be updated
     */
    public void updateLanguageRepresentations(Concept oldConcept) {
        for (int i = 0; i <
                this.getLanguageRepresentationsEntries().size(); i++) {
            if (!oldConcept.getLanguageRepresentationsEntries().contains(
                    this.getLanguageRepresentationsEntries().get(i))) {
                this.getLanguageRepresentationsEntries().get(i).
                        getLanguageRepresentation().
                        getConcepts().
                        remove(this);
                this.getLanguageRepresentationsEntries().get(i).
                        getLanguageRepresentation().
                        getConcepts().
                        add(this);
                oldConcept.getLanguageRepresentationsEntries().add(
                        this.getLanguageRepresentationsEntries().get(i));
            }
        }
    }

    /**
     * Gets the visual representations of the concept
     *
     * @return visual representations construct
     */
    public List<VisualRepresentation> getVisualRepresentations() {
        return visualRepresentations;
    }

    /**
     * Add a visual representation for this concept.
     *
     * @param visualRepresentation
     */
    public void addVisualRepresentation(
            VisualRepresentation visualRepresentation) {
        this.visualRepresentations.add(visualRepresentation);
    }

    /**
     * Sets the visual representation for this concept.
     *
     * @param visualRepresentations
     */
    public void setVisualRepresentation(
            List<VisualRepresentation> visualRepresentations) {
        this.visualRepresentations = visualRepresentations;
    }

    /**
     * Gets a list of visual representations for this concept
     *
     * @return a list of visual representations
     */
    public final List<VisualRepresentation> getVisualRepresentationsEntries() {
        List<VisualRepresentation> visualRepresentationEntries =
                new ArrayList<>();
        for (VisualRepresentation VisualRepresentation
                : this.visualRepresentations) {
            visualRepresentationEntries.add(VisualRepresentation);
        }
        return visualRepresentationEntries;
    }

    /**
     * Updates VisualRepresentations of a concept using this concept
     * VisualRepresentations.
     *
     * @param oldConcept the concept to be updated
     */
    public void updateVisualRepresentations(Concept oldConcept) {
        for (int i = 0; i < this.getVisualRepresentations().size(); i++) {
            if (!oldConcept.getVisualRepresentations().contains(
                    this.getVisualRepresentations().get(i))) {
                this.getVisualRepresentations().get(i);
                oldConcept.getVisualRepresentations().add(
                        this.getVisualRepresentations().get(i));
            }
        }
    }

    /**
     * Gets the motoric representation for this concept.
     *
     * @return motoric representation construct
     */
    public final List<MotoricRepresentation> getMotoricRepresentations() {
        return motoricRepresentations;
    }

    /**
     * Gets a list of motoric representations for this concept.
     *
     * @return a list of motoric representations
     */
    public List<MotoricRepresentation> getMotoricRepresentationsEntries() {
        List<MotoricRepresentation> motoric_representation_entries =
                new ArrayList<>();
        for (MotoricRepresentation MotoricRepresentation
                : this.motoricRepresentations) {
            motoric_representation_entries.add(MotoricRepresentation);
        }
        return motoric_representation_entries;
    }

    /**
     * Sets the motoric representations for this concept.
     *
     * @param motoricRepresentations
     */
    public void setMotoricRepresentations(
            List<MotoricRepresentation> motoricRepresentations) {
        this.motoricRepresentations = motoricRepresentations;
    }

    public void addMotoricRepresentation(
            MotoricRepresentation motoricRepresentation) {
        this.motoricRepresentations.add(motoricRepresentation);
    }

    /**
     * Updates MotoricRepresentations of a concept using this concept
     * MotoricRepresentations
     *
     * @param oldConcept the concept to be updated
     */
    public void updateMotoricRepresentations(Concept oldConcept) {
        for (int i = 0; i < this.getMotoricRepresentations().size(); i++) {
            if (!oldConcept.getMotoricRepresentations().contains(
                    this.getMotoricRepresentations().get(i))) {
                this.getMotoricRepresentations().get(i);
                oldConcept.getMotoricRepresentations().add(
                        this.getMotoricRepresentations().get(i));
            }
        }
    }

    /**
     * Gets a string of concatenated full info for this concept. concept ConceptType,
     * Status, pragmatic Status, specificity level, description
     *
     * @return a string of information for this concept
     */
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getConceptType());
        sb.append("#");
        sb.append(this.getStatus());
        sb.append("#");
        sb.append(this.getSpecificityLevel());

        return sb.toString();
    }

    /**
     * Gets a string of concatenated short info for the concept. concept ConceptType
     * and specificity level
     *
     * @return a string of short information for this concept
     */
    public String getInfoShort() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getConceptType());
        sb.append("#");
        sb.append(this.getSpecificityLevel());
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.externalSourceId);
        hash = 13 * hash + Objects.hashCode(this.conceptType);
        hash = 13 * hash + Objects.hashCode(this.specificityLevel);
        hash = 13 * hash + Objects.hashCode(this.status);
        hash = 13 * hash + Objects.hashCode(this.pragmaticStatus);
        hash = 13 * hash + Objects.hashCode(this.uniqueInstance);
        hash = 13 * hash + Objects.hashCode(this.ontologicalDomain);
        hash = 13 * hash + Objects.hashCode(this.source);
        hash = 13 * hash + Objects.hashCode(this.getLanguageRepresentations());
        hash = 13 * hash + Objects.hashCode(this.
                getVisualRepresentationsEntries());
        hash = 13 * hash + Objects.hashCode(this.
                getMotoricRepresentationsEntries());
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
        final Concept other = (Concept)obj;
        if (!Objects.equals(this.externalSourceId, other.externalSourceId)) {
            return false;
        }
        if (this.conceptType != other.conceptType) {
            return false;
        }
        if (this.specificityLevel != other.specificityLevel) {
            return false;
        }
        if (this.status != other.status) {
            return false;
        }
        if (this.pragmaticStatus != other.pragmaticStatus) {
            return false;
        }
        if (this.uniqueInstance != other.uniqueInstance) {
            return false;
        }
        if (!Objects.equals(this.ontologicalDomain, other.ontologicalDomain)) {
            return false;
        }
        if (!Objects.equals(this.source, other.source)) {
            return false;
        }
        if (!Objects.equals(this.getLanguageRepresentations(), other.
                getLanguageRepresentations())) {
            return false;
        }
        if (!Objects.equals(this.getVisualRepresentationsEntries(), other.
                getVisualRepresentationsEntries())) {
            return false;
        }
        if (!Objects.equals(this.getMotoricRepresentationsEntries(), other.
                getMotoricRepresentationsEntries())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (externalSourceId != null &&
                !externalSourceId.equalsIgnoreCase("")) {
            return externalSourceId;
            // + " (Entity)";
        } else {
            List<Concept_LanguageRepresentation> tmpList =
                    this.getLanguageRepresentationsEntries();
            if (tmpList.size() > 0) {
                StringBuilder tmp = new StringBuilder(
                        tmpList.get(0).getLanguageRepresentation().getText());
                for (int i = 1; i < tmpList.size(); i++) {
                    tmp.append("\\").append(
                            tmpList.get(i).getLanguageRepresentation().
                            getText());
                }
                return tmp.toString();
            } else {
                return id + "";
            }
        }
    }
}

////@XmlRegistry
//class ObjectFactory {
//
//    Concept createConcept() {
//        return new Concept();
//    }
//}
