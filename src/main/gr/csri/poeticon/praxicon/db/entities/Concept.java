/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import gr.csri.poeticon.praxicon.Constants;
import gr.csri.poeticon.praxicon.Globals;
import gr.csri.poeticon.praxicon.db.dao.ConceptDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
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
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.XmlRootElement;
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
            "FROM Concept c WHERE c.externalSourceId LIKE :conceptExternalSourceId"),
    @NamedQuery(name = "findConceptByExternalSourceIdExact", query =
            "FROM Concept c WHERE c.externalSourceId = :conceptExternalSourceId"),
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
    @NamedQuery(name = "getConceptEntityQuery", query =
            "SELECT c FROM Concept c " +
            "WHERE c.status = :status " +
            "AND c.externalSourceId = :externalSourceId " +
            "AND c.conceptType = :type"),})
@Table(name = "Concepts", indexes = {
    //    @Index(columnList = "ExternalSourceId"),
    @Index(columnList = "ConceptId")})
//@ConceptConstraint(groups=ConceptGroup.class)
public class Concept implements Serializable {

    /**
     * Enumeration for the concept Type.
     */
    public static enum Type {

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
     * Enumeration for the Type of concept Status.
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
     * Enumeration for the Type of concept Status.
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
     * A YES/NO/UNKNOWN enumeration for the unique instance.
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
    @XmlElement(required = true)
    @SequenceGenerator(name = "CUST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CUST_SEQ")
    @Column(name = "ConceptId")
    private Long id;

    @Column(name = "ExternalSourceId")
    //@Size(min = 5, max = 14)
    //@XmlElement(required = true)
    //@NotNull(message = "Concept externalSourceId must be specified.")
    private String externalSourceId;

    @Column(name = "Type")
    //@XmlElement(required = true)
    @NotNull(message = "Concept type must be specified.")
    @Enumerated(EnumType.STRING)
    private Type conceptType;

    @Column(name = "SpecificityLevel")
    //@XmlElement(required = true)
    @NotNull(message = "Specificity level must be specified.")
    @Enumerated(EnumType.STRING)
    private SpecificityLevel specificityLevel;

    @Column(name = "Status")
    //@XmlElement(required = true)
    @NotNull(message = "Concept status must be specified.")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "PragmaticStatus")
    @NotNull(message = "Concept pragmatic status must be specified")
    @Enumerated(EnumType.STRING)
    private PragmaticStatus pragmaticStatus;

    @Column(name = "UniqueInstance")
    //@XmlElement(required = false)
    @NotNull(message = "Concept unique instance must be specified.")
    @Enumerated(EnumType.STRING)
    private UniqueInstance uniqueInstance;

    @Column(name = "OntologicalDomain")
    //@XmlElement(required = false)
    private String ontologicalDomain;

    @Column(name = "Source")
    //@XmlElement(required = false)
    private String source;

    @Column(name = "Comment")
    //@XmlElement(required = false)
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
        externalSourceId = null;
        comment = "";
        specificityLevel = Concept.SpecificityLevel.UNKNOWN;
        pragmaticStatus = Concept.PragmaticStatus.UNKNOWN;
        languageRepresentations = new ArrayList<>();
        visualRepresentations = new ArrayList<>();
        motoricRepresentations = new ArrayList<>();
    }

    /**
     * Public Constructor with argument.
     *
     * @param newConcept
     */
    private Concept(Concept newConcept) {
        this.externalSourceId = newConcept.externalSourceId;
        this.conceptType = newConcept.getConceptType();
        this.specificityLevel = newConcept.getSpecificityLevel();
        this.status = newConcept.getStatus();
        this.pragmaticStatus = newConcept.getPragmaticStatus();
        languageRepresentations = new ArrayList<>();
        visualRepresentations = new ArrayList<>();
        motoricRepresentations = new ArrayList<>();

        for (LanguageRepresentation tmpLanguageRepresentation : newConcept.
                getLanguageRepresentations()) {
            if (!this.getLanguageRepresentations().contains(
                    tmpLanguageRepresentation)) {
                tmpLanguageRepresentation.getConcepts().remove(newConcept);
                this.getLanguageRepresentations().add(
                        tmpLanguageRepresentation);
            }
        }

        for (int i = 0; i < newConcept.getVisualRepresentationsEntries().size();
                i++) {
            if (!this.getVisualRepresentationsEntries().contains(newConcept.
                    getVisualRepresentationsEntries().get(i))) {
                this.getVisualRepresentationsEntries().add(newConcept.
                        getVisualRepresentationsEntries().get(i));
            }
        }

        for (int i = 0; i < newConcept.getMotoricRepresentations().size();
                i++) {
            if (!this.getMotoricRepresentations().contains(newConcept.
                    getMotoricRepresentations().get(i))) {
                this.getMotoricRepresentations().add(newConcept.
                        getMotoricRepresentations().get(i));
            }
        }
    }

    /**
     * Gets the id of the concept.
     *
     * @return long
     */
    @XmlAttribute
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
     * @return Type - The Type of the concept.
     */
    public Type getConceptType() {
        return conceptType;
    }

    /**
     * Sets the type of the concept. Permitted values:
     * ABSTRACT, ENTITY, FEATURE, MOVEMENT, UNKNOWN;
     *
     * @param conceptType - Type
     */
    public void setConceptType(Type conceptType) {
        this.conceptType = conceptType;
    }

    /**
     * Sets the type of the concept. Overloaded.
     *
     * @param conceptType - String
     */
    public void setConceptType(String conceptType) {
        this.conceptType = Type.valueOf(conceptType.trim().toUpperCase());
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
     * @return a list of Concept_LanguageRepresentation instances for the concept
     */
    public List<Concept_LanguageRepresentation> getLanguageRepresentationsEntries() {
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
    public void addLanguageRepresentation(
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
     * @return the externalSourceId of the first Language representation of the concept.
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
     * Gets a string of concatenated full info for this concept. concept Type,
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
     * Gets a string of concatenated short info for the concept. concept Type
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
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - method won't work in case the id fields are not set
        if (!(object instanceof Concept)) {
            return false;
        }
        Concept other = (Concept)object;
        if (this.externalSourceId != null && other.externalSourceId != null &&
                this.externalSourceId.equalsIgnoreCase(other.externalSourceId)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        if (externalSourceId != null && !externalSourceId.equalsIgnoreCase("")) {
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
                            tmpList.get(i).getLanguageRepresentation().getText());
                }
                return tmp.toString();
            } else {
                return id + "";
            }
        }
    }

    public void afterUnmarshal(Unmarshaller u, Object parent) {

        if (Globals.ToMergeAfterUnMarshalling) {
            ConceptDao cDao = new ConceptDaoImpl();
            Concept tmp = cDao.getConceptWithExternalSourceIdOrId(this.
                    getExternalSourceId());
            if (tmp == null) {
                if (this.conceptType == null) {
                    this.conceptType = Type.UNKNOWN;
                }
                cDao.merge(this);
            } else {
                cDao.update(this);
            }
        } else {
            Concept tmp = (Concept)Constants.globalConcepts.get(this.
                    getExternalSourceId());
            if (tmp == null) {
                if (this.conceptType == null) {
                    this.conceptType = Type.UNKNOWN;
                }
                tmp = new Concept(this);
                Constants.globalConcepts.put(tmp.getExternalSourceId(), tmp);
            } else {
                tmp.conceptType = this.conceptType;
                updateLanguageRepresentations(tmp);
                updateVisualRepresentations(tmp);
                updateMotoricRepresentations(tmp);
            }
        }
        System.err.
                println("Finish unmarshalling: " + this.getExternalSourceId());
    }
}

@XmlRegistry
class ObjectFactory {

    Concept createConcept() {
        return new Concept();
    }
}
