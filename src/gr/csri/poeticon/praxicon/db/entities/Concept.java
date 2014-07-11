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
import javax.persistence.OneToOne;
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
            "FROM Concept c WHERE c.id = :concept_id"),
    @NamedQuery(name = "findConceptsByName", query =
            "FROM Concept c WHERE c.name LIKE :concept_name"),
    @NamedQuery(name = "findConceptByNameExact", query =
            "FROM Concept c WHERE c.name = :concept_name"),
    @NamedQuery(name = "findConceptsByLanguageRepresentation", query =
            "SELECT c FROM Concept c " +
            "JOIN c.languageRepresentations clr " +
            "JOIN clr.languageRepresentation lr " +
            "WHERE lr.text LIKE :lr_name"),
    @NamedQuery(name = "findConceptsByLanguageRepresentationExact", query =
            "SELECT c FROM Concept c " +
            "JOIN c.languageRepresentations clr " +
            "JOIN clr.languageRepresentation lr " +
            "WHERE lr.text = :lr_name"),
    @NamedQuery(name = "findConceptsByStatusExact", query =
            "SELECT c FROM Concept c " +
            "WHERE c.status = :status"),
    @NamedQuery(name = "getConceptEntityQuery", query =
            "SELECT c FROM Concept c " +
            "WHERE c.status = :status " +
            "AND c.name = :name " +
            "AND c.conceptType = :type " +
            "AND c.pragmaticStatus = :pragmatic_status"),})
@Table(name = "Concepts", indexes = {
    @Index(columnList = "Name"),
    @Index(columnList = "ConceptId")})
//@ConceptConstraint(groups=ConceptGroup.class)
public class Concept implements Serializable {

    /**
     * Enumeration for the concept type.
     */
    public static enum type {

        ABSTRACT, ENTITY, FEATURE, MOVEMENT, UNKNOWN;

        @Override
        public String toString() {
            return this.name();
        }
    }

    /**
     * Enumeration for the specificity level of a concept.
     */
    public static enum specificity_level {

        BASIC_LEVEL, SUPERORDINATE, SUBORDINATE, UNKNOWN;

        @Override
        public String toString() {
            return this.name();
        }
    }

    /**
     * Enumeration for the type of concept status.
     */
    public static enum status {

        CONSTANT, VARIABLE, TEMPLATE;

        @Override
        public String toString() {
            return this.name();
        }
    }

    /**
     * A YES/NO/UNKNOWN enumeration for the unique instance.
     */
    public static enum unique_instance {

        YES, NO, UNKNOWN;

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

    private static final long serialVersionUID = 1L;

    @Id
    @XmlElement(required = true)
    @SequenceGenerator(name = "CUST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CUST_SEQ")
    @Column(name = "ConceptId")
    private Long id;

    @Column(name = "Name")
    //@Size(min = 5, max = 14)
    //@XmlElement(required = true)
    @NotNull(message = "Concept name must be specified.")
    private String name;

    @Column(name = "Type")
    //@XmlElement(required = true)
    @NotNull(message = "Concept type must be specified.")
    @Enumerated(EnumType.STRING)
    private type conceptType;

    @Column(name = "SpecificityLevel")
    //@XmlElement(required = true)
    @NotNull(message = "Specificity level must be specified.")
    @Enumerated(EnumType.STRING)
    private specificity_level specificityLevel;

    @Column(name = "Status")
    //@XmlElement(required = true)
    @NotNull(message = "Concept status must be specified.")
    @Enumerated(EnumType.STRING)
    private status status;

    @Column(name = "UniqueInstance")
    //@XmlElement(required = false)
    @NotNull(message = "Concept unique instance must be specified.")
    @Enumerated(EnumType.STRING)
    private unique_instance uniqueInstance;

    @Column(name = "PragmaticStatus")
    //@XmlElement(required = false)
    @NotNull(message = "Concept pragmatic status must be specified.")
    @Enumerated(EnumType.STRING)
    private pragmatic_status pragmaticStatus;

    @Column(name = "OntologicalDomain")
    //@XmlElement(required = false)
    private String ontologicalDomain;

    @Column(name = "Source")
    //@XmlElement(required = false)
    private String source;

    @Column(name = "Comment")
    //@XmlElement(required = false)
    private String comment;

    @OneToOne(cascade = CascadeType.ALL)
    private RelationArgument relationArgument;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "concept")
    private List<Concept_LanguageRepresentation> languageRepresentations;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "concept")
    private List<VisualRepresentation> visualRepresentations;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "concept")
    private List<MotoricRepresentation> motoricRepresentations;

    // Public Constructor
    public Concept() {
        name = null;
        comment = "";
        specificityLevel = Concept.specificity_level.UNKNOWN;
        languageRepresentations = new ArrayList<>();
        visualRepresentations = new ArrayList<>();
        motoricRepresentations = new ArrayList<>();
    }

    private Concept(Concept newConcept) {
        this.name = newConcept.name;
        this.conceptType = newConcept.getConceptType();
        this.specificityLevel = newConcept.getSpecificityLevel();
        this.pragmaticStatus = newConcept.getPragmaticStatus();
        this.status = newConcept.getStatus();
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

    @XmlAttribute
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name of the concept.
     * @xmlcomments.args xmltag="name" xmldescription="This attribute defines
     * the name of the element"
     */
    public String getName() {
        if (name != null) {
            return name;
        } else {
            return id + "";
        }
    }

    /**
     *
     * @return the pure name of the concept without the wordnet identifiers.
     */
    public String getNameNoNumbers() {
        if (name != null) {
            return name.replaceAll("%\\d+:\\d+:\\d+:\\d*:\\d*", "");
        } else {
            return id + "";
        }
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    /**
     * @return the type of the concept.
     * @xmlcomments.args xmltag="&lt;concept_type&gt;" xmldescription="This tag
     * defines the type of the concept entity (abstract, entity, feature,
     * movement, unknown)"
     */
    public type getConceptType() {
        return conceptType;
    }

    public void setConceptType(type conceptType) {
        this.conceptType = conceptType;
    }

    public void setConceptType(String conceptType) {
        this.conceptType = type.valueOf(conceptType.trim().toUpperCase());
    }

    /**
     * @return the specificity level of the concept.
     * @xmlcomments.args xmltag="&lt;specificity_level&gt;" xmldescription="This
     * tag defines the specificity level of the concept"
     */
    public specificity_level getSpecificityLevel() {
        return specificityLevel;
    }

    public void setSpecificityLevel(specificity_level levelType) {
        this.specificityLevel = levelType;
    }

    public void setSpecificityLevel(String levelType) {
        if (levelType.equalsIgnoreCase("BASIC_LEVEL")) {
            this.specificityLevel = Concept.specificity_level.BASIC_LEVEL;
        } else if (levelType.equalsIgnoreCase("SUPERORDINATE")) {
            this.specificityLevel = Concept.specificity_level.SUPERORDINATE;
        } else if (levelType.equalsIgnoreCase("SUBORDINATE")) {
            this.specificityLevel = Concept.specificity_level.SUBORDINATE;
        } else {
            this.specificityLevel = Concept.specificity_level.UNKNOWN;
        }
    }

    /**
     * @return the status of the concept.
     * @xmlcomments.args xmltag="&lt;status&gt;" xmldescription="This tag
     * defines if the entity is a variable, a constant or a template"
     */
    //@ConstantConcepts(value=status.CONSTANT)
    public status getStatus() {
        return status;
    }

    //@ConstantConcepts
    public void setStatus(status varType) {
        this.status = varType;
    }

    //@ConstantConcepts
    public void setStatus(String varType) {
        this.status = status.valueOf(varType.trim().toUpperCase());
    }

    /**
     * @return @xmlcomments.args xmltag="&lt;unique_instance&gt;"
     *         xmldescription="This tag defines the source of the concept
     *         (from which resources was generated (for example: Wordnet)"
     */
    public unique_instance getUniqueInstance() {
        return uniqueInstance;
    }

    public void setUniqueInstance(unique_instance uniqueInstance) {
        this.uniqueInstance = uniqueInstance;
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
     * @return The ontological domain of the concept
     *         (e.g. natural event, activity or physical process.)
     * @xmlcomments.args xmltag="&lt;ontological_domain&gt;" xmldescription="This
     * tag defines the domain in terms of ontology that the concept belongs
     * (for example: natural event, activity or physical process) "
     */
    public String getOntologicalDomain() {
        return ontologicalDomain;
    }

    public void setOntologicalDomain(String ontologicalDomain) {
        this.ontologicalDomain = ontologicalDomain;
    }

    /**
     * @return the source of the concept (where the concept came from)
     * @xmlcomments.args xmltag="&lt;source&gt;" xmldescription="This
     * tag defines the source of the concept (from which resources
     * was generated (for example: Wordnet)"
     */
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return a string containing additional information about the concept.
     */
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the language representations of the concept
     *
     */
    public final List<LanguageRepresentation> getLanguageRepresentations() {
        List<LanguageRepresentation> lrs;
        lrs = new ArrayList();
        for (Concept_LanguageRepresentation clr : this.languageRepresentations) {
            lrs.add(clr.getLanguageRepresentation());
        }
        return lrs;
    }

    /**
     * @return the concept language representation instance of the concept
     *
     */
    public final List<Concept_LanguageRepresentation>
            getConceptLanguageRepresentation() {
        return languageRepresentations;
    }

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
     *                                      language representation with
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
     * @param languageRepresentation a language representation.
     * @param isRepresentative       whether the language representation is
     *                               representative of the concept or not.
     *                               There can be more than one representative
     *                               language representations.
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

    public void setLanguageRepresentation(
            List<Concept_LanguageRepresentation> languageRepresentations) {
        this.languageRepresentations = languageRepresentations;
    }

    /**
     * Gets text of the first language representation of language "en" for this
     * concept.
     *
     * @return the name of the first language representation of the concept.
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
     * @return @xmlcomments.args xmltag="&lt;visual_representation&gt;"
     *         xmldescription="This tag defines the Visual Representation of the
     *         concept"
     */
    public List<VisualRepresentation> getVisualRepresentations() {
        return visualRepresentations;
    }

    public void addVisualRepresentation(
            VisualRepresentation visualRepresentation) {
        this.visualRepresentations.add(visualRepresentation);
    }

    public void setVisualRepresentation(
            List<VisualRepresentation> visualRepresentations) {
        this.visualRepresentations = visualRepresentations;
    }

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
     * VisualRepresentations
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
     * @return @xmlcomments.args xmltag="&lt;motoric_representation&gt;"
     *         xmldescription="This tag defines the motoric representation"
     */
    public final List<MotoricRepresentation> getMotoricRepresentations() {
        return motoricRepresentations;
    }

    public List<MotoricRepresentation> getMotoricRepresentationsEntries() {
        List<MotoricRepresentation> motoric_representation_entries =
                new ArrayList<>();
        for (MotoricRepresentation MotoricRepresentation
                : this.motoricRepresentations) {
            motoric_representation_entries.add(MotoricRepresentation);
        }
        return motoric_representation_entries;
    }

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
     * Gets all relations that contain this concept as object.
     *
     * @return a list of relations
     */
    public List<Relation> getRelationsContainingConceptAsObject() {
        List<Relation> relationList = new ArrayList();
        for (Relation relation : this.relationArgument.
                getRelationsContainingRelationArgumentAsObject()) {
            if (relation.getObject().isConcept()) {
                if (relation.getObject().getConcept().equals(this)) {
                    relationList.add(relation);
                }
            }
        }
        return relationList;
    }

    /**
     * Gets all relations that contain this concept as subject.
     *
     * @return a list of relations
     *
     */
    public List<Relation> getRelationsContainingConceptAsSubject() {
        List<Relation> relationList = new ArrayList();
        for (Relation relation : this.relationArgument.
                getRelationsContainingRelationArgumentAsSubject()) {
            if (relation.getObject().isConcept()) {
                if (relation.getSubject().getConcept().equals(this)) {
                    relationList.add(relation);
                }
            }
        }
        return relationList;
    }

    /* TODO: The following is not used anywhere in the project. So, for now
     it is commented. */
//    /**
//     * Updates object relations of a concept using this concept object relations
//     *
//     * @param oldConcept the concept to be updated
//     */
//    public void updateObjOfRelations(Concept oldConcept) {
//        for (int i = 0; i < this.getRelationsContainingConceptAsObject().size();
//                i++) {
//            if (!oldConcept.getRelationsContainingConceptAsObject().contains(
//                    this.getRelationsContainingConceptAsObject().get(i))) {
//                if (this.getRelationsContainingConceptAsObject().get(i).
//                        getObject().equals(this)) {
//                    this.getRelationsContainingConceptAsObject().get(i).
//                            setObject(oldConcept);
//                } else {
//                    this.getRelationsContainingConceptAsObject().get(i).
//                            setSubject(oldConcept);
//                }
//                oldConcept.getRelationsContainingConceptAsObject().add(
//                        this.getRelationsContainingConceptAsObject().get(i));
//            }
//        }
//    }

    /* TODO: The following is not used anywhere in the project. So, for now
     it is commented. */
//    /**
//     * Adds a new intersection of relation chains to this concept created using
//     * given relation types (fw+bw) and given relation objects
//     *
//     * @param relationTypeForward  list of forward types of relations
//     * @param relationTypeBackward list of backward types of relations
//     * @param objectConcepts       list of concepts to be used as objects
//     */
//    public void addRelation(List<String> relationTypeForward,
//            List<String> relationTypeBackward, List<Concept> objectConcepts) {
//        IntersectionOfRelationChains inter = new IntersectionOfRelationChains();
//        for (int i = 0; i < relationTypeForward.size(); i++) {
//            RelationType rType = new RelationType();
//            rType.setForwardName(relationTypeForward.get(i));
//            rType.setBackwardName(relationTypeBackward.get(i));
//            Relation rel = new Relation();
//            rel.setType(rType);
//            rel.setSubject(this);
//            rel.setObject(objectConcepts.get(i));
//            RelationChain rChain = new RelationChain();
//            rChain.addRelation(rel, 0);
//            inter.addRelationChain(rChain);
//        }
//        this.addIntersectionOfRelationChains(inter);
//    }
//    /**
//     * Updates relations of a concept using this concept relations
//     *
//     * @param oldConcept the concept to be updated
//     */
//    public void updateRelations(Concept oldConcept) {
//        for (int i = 0; i < this.getIntersectionsOfRelationChains().size();
//                i++) {
//            if (!oldConcept.getIntersectionsOfRelationChains().contains(
//                    this.getIntersectionsOfRelationChains().get(i))) {
//                this.getIntersectionsOfRelationChains().get(i).setConcept(
//                        oldConcept);
//                oldConcept.getIntersectionsOfRelationChains().add(
//                        this.getIntersectionsOfRelationChains().get(i));
//            }
//        }
//    }
    /**
     * Gets a string of concatenated full info for the concept. concept type,
     * status, pragmatic status, basic level, description
     *
     * @return a string
     */
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getConceptType());
        sb.append("#");
        sb.append(this.getStatus());
        sb.append("#");
        sb.append(this.getPragmaticStatus());
        sb.append("#");
        sb.append(this.getSpecificityLevel());

        return sb.toString();
    }

    /**
     * Gets a string of concatenated short info for the concept. concept type
     * and basic level
     *
     * @return a string
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
        if (this.name != null && other.name != null &&
                this.name.equalsIgnoreCase(other.name)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        if (name != null && !name.equalsIgnoreCase("")) {
            return name;
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
            Concept tmp = cDao.getConceptWithNameOrID(this.getName());
            if (tmp == null) {
                if (this.conceptType == null) {
                    this.conceptType = type.UNKNOWN;
                }
                cDao.merge(this);
            } else {
                cDao.update(this);
            }
        } else {
            Concept tmp = (Concept)Constants.globalConcepts.get(
                    this.getName());
            if (tmp == null) {
                if (this.conceptType == null) {
                    this.conceptType = type.UNKNOWN;
                }
                tmp = new Concept(this);
                Constants.globalConcepts.put(tmp.getName(), tmp);
            } else {
                tmp.conceptType = this.conceptType;
                updateLanguageRepresentations(tmp);
                updateVisualRepresentations(tmp);
                updateMotoricRepresentations(tmp);
                //updateObjOfRelations(tmp);
                //updateRelations(tmp);
            }
        }
        System.err.println("Finish unmarshalling: " + this.getName());
    }
}

@XmlRegistry
class ObjectFactory {

    Concept createConcept() {
        return new Concept();
    }
}
