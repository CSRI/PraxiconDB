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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author dmavroeidis
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "relation_type", namespace = "http://www.csri.gr/relation_type")
@Entity
@Table(name = "RelationTypes", indexes = {
    @Index(columnList = "RelationTypeId")})
public class RelationType implements Serializable {

    //public static relation_name[] Bequethed = {relation_name_forward.HAS_PART, relation_name_backward.PART_OF};
    public static enum relation_name_forward {

        ACTION_AGENT, ACTION_GOAL, ACTION_OBJECT, ACTION_RESULT, ACTION_TOOL,
        ASPECT_CONCEPT, COMPARED_WITH, ENABLES, HAS_ANTHROPOGENIC_EFFECT,
        HAS_COLOUR, HAS_CONDITION, HAS_CONTENT, HAS_DEPTH, HAS_FORCE,
        HAS_HEIGHT, HAS_HUE, HAS_INSTANCE, HAS_INTENSITY, HAS_LENGTH,
        HAS_LOCATION, HAS_LUMINANCE, HAS_MATERIAL, HAS_MEASUREMENT_UNIT,
        HAS_MEASUREMENT_VALUE, HAS_MEMBER, HAS_NATURAL_EFFECT, HAS_PART,
        HAS_PARTIAL_INSTANCE, HAS_SHAPE, HAS_SIZE, HAS_SPEED_RATE, HAS_STEP,
        HAS_TEMPERATURE, HAS_TEXTURE, HAS_TIME_PERIOD, HAS_VISUAL_PATTERN,
        HAS_VOLUME, HAS_WEIGHT, HAS_WIDTH, LESS, METAPHOR_OF, MORE, PRODUCER_OF,
        TYPE_TOKEN, HAS_VALUE;

        @Override
        public String toString() {
            return this.name();
        }
    }

    public static enum relation_name_backward {

        AGENT_ACTION, GOAL_ACTION, OBJECT_ACTION, RESULT_ACTION, TOOL_ACTION,
        CONCEPT_ASPECT, ENABLED_BY, ANTHROPOGENIC_EFFECT_OF, COLOUR_OF,
        CONDITION_OF, CONTENT_OF, DEPTH_OF, FORCE_OF, HEIGHT_OF, HUE_OF,
        INSTANCE_OF, INTENSITY_OF, LENGTH_OF, LOCATION_OF, LUMINANCE_OF,
        MATERIAL_OF, MEASUREMENT_UNIT_OF, MEASUREMENT_VALUE_OF, MEMBER_OF,
        NATURAL_EFFECT_OF, PART_OF, PARTIAL_INSTANCE_OF, SHAPE_OF, SIZE_OF,
        SPEED_RATE_OF, STEP_OF, TEMPERATURE_OF, TEXTURE_OF, TIME_PERIOD_OF,
        VISUAL_PATTERN_OF, VOLUME_OF, WEIGHT_OF, WIDTH_OF, NO, METAPHOR_AS,
        PRODUCT_OF, TOKEN_TYPE, VALUE_OF;

        @Override
        public String toString() {
            return this.name();
        }
    }

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "CUST_SEQ", allocationSize = 1)
    @Column(name = "RelationTypeId")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CUST_SEQ")
    private Long id;

    @Column(name = "ForwardName")
    @Enumerated(EnumType.STRING)
    private relation_name_forward forwardName;

    @Column(name = "BackwardName")
    @Enumerated(EnumType.STRING)
    private relation_name_backward backwardName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "type")
    private List<Relation> relations;

    //@Transient
    private String xmlRelationForward;

    //@Transient
    private String xmlRelationBackward;

    @XmlTransient
    public String getXmlRelationForward() {
        return xmlRelationForward;
    }

    public void setXmlRelationForward(String xmlRelation) {
        this.xmlRelationForward = xmlRelation;
    }

    @XmlTransient
    public String getXmlRelationBackward() {
        return xmlRelationBackward;
    }

    public void setXmlRelationBackward(String xmlRelation) {
        this.xmlRelationBackward = xmlRelation;
    }

    public static List<RelationType> getLocationRelations() {
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

    public RelationType() {
        relations = new ArrayList<>();
    }

    public RelationType(String forward_name, String backward_name) {
        relations = new ArrayList<>();
        this.setForwardName(forward_name);
        this.setBackwardName(backward_name);
    }

    public RelationType(relation_name_forward forward_name,
            relation_name_backward backward_name) {
        relations = new ArrayList<>();
        this.forwardName = forward_name;
        this.backwardName = backward_name;
    }

    public relation_name_forward getForwardName() {
        return forwardName;
    }

    public void setForwardName(relation_name_forward name) {
        this.forwardName = name;
    }

    /**
     * Enforces the system to use as a relation type the given string (even if
     * it is not a relation_name). This isn't stored in the db
     *
     * @param name The forward name of the relation
     */
    public void setForwardNameString(String name) {
        String tmp = name.replaceAll("-", "_");
        try {
            this.forwardName = relation_name_forward.valueOf(tmp.toUpperCase());
        } catch (Exception e) {
            this.xmlRelationForward = name;
        }
    }

    public final void setForwardName(String name) {
        this.forwardName = relation_name_forward.valueOf(name.toUpperCase());
    }

    /**
     * @return the forward name.
     * @xmlcomments.args xmltag="&lt;left-to-right_name&gt;" xmldescription="The
     * name of the relation when the relational triplet is used from left to
     * right"
     */
    //@XmlAttribute(name = "left-to-right_name")
    public String getForwardNameString() {
        if (forwardName != null && !forwardName.name().equalsIgnoreCase("")) {
            return forwardName.name();
        } else {
            return this.xmlRelationForward;
        }
    }

    public relation_name_backward getBackwardName() {
        return backwardName;
    }

    public void setBackwardName(relation_name_backward backwardName) {
        this.backwardName = backwardName;
    }

    public final void setBackwardName(String name) {
        this.backwardName = relation_name_backward.valueOf(name.toUpperCase());
    }

    /**
     * Enforces the system to use as a relation type the given string (even if
     * it is not a relation_name. This isn't stored in the db
     *
     * @param name The backward name of the relation
     */
    public void setBackwardNameString(String name) {
        String tmp = name.replaceAll("-", "_");
        try {
            this.backwardName =
                    relation_name_backward.valueOf(tmp.toUpperCase());
        } catch (Exception e) {
            this.xmlRelationBackward = name;
        }
    }

    /**
     * @return the backward name.
     * @xmlcomments.args xmltag="&lt;left-to-right_name&gt;" xmldescription="The
     * name of the relation when the relational triplet is used from left to
     * right"
     */
    //@XmlAttribute(name = "right-to-left_name")
    public String getBackwardNameString() {
        if (backwardName != null && !backwardName.name().equalsIgnoreCase("")) {
            return backwardName.name();
        } else {
            return this.xmlRelationBackward;
        }
    }

    @XmlTransient
    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    public void addRelation(Relation relation) {
        relation.setType(this);
        this.relations.add(relation);
    }

    /**
     *
     * @return the id of the relation type.
     */
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
        if (!(object instanceof RelationType)) {
            return false;
        }
        RelationType other = (RelationType)object;
        if (this.forwardName != null && other.forwardName != null &&
                this.forwardName.name().equalsIgnoreCase(
                        other.forwardName.name()) &&
                this.backwardName != null &&
                other.backwardName != null &&
                this.backwardName.name().
                equalsIgnoreCase(other.backwardName.name())) {
            return true;
        }
        if (this.forwardName != null && other.backwardName != null &&
                this.forwardName.name().
                equalsIgnoreCase(other.backwardName.name()) &&
                this.backwardName != null && other.forwardName != null &&
                this.backwardName.name().equalsIgnoreCase(
                        other.forwardName.name())) {
            return true;
        }

        String equalsRelations[] = {"has_colour#has_color#has_hue#" +
            "has_luminance#has_intensity#has_colour#has_color#has_hue#" +
            "has_luminance#has_intensity",
            "has_condition#has_natural_effect#has_anthropogenic_effect#" +
            "has_condition#has_natural_effect#has_anthropogenic_effect",
            "has_size#has_length#has_height#has_width#has_depth#has_size#" +
            "has_length#has_height#has_width#has_depth"
        };

        for (String equalsRelation : equalsRelations) {
            if (this.forwardName != null && other.forwardName != null &&
                    this.backwardName != null &&
                    other.backwardName != null) {
                if ((equalsRelation.contains(this.forwardName.name()) &&
                        (equalsRelation.contains(other.forwardName.name()) ||
                        equalsRelation.contains(other.backwardName.name()))) ||
                        (equalsRelation.contains(this.backwardName.name()) &&
                        (equalsRelation.contains(other.forwardName.name()) ||
                        equalsRelation.contains(other.backwardName.name())))) {
                    return true;
                }
            }
        }

        if ((this.id == null && other.id != null) ||
                (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        if (this.id == null && other.id == null) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "gr.csri.poeticon.praxicon.db.entities.RelationType[Id=" + id +
                "]";
    }

}
