/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.logs;

import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.IntersectionOfRelationChains;
import csri.poeticon.praxicon.db.entities.Relation;
import csri.poeticon.praxicon.db.entities.RelationChain;
import csri.poeticon.praxicon.db.entities.RelationType;
//import csri.poeticon.praxicon.db.entities.UnionOfIntersections;
import csri.poeticon.praxicon.wrappers.ObjectsAroundWrapper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

/**
 *
 * @author Erevodifwntas
 */
@Entity
public class SentenceToAnalyze implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CUST_SEQLOG", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQLOG")
    @Column(name="SENTENCE_ID")
    private Long id;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    String coreConcept;

    public String getCoreConcept()
    {
        return coreConcept;
    }

    public void setCoreConcept(String coreConcept)
    {
        this.coreConcept = coreConcept;
    }

    @Column(name="SENTENCE")
    String sentence;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "owner")
    List<ConceptOfSentence> concepts;

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public SentenceToAnalyze()
    {
        this.concepts = new ArrayList<ConceptOfSentence>();
    }

    public List<ConceptOfSentence> getConcepts()
    {
        return concepts;
    }

    public void setConcepts(List<ConceptOfSentence> concepts)
    {
        this.concepts = concepts;
    }

    public String getSentence()
    {
        return sentence;
    }

    public void setSentence(String sentence)
    {
        this.sentence = sentence;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SentenceToAnalyze))
        {
            return false;
        }
        SentenceToAnalyze other = (SentenceToAnalyze) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return this.sentence;
    }

//    public Concept findCoreConcept()
//    {
//        Concept res = new Concept();
//        res.setId(-1L);
//        res.setName(this.coreConcept);
//
//        UnionOfIntersections union = new UnionOfIntersections();
//        union.setId(-1L);
//        IntersectionOfRelationChains inter = new IntersectionOfRelationChains();
//        inter.setId(-1L);
//        //goal
//        Concept g = null;
//        for(ConceptOfSentence cS: this.getConcepts())
//        {
//            if(cS.getRelationName().equalsIgnoreCase("ACTION_GOAL"))
//            {
//                g = new Concept();
//                g.setId(-2L);
//                g.setName(cS.getConceptName());
//
//            }
//        }
//        if(g!=null)
//        {
//            RelationChain rChain = new RelationChain();
//            Relation rel = new Relation();
//            RelationType goal = new RelationType();
//            goal.setForwardName(RelationType.RELATION_NAME.ACTION_GOAL);
//            goal.setBackwardName(RelationType.RELATION_NAME.GOAL_ACTION);
//            rel.setSubject(res);
//            rel.setObject(g);
//            rel.setType(goal);
//            rChain.addRelation(rel, 0);
//            inter.addRelationChain(rChain);
//
//
//            UnionOfIntersections unionC = new UnionOfIntersections();
//            unionC.setId(-1L);
//            IntersectionOfRelationChains interC = new IntersectionOfRelationChains();
//            interC.setId(-1L);
//            RelationChain rChainC = new RelationChain();
//            Relation relC = new Relation();
//            RelationType goalC = new RelationType();
//            goalC.setForwardName(RelationType.RELATION_NAME.GOAL_ACTION);
//            goalC.setBackwardName(RelationType.RELATION_NAME.ACTION_GOAL);
//            relC.setSubject(g);
//            relC.setObject(res);
//            relC.setType(goalC);
//            rChainC.addRelation(relC, 0);
//            interC.addRelationChain(rChainC);
//            unionC.addIntersection(interC);
//            g.addRelation(unionC);
//        }
//
//        //tool
//        Concept t = null;
//        for(ConceptOfSentence cS: this.getConcepts())
//        {
//            if(cS.getRelationName().equalsIgnoreCase("ACTION_TOOL"))
//            {
//                t = new Concept();
//                t.setId(-3L);
//                t.setName(cS.getConceptName());
//            }
//        }
//        if(t!=null)
//        {
//            RelationChain rChain = new RelationChain();
//            Relation rel = new Relation();
//            RelationType tool = new RelationType();
//            tool.setForwardName(RelationType.RELATION_NAME.ACTION_TOOL);
//            tool.setBackwardName(RelationType.RELATION_NAME.TOOL_ACTION);
//            rel.setSubject(res);
//            rel.setObject(t);
//            rel.setType(tool);
//            rChain.addRelation(rel, 0);
//            inter.addRelationChain(rChain);
//
//
//            UnionOfIntersections unionC = new UnionOfIntersections();
//            unionC.setId(-1L);
//            IntersectionOfRelationChains interC = new IntersectionOfRelationChains();
//            interC.setId(-1L);
//            RelationChain rChainC = new RelationChain();
//            Relation relC = new Relation();
//            RelationType goalC = new RelationType();
//            goalC.setForwardName(RelationType.RELATION_NAME.TOOL_ACTION);
//            goalC.setBackwardName(RelationType.RELATION_NAME.ACTION_TOOL);
//            relC.setSubject(t);
//            relC.setObject(res);
//            relC.setType(goalC);
//            rChainC.addRelation(relC, 0);
//            interC.addRelationChain(rChainC);
//            unionC.addIntersection(interC);
//            t.addRelation(unionC);
//        }
//
//
//        //object
//        Concept affectedObject = null;
//        for(ConceptOfSentence cS: this.getConcepts())
//        {
//            if(cS.getRelationName().equalsIgnoreCase("ACTION_OBJECT"))
//            {
//                affectedObject = new Concept();
//                affectedObject.setId(-4L);
//                affectedObject.setName(cS.getConceptName());
//            }
//        }
//        if(affectedObject!=null)
//        {
//            RelationChain rChain = new RelationChain();
//            Relation rel = new Relation();
//            RelationType obj = new RelationType();
//            obj.setForwardName(RelationType.RELATION_NAME.ACTION_OBJECT);
//            obj.setBackwardName(RelationType.RELATION_NAME.OBJECT_ACTION);
//            rel.setSubject(res);
//            rel.setObject(affectedObject);
//            rel.setType(obj);
//            rChain.addRelation(rel, 0);
//            inter.addRelationChain(rChain);
//
//            UnionOfIntersections unionC = new UnionOfIntersections();
//            unionC.setId(-1L);
//            IntersectionOfRelationChains interC = new IntersectionOfRelationChains();
//            interC.setId(-1L);
//            RelationChain rChainC = new RelationChain();
//            Relation relC = new Relation();
//            RelationType goalC = new RelationType();
//            goalC.setForwardName(RelationType.RELATION_NAME.OBJECT_ACTION);
//            goalC.setBackwardName(RelationType.RELATION_NAME.ACTION_OBJECT);
//            relC.setSubject(affectedObject);
//            relC.setObject(res);
//            relC.setType(goalC);
//            rChainC.addRelation(relC, 0);
//            interC.addRelationChain(rChainC);
//            unionC.addIntersection(interC);
//            affectedObject.addRelation(unionC);
//        }
//
//        union.addIntersection(inter);
//
//        res.addRelation(union);
//
//        return res;
//    }

    public Concept findVariable()
    {
        for (int i = 0; i < this.getConcepts().size(); i++)
        {
            Concept tmp = this.getConcepts().get(i).getActualConcept();
            if (tmp.getStatus() == Concept.status.VARIABLE &&
                    tmp.getConceptType() != Concept.type.MOVEMENT &&
                    this.getConcepts().get(i).getValues().size() > 0)
            {
                return tmp;
            }
        }

        return null;
    }

    public Concept findMiddleConcept()
    {
        if (this.getConcepts().size()>1)
        {
            Concept tmp = null;
            for(ConceptOfSentence c: this.getConcepts())
            {
                if(c.getConceptName().toLowerCase().indexOf("dummy")>=0)
                {
                    tmp = c.getActualConcept();
                    break;
                }
            }
            if(tmp==null)
            {
                int size = this.getConcepts().size()+1;
                int middle = size/2;
                tmp = this.getConcepts().get(middle).getActualConcept();
            }
            return tmp;
        }

        return null;
    }

    public String findSubs(Concept c)
    {
        StringBuilder sb= new StringBuilder();
        for (int i = 0; i < this.getConcepts().size(); i++)
        {
            if (this.getConcepts().get(i).getActualConcept().equals(c))
            {
                if (!this.getConcepts().get(i).isVariable)
                {
                    for (int j = 0; j < this.getConcepts().get(i).getSubstitutes().size(); j++)
                    {
                        sb.append(this.getConcepts().get(i).getSubstitutes().get(j).getActualConcept());
                        sb.append("\n");
                    }
                }
                else
                {
                    for (int k = 0; k < this.getConcepts().get(i).getValues().size(); k++)
                    {
                        sb.append("The variable value is: ").append(this.getConcepts().get(i).getValues().get(k).getVariableValue()).append("\n");
                        for (int j = 0; j < this.getConcepts().get(i).getValues().get(k).getSubstitutes().size(); j++)
                        {
                            sb.append(this.getConcepts().get(i).getValues().get(k).getSubstitutes().get(j).getActualConcept());
                            sb.append("\n");
                        }
                    }
                }
                break;
            }
            else
            {
                if (this.getConcepts().get(i).isVariable)
                {
                    for(int k = 0; k < this.getConcepts().get(i).getValues().size(); k++)
                    {
                        if (this.getConcepts().get(i).getValues().get(k).getActualVariableValue().equals(c))
                        {
                            for (int j = 0; j < this.getConcepts().get(i).getValues().get(k).getSubstitutes().size(); j++)
                            {
                                sb.append(this.getConcepts().get(i).getValues().get(k).getSubstitutes().get(j).getActualConcept());
                                sb.append("\n");
                            }
                        }
                        i = this.getConcepts().size() + 1;
                        break;
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * To be REFACTORED!!!
     * @param obj
     */
    public void unselectObjectSubtitution(ObjectsAroundWrapper obj)
    {
        for (int i =0; i < this.getConcepts().size(); i++)
        {
            if (!this.getConcepts().get(i).isIsVariable())
            {
                for (int j = 0; j < this.getConcepts().get(i).getSubstitutes().size(); j++)
                {
                    if (this.getConcepts().get(i).getSubstitutes().get(j).getActualConcept().equals(obj.getObject()))
                    {
                        this.getConcepts().get(i).getSubstitutes().get(j).setUsed(false);
                    }
                }
            }
        }
    }

    /**
     * To be REFACTORED!!!
     * @param obj
     */
    public void unselectToolSubtitution(ObjectsAroundWrapper obj)
    {
        for (int i =0; i < this.getConcepts().size(); i++)
        {
            if (this.getConcepts().get(i).isIsVariable())
            {
                for (int k = 0; k < this.getConcepts().get(i).getValues().size(); k++)
                {
                    for (int j = 0; j < this.getConcepts().get(i).getValues().get(k).getSubstitutes().size(); j++)
                    {
                        if (this.getConcepts().get(i).getValues().get(k).getSubstitutes().get(j).getActualConcept().equals(obj.getObject()))
                        {
                            this.getConcepts().get(i).getValues().get(k).getSubstitutes().get(j).setUsed(false);
                        }
                    }
                }
            }
        }
    }
}
