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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author dmavroeidis
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "relation_type", namespace = "http://www.csri.gr/relation_type")
@Entity
@NamedQueries({
    @NamedQuery(name = "getRelationTypeEntityQuery", query =
            "SELECT e FROM RelationType e " +
            "WHERE e.forwardName = :forwardName " +
            "AND e.backwardName = :backwardName"),})
@Table(name = "RelationTypes", indexes = {
    @Index(columnList = "RelationTypeId")})
public class RelationType implements Serializable {

    public static enum RelationNameForward {

        ACTION_AGENT, ACTION_GOAL, ACTION_OBJECT, ACTION_RESULT, ACTION_TOOL,
        ASPECT_CONCEPT, COMPARED_WITH, ENABLES, HAS_ANTHROPOGENIC_EFFECT,
        HAS_COLOUR, HAS_CONDITION, HAS_CONTENT, HAS_DEPTH, HAS_FORCE,
        HAS_HEIGHT, HAS_HUE, HAS_MOTOR_PROGRAM, HAS_INSTANCE, HAS_INTENSITY,
        HAS_LENGTH, HAS_LOCATION, HAS_LUMINANCE, HAS_MATERIAL,
        HAS_MEASUREMENT_UNIT, HAS_MEASUREMENT_VALUE, HAS_NATURAL_EFFECT,
        HAS_PART, HAS_SHAPE, HAS_SIZE, HAS_SPEED_RATE, HAS_STEP,
        HAS_TEMPERATURE, HAS_TEXTURE, HAS_TIME_PERIOD, HAS_VISUAL_PATTERN,
        HAS_VOLUME, HAS_WEIGHT, HAS_WIDTH, LESS, MORE, TYPE_TOKEN, HAS_VALUE,
        HAS_MATERIAL_RESISTANCE, HAS_REGION, HAS_PARTIAL_INSTANCE,
        HAS_BASIC_LEVEL;

        @Override
        public String toString() {
            return this.name();
        }
    }

    public static enum RelationNameBackward {

        AGENT_ACTION, GOAL_ACTION, OBJECT_ACTION, RESULT_ACTION, TOOL_ACTION,
        CONCEPT_ASPECT, ENABLED_BY, ANTHROPOGENIC_EFFECT_OF, COLOUR_OF,
        CONDITION_OF, CONTENT_OF, DEPTH_OF, FORCE_OF, HEIGHT_OF, HUE_OF,
        MOTOR_PROGRAM_OF, INSTANCE_OF, INTENSITY_OF, LENGTH_OF, LOCATION_OF,
        LUMINANCE_OF, MATERIAL_OF, MEASUREMENT_UNIT_OF, MEASUREMENT_VALUE_OF,
        NATURAL_EFFECT_OF, PART_OF, SHAPE_OF, SIZE_OF, SPEED_RATE_OF, STEP_OF,
        TEMPERATURE_OF, TEXTURE_OF, TIME_PERIOD_OF, VISUAL_PATTERN_OF,
        VOLUME_OF, WEIGHT_OF, WIDTH_OF, NO, TOKEN_TYPE, VALUE_OF,
        MATERIAL_RESISTANCE_OF, REGION_OF, PARTIAL_INSTANCE_OF, BASIC_LEVEL_OF;

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
    private RelationNameForward forwardName;

    @Column(name = "BackwardName")
    @Enumerated(EnumType.STRING)
    private RelationNameBackward backwardName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "type")
    private List<Relation> relations;

    public RelationType() {
        relations = new ArrayList<>();
    }

    public RelationType(String forward_name, String backward_name) {
        relations = new ArrayList<>();
        this.setForwardName(forward_name);
        this.setBackwardName(backward_name);
    }

    public RelationType(RelationNameForward forward_name,
            RelationNameBackward backward_name) {
        relations = new ArrayList<>();
        this.forwardName = forward_name;
        this.backwardName = backward_name;
    }

    public RelationNameForward getForwardName() {
        return forwardName;
    }

    public void setForwardName(RelationNameForward name) {
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
            this.forwardName = RelationNameForward.valueOf(tmp.toUpperCase());
        } catch (Exception e) {

        }
    }

    public final void setForwardName(String name) {
        this.forwardName = RelationNameForward.valueOf(name.toUpperCase());
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
        }
        return null;
    }

    public RelationNameBackward getBackwardName() {
        return backwardName;
    }

    public void setBackwardName(RelationNameBackward backwardName) {
        this.backwardName = backwardName;
    }

    public final void setBackwardName(String name) {
        this.backwardName = RelationNameBackward.valueOf(name.toUpperCase());
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
                    RelationNameBackward.valueOf(tmp.toUpperCase());
        } catch (Exception e) {
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
        }
        return null;
    }

    //@XmlTransient
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
    //@XmlAttribute
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
