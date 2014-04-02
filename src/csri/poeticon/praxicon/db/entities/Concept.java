/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.entities;

import csri.poeticon.praxicon.Constants;
import csri.poeticon.praxicon.Globals;
import csri.poeticon.praxicon.db.dao.ConceptDao;
import csri.poeticon.praxicon.db.dao.RelationDao;
import csri.poeticon.praxicon.db.dao.LanguageRepresentationDao;
import csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import csri.poeticon.praxicon.db.dao.implSQL.RelationDaoImpl;
import csri.poeticon.praxicon.db.dao.implSQL.LanguageRepresentationDaoImpl;
import csri.poeticon.praxicon.db.entities.listeners.ConceptListener;
import csri.poeticon.praxicon.db.entities.validators.ConstantConcepts;
import csri.poeticon.praxicon.db.entities.validators.ConceptValidator;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Erevodifwntas
 * @author Dimitris Mavroeidis
 */

@XmlRootElement(name="entity")
@Entity
@EntityListeners(ConceptListener.class)
@NamedQuery(name = "findAllConcepts", query= "select c from Concept c")
@Table(name="Concepts") //, Definition = "SMALLINT UNSIGNED COMMENT 'The Concept table. This is the key table of the database'")
//@ConceptConstraint(groups=ConceptGroup.class)
public class Concept implements Serializable
{
    public static enum type
    {
        ABSTRACT, ENTITY, FEATURE, MOVEMENT, UNKNOWN;
        @Override
        public String toString()
        {
            return this.name();
        }
    }

    public static enum specificity_level
    {
        BASIC_LEVEL, SUPERORDINATE, SUBORDINATE, UNKNOWN;
        @Override
        public String toString()
        {
            return this.name();
        }
    }

    public static enum status
    {
        CONSTANT, VARIABLE, TEMPLATE;
        @Override
        public String toString()
        {
            return this.name();
        }
    }

    public static enum unique_instance
    {
        YES, NO, UNKNOWN ;
        @Override
        public String toString()
        {
            return this.name();
        }
    }

    public static enum pragmatic_status
    {
        FIGURATIVE, LITERAL, UNKNOWN ;
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
    @Column(name="ConceptId")
    protected Long Id;

    @Column(name="Name")
    //@Size(min = 5, max = 14)
    @NotNull(message="Concept name must be specified.")
    String Name;

    @Column(name="Type")
    @NotNull(message="Concept type must be specified.")
    @Enumerated(EnumType.STRING)
    protected type ConceptType;

    @Column(name="SpecificityLevel")
    @NotNull(message="Specificity level must be specified.")
    @Enumerated(EnumType.STRING)
    protected specificity_level SpecificityLevel;

    @Column(name="Status")
    @NotNull(message="Concept status must be specified.")
    @Enumerated(EnumType.STRING)
    protected status Status;
    
    @Column(name="UniqueInstance")
    @NotNull(message="Concept unique instance must be specified.")
    @Enumerated(EnumType.STRING)
    protected unique_instance UniqueInstance;

    @Column(name="PragmaticStatus")
    @NotNull(message="Concept pragmatic status must be specified.")
    @Enumerated(EnumType.STRING)
    protected pragmatic_status PragmaticStatus;

    @Column(name="Source")
    protected String Source;

    @Column(name="Comment")
    protected String Comment;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name="Concept_LanguageRepresentation",
        joinColumns={@JoinColumn(name="LanguageRepresentationId")},
        inverseJoinColumns={@JoinColumn(name="ConceptId")}
    )
    private List<LanguageRepresentation> LanguageRepresentations;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "Concept")
    private List<VisualRepresentation> VisualRepresentations;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "Concept")
    private List<MotoricRepresentation> MotoricRepresentations;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "Concept")
    private List<IntersectionOfRelationChains> IntersectionsOfRelationChains;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "Object")
    private List<Relation> RelationsContainingConceptAsObject; //Relations that have "this" concept as Object.

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "Subject")
    private List<Relation> RelationsContainingConceptAsSubject; //Relations that have "this" concept as Subject.


    // Public Constructor
    public Concept()
    {
        Name = null;
        Comment = "";
        SpecificityLevel = Concept.specificity_level.UNKNOWN;
        LanguageRepresentations =  new ArrayList<LanguageRepresentation>();
        VisualRepresentations = new ArrayList<VisualRepresentation>();
        MotoricRepresentations = new ArrayList<MotoricRepresentation>();
        RelationsContainingConceptAsObject =  new ArrayList<Relation>();
        RelationsContainingConceptAsSubject =  new ArrayList<Relation>();
        IntersectionsOfRelationChains = new ArrayList<IntersectionOfRelationChains>();
    }


    private Concept(Concept newConcept)
    {
        this.Name = newConcept.Name;
        this.ConceptType = newConcept.getConceptType();
        this.SpecificityLevel = newConcept.getSpecificityLevel();
        this.PragmaticStatus = newConcept.getPragmaticStatus();
        this.Status = newConcept.getStatus();
        LanguageRepresentations = new ArrayList<LanguageRepresentation>();
        VisualRepresentations = new ArrayList<VisualRepresentation>();
        MotoricRepresentations = new ArrayList<MotoricRepresentation>();
        RelationsContainingConceptAsObject =  new ArrayList<Relation>();
        IntersectionsOfRelationChains = new ArrayList<IntersectionOfRelationChains>();

    
        for(int i = 0; i < newConcept.getLanguageRepresentations().size(); i++)
        {
            if (!this.getLanguageRepresentations().contains(newConcept.getLanguageRepresentations().get(i)))
            {
                newConcept.getLanguageRepresentations().get(i).getConcepts().remove(newConcept);
                this.getLanguageRepresentations().add(newConcept.getLanguageRepresentations().get(i));
            }
        }

        for(int i = 0; i < newConcept.getVisualRepresentationsEntries().size(); i++)
        {
            if (!this.getVisualRepresentationsEntries().contains(newConcept.getVisualRepresentationsEntries().get(i)))
            {
                // newConcept.getVisualRepresentationsEntries().get(i).setOwner(this); //Not needed since VisualRepresentationGroup is out
                this.getVisualRepresentationsEntries().add(newConcept.getVisualRepresentationsEntries().get(i));
            }
        }

        for(int i = 0; i < newConcept.getMotoricRepresentations().size(); i++)
        {
            if (!this.getMotoricRepresentations().contains(newConcept.getMotoricRepresentations().get(i)))
            {
                // newConcept.getMotoricRepresentations().get(i).setOwner(this); //Not needed since MotoricRepresentationGroup is out
                this.getMotoricRepresentations().add(newConcept.getMotoricRepresentations().get(i));
            }
        }

        for(int i = 0; i < newConcept.getRelationsContainingConceptAsObject().size(); i++)
        {
            if (!this.getRelationsContainingConceptAsObject().contains(newConcept.getRelationsContainingConceptAsObject().get(i)))
            {
                if (newConcept.getRelationsContainingConceptAsObject().get(i).getObject().equals(newConcept))
                {
                    newConcept.getRelationsContainingConceptAsObject().get(i).setObject(this);
                }
                else
                {
                    newConcept.getRelationsContainingConceptAsObject().get(i).setSubject(this);
                }
                this.getRelationsContainingConceptAsObject().add(newConcept.getRelationsContainingConceptAsObject().get(i));
            }
        }

        for(int i = 0; i < newConcept.getIntersectionsOfRelationChains().size(); i++)
        {
            if (!this.getIntersectionsOfRelationChains().contains(newConcept.getIntersectionsOfRelationChains().get(i)))
            {
                newConcept.getIntersectionsOfRelationChains().get(i).setConcept(this);
                this.getIntersectionsOfRelationChains().add(newConcept.getIntersectionsOfRelationChains().get(i));
            }
        }
    }


    /**
     * @xmlcomments.args
     *	   xmltag="&lt;intersection_of_relation_chains&gt;"
     *     xmldescription="This tag defines the interesections
     *     of relation chains this concept participates in"
     */
    @XmlElement(name="intersection_of_relation_chains")
    public List<IntersectionOfRelationChains> getRelations() {
        return IntersectionsOfRelationChains;
    }


    @XmlTransient
    public final List<Relation> getRelationsContainingConceptAsObject()
    {
        return RelationsContainingConceptAsObject;
    }

    public void setRelationsContainingConceptAsObject(List<Relation> objOfRelations)
    {
        this.RelationsContainingConceptAsObject = objOfRelations;
    }


        /**
     * @xmlcomments.args
     *	   xmltag="&lt;language_representation&gt;"
     *     xmldescription="This tag defines the Language Representation of the
     *     concept"
     */
    @XmlElement(name="language_representation")
    public final List<LanguageRepresentation> getLanguageRepresentations()
    {
        return LanguageRepresentations;
    }

    public List<LanguageRepresentation> getLanguageRepresentationsEntries()
    {
        List<LanguageRepresentation> language_representation_entries = new ArrayList<LanguageRepresentation>();
        for (int i=0; i<this.LanguageRepresentations.size(); i++)
        {
            language_representation_entries.add(this.LanguageRepresentations.get(i));
        }
        return language_representation_entries;
    }

    public void addLanguageRepresentation(LanguageRepresentation language_representation)
    {
        this.LanguageRepresentations.add(language_representation);
    }

    public void setLanguageRepresentation(List<LanguageRepresentation> language_representations)
    {
        this.LanguageRepresentations = language_representations;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;mr&gt;"
     *     xmldescription="This tag defines the motoric representation"
     */
    @XmlElement(name="mr")
    public final List<MotoricRepresentation> getMotoricRepresentations()
    {
        return MotoricRepresentations;
    }

    public List<MotoricRepresentation> getMotoricRepresentationsEntries()
    {
        List<MotoricRepresentation> motoric_representation_entries = new ArrayList<MotoricRepresentation>();
        for(int i=0; i<this.MotoricRepresentations.size(); i++)
        {
            motoric_representation_entries.add(this.MotoricRepresentations.get(i));
        }
        return motoric_representation_entries;
    }

    public void setMotoricRepresentations(List<MotoricRepresentation> motoric_representations)
    {
        this.MotoricRepresentations = motoric_representations;
    }

    public void addMotoricRepresentation(MotoricRepresentation motoric_representation)
    {
        this.MotoricRepresentations.add(motoric_representation);
    }


    /**
     * @xmlcomments.args
     *	   xmltag="&lt;vr&gt;"
     *     xmldescription="This tag defines the Visual Representation of the
     *     concept"
     */
    @XmlElement(name="vr")
    public List<VisualRepresentation> getVisualRepresentations()
    {
        return VisualRepresentations;
    }

    public void addVisualRepresentation(VisualRepresentation visual_representation)
    {
        this.VisualRepresentations.add(visual_representation);
    }

    public void setVisualRepresentation(List<VisualRepresentation> visual_representations)
    {
        this.VisualRepresentations = visual_representations;
    }
    
    public final List<VisualRepresentation> getVisualRepresentationsEntries()
    {
        List<VisualRepresentation> visual_representation_entries = new ArrayList<VisualRepresentation>();
        for(int i=0; i<this.VisualRepresentations.size(); i++)
        {
            visual_representation_entries.add(this.VisualRepresentations.get(i));
        }
        return visual_representation_entries;
    }


    /**
     * Gets text of the first language representation of language "en" for this concept
     * @return a string
     */
    public String getLanguageRepresentationName()
    {
        List<LanguageRepresentation> les = this.getLanguageRepresentations();
        for(int i = 0; i < les.size(); i++)
        {
            if (les.get(i).getLanguage().name().equalsIgnoreCase("en"))
            {
                return les.get(i).getText();
            }
        }
        if (les.size() > 0)
        {
            return les.get(0).getText();
        }
        return "noname";
    }


    /**
     * @xmlcomments.args
     *	   xmltag="&lt;source&gt;"
     *     xmldescription="This tag defines the source of the concept (from which
     *          resources was generated (for example: Wordnet)"
     */
    @XmlElement(name="source")
    public String getSource()
    {
        return Source;
    }

    public void setSource(String source)
    {
        this.Source = source;
    }

        /**
     * @xmlcomments.args
     *	   xmltag="&lt;unique_instance&gt;"
     *     xmldescription="This tag defines the source of the concept (from which
     *          resources was generated (for example: Wordnet)"
     */
    @XmlElement(name="unique_instance")
    public unique_instance getUniqueInstance()
    {
        return UniqueInstance;
    }

    public void setUniqueInstance(unique_instance unique_instance)
    {
        this.UniqueInstance = unique_instance;
    }

    @XmlElement(name="comment")
    public String getComment()
    {
        return Comment;
    }

    public void setComment(String comment)
    {
        this.Comment = comment;
    }

    //
    /**
     * @xmlcomments.args
     *	   xmltag="&lt;intersection_of_relation_chains&gt;"
     *     xmldescription="This tag defines the intersection of relation chains
     *     this concept participates in"
     */
    @XmlElement(name="intersection_of_relation_chains")
    public final List<IntersectionOfRelationChains> getIntersectionsOfRelationChains()
    {
        return IntersectionsOfRelationChains;
    }


    /**
     * @xmlcomments.args
     *	   xmltag="&lt;specificity_level&gt;"
     *     xmldescription="This tag defines the specificity level of the concept"
     */
    @XmlElement(name="specificity_level")
    public specificity_level getSpecificityLevel()
    {
        return SpecificityLevel;
    }

    public void setSpecificityLevel(specificity_level level_type)
    {
        this.SpecificityLevel = level_type;
    }

    public void setSpecificityLevel(String level_type)
    {
        if(level_type.equalsIgnoreCase("BASIC_LEVEL"))
        {
            this.SpecificityLevel = Concept.specificity_level.BASIC_LEVEL;
        }
        else if(level_type.equalsIgnoreCase("SUPERORDINATE"))
        {
            this.SpecificityLevel = Concept.specificity_level.SUPERORDINATE;
        }
        else if(level_type.equalsIgnoreCase("SUBORDINATE"))
        {
            this.SpecificityLevel = Concept.specificity_level.SUBORDINATE;
        }
        else
        {
            this.SpecificityLevel = Concept.specificity_level.UNKNOWN;
        }
    }

    /**
     * Gets a string of concatenated full info for the concept.
     * concept type, status, pragmatic status, basic level, description
     * @return a string
     */
    public String getInfo()
    {
        StringBuilder sb= new StringBuilder();
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
     * Gets a string of concatenated short info for the concept.
     * concept type and basic level
     * @return a string
     */
    public String getInfoShort()
    {
        StringBuilder sb= new StringBuilder();
        sb.append(this.getConceptType());
        sb.append("#");
        sb.append(this.getSpecificityLevel());
        return sb.toString();
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;concept_type&gt;"
     *     xmldescription="This tag defines the type of the concept entity
     *     (abstract, entity, feature, movement, unknown)"
     */
    @XmlElement(name="concept_type")
    public type getConceptType()
    {
        return ConceptType;
    }

    public void setConceptType(type concept_type)
    {
        this.ConceptType = concept_type;
    }

    public void setConceptType(String concept_type)
    {
        this.ConceptType = type.valueOf(concept_type.trim().toUpperCase());
    }


    /**
     * @xmlcomments.args
     *	   xmltag="&lt;pragmatic_status&gt;"
     *     xmldescription="This tag defines if the entity is literal or figurative"
     */
    @XmlElement(name="pragmatic_status")
    public pragmatic_status getPragmaticStatus()
    {
        return PragmaticStatus;
    }

    public void setPragmaticStatus(pragmatic_status pragmatic_status)
    {
        this.PragmaticStatus = pragmatic_status;
    }

    public void setPragmaticStatus(String pragmatic_status)
    {
        String tmp = pragmatic_status;
        tmp = tmp.replace(".", "_");
        tmp = tmp.replace(":", "_");
        // TODO: Check below if it returns the correct value.
        this.PragmaticStatus = PragmaticStatus.valueOf(tmp.trim().toUpperCase());
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;status&gt;"
     *     xmldescription="This tag defines if the entity is a variable,
     *                     a constant or a template"
     */
    @XmlElement(name="status")
    //@ConstantConcepts(value=status.CONSTANT)
    public status getStatus()
    {
        return Status;
    }

    //@ConstantConcepts
    public void setStatus(status var_type)
    {
        this.Status = var_type;
    }

    //@ConstantConcepts
    public void setStatus(String var_type)
    {
        this.Status = status.valueOf(var_type.trim().toUpperCase());
    }

    /**
     * Adds a new intersection of relation chains to this concept
     * created using given relation types (fw+bw) and given relation objects
     * @param rTypeForward list of forward types of relations
     * @param rTypeBackward list of backward types of relations
     * @param obj list of concepts to be used as objects
     */
    public void addRelation(List<String> rTypeForward, List<String> rTypeBackward, List<Concept> obj)
    {
        IntersectionOfRelationChains inter = new IntersectionOfRelationChains();
        for (int i = 0; i < rTypeForward.size(); i++)
        {
            RelationType rType = new RelationType();
            rType.setForwardName(rTypeForward.get(i));
            rType.setBackwardName(rTypeBackward.get(i));
            Relation rel = new Relation();
            rel.setType(rType);
            rel.setSubject(this);
            rel.setObject(obj.get(i));
            RelationChain rChain = new RelationChain();
            rChain.addRelation(rel, 0);
            inter.addRelationChain(rChain);
        }
        this.addIntersectionOfRelationChains(inter);
    }

    public void addIntersectionOfRelationChains(IntersectionOfRelationChains intersection)
    {
        intersection.setConcept(this);
        this.IntersectionsOfRelationChains.add(intersection);
    }

    /**
     * @xmlcomments.args
     *	   xmltag="name"
     *     xmldescription="This attribute defines the name of the element"
     */
    @XmlAttribute()
    public String getName()
    {
        if (Name!=null)
        {
            return Name;
        }
        else
            return Id+"";
    }

    public String getNameNoNumbers()
    {
        if (Name!=null)
        {
            return Name.replaceAll("%\\d+:\\d+:\\d+:\\d*:\\d*", "");
        }
        else
            return Id+"";
    }

    public void setName(String name)
    {
        this.Name = name.trim();
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
        if (!(object instanceof Concept))
        {
            return false;
        }
        Concept other = (Concept) object;
        if (this.Name !=null && other.Name !=null && this.Name.equalsIgnoreCase(other.Name))
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
        if(Name != null && !Name.equalsIgnoreCase(""))
        {
            return Name;// + " (Entity)";
        }
        else
        {
            List <LanguageRepresentation> tmpList = this.getLanguageRepresentationsEntries();
            if (tmpList.size()>0)
            {
                StringBuilder tmp = new StringBuilder(tmpList.get(0).getText());
                for (int i = 1; i< tmpList.size(); i++)
                {
                    tmp.append("\\").append(tmpList.get(i).getText());
                }
                return tmp.toString();
            }
            else
            {
                return Id + "";
            }
        }
    }

    public void afterUnmarshal(Unmarshaller u, Object parent) {

        if (Globals.ToMergeAfterUnMarshalling)
        {
            ConceptDao cDao = new ConceptDaoImpl();
            Concept tmp = cDao.getConceptWithNameOrID(this.getName());
            if (tmp == null)
            {
                if (this.ConceptType == null)
                {
                    this.ConceptType = type.UNKNOWN;
                }

                cDao.merge(this);
            }
            else
            {
                cDao.update(this);
            }
        }
        else
        {
            Concept tmp = (Concept)Constants.globalConcepts.get(this.getName());
            if (tmp == null)
            {
                if (this.ConceptType == null)
                {
                    this.ConceptType = type.UNKNOWN;
                }
                tmp = new Concept(this);
                Constants.globalConcepts.put(tmp.getName(), tmp);
            }
            else
            {
                tmp.ConceptType = this.ConceptType;
                updateLanguageRepresentations(tmp);
                updateVisualRepresentations(tmp);
                updateMotoricRepresentations(tmp);
                updateObjOfRelations(tmp);
                updateRelations(tmp);
              }
        }

        System.err.println("Finish unmarshalling: " + this.getName());
    }

    /**
     * Updates LanguageRepresentation of a concept using this concept LanguageRepresentation
     * @param oldCon the concept to be updated
     */
    public void updateLanguageRepresentations(Concept oldCon)
    {
        for (int i = 0; i < this.getLanguageRepresentationsEntries().size(); i++)
        {
            if (!oldCon.getLanguageRepresentationsEntries().contains(this.getLanguageRepresentationsEntries().get(i)))
            {
                this.getLanguageRepresentationsEntries().get(i).getConcepts().remove(this);
                this.getLanguageRepresentationsEntries().get(i).getConcepts().add(this);
                oldCon.getLanguageRepresentationsEntries().add(this.getLanguageRepresentationsEntries().get(i));
            }
        }
    }

    /**
     * Updates MotoricRepresentations of a concept using this concept MotoricRepresentations
     * @param oldCon the concept to be updated
     */
    public void updateMotoricRepresentations(Concept oldCon)
    {
        for (int i = 0; i < this.getMotoricRepresentations().size(); i++)
        {
            if (!oldCon.getMotoricRepresentations().contains(this.getMotoricRepresentations().get(i)))
            {
                this.getMotoricRepresentations().get(i);
                oldCon.getMotoricRepresentations().add(this.getMotoricRepresentations().get(i));
            }
        }
    }

    /**
     * Updates VisualRepresentations of a concept using this concept VisualRepresentations
     * @param oldCon the concept to be updated
     */
    public void updateVisualRepresentations(Concept oldCon)
    {
        for (int i = 0; i < this.getVisualRepresentations().size(); i++)
        {
            if (!oldCon.getVisualRepresentations().contains(this.getVisualRepresentations().get(i)))
            {
                this.getVisualRepresentations().get(i);
                oldCon.getVisualRepresentations().add(this.getVisualRepresentations().get(i));
            }
        }
    }

    /**
     * Updates object relations of a concept using this concept object relations
     * @param oldCon the concept to be updated
     */
    public void updateObjOfRelations(Concept oldCon)
    {
        for (int i = 0; i < this.getRelationsContainingConceptAsObject().size(); i++)
        {
            if (!oldCon.getRelationsContainingConceptAsObject().contains(this.getRelationsContainingConceptAsObject().get(i)))
            {
                if (this.getRelationsContainingConceptAsObject().get(i).getObject().equals(this))
                {
                    this.getRelationsContainingConceptAsObject().get(i).setObject(oldCon);
                }
                else
                {
                    this.getRelationsContainingConceptAsObject().get(i).setSubject(oldCon);
                }
                oldCon.getRelationsContainingConceptAsObject().add(this.getRelationsContainingConceptAsObject().get(i));
            }
        }
    }

    /**
     * Updates relations of a concept using this concept relations
     * @param oldCon the concept to be updated
     */
    public void updateRelations(Concept oldCon)
    {
        for (int i = 0; i < this.getIntersectionsOfRelationChains().size(); i++)
        {
            if (!oldCon.getIntersectionsOfRelationChains().contains(this.getIntersectionsOfRelationChains().get(i)))
            {
                this.getIntersectionsOfRelationChains().get(i).setConcept(oldCon);
                oldCon.getIntersectionsOfRelationChains().add(this.getIntersectionsOfRelationChains().get(i));
            }
        }
    }
}
