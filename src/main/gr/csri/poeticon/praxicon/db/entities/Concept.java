/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import static java.util.Objects.isNull;
import java.util.Set;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author dmavroeidis
 */
@Entity
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "concept", namespace = "http://www.csri.gr/concept")
@NamedQueries({
    @NamedQuery(name = "findAllConcepts", query = "FROM Concept c"),
    @NamedQuery(name = "findConceptsByConceptId", query =
            "FROM Concept c WHERE c.id = :conceptId"),
    @NamedQuery(name = "findAllBasicLevelConcepts", query =
            "FROM Concept c WHERE c.specificityLevel = 'BASIC_LEVEL'"),
    @NamedQuery(name = "findAllNonBasicLevelConcepts", query =
            "FROM Concept c WHERE c.specificityLevel != 'BASIC_LEVEL'"),
    @NamedQuery(name = "findConceptsByName", query =
            "FROM Concept c " +
            "WHERE c.name LIKE :conceptName"),
    @NamedQuery(name = "findConceptByNameExact", query =
            "FROM Concept c " +
            "WHERE c.name = :conceptName"),
    @NamedQuery(name = "findConceptsByNameAndStatus", query =
            "FROM Concept c " +
            "WHERE c.name LIKE :conceptName " +
            "AND c.status = :conceptStatus"),
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
            "WHERE lr.text LIKE :languageRepresentationName " +
            "ORDER BY " +
            "  CASE c.specificityLevel " +
            "       WHEN 'BASIC_LEVEL' THEN 1 " +
            "       WHEN 'SUPERORDINATE' THEN 2 " +
            "       WHEN 'SUBORDINATE' THEN 3 " +
            "       WHEN 'UNKNOWN' THEN 4 " +
            "   END " +
            ", CASE lr.useStatus " +
            "       WHEN 'LITERAL' THEN 1 " +
            "       WHEN 'FIGURATIVE' THEN 2 " +
            "       WHEN 'UNKNOWN' THEN 3 " +
            "   END " +
            ", CASE lr.productivity" +
            "       WHEN 'FULL' THEN 1 " +
            "       WHEN 'PARTIAL' THEN 2 " +
            "       WHEN 'NONE' THEN 3 " +
            "       WHEN 'UNKNOWN' THEN 4 " +
            "   END "),
    @NamedQuery(name = "findConceptsByLanguageRepresentationExact", query =
            "SELECT c FROM Concept c " +
            "JOIN c.languageRepresentations clr " +
            "JOIN clr.languageRepresentation lr " +
            "WHERE lr.text = :languageRepresentationName " +
            "ORDER BY " +
            "  CASE c.specificityLevel " +
            "       WHEN 'BASIC_LEVEL' THEN 1 " +
            "       WHEN 'SUPERORDINATE' THEN 2 " +
            "       WHEN 'SUBORDINATE' THEN 3 " +
            "       WHEN 'UNKNOWN' THEN 4 " +
            "  END " +
            ", CASE lr.useStatus " +
            "       WHEN 'LITERAL' THEN 1 " +
            "       WHEN 'FIGURATIVE' THEN 2 " +
            "       WHEN 'UNKNOWN' THEN 3 " +
            "  END " +
            ", CASE lr.productivity" +
            "       WHEN 'FULL' THEN 1 " +
            "       WHEN 'PARTIAL' THEN 2 " +
            "       WHEN 'NONE' THEN 3 " +
            "       WHEN 'UNKNOWN' THEN 4 " +
            "  END "),
    @NamedQuery(name = "findConceptsByLanguageRepresentationStartsWith",
            query =
            "SELECT c FROM Concept c " +
            "JOIN c.languageRepresentations clr " +
            "JOIN clr.languageRepresentation lr " +
            "WHERE lr.text LIKE :languageRepresentationNameStart " +
            "ORDER BY " +
            " CASE c.specificityLevel " +
            "       WHEN 'BASIC_LEVEL' THEN 1 " +
            "       WHEN 'SUPERORDINATE' THEN 2 " +
            "       WHEN 'SUBORDINATE' THEN 3 " +
            "       WHEN 'UNKNOWN' THEN 4 " +
            " END " +
            ", CASE lr.useStatus " +
            "       WHEN 'LITERAL' THEN 1 " +
            "       WHEN 'FIGURATIVE' THEN 2 " +
            "       WHEN 'UNKNOWN' THEN 3 " +
            "  END " +
            ", CASE lr.productivity" +
            "       WHEN 'FULL' THEN 1 " +
            "       WHEN 'PARTIAL' THEN 2 " +
            "       WHEN 'NONE' THEN 3 " +
            "       WHEN 'UNKNOWN' THEN 4 " +
            "  END "),
    @NamedQuery(name = "findConceptsByLanguageRepresentationEndsWith", query =
            "SELECT c FROM Concept c " +
            "JOIN c.languageRepresentations clr " +
            "JOIN clr.languageRepresentation lr " +
            "WHERE lr.text LIKE :languageRepresentationNameEnd " +
            "ORDER BY " +
            " CASE c.specificityLevel " +
            "       WHEN 'BASIC_LEVEL' THEN 1 " +
            "       WHEN 'SUPERORDINATE' THEN 2 " +
            "       WHEN 'SUBORDINATE' THEN 3 " +
            "       WHEN 'UNKNOWN' THEN 4 " +
            " END " +
            ", CASE lr.useStatus " +
            "       WHEN 'LITERAL' THEN 1 " +
            "       WHEN 'FIGURATIVE' THEN 2 " +
            "       WHEN 'UNKNOWN' THEN 3 " +
            "  END " +
            ", CASE lr.productivity" +
            "       WHEN 'FULL' THEN 1 " +
            "       WHEN 'PARTIAL' THEN 2 " +
            "       WHEN 'NONE' THEN 3 " +
            "       WHEN 'UNKNOWN' THEN 4 " +
            "  END "),
    @NamedQuery(name = "findConceptsByStatusExact", query =
            "SELECT c FROM Concept c " +
            "WHERE c.status = :status"),
    @NamedQuery(name = "findConcept", query =
            "SELECT c FROM Concept c " +
            "WHERE c.conceptType = :type " +
            "AND c.name = :name " +
            "AND c.externalSourceId = :externalSourceId " +
            "AND c.specificityLevel = :specificityLevel " +
            "AND c.status = :status " +
            "AND c.pragmaticStatus = :pragmaticStatus " +
            "AND c.uniqueInstance = :uniqueInstance"
    ),
    @NamedQuery(name = "getConceptEntityQuery", query =
            "SELECT c FROM Concept c " +
            "WHERE c.status = :status " +
            "AND c.name = :name " +
            "AND c.specificityLevel = :specificityLevel " +
            "AND c.externalSourceId = :externalSourceId " +
            "AND c.conceptType = :type " +
            "AND c.pragmaticStatus = :pragmaticStatus"),})
@Table(name = "Concepts",
        indexes = {
            @Index(columnList = "ExternalSourceId"),
            @Index(columnList = "Name"),}
//,
//        uniqueConstraints = {
//            @UniqueConstraint(columnNames = {"ExternalSourceId", "Type",
//                "SpecificityLevel", "Status", "PragmaticStatus",
//                "UniqueInstance", "OntologicalDomain", "Source"
//            }),}
)
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

        BASIC_LEVEL, SUPERORDINATE, SUBORDINATE, UNKNOWN;

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
    @XmlTransient
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ConceptId")
    private Long id;

    @Column(name = "ExternalSourceId")
    //@Size(min = 5, max = 14)
//    @NotNull(message = "Concept externalSourceId must be specified.")
    private String externalSourceId;

    @Column(name = "Name", nullable = false)
    //@Size(min = 5, max = 14)
    //@NotNull(message = "Concept Name must be specified.")
    private String name;

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

    @Column(name = "Source")
    private String source;

    @Column(name = "Comment")
    private String comment;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "OntologicalDomain_Concept",
            joinColumns = {
                @JoinColumn(name = "ConceptId")},
            inverseJoinColumns = {
                @JoinColumn(name = "OntologicalDomainId")}
    )
    private Set<OntologicalDomain> ontologicalDomains;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "concept")
    private Set<Concept_LanguageRepresentation> languageRepresentations;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "concept")
    private Set<VisualRepresentation> visualRepresentations;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "concept")
    private Set<MotoricRepresentation> motoricRepresentations;

    /**
     * Public Constructor.
     */
    public Concept() {
        externalSourceId = "";
        name = "";
        conceptType = Concept.ConceptType.UNKNOWN;
        specificityLevel = Concept.SpecificityLevel.UNKNOWN;
        status = Concept.Status.CONSTANT;
        pragmaticStatus = Concept.PragmaticStatus.UNKNOWN;
        uniqueInstance = Concept.UniqueInstance.UNKNOWN;
        source = "";
        comment = "";
        languageRepresentations = new LinkedHashSet<>();
        visualRepresentations = new LinkedHashSet<>();
        motoricRepresentations = new LinkedHashSet<>();
        ontologicalDomains = new LinkedHashSet<>();
    }

    /**
     * Public Constructor with arguments.
     *
     * @param newConcept
     * @param keepLanguageRepresentation
     * @param keepVisualRepresentation
     * @param keepMotoricRepresentation
     */
    public Concept(Concept newConcept,
            boolean keepLanguageRepresentation,
            boolean keepVisualRepresentation,
            boolean keepMotoricRepresentation) {
        this.name = newConcept.getName();
        this.comment = newConcept.getComment();
        this.externalSourceId = newConcept.externalSourceId;
        this.conceptType = newConcept.getConceptType();
        this.pragmaticStatus = newConcept.getPragmaticStatus();
        this.source = newConcept.getSource();
        this.specificityLevel = newConcept.getSpecificityLevel();
        this.status = newConcept.getStatus();
        this.uniqueInstance = newConcept.getUniqueInstance();
        this.languageRepresentations = new LinkedHashSet<>();
        this.visualRepresentations = new LinkedHashSet<>();
        this.motoricRepresentations = new LinkedHashSet<>();
        this.ontologicalDomains = new LinkedHashSet<>();

        if (keepLanguageRepresentation) {
            for (LanguageRepresentation lr : newConcept.
                    getLanguageRepresentations()) {
                if (!this.getLanguageRepresentations().contains(lr)) {
                    LanguageRepresentation newLr =
                            new LanguageRepresentation(lr);
                    this.addLanguageRepresentation(newLr, false);
                }
            }
        }

        if (keepVisualRepresentation) {
            for (VisualRepresentation tmpVisualRepresentation : newConcept.
                    getVisualRepresentationsEntries()) {
                if (!this.getVisualRepresentationsEntries().contains(
                        tmpVisualRepresentation)) {
                    this.getVisualRepresentationsEntries().add(
                            tmpVisualRepresentation);
                }
            }
        }

        if (keepMotoricRepresentation) {
            for (MotoricRepresentation tmpMotoricRepresentation : newConcept.
                    getMotoricRepresentations()) {
                if (!this.getMotoricRepresentations().contains(
                        tmpMotoricRepresentation)) {
                    this.getMotoricRepresentations().add(
                            tmpMotoricRepresentation);
                }
            }
        }

        if (!newConcept.getOntologicalDomains().isEmpty()) {
            for (OntologicalDomain od : newConcept.getOntologicalDomains()) {
                if (!this.getOntologicalDomains().contains(od)) {
                    this.getOntologicalDomains().add(od);
                }
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
     * Gets the external source id of the concept. This is usually the id of the
     * resource that was used to populate the Praxicon.
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
     * Gets the name of the concept. This is usually a human-readable string
     * connected with the concept, usually provided by the external resource.
     *
     * @return String - The name of the concept.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the concept.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name.trim();
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
     * Sets the type of the concept. Permitted values: ABSTRACT, ENTITY,
     * FEATURE, MOVEMENT, UNKNOWN;
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
        this.conceptType = ConceptType.valueOf(
                conceptType.trim().toUpperCase());
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
     * Sets the specificity level of the concept. Permitted values: SUBORDINATE,
     * BASIC_LEVEL, BASIC_LEVEL_EXTENDED, SUPERORDINATE, UNKNOWN
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
     * Sets the status of the concept. Permitted values: CONSTANT, VARIABLE
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
     * Sets the pragmatic status of the concept. Permitted values: CONCRETE,
     * ABSTRACT, UNKNOWN
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
     * Sets the unique instance of the concept. Permitted values: YES, NO,
     * UNKNOWN
     *
     * @param uniqueInstance
     */
    public void setUniqueInstance(UniqueInstance uniqueInstance) {
        this.uniqueInstance = uniqueInstance;
    }

    /**
     * Gets the source of the concept. Where the concept came from (e.g.
     * WordNew).
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
     * Gets the ontological domain of the concept. It defines the domain in
     * terms of ontology that the concept belongs to (for example: natural
     * event, activity or physical process)
     *
     * @return The ontological domain of the concept
     */
    public Set<OntologicalDomain> getOntologicalDomains() {
        return ontologicalDomains;
    }

    /**
     * Sets the ontological domain of the concept.
     *
     * @param ontologicalDomains
     */
    public void setOntologicalDomains(
            Set<OntologicalDomain> ontologicalDomains) {
        this.ontologicalDomains = ontologicalDomains;
    }

    /**
     * Adds an ontological domain to the concept.
     *
     */
    void addOntologicalDomain(OntologicalDomain ontologicalDomain) {
        this.ontologicalDomains.add(ontologicalDomain);
    }

    /**
     * Gets the language representations of the concept.
     *
     * @return a set containing the concept's language representations
     *
     */
    public final Set<LanguageRepresentation> getLanguageRepresentations() {
        Set<LanguageRepresentation> lrs = new LinkedHashSet<>();
        for (Concept_LanguageRepresentation clr
                : getConceptLanguageRepresentationsEntries()) {
            lrs.add(clr.getLanguageRepresentation());
        }
        return new LinkedHashSet<>(lrs);
    }

    /**
     * Gets the Concept_LanguageRepresentation construct which helps get the
     * language representations.
     *
     * @return the Concept_LanguageRepresentation instance of the concept
     *
     */
    public final Set<Concept_LanguageRepresentation>
            getConcept_LanguageRepresentation() {
        return languageRepresentations;
    }

    public void setConcept_LanguageRepresentation(
            Set<Concept_LanguageRepresentation> languageRepresentations) {
        this.languageRepresentations = languageRepresentations;
    }

    /**
     * Gets a set of Concept_LanguageRepresentation instances.
     *
     * @return a set of Concept_LanguageRepresentation instances for the
     *         concept
     */
    public Set<Concept_LanguageRepresentation>
            getConceptLanguageRepresentationsEntries() {
        Set<Concept_LanguageRepresentation> languageRepresentationEntries =
                this.getConcept_LanguageRepresentation();
        if (!languageRepresentationEntries.isEmpty()) {
            return new LinkedHashSet<>(languageRepresentationEntries);
        } else {
            return new LinkedHashSet<>();
        }
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
        this.languageRepresentations.
                add(conceptLanguageRepresentation);
    }

    /**
     * Adds a LanguageRepresentation instance to the concept.
     *
     * @param languageRepresentation a Language representation.
     * @param isRepresentative       whether the Language representation is
     *                               representative of the concept or not. 
     *                               There can be more than one
     *                               representative Language representations.
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

    /**
     * Gets text of the first Language representation of Language "en" for this
     * concept.
     *
     * @return the text of the first Language Representation of the concept.
     */
    public String getFirstLanguageRepresentationName() {
        Set<LanguageRepresentation> lrs = this.getLanguageRepresentations();
        for (LanguageRepresentation lr : lrs) {
            if (lr.getLanguage().name().equalsIgnoreCase("en")) {
                return lr.getText();
            }
        }
        if (lrs.size() > 0) {
            return lrs.iterator().next().getText();
        }
        return "noname";
    }

    /**
     * Gets a set of Language representation texts for this concept.
     *
     * @return set of strings with all the texts of the Language
     *         Representations of the Concept.
     */
    public Set<String> getLanguageRepresentationsNames() {
        Set<LanguageRepresentation> lrs = this.getLanguageRepresentations();
        List<String> lrNames = new ArrayList<>();
        if (!lrs.isEmpty()) {
            for (LanguageRepresentation lr : lrs) {
                lrNames.add(lr.getText());
            }
            Collections.sort(lrNames);
            return new LinkedHashSet<>(lrNames);
        } else {
            return new LinkedHashSet<>();
        }
    }

    /**
     * Gets a set of Language representation texts for this concept.
     *
     * @return set of strings with all the texts of the Language
     *         Representations of the Concept.
     */
    public Set<String> getLanguageRepresentationsAndRepresentative() {
        Set<Concept_LanguageRepresentation> clrs = this.
                getConceptLanguageRepresentationsEntries();
        List<String> lrNamesAndRepresentative = new ArrayList<>();
        if (!clrs.isEmpty()) {
            for (Concept_LanguageRepresentation clr : clrs) {
                if (!isNull(clr)) {
                    lrNamesAndRepresentative.add(clr.toString());
                }
            }
            Collections.sort(lrNamesAndRepresentative);
            return new LinkedHashSet<>(lrNamesAndRepresentative);
        } else {
            return new LinkedHashSet<>();
        }
    }

    /**
     * Updates LanguageRepresentation of a concept using this concept's
     * LanguageRepresentation
     *
     * @param oldConcept the concept to be updated
     */
    public void updateLanguageRepresentations(Concept oldConcept) {
        for (Concept_LanguageRepresentation conceptLanguageRepresentation
                : this.getConceptLanguageRepresentationsEntries()) {
            if (!oldConcept.getConceptLanguageRepresentationsEntries().
                    contains(conceptLanguageRepresentation)) {
                conceptLanguageRepresentation.getLanguageRepresentation().
                        getConcepts().remove(this);
                conceptLanguageRepresentation.getLanguageRepresentation().
                        getConcepts().add(this);
                oldConcept.getConceptLanguageRepresentationsEntries().add(
                        conceptLanguageRepresentation);
            }
        }
    }

    /**
     * Gets the visual representations of the concept
     *
     * @return visual representations construct
     */
    public Set<VisualRepresentation> getVisualRepresentations() {
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
            Set<VisualRepresentation> visualRepresentations) {
        this.visualRepresentations = visualRepresentations;
    }

    /**
     * Gets a set of visual representations for this concept
     *
     * @return a set of visual representations
     */
    public final Set<VisualRepresentation> getVisualRepresentationsEntries() {
        Set<VisualRepresentation> visualRepresentationEntries =
                new LinkedHashSet<>();
        for (VisualRepresentation VisualRepresentation
                : this.visualRepresentations) {
            visualRepresentationEntries.add(VisualRepresentation);
        }
        return new LinkedHashSet<>(visualRepresentationEntries);
    }

    /**
     * Updates VisualRepresentations of a concept using this concept's
     * VisualRepresentations.
     *
     * @param oldConcept the concept to be updated
     */
    public void updateVisualRepresentations(Concept oldConcept) {
        for (VisualRepresentation visualRepresentation : this.
                getVisualRepresentations()) {
            if (!oldConcept.getVisualRepresentations().contains(
                    visualRepresentation)) {
                oldConcept.getVisualRepresentations().
                        add(visualRepresentation);
            }
        }
    }

    /**
     * Gets the motoric representation for this concept.
     *
     * @return motoric representation construct
     */
    public final Set<MotoricRepresentation> getMotoricRepresentations() {
        return motoricRepresentations;
    }

    /**
     * Gets a set of motoric representations for this concept.
     *
     * @return a set of motoric representations
     */
    public Set<MotoricRepresentation> getMotoricRepresentationsEntries() {
        Set<MotoricRepresentation> motoricRepresentationEntries =
                new LinkedHashSet<>();
        for (MotoricRepresentation MotoricRepresentation
                : this.motoricRepresentations) {
            motoricRepresentationEntries.add(MotoricRepresentation);
        }
        return new LinkedHashSet<>(motoricRepresentationEntries);
    }

    /**
     * Sets the motoric representations for this concept.
     *
     * @param motoricRepresentations
     */
    public void setMotoricRepresentations(
            Set<MotoricRepresentation> motoricRepresentations) {
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
        for (MotoricRepresentation motoricRepresentation : this.
                getMotoricRepresentations()) {
            if (!oldConcept.getMotoricRepresentations().contains(
                    motoricRepresentation)) {
                oldConcept.getMotoricRepresentations().add(
                        motoricRepresentation);
            }
        }
    }

    /**
     * Gets a string of concatenated full info for this concept. concept
     * ConceptType, Status, pragmatic Status, specificity level, description
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
     * Gets a string of concatenated short info for the concept. concept
     * ConceptType and specificity level
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
        hash = 13 * hash + Objects.hashCode(this.name);
        hash = 13 * hash + Objects.hashCode(this.externalSourceId);
        hash = 13 * hash + Objects.hashCode(this.conceptType);
        hash = 13 * hash + Objects.hashCode(this.specificityLevel);
        hash = 13 * hash + Objects.hashCode(this.status);
        hash = 13 * hash + Objects.hashCode(this.pragmaticStatus);
        hash = 13 * hash + Objects.hashCode(this.uniqueInstance);
        hash = 13 * hash + Objects.hashCode(this.languageRepresentations);
        hash = 13 * hash + Objects.hashCode(this.visualRepresentations);
        hash = 13 * hash + Objects.hashCode(this.motoricRepresentations);
        hash = 13 * hash + Objects.hashCode(this.ontologicalDomains);
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
        if (!isNull(this.name) && !isNull(other.getName())) {
            if (!this.name.equals(other.getName())) {
                return false;
            }
        }
        if (!isNull(this.externalSourceId) && !isNull(other.
                getExternalSourceId())) {
            if (!this.externalSourceId.
                    equals(other.getExternalSourceId())) {
                return false;
            }
        }
        if (!this.conceptType.equals(other.getConceptType())) {
            return false;
        }
        if (!this.specificityLevel.equals(other.getSpecificityLevel())) {
            return false;
        }
        if (!this.status.equals(other.getStatus())) {
            return false;
        }
        if (!this.pragmaticStatus.equals(other.getPragmaticStatus())) {
            return false;
        }
        if (!this.uniqueInstance.equals(other.getUniqueInstance())) {
            return false;
        }
        if (!this.languageRepresentations.equals(other.
                getConcept_LanguageRepresentation())) {
            return false;
        }
        if (!this.visualRepresentations.equals(other.
                getVisualRepresentations())) {
            return false;
        }
        if (!this.motoricRepresentations.equals(other.
                getMotoricRepresentations())) {
            return false;
        }
        if (!this.ontologicalDomains.toString().equals(other.
                getOntologicalDomains().toString())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (name != null && !name.equalsIgnoreCase("")) {
            return name;
            // + " (Entity)";
        } else if (!isNull(this.getConceptLanguageRepresentationsEntries()) &&
                !this.getConceptLanguageRepresentationsEntries().isEmpty()) {
            Set<Concept_LanguageRepresentation> tmpSet =
                    this.getConceptLanguageRepresentationsEntries();
            if (tmpSet.size() > 0) {
                StringBuilder tmp = new StringBuilder(
                        tmpSet.iterator().next().getLanguageRepresentation().
                        getText());
                for (Concept_LanguageRepresentation item : tmpSet) {
                    tmp.append("\\").append(item.getLanguageRepresentation().
                            getText());
                }
                return tmp.toString();
            } else {
                return id + "";
            }
        } else {
            return "";
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
