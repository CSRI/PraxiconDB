/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.entities;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
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
@Table(name="RelationTypes")
public class RelationType implements Serializable
{

    public static relation_name[] Bequethed = {relation_name.HAS_PART, relation_name.PART_OF};

    public static enum relation_name
    {
        ACTION_AGENT, ACTION_GOAL, ACTION_OBJECT, ACTION_RESULT,
        ACTION_TOOL, ASPECT_CONCEPT, COMPARED_WITH, ENABLES,
        HAS_ANTHROPOGENIC_EFFECT, HAS_COLOUR, HAS_CONDITION, HAS_CONTENT,
        HAS_DEPTH, HAS_FORCE, HAS_HEIGHT, HAS_HUE,
        HAS_INSTANCE, HAS_INTENSITY, HAS_LENGTH, HAS_LOCATION,
        HAS_LUMINANCE, HAS_MATERIAL, HAS_MEASUREMENT_UNIT, HAS_MEASUREMENT_VALUE,
        HAS_MEMBER, HAS_NATURAL_EFFECT, HAS_PART, HAS_PARTIAL_INSTANCE,
        HAS_SHAPE, HAS_SIZE, HAS_SPEED_RATE, HAS_STEP,
        HAS_TEMPERATURE, HAS_TEXTURE, HAS_TIME_PERIOD, HAS_VISUAL_PATTERN,
        HAS_VOLUME, HAS_WEIGHT, HAS_WIDTH, LESS,
        METAPHOR_OF, MORE, PRODUCER_OF, TYPE_TOKEN, HAS_VALUE,

        AGENT_ACTION, GOAL_ACTION, OBJECT_ACTION, RESULT_ACTION,
        TOOL_ACTION, CONCEPT_ASPECT, ENABLED_BY,
        ANTHROPOGENIC_EFFECT_OF, COLOUR_OF, CONDITION_OF, CONTENT_OF,
        DEPTH_OF, FORCE_OF, HEIGHT_OF, HUE_OF,
        INSTANCE_OF, INTENSITY_OF, LENGTH_OF, LOCATION_OF,
        LUMINANCE_OF, MATERIAL_OF, MEASUREMENT_UNIT_OF, MEASUREMENT_VALUE_OF,
        MEMBER_OF, NATURAL_EFFECT_OF, PART_OF, PARTIAL_INSTANCE_OF,
        SHAPE_OF, SIZE_OF, SPEED_RATE_OF, STEP_OF,
        TEMPERATURE_OF, TEXTURE_OF, TIME_PERIOD_OF, VISUAL_PATTERN_OF,
        VOLUME_OF, WEIGHT_OF, WIDTH_OF, NO,
        METAPHOR_AS, PRODUCT_OF, TOKEN_TYPE, VALUE_OF;

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
    private Long Id;

    @Column(name="ForwardName")
    @Enumerated(EnumType.STRING)
    relation_name ForwardName;

    @Column(name="BackwardName")
    @Enumerated(EnumType.STRING)
    relation_name BackwardName;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="Type")
    List<Relation> Relations;

    @Transient
    String XmlRelationForward;

    @Transient
    String XmlRelationBackward;

    @XmlTransient
    public String getXmlRelationForward()
    {
        return XmlRelationForward;
    }

    public void setXmlRelationForward(String xmlRelation)
    {
        this.XmlRelationForward = xmlRelation;
    }

    @XmlTransient
    public String getXmlRelationBackward()
    {
        return XmlRelationBackward;
    }

    public void setXmlRelationBackward(String xmlRelation)
    {
        this.XmlRelationBackward = xmlRelation;
    }

    public static List<RelationType> getLocationRelations()
    {
        List<RelationType> res = new ArrayList<>();
        RelationType tmp = new RelationType();
        tmp.setForwardName("action_location");
        tmp.setBackwardName("location_action");
        res.add(tmp);

        tmp = new RelationType();
        tmp.setForwardName("place_event");
        tmp.setBackwardName("event_place");
        res.add(tmp);

        tmp = new RelationType();
        tmp.setForwardName("place_institution");
        tmp.setBackwardName("institution_place");
        res.add(tmp);

        tmp = new RelationType();
        tmp.setForwardName("place_people");
        tmp.setBackwardName("people_place");
        res.add(tmp);

        tmp = new RelationType();
        tmp.setForwardName("entity_location");
        tmp.setBackwardName("location_entity");
        res.add(tmp);

        tmp = new RelationType();
        tmp.setForwardName("has_location");
        tmp.setBackwardName("location_of");
        res.add(tmp);

        tmp = new RelationType();
        tmp.setForwardName("has_direction");
        tmp.setBackwardName("direction_of");
        res.add(tmp);

        return res;
    }

    public RelationType()
    {
        Relations = new ArrayList<>();
    }

    public RelationType(String forward_name, String backward_name)
    {
        Relations = new ArrayList<>();
        this.setForwardName(forward_name);
        this.setBackwardName(backward_name);
    }

    public RelationType(relation_name forward_name, relation_name backward_name)
    {
        Relations = new ArrayList<>();
        this.ForwardName = forward_name;
        this.BackwardName = backward_name;
    }
  
    public relation_name getForwardName()
    {
        return ForwardName;
    }

    public void setForwardName(relation_name name)
    {
        this.ForwardName = name;
    }


    /**
     * Enforces the system to use as a relation type the given string (even if it
     * is not a relation_name. This isn't stored in the db
     * @param name The forward name of the relation
     */
    public void setForwardNameString(String name)
    {
        String tmp = name.replaceAll("-", "_");
        try
        {
            this.ForwardName = relation_name.valueOf(tmp.toUpperCase());
        }
        catch(Exception e)
        {
            this.XmlRelationForward = name;
        }
    }

    public final void setForwardName(String name)
    {
        this.ForwardName = relation_name.valueOf(name.toUpperCase());
    }

    /**
     * @return the forward name.
     * @xmlcomments.args
     *	   xmltag="&lt;left-to-right_name&gt;"
     *     xmldescription="The name of the relation when the relational triplet
     *     is used from left to right"
     */
    @XmlAttribute(name="left-to-right_name")
    public String getForwardNameString()
    {
        if(ForwardName !=null && !ForwardName.name().equalsIgnoreCase(""))
        {
            return ForwardName.name();
        }
        else
        {
            return this.XmlRelationForward;
        }
    }

    
    public relation_name getBackwardName()
    {
        return BackwardName;
    }

    public void setBackwardName(relation_name backward_name)
    {
        this.BackwardName = backward_name;
    }

    public final void setBackwardName(String name)
    {
        this.BackwardName = relation_name.valueOf(name.toUpperCase());
    }

    /**
     * Enforces the system to use as a relation type the given string (even if it
     * is not a relation_name. This isn't stored in the db
     * @param name The backward name of the relation
     */
    public void setBackwardNameString(String name)
    {
        String tmp = name.replaceAll("-", "_");
        try
        {
            this.BackwardName = relation_name.valueOf(tmp.toUpperCase());
        }
        catch(Exception e)
        {
            this.XmlRelationBackward = name;
        }
    }

    /**
     * @return the backward name.
     * @xmlcomments.args
     *	   xmltag="&lt;left-to-right_name&gt;"
     *     xmldescription="The name of the relation when the relational triplet
     *     is used from left to right"
     */
    @XmlAttribute(name="right-to-left_name")
    public String getBackwardNameString()
    {
        if(BackwardName !=null && !BackwardName.name().equalsIgnoreCase(""))
        {
            return BackwardName.name();
        }
        else
        {
            return this.XmlRelationBackward;
        }
    }

    @XmlTransient
    public List<Relation> getRelations()
    {
        return Relations;
    }

    public void setRelations(List<Relation> Relations)
    {
        this.Relations = Relations;
    }

    public void addRelation(Relation relation)
    {
        relation.setType(this);
        this.Relations.add(relation);
    }

    /**
     *
     * @return the id of the relation type.
     */
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
        if (!(object instanceof RelationType))
        {
            return false;
        }
        RelationType other = (RelationType) object;
        if (this.ForwardName !=null && other.ForwardName !=null && this.ForwardName.name().equalsIgnoreCase(other.ForwardName.name()) &&
                this.BackwardName !=null && other.BackwardName !=null && this.BackwardName.name().equalsIgnoreCase(other.BackwardName.name()))
        {
            return true;
        }
        if (this.ForwardName !=null && other.BackwardName !=null && this.ForwardName.name().equalsIgnoreCase(other.BackwardName.name()) &&
                this.BackwardName !=null && other.ForwardName !=null && this.BackwardName.name().equalsIgnoreCase(other.ForwardName.name()))
        {
            return true;
        }

        String equalsRelations[] = {"has_colour#has_color#has_hue#has_luminance#has_intensity#has_colour#has_color#has_hue#has_luminance#has_intensity",
                                        "has_condition#has_natural_effect#has_anthropogenic_effect#has_condition#has_natural_effect#has_anthropogenic_effect",
                                        "has_size#has_length#has_height#has_width#has_depth#has_size#has_length#has_height#has_width#has_depth"
                                        };

        for (String equalsRelation : equalsRelations) {
            if (this.ForwardName !=null && other.ForwardName !=null &&
                    this.BackwardName !=null && other.BackwardName !=null) {
                if ((equalsRelation.contains(this.ForwardName.name()) && (equalsRelation.contains(other.ForwardName.name()) || equalsRelation.contains(other.BackwardName.name()))) || (equalsRelation.contains(this.BackwardName.name()) && (equalsRelation.contains(other.ForwardName.name()) || equalsRelation.contains(other.BackwardName.name())))) {
                    return true;
                }
            }
        }

        if ((this.Id == null && other.Id != null) || (this.Id != null && !this.Id.equals(other.Id)))
        {
            return false;
        }
        if (this.Id == null && other.Id == null)
        {
            return false;
        }

        return true;
    }

    @Override
    public String toString()
    {
        return "csri.poeticon.praxicon.db.entities.RelationType[Id=" + Id + "]";
    }

}
