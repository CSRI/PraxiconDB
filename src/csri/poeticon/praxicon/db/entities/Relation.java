/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.entities;

import csri.poeticon.praxicon.Constants;
import csri.poeticon.praxicon.Globals;
import csri.poeticon.praxicon.db.dao.ConceptDao;
import csri.poeticon.praxicon.db.dao.LanguageRepresentationGroupDao;
import csri.poeticon.praxicon.db.dao.MotoricRepresentationGroupDao;
import csri.poeticon.praxicon.db.dao.TypeOfRelationDao;
import csri.poeticon.praxicon.db.dao.VisualRepresentationGroupDao;
import csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import csri.poeticon.praxicon.db.dao.implSQL.LanguageRepresentationGroupDaoImpl;
import csri.poeticon.praxicon.db.dao.implSQL.MotoricRepresentationGroupDaoImpl;
import csri.poeticon.praxicon.db.dao.implSQL.TypeOfRelationDaoImpl;
import csri.poeticon.praxicon.db.dao.implSQL.VisualRepresentationGroupDaoImpl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 *
 * @author Dimitris Mavroeidis
 */
@XmlRootElement()
@Entity
@Table(name="Relation")
public class Relation implements Serializable {

    public static enum DerivationSupported {
        YES, NO, UNKNOWN ;
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
    @Column(name="RelationId")
    private Long id;

// TODO: Check if this is needed!
//    @OneToMany(cascade=CascadeType.ALL, mappedBy = "Relation")
//    private List<RelationChain_Relation> mainFunctions;

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="Id")
    RelationType type;

    @Column(name="Comment")
    String comment;

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="ConceptIdObject")
    Concept object;

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="ConceptIdSubject")
    Concept subject;

    @Column(name="DerivationSupported")
    @Enumerated(EnumType.STRING)
    protected DerivationSupported derivationSupported;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="LanguageRepresentationSubject")
    @JoinTable(
        name="LanguageRepresentation_RelationSubject",
        joinColumns={@JoinColumn(name="RelationId")},
        inverseJoinColumns={@JoinColumn(name="LanguageRepresentationId")}
    )
    LanguageRepresentation LanguageRepresentationSubject;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="LangugageRepresentationObject")
    @JoinTable(
        name="LangugeRepresentation_RelationObject",
        joinColumns={@JoinColumn(name="RelationId")},
        inverseJoinColumns={@JoinColumn(name="LanguageRepresentationId")}
    )
    LanguageRepresentation LanguageRepresentationObject;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="MotoricRepresentationSubject")
    @JoinTable(
        name="MotoricRepresentation_RelationSubject",
        joinColumns={@JoinColumn(name="RelationId")},
        inverseJoinColumns={@JoinColumn(name="MotoricId")}
    )
    MotoricRepresentation MotoricRepresentationSubject;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="MotoricRepresentationObject")
    @JoinTable(
        name="MotoricRepresentation_RelationObject",
        joinColumns={@JoinColumn(name="RelationId")},
        inverseJoinColumns={@JoinColumn(name="MotoricRepresentationId")}
    )
    MotoricRepresentation MotoricRepresentationObject;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="VisualRepresentationSubject")
    @JoinTable(
        name="VisualRepresentation_RelationSubject",
        joinColumns={@JoinColumn(name="RelationId")},
        inverseJoinColumns={@JoinColumn(name="VisualRepresentationId")}
    )
    VisualRepresentation VisualRepresentationSubject;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="VisualRepresentationObject")
    @JoinTable(
        name="VisualRepresentation_RelationObject",
        joinColumns={@JoinColumn(name="RelationId")},
        inverseJoinColumns={@JoinColumn(name="VisualRepresentationId")}
    )
    VisualRepresentation VisualRepresentationObject;




// TODO: Uncomment each method after checking it first
//    public Relation()
//    {
//        mainFunctions = new ArrayList<RelationChain_Relation>();
//        VisualRepresentationObject = new VisualRepresentation();
//        VisualRepresentationSubject = new VisualRepresentation();
//        LanguageRepresentationObject = new LanguageRepresentation();
//        LanguageRepresentationSubject = new LanguageRepresentation();
//        MotoricRepresentationObject = new MotoricRepresentation();
//        MotoricRepresentationSubject = new MotoricRepresentation();
//        type = new RelationType();
//    }

    @XmlTransient
    public Concept getSubject() {
        return subject;
    }

//    public void setSubject(Concept subject) {
//        this.subject = subject;
//    }
//
//    /**
//     * @xmlcomments.args
//     *	   xmltag="is_derivative"
//     *     xmldescription="This attribute defines if the relation is derivative or not"
//     */
//    @XmlAttribute(name="is_derivative")
//    public boolean isDerivation() {
//        return derivation;
//    }
//
//    public void setDerivation(boolean derivation) {
//        this.derivation = derivation;
//    }
//
//    /**
//     * @xmlcomments.args
//     *	   xmltag="subject"
//     *     xmldescription="This attribute defines the object that the relation is
//     *     related to"
//     */
//    @XmlAttribute(name="subject")
//    private String getSubject_() {
//        StringBuilder sb = new StringBuilder();
//        if(subject.getName()!=null && subject.getName() != "")
//        {
//            return subject.getName();
//        }
//        else
//        {
//            return subject.getId()+"";
//        }
//    }
//
//    private void setSubject_(String v) throws Exception {
//        if (Globals.ToMergeAfterUnMarshalling)
//        {
//            ConceptDao cDao = new ConceptDaoImpl();
//            Concept subjectCon = cDao.getConceptWithNameOrID(v.trim());
//            if (subjectCon!=null)
//            {
//                subject = subjectCon;
//            }
//            else
//            {
//                subjectCon = cDao.getConceptWithNameOrID(v.trim());
//                Concept c = new Concept();
//
//                c.setName(v);
//                subject = c;
//                cDao.persist(subject);
//            }
//
//         }
//         else
//         {
//            Concept c = new Concept();
//            c.setName(v);
//            if (Constants.globalConcepts.contains(c))
//            {
//                subject = (Concept)Constants.globalConcepts.get(c.getName());
//            }
//            else
//            {
//            subject = c;
//            Constants.globalConcepts.put(c.getName(), c);
//            }
//
//         }
//    }
//
//    public void addRelationChain(RelationChain relation, long order)
//    {
//        //i think that is redundant
//    }
//
//    @XmlTransient
//    public List<RelationChain_Relation> getMainFunctions() {
//        return mainFunctions;
//    }
//
//    public void setMainFunctions(List<RelationChain_Relation> mainFunctions) {
//        this.mainFunctions = mainFunctions;
//    }

    @XmlTransient
    public Concept getObject() {
        return object;
    }

    public void setObject(Concept object) {
        this.object = object;
    }
//
//    /**
//     * @xmlcomments.args
//     *	   xmltag="obj"
//     *     xmldescription="This attribute defines the object that the relation is
//     *     related to"
//     */
//    @XmlAttribute(name="obj")
//    private String getObj_() {
//        StringBuilder sb = new StringBuilder();
//        if(object.getName()!=null && object.getName() != "")
//        {
//            return object.getName();
//        }
//        else
//        {
//            return object.getId()+"";
//        }
//    }
//
//    private void setObj_(String v) throws Exception {
//
//        if (Globals.ToMergeAfterUnMarshalling)
//        {
//            ConceptDao cDao = new ConceptDaoImpl();
//            Concept objCon = cDao.getConceptWithNameOrID(v.trim());
//            if (objCon!=null)
//            {
//                object = objCon;
//            }
//            else
//            {
//                objCon = cDao.getConceptWithNameOrID(v.trim());
//                Concept c = new Concept();
//                c.setName(v);
//                object = c;
//                cDao.persist(object);
//            }
//        }
//        else
//        {
//            Concept c = new Concept();
//
//            c.setName(v);
//            if (Constants.globalConcepts.contains(c))
//            {
//                object = (Concept)Constants.globalConcepts.get(c.getName());
//            }
//            else
//            {
//                object = c;
//                Constants.globalConcepts.put(c.getName(), c);
//            }
//        }
//    }
//
//    /**
//     * @xmlcomments.args
//     *	   xmltag="&lt;type&gt;"
//     *     xmldescription="This tag defines the type of the relation"
//     */
//   @XmlElement
//    public RelationType getType() {
//        return type;
//    }
//
//   /**
//    * Sets the type of the Relation but it doesn't check if there is the same
//    * type twice
//    * @param type the tyep of the relation
//    */
    public void setTypeSimple(RelationType type) {
        this.type = type;
    }

    public void setType(RelationType type) {
        if(type.getId() == null)
        {
            TypeOfRelationDao tmp = new TypeOfRelationDaoImpl();
            RelationType res = (RelationType) tmp.getEntity(type);
            if(res!=null)
            {
                type = res;
            }
        }
        this.type = type;
    }



    @XmlAttribute
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
//    @XmlAttribute()
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//   @XmlTransient
//    public LanguageRepresentation getLanguageRepresentationObject()
//    {
//        return LanguageRepresentationObject;
//    }
//
//   /**
//     * @xmlcomments.args
//     *	   xmltag="&lt;LanguageRepresentationGroupObject&gt;"
//     *     xmldescription="This tag defines the LanguageRepresentationGroup that should be used to express the Object in this relation"
//     */
//   @XmlElement(name="LanguageRepresentationObject")
//    public String getLanguageRepresentationObject_()
//    {
//        String LanguageRepresentationObject_ = new String();
//        LanguageRepresentationObject_ = LanguageRepresentationObject.getText();
//        return LanguageRepresentationObject_;
//    }
//
//    public void setLanguageRepresentationObject(LanguageRepresentation LanguageRepresentationObject)
//    {
//        this.LanguageRepresentationObject = LanguageRepresentationObject;
//    }
//
//    private void setLanguageRepresentationObject_(String v) throws Exception
//    {
//        if (Globals.ToMergeAfterUnMarshalling)
//        {
//       //     System.err.println("start "+v);
//            LanguageRepresentationDao lrgDao = new LanguageRepresentationGroupDaoImpl();
//            LanguageRepresentation lrg = lrgDao.findAllByName(v.trim());
//            if(lrg!=null && lrg.isEmpty()&&Constants.globalConcepts.get(v.get(i).trim())!=null)
//            {
//                lrg.add((LanguageRepresentationGroup)Constants.globalConcepts.get(v.get(i).trim()));
//            }
//            if (lrg!=null && !lrg.isEmpty())
//            {
//                LanguageRepresentationGroupObject.addAll(lrg);
//                for(int j  = 0; j < lrg.size(); j++)
//                {
//                    lrg.get(j).getLanguageRepresentationObject().add(this);
//                }
//            }
//            else
//            {
//                LanguageRepresentationGroup c = new LanguageRepresentationGroup();
//
//                c.setName(v.get(i));
//                c.getLanguageRepresentationObject().add(this);
//                lrgDao.persist(c);
//                LanguageRepresentationGroupObject.add(c);
//            }
//
//         }
//         else
//         {
//            LanguageRepresentationGroup c = new LanguageRepresentationGroup();
//            c.setName(v.get(i));
//            c.getLanguageRepresentationObject().add(this);
//            if (Constants.globalConcepts.contains(c))
//            {
//                LanguageRepresentationGroupObject.add((LanguageRepresentationGroup)Constants.globalConcepts.get(c.getName()));
//            }
//            else
//            {
//                LanguageRepresentationGroupObject.add(c);
//                Constants.globalConcepts.put(c.getName(), c);
//            }
//         }
//    }
//
//    @XmlTransient
//    public List<LanguageRepresentationGroup> getLanguageRepresentationGroupSubject()
//    {
//        return LanguageRepresentationGroupSubject;
//    }
//
//    /**
//     * @xmlcomments.args
//     *	   xmltag="&lt;LanguageRepresentationGroupSubject&gt;"
//     *     xmldescription="This tag defines the LanguageRepresentationGroup that should be used to express the Subject in this relation"
//     */
//    @XmlElement(name="LanguageRepresentationGroupSubject")
//    public List<String> getLanguageRepresentationGroupSubject_()
//    {
//        List<String> LanguageRepresentationGroupSubject_ = new ArrayList<String>();
//       for(int i = 0; i < LanguageRepresentationGroupSubject.size(); i++)
//       {
//           LanguageRepresentationGroupSubject_.add(LanguageRepresentationGroupSubject.get(i).getName());
//       }
//        return LanguageRepresentationGroupSubject_;
//    }
//
//    public void setLanguageRepresentationGroupSubject(List<LanguageRepresentationGroup> LanguageRepresentationGroupSubject)
//    {
//        this.LanguageRepresentationGroupSubject = LanguageRepresentationGroupSubject;
//    }
//
//    private void setLanguageRepresentationGroupSubject_(List<String> v) throws Exception
//    {
//        for (int i = 0; i < v.size(); i++)
//        {
//            if (Globals.ToMergeAfterUnMarshalling)
//            {
//                LanguageRepresentationGroupDao lrgDao = new LanguageRepresentationGroupDaoImpl();
//                List<LanguageRepresentationGroup> lrg = lrgDao.findAllByName(v.get(i).trim());
//                if(lrg!=null && lrg.isEmpty()&&Constants.globalConcepts.get(v.get(i).trim())!=null)
//                {
//                    lrg.add((LanguageRepresentationGroup)Constants.globalConcepts.get(v.get(i).trim()));
//                }
//                if (lrg!=null && !lrg.isEmpty())
//                {
//                    LanguageRepresentationGroupSubject.addAll(lrg);
//                    for(int j  = 0; j < lrg.size(); j++)
//                    {
//                        lrg.get(j).getLanguageRepresentationSubject().add(this);
//                    }
//                }
//                else
//                {
//                    LanguageRepresentationGroup c = new LanguageRepresentationGroup();
//
//                    c.setName(v.get(i));
//                    c.getLanguageRepresentationSubject().add(this);
//                    lrgDao.persist(c);
//                    LanguageRepresentationGroupSubject.add(c);
//                }
//
//             }
//             else
//             {
//                LanguageRepresentationGroup c = new LanguageRepresentationGroup();
//                c.setName(v.get(i));
//                c.getLanguageRepresentationSubject().add(this);
//                if (Constants.globalConcepts.contains(c))
//                {
//                    LanguageRepresentationGroupSubject.add((LanguageRepresentationGroup)Constants.globalConcepts.get(c.getName()));
//                }
//                else
//                {
//                    LanguageRepresentationGroupSubject.add(c);
//                    Constants.globalConcepts.put(c.getName(), c);
//                }
//             }
//        }
//    }
//
//    @XmlTransient
//    public List<MotoricRepresentationGroup> getMotoricRepresentationGroupObject()
//    {
//        return MotoricRepresentationGroupObject;
//    }
//
//    /**
//     * @xmlcomments.args
//     *	   xmltag="&lt;MotoricRepresentationGroupObject&gt;"
//     *     xmldescription="This tag defines the MotoricRepresentationGroup that should be used to express the Object in this relation"
//     */
//    @XmlElement(name="MotoricRepresentationGroupObject")
//    public List<String> getMotoricRepresentationGroupObject_()
//    {
//        List<String> MotoricRepresentationGroupObject_ = new ArrayList<String>();
//       for(int i = 0; i < MotoricRepresentationGroupObject.size(); i++)
//       {
//           MotoricRepresentationGroupObject_.add(MotoricRepresentationGroupObject.get(i).getName());
//       }
//        return MotoricRepresentationGroupObject_;
//    }
//
//    public void setMotoricRepresentationGroupObject(List<MotoricRepresentationGroup> MotoricRepresentationGroupObject)
//    {
//        this.MotoricRepresentationGroupObject = MotoricRepresentationGroupObject;
//    }
//
//    private void setMotoricRepresentationGroupObject_(List<String> v) throws Exception
//    {
//        for (int i = 0; i < v.size(); i++)
//        {
//            if (Globals.ToMergeAfterUnMarshalling)
//            {
//                MotoricRepresentationGroupDao mrgDao = new MotoricRepresentationGroupDaoImpl();
//                List<MotoricRepresentationGroup> mrg = mrgDao.findAllByName(v.get(i).trim());
//                if(mrg!=null && mrg.isEmpty()&&Constants.globalConcepts.get(v.get(i).trim())!=null)
//                {
//                    mrg.add((MotoricRepresentationGroup)Constants.globalConcepts.get(v.get(i).trim()));
//                }
//                if (mrg!=null && !mrg.isEmpty())
//                {
//                    MotoricRepresentationGroupObject.addAll(mrg);
//                    for(int j  = 0; j < mrg.size(); j++)
//                    {
//                        mrg.get(j).getMotoricRepresentationObject().add(this);
//                    }
//                }
//                else
//                {
//                    MotoricRepresentationGroup c = new MotoricRepresentationGroup();
//
//                    c.setName(v.get(i));
//                    c.getMotoricRepresentationObject().add(this);
//                    mrgDao.persist(c);
//                    MotoricRepresentationGroupObject.add(c);
//                }
//             }
//             else
//             {
//                MotoricRepresentationGroup c = new MotoricRepresentationGroup();
//                c.setName(v.get(i));
//                c.getMotoricRepresentationObject().add(this);
//                if (Constants.globalConcepts.contains(c))
//                {
//                    MotoricRepresentationGroupObject.add((MotoricRepresentationGroup)Constants.globalConcepts.get(c.getName()));
//                }
//                else
//                {
//                    MotoricRepresentationGroupObject.add(c);
//                    Constants.globalConcepts.put(c.getName(), c);
//                }
//
//             }
//        }
//    }
//
//    @XmlTransient
//    public List<MotoricRepresentationGroup> getMotoricRepresentationGroupSubject()
//    {
//        return MotoricRepresentationGroupSubject;
//    }
//
//        /**
//     * @xmlcomments.args
//     *	   xmltag="&lt;MotoricRepresentationGroupSubject&gt;"
//     *     xmldescription="This tag defines the MotoricRepresentationGroup that should be used to express the Subject in this relation"
//     */
//    @XmlElement(name="MotoricRepresentationGroupSubject")
//    public List<String> getMotoricRepresentationGroupSubject_()
//    {
//        List<String> MotoricRepresentationGroupSubject_ = new ArrayList<String>();
//       for(int i = 0; i < MotoricRepresentationGroupSubject.size(); i++)
//       {
//           MotoricRepresentationGroupSubject_.add(MotoricRepresentationGroupSubject.get(i).getName());
//       }
//        return MotoricRepresentationGroupSubject_;
//    }
//
//    public void setMotoricRepresentationGroupSubject(List<MotoricRepresentationGroup> MotoricRepresentationGroupSubject)
//    {
//        this.MotoricRepresentationGroupSubject = MotoricRepresentationGroupSubject;
//    }
//
//    private void setMotoricRepresentationGroupSubject_(List<String> v) throws Exception
//    {
//        for (int i = 0; i < v.size(); i++)
//        {
//            if (Globals.ToMergeAfterUnMarshalling)
//            {
//                MotoricRepresentationGroupDao mrgDao = new MotoricRepresentationGroupDaoImpl();
//                List<MotoricRepresentationGroup> mrg = mrgDao.findAllByName(v.get(i).trim());
//                if(mrg!=null && mrg.isEmpty()&&Constants.globalConcepts.get(v.get(i).trim())!=null)
//                {
//                    mrg.add((MotoricRepresentationGroup)Constants.globalConcepts.get(v.get(i).trim()));
//                }
//                if (mrg!=null && !mrg.isEmpty())
//                {
//                    MotoricRepresentationGroupSubject.addAll(mrg);
//                    for(int j  = 0; j < mrg.size(); j++)
//                    {
//                        mrg.get(j).getMotoricRepresentationSubject().add(this);
//                    }
//                }
//                else
//                {
//                    MotoricRepresentationGroup c = new MotoricRepresentationGroup();
//
//                    c.setName(v.get(i));
//                    c.getMotoricRepresentationSubject().add(this);
//                    mrgDao.persist(c);
//                    MotoricRepresentationGroupSubject.add(c);
//                }
//
//             }
//             else
//             {
//                MotoricRepresentationGroup c = new MotoricRepresentationGroup();
//                c.setName(v.get(i));
//                c.getMotoricRepresentationSubject().add(this);
//                if (Constants.globalConcepts.contains(c))
//                {
//                    MotoricRepresentationGroupSubject.add((MotoricRepresentationGroup)Constants.globalConcepts.get(c.getName()));
//                }
//                else
//                {
//                    MotoricRepresentationGroupSubject.add(c);
//                    Constants.globalConcepts.put(c.getName(), c);
//                }
//
//             }
//        }
//    }
//
//    @XmlTransient
//    public List<VisualRepresentationGroup> getVisualRepresentationGroupObject()
//    {
//        return VisualRepresentationGroupObject;
//    }
//
//        /**
//     * @xmlcomments.args
//     *	   xmltag="&lt;VisualRepresentationGroupObject&gt;"
//     *     xmldescription="This tag defines the VisualRepresentationGroup that should be used to express the Object in this relation"
//     */
//    @XmlElement(name="VisualRepresentationGroupObject")
//    public List<String> getVisualRepresentationGroupObject_()
//    {
//        List<String> VisualRepresentationGroupObject_ = new ArrayList<String>();
//       for(int i = 0; i < VisualRepresentationGroupObject.size(); i++)
//       {
//           VisualRepresentationGroupObject_.add(VisualRepresentationGroupObject.get(i).getName());
//       }
//        return VisualRepresentationGroupObject_;
//    }
//
//    public void setVisualRepresentationGroupObject(List<VisualRepresentationGroup> VisualRepresentationGroupObject)
//    {
//        this.VisualRepresentationGroupObject = VisualRepresentationGroupObject;
//    }
//
//    private void setVisualRepresentationGroupObject_(List<String> v) throws Exception
//    {
//        for (int i = 0; i < v.size(); i++)
//        {
//            if (Globals.ToMergeAfterUnMarshalling)
//            {
//                VisualRepresentationGroupDao vrgDao = new VisualRepresentationGroupDaoImpl();
//                List<VisualRepresentationGroup> vrg = vrgDao.findAllByName(v.get(i).trim());
//                if(vrg!=null && vrg.isEmpty()&&Constants.globalConcepts.get(v.get(i).trim())!=null)
//                {
//                    vrg.add((VisualRepresentationGroup)Constants.globalConcepts.get(v.get(i).trim()));
//                }
//                if (vrg!=null && !vrg.isEmpty())
//                {
//                    VisualRepresentationGroupObject.addAll(vrg);
//                    for(int j  = 0; j < vrg.size(); j++)
//                    {
//                        vrg.get(j).getVisualRepresentationObject().add(this);
//                    }
//                }
//                else
//                {
//                    VisualRepresentationGroup c = new VisualRepresentationGroup();
//
//                    c.setName(v.get(i));
//                    c.getVisualRepresentationObject().add(this);
//                    vrgDao.persist(c);
//                    VisualRepresentationGroupObject.add(c);
//                }
//
//             }
//             else
//             {
//                VisualRepresentationGroup c = new VisualRepresentationGroup();
//                c.setName(v.get(i));
//                c.getVisualRepresentationObject().add(this);
//                if (Constants.globalConcepts.contains(c))
//                {
//                    VisualRepresentationGroupObject.add((VisualRepresentationGroup)Constants.globalConcepts.get(c.getName()));
//                }
//                else
//                {
//                    VisualRepresentationGroupObject.add(c);
//                    Constants.globalConcepts.put(c.getName(), c);
//                }
//
//             }
//        }
//    }
//
//    @XmlTransient
//    public List<VisualRepresentationGroup> getVisualRepresentationGroupSubject()
//    {
//        return VisualRepresentationGroupSubject;
//    }
//
//    /**
//     * @xmlcomments.args
//     *	   xmltag="&lt;VisualRepresentationGroupSubject&gt;"
//     *     xmldescription="This tag defines the VisualRepresentationGroup that should be used to express the Subject in this relation"
//     */
//    @XmlElement(name="VisualRepresentationGroupSubject")
//    public List<String> getVisualRepresentationGroupSubject_()
//    {
//        List<String> VisualRepresentationGroupSubject_ = new ArrayList<String>();
//       for(int i = 0; i < VisualRepresentationGroupSubject.size(); i++)
//       {
//           VisualRepresentationGroupSubject_.add(VisualRepresentationGroupSubject.get(i).getName());
//       }
//        return VisualRepresentationGroupSubject_;
//    }
//
//    public void setVisualRepresentationGroupSubject(List<VisualRepresentationGroup> VisualRepresentationGroupSubject)
//    {
//        this.VisualRepresentationGroupSubject = VisualRepresentationGroupSubject;
//    }
//
//    private void setVisualRepresentationGroupSubject_(List<String> v) throws Exception
//    {
//        for (int i = 0; i < v.size(); i++)
//        {
//            if (Globals.ToMergeAfterUnMarshalling)
//            {
//                VisualRepresentationGroupDao vrgDao = new VisualRepresentationGroupDaoImpl();
//                List<VisualRepresentationGroup> vrg = vrgDao.findAllByName(v.get(i).trim());
//                if(vrg!=null && vrg.isEmpty()&&Constants.globalConcepts.get(v.get(i).trim())!=null)
//                {
//                    vrg.add((VisualRepresentationGroup)Constants.globalConcepts.get(v.get(i).trim()));
//                }
//                if (vrg!=null && !vrg.isEmpty())
//                {
//                    VisualRepresentationGroupSubject.addAll(vrg);
//                    for(int j  = 0; j < vrg.size(); j++)
//                    {
//                        vrg.get(j).getVisualRepresentationSubject().add(this);
//                    }
//                }
//                else
//                {
//                    VisualRepresentationGroup c = new VisualRepresentationGroup();
//
//                    c.setName(v.get(i));
//                    c.getVisualRepresentationSubject().add(this);
//                    vrgDao.persist(c);
//                    VisualRepresentationGroupSubject.add(c);
//                }
//
//             }
//             else
//             {
//                VisualRepresentationGroup c = new VisualRepresentationGroup();
//                c.setName(v.get(i));
//                c.getVisualRepresentationSubject().add(this);
//                if (Constants.globalConcepts.contains(c))
//                {
//                    VisualRepresentationGroupSubject.add((VisualRepresentationGroup)Constants.globalConcepts.get(c.getName()));
//                }
//                else
//                {
//                    VisualRepresentationGroupSubject.add(c);
//                    Constants.globalConcepts.put(c.getName(), c);
//                }
//
//             }
//        }
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (id != null ? id.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof Relation)) {
//            return false;
//        }
//        Relation other = (Relation) object;
//        try
//        {
//            if ((this.type!=null && this.object!=null && this.subject!=null
//                && this.type.equals(other.type) && this.object.equals(other.object) && this.subject.equals(other.subject))||
//                (this.type!=null && this.object!=null && this.subject!=null
//                &&this.type.equals(other.type) && this.object.equals(other.subject) && this.subject.equals(other.object)))
//            {
//                return true;
//            }
//            else
//            {
//                return false;
//            }
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    @Override
//    public String toString() {
//        return this.getSubject() + " " + this.getType().getForwardName() + " " + this.getObject();
//    }
//
//    public void afterUnmarshal(Unmarshaller u, Object parent) {
//        if (Globals.ToMergeAfterUnMarshalling)
//        {
//            TypeOfRelationDao rDao = new TypeOfRelationDaoImpl();
//            this.type = rDao.getEntity(type);
//            ConceptDao cDao = new ConceptDaoImpl();
//            this.object = cDao.getEntity(object);
//            object.getObjOfRelations().add(this);
//            this.subject = cDao.getEntity(subject);
//        }
//    }
//}
