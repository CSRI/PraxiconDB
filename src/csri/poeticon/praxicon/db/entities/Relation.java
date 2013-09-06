/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.entities;

import csri.poeticon.praxicon.Constants;
import csri.poeticon.praxicon.Globals;
import csri.poeticon.praxicon.db.dao.ConceptDao;
import csri.poeticon.praxicon.db.dao.LRGroupDao;
import csri.poeticon.praxicon.db.dao.MRGroupDao;
import csri.poeticon.praxicon.db.dao.TypeOfRelationDao;
import csri.poeticon.praxicon.db.dao.VRGroupDao;
import csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import csri.poeticon.praxicon.db.dao.implSQL.LRGroupDaoImpl;
import csri.poeticon.praxicon.db.dao.implSQL.MRGroupDaoImpl;
import csri.poeticon.praxicon.db.dao.implSQL.TypeOfRelationDaoImpl;
import csri.poeticon.praxicon.db.dao.implSQL.VRGroupDaoImpl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
 * @author Erevodifwntas
 */
@XmlRootElement()
@Entity
@Table(name="RELATION")
public class Relation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="CUST_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="CUST_SEQ")
    @Column(name="RELATION_ID")
    private Long id;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "relation")
    private List<RelationChain_Relation> mainFunctions;

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="ID")
    TypeOfRelation type;

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="CONCEPT_ID_OBJ")
    Concept obj;

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="CONCEPT_ID_SUBJ")
    Concept subject;

    @Column(name="NAME")
    String name;

    @Column(name="DERIVATION")
    boolean derivation;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="LRSubject")
    @JoinTable(
        name="LRGROUP_RELATIONSUBJECT",
        joinColumns={@JoinColumn(name="RELATION_ID")},
        inverseJoinColumns={@JoinColumn(name="LRGROUP_ID")}
    )
    List<LRGroup> LRGroupSubject;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="LRObject")
    @JoinTable(
        name="LRGROUP_RELATIONOBJECT",
        joinColumns={@JoinColumn(name="RELATION_ID")},
        inverseJoinColumns={@JoinColumn(name="LRGROUP_ID")}
    )
    List<LRGroup> LRGroupObject;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="MRSubject")
    @JoinTable(
        name="MRGROUP_RELATIONSUBJECT",
        joinColumns={@JoinColumn(name="RELATION_ID")},
        inverseJoinColumns={@JoinColumn(name="MRGROUP_ID")}
    )
    List<MRGroup> MRGroupSubject;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="MRObject")
    @JoinTable(
        name="MRGROUP_RELATIONOBJECT",
        joinColumns={@JoinColumn(name="RELATION_ID")},
        inverseJoinColumns={@JoinColumn(name="MRGROUP_ID")}
    )
    List<MRGroup> MRGroupObject;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="VRSubject")
    @JoinTable(
        name="VRGROUP_RELATIONSUBJECT",
        joinColumns={@JoinColumn(name="RELATION_ID")},
        inverseJoinColumns={@JoinColumn(name="VRGROUP_ID")}
    )
    List<VRGroup> VRGroupSubject;

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="VRObject")
    @JoinTable(
        name="VRGROUP_RELATIONOBJECT",
        joinColumns={@JoinColumn(name="RELATION_ID")},
        inverseJoinColumns={@JoinColumn(name="VRGROUP_ID")}
    )
    List<VRGroup> VRGroupObject;

    public Relation()
    {
        mainFunctions = new ArrayList<RelationChain_Relation>();
        VRGroupObject = new ArrayList<VRGroup>();
        VRGroupSubject = new ArrayList<VRGroup>();
        LRGroupObject = new ArrayList<LRGroup>();
        LRGroupSubject = new ArrayList<LRGroup>();
        MRGroupObject = new ArrayList<MRGroup>();
        MRGroupSubject = new ArrayList<MRGroup>();
        type = new TypeOfRelation();
    }

    @XmlTransient
    public Concept getSubject() {
        return subject;
    }

    public void setSubject(Concept subject) {
        this.subject = subject;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="is_derivative"
     *     xmldescription="This attribute defines if the relation is derivative or not"
     */
    @XmlAttribute(name="is_derivative")
    public boolean isDerivation() {
        return derivation;
    }

    public void setDerivation(boolean derivation) {
        this.derivation = derivation;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="subject"
     *     xmldescription="This attribute defines the object that the relation is
     *     related to"
     */
    @XmlAttribute(name="subject")
    private String getSubject_() {
        StringBuilder sb = new StringBuilder();
        if(subject.getName()!=null && subject.getName() != "")
        {
            return subject.getName();
        }
        else
        {
            return subject.getId()+"";
        }
    }

    private void setSubject_(String v) throws Exception {
        if (Globals.ToMergeAfterUnMarshalling)
        {
            ConceptDao cDao = new ConceptDaoImpl();
            Concept subjectCon = cDao.getConceptWithNameOrID(v.trim());
            if (subjectCon!=null)
            {
                subject = subjectCon;
            }
            else
            {
                subjectCon = cDao.getConceptWithNameOrID(v.trim());
                Concept c = new Concept();

                c.setName(v);
                subject = c;
                cDao.persist(subject);
            }

         }
         else
         {
            Concept c = new Concept();
            c.setName(v);
            if (Constants.globalConcepts.contains(c))
            {
                subject = (Concept)Constants.globalConcepts.get(c.getName());
            }
            else
            {
            subject = c;
            Constants.globalConcepts.put(c.getName(), c);
            }

         }
    }

    public void addRelationChain(RelationChain relation, long order)
    {
        //i think that is redundant
    }

    @XmlTransient
    public List<RelationChain_Relation> getMainFunctions() {
        return mainFunctions;
    }

    public void setMainFunctions(List<RelationChain_Relation> mainFunctions) {
        this.mainFunctions = mainFunctions;
    }

    /**
     * @deprecated  As of now, replaced by
     * getObject()
     */
    @XmlTransient
    public Concept getObj() {
        return obj;
    }

    /**
     * @deprecated  As of now, replaced by
     * setObject(Concept obj)
     */
    public void setObj(Concept obj) {
        this.obj = obj;
    }

    @XmlTransient
    public Concept getObject() {
        return obj;
    }

    public void setObject(Concept obj) {
        this.obj = obj;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="obj"
     *     xmldescription="This attribute defines the object that the relation is
     *     related to"
     */
    @XmlAttribute(name="obj")
    private String getObj_() {
        StringBuilder sb = new StringBuilder();
        if(obj.getName()!=null && obj.getName() != "")
        {
            return obj.getName();
        }
        else
        {
            return obj.getId()+"";
        }
    }

    private void setObj_(String v) throws Exception {

        if (Globals.ToMergeAfterUnMarshalling)
        {
            ConceptDao cDao = new ConceptDaoImpl();
            Concept objCon = cDao.getConceptWithNameOrID(v.trim());
            if (objCon!=null)
            {
                obj = objCon;
            }
            else
            {
                objCon = cDao.getConceptWithNameOrID(v.trim());
                Concept c = new Concept();
                c.setName(v);
                obj = c;
                cDao.persist(obj);
            }
        }
        else
        {
            Concept c = new Concept();

            c.setName(v);
            if (Constants.globalConcepts.contains(c))
            {
                obj = (Concept)Constants.globalConcepts.get(c.getName());
            }
            else
            {
                obj = c;
                Constants.globalConcepts.put(c.getName(), c);
            }
        }
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;type&gt;"
     *     xmldescription="This tag defines the type of the relation"
     */
   @XmlElement
    public TypeOfRelation getType() {
        return type;
    }

   /**
    * Sets the type of the Relation but it doesn't check if there is the same
    * type twice
    * @param type the tyep of the relation
    */
   public void setTypeSimple(TypeOfRelation type) {
        this.type = type;
    }

    public void setType(TypeOfRelation type) {
        if(type.getId() == null)
        {
            TypeOfRelationDao tmp = new TypeOfRelationDaoImpl();
            TypeOfRelation res = (TypeOfRelation) tmp.getEntity(type);
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

    @XmlAttribute()
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   @XmlTransient
    public List<LRGroup> getLRGroupObject()
    {
        return LRGroupObject;
    }

   /**
     * @xmlcomments.args
     *	   xmltag="&lt;LRGroupObject&gt;"
     *     xmldescription="This tag defines the LRGroup that should be used to express the Object in this relation"
     */
   @XmlElement(name="LRGroupObject")
    public List<String> getLRGroupObject_()
    {
       List<String> LRGroupObject_ = new ArrayList<String>();
       for(int i = 0; i < LRGroupObject.size(); i++)
       {
           LRGroupObject_.add(LRGroupObject.get(i).getName());
       }
        return LRGroupObject_;
    }

    public void setLRGroupObject(List<LRGroup> LRGroupObject)
    {
        this.LRGroupObject = LRGroupObject;
    }

    private void setLRGroupObject_(List<String> v) throws Exception
    {
        for (int i = 0; i < v.size(); i++)
        {
            if (Globals.ToMergeAfterUnMarshalling)
            {
           //     System.err.println("start "+v);
                LRGroupDao lrgDao = new LRGroupDaoImpl();
                List<LRGroup> lrg = lrgDao.findAllByName(v.get(i).trim());
                if(lrg!=null && lrg.isEmpty()&&Constants.globalConcepts.get(v.get(i).trim())!=null)
                {
                    lrg.add((LRGroup)Constants.globalConcepts.get(v.get(i).trim()));
                }
                if (lrg!=null && !lrg.isEmpty())
                {
                    LRGroupObject.addAll(lrg);
                    for(int j  = 0; j < lrg.size(); j++)
                    {
                        lrg.get(j).getLRObject().add(this);
                    }
                }
                else
                {
                    LRGroup c = new LRGroup();

                    c.setName(v.get(i));
                    c.getLRObject().add(this);
                    lrgDao.persist(c);
                    LRGroupObject.add(c);
                }

             }
             else
             {
                LRGroup c = new LRGroup();
                c.setName(v.get(i));
                c.getLRObject().add(this);
                if (Constants.globalConcepts.contains(c))
                {
                    LRGroupObject.add((LRGroup)Constants.globalConcepts.get(c.getName()));
                }
                else
                {
                    LRGroupObject.add(c);
                    Constants.globalConcepts.put(c.getName(), c);
                }
             }
        }
    }

    @XmlTransient
    public List<LRGroup> getLRGroupSubject()
    {
        return LRGroupSubject;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;LRGroupSubject&gt;"
     *     xmldescription="This tag defines the LRGroup that should be used to express the Subject in this relation"
     */
    @XmlElement(name="LRGroupSubject")
    public List<String> getLRGroupSubject_()
    {
        List<String> LRGroupSubject_ = new ArrayList<String>();
       for(int i = 0; i < LRGroupSubject.size(); i++)
       {
           LRGroupSubject_.add(LRGroupSubject.get(i).getName());
       }
        return LRGroupSubject_;
    }

    public void setLRGroupSubject(List<LRGroup> LRGroupSubject)
    {
        this.LRGroupSubject = LRGroupSubject;
    }

    private void setLRGroupSubject_(List<String> v) throws Exception
    {
        for (int i = 0; i < v.size(); i++)
        {
            if (Globals.ToMergeAfterUnMarshalling)
            {
                LRGroupDao lrgDao = new LRGroupDaoImpl();
                List<LRGroup> lrg = lrgDao.findAllByName(v.get(i).trim());
                if(lrg!=null && lrg.isEmpty()&&Constants.globalConcepts.get(v.get(i).trim())!=null)
                {
                    lrg.add((LRGroup)Constants.globalConcepts.get(v.get(i).trim()));
                }
                if (lrg!=null && !lrg.isEmpty())
                {
                    LRGroupSubject.addAll(lrg);
                    for(int j  = 0; j < lrg.size(); j++)
                    {
                        lrg.get(j).getLRSubject().add(this);
                    }
                }
                else
                {
                    LRGroup c = new LRGroup();

                    c.setName(v.get(i));
                    c.getLRSubject().add(this);
                    lrgDao.persist(c);
                    LRGroupSubject.add(c);
                }

             }
             else
             {
                LRGroup c = new LRGroup();
                c.setName(v.get(i));
                c.getLRSubject().add(this);
                if (Constants.globalConcepts.contains(c))
                {
                    LRGroupSubject.add((LRGroup)Constants.globalConcepts.get(c.getName()));
                }
                else
                {
                    LRGroupSubject.add(c);
                    Constants.globalConcepts.put(c.getName(), c);
                }
             }
        }
    }

    @XmlTransient
    public List<MRGroup> getMRGroupObject()
    {
        return MRGroupObject;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;MRGroupObject&gt;"
     *     xmldescription="This tag defines the MRGroup that should be used to express the Object in this relation"
     */
    @XmlElement(name="MRGroupObject")
    public List<String> getMRGroupObject_()
    {
        List<String> MRGroupObject_ = new ArrayList<String>();
       for(int i = 0; i < MRGroupObject.size(); i++)
       {
           MRGroupObject_.add(MRGroupObject.get(i).getName());
       }
        return MRGroupObject_;
    }

    public void setMRGroupObject(List<MRGroup> MRGroupObject)
    {
        this.MRGroupObject = MRGroupObject;
    }

    private void setMRGroupObject_(List<String> v) throws Exception
    {
        for (int i = 0; i < v.size(); i++)
        {
            if (Globals.ToMergeAfterUnMarshalling)
            {
                MRGroupDao mrgDao = new MRGroupDaoImpl();
                List<MRGroup> mrg = mrgDao.findAllByName(v.get(i).trim());
                if(mrg!=null && mrg.isEmpty()&&Constants.globalConcepts.get(v.get(i).trim())!=null)
                {
                    mrg.add((MRGroup)Constants.globalConcepts.get(v.get(i).trim()));
                }
                if (mrg!=null && !mrg.isEmpty())
                {
                    MRGroupObject.addAll(mrg);
                    for(int j  = 0; j < mrg.size(); j++)
                    {
                        mrg.get(j).getMRObject().add(this);
                    }
                }
                else
                {
                    MRGroup c = new MRGroup();

                    c.setName(v.get(i));
                    c.getMRObject().add(this);
                    mrgDao.persist(c);
                    MRGroupObject.add(c);
                }
             }
             else
             {
                MRGroup c = new MRGroup();
                c.setName(v.get(i));
                c.getMRObject().add(this);
                if (Constants.globalConcepts.contains(c))
                {
                    MRGroupObject.add((MRGroup)Constants.globalConcepts.get(c.getName()));
                }
                else
                {
                    MRGroupObject.add(c);
                    Constants.globalConcepts.put(c.getName(), c);
                }

             }
        }
    }

    @XmlTransient
    public List<MRGroup> getMRGroupSubject()
    {
        return MRGroupSubject;
    }

        /**
     * @xmlcomments.args
     *	   xmltag="&lt;MRGroupSubject&gt;"
     *     xmldescription="This tag defines the MRGroup that should be used to express the Subject in this relation"
     */
    @XmlElement(name="MRGroupSubject")
    public List<String> getMRGroupSubject_()
    {
        List<String> MRGroupSubject_ = new ArrayList<String>();
       for(int i = 0; i < MRGroupSubject.size(); i++)
       {
           MRGroupSubject_.add(MRGroupSubject.get(i).getName());
       }
        return MRGroupSubject_;
    }

    public void setMRGroupSubject(List<MRGroup> MRGroupSubject)
    {
        this.MRGroupSubject = MRGroupSubject;
    }

    private void setMRGroupSubject_(List<String> v) throws Exception
    {
        for (int i = 0; i < v.size(); i++)
        {
            if (Globals.ToMergeAfterUnMarshalling)
            {
                MRGroupDao mrgDao = new MRGroupDaoImpl();
                List<MRGroup> mrg = mrgDao.findAllByName(v.get(i).trim());
                if(mrg!=null && mrg.isEmpty()&&Constants.globalConcepts.get(v.get(i).trim())!=null)
                {
                    mrg.add((MRGroup)Constants.globalConcepts.get(v.get(i).trim()));
                }
                if (mrg!=null && !mrg.isEmpty())
                {
                    MRGroupSubject.addAll(mrg);
                    for(int j  = 0; j < mrg.size(); j++)
                    {
                        mrg.get(j).getMRSubject().add(this);
                    }
                }
                else
                {
                    MRGroup c = new MRGroup();

                    c.setName(v.get(i));
                    c.getMRSubject().add(this);
                    mrgDao.persist(c);
                    MRGroupSubject.add(c);
                }

             }
             else
             {
                MRGroup c = new MRGroup();
                c.setName(v.get(i));
                c.getMRSubject().add(this);
                if (Constants.globalConcepts.contains(c))
                {
                    MRGroupSubject.add((MRGroup)Constants.globalConcepts.get(c.getName()));
                }
                else
                {
                    MRGroupSubject.add(c);
                    Constants.globalConcepts.put(c.getName(), c);
                }

             }
        }
    }

    @XmlTransient
    public List<VRGroup> getVRGroupObject()
    {
        return VRGroupObject;
    }

        /**
     * @xmlcomments.args
     *	   xmltag="&lt;VRGroupObject&gt;"
     *     xmldescription="This tag defines the VRGroup that should be used to express the Object in this relation"
     */
    @XmlElement(name="VRGroupObject")
    public List<String> getVRGroupObject_()
    {
        List<String> VRGroupObject_ = new ArrayList<String>();
       for(int i = 0; i < VRGroupObject.size(); i++)
       {
           VRGroupObject_.add(VRGroupObject.get(i).getName());
       }
        return VRGroupObject_;
    }

    public void setVRGroupObject(List<VRGroup> VRGroupObject)
    {
        this.VRGroupObject = VRGroupObject;
    }

    private void setVRGroupObject_(List<String> v) throws Exception
    {
        for (int i = 0; i < v.size(); i++)
        {
            if (Globals.ToMergeAfterUnMarshalling)
            {
                VRGroupDao vrgDao = new VRGroupDaoImpl();
                List<VRGroup> vrg = vrgDao.findAllByName(v.get(i).trim());
                if(vrg!=null && vrg.isEmpty()&&Constants.globalConcepts.get(v.get(i).trim())!=null)
                {
                    vrg.add((VRGroup)Constants.globalConcepts.get(v.get(i).trim()));
                }
                if (vrg!=null && !vrg.isEmpty())
                {
                    VRGroupObject.addAll(vrg);
                    for(int j  = 0; j < vrg.size(); j++)
                    {
                        vrg.get(j).getVRObject().add(this);
                    }
                }
                else
                {
                    VRGroup c = new VRGroup();

                    c.setName(v.get(i));
                    c.getVRObject().add(this);
                    vrgDao.persist(c);
                    VRGroupObject.add(c);
                }

             }
             else
             {
                VRGroup c = new VRGroup();
                c.setName(v.get(i));
                c.getVRObject().add(this);
                if (Constants.globalConcepts.contains(c))
                {
                    VRGroupObject.add((VRGroup)Constants.globalConcepts.get(c.getName()));
                }
                else
                {
                    VRGroupObject.add(c);
                    Constants.globalConcepts.put(c.getName(), c);
                }

             }
        }
    }

    @XmlTransient
    public List<VRGroup> getVRGroupSubject()
    {
        return VRGroupSubject;
    }

    /**
     * @xmlcomments.args
     *	   xmltag="&lt;VRGroupSubject&gt;"
     *     xmldescription="This tag defines the VRGroup that should be used to express the Subject in this relation"
     */
    @XmlElement(name="VRGroupSubject")
    public List<String> getVRGroupSubject_()
    {
        List<String> VRGroupSubject_ = new ArrayList<String>();
       for(int i = 0; i < VRGroupSubject.size(); i++)
       {
           VRGroupSubject_.add(VRGroupSubject.get(i).getName());
       }
        return VRGroupSubject_;
    }

    public void setVRGroupSubject(List<VRGroup> VRGroupSubject)
    {
        this.VRGroupSubject = VRGroupSubject;
    }

    private void setVRGroupSubject_(List<String> v) throws Exception
    {
        for (int i = 0; i < v.size(); i++)
        {
            if (Globals.ToMergeAfterUnMarshalling)
            {
                VRGroupDao vrgDao = new VRGroupDaoImpl();
                List<VRGroup> vrg = vrgDao.findAllByName(v.get(i).trim());
                if(vrg!=null && vrg.isEmpty()&&Constants.globalConcepts.get(v.get(i).trim())!=null)
                {
                    vrg.add((VRGroup)Constants.globalConcepts.get(v.get(i).trim()));
                }
                if (vrg!=null && !vrg.isEmpty())
                {
                    VRGroupSubject.addAll(vrg);
                    for(int j  = 0; j < vrg.size(); j++)
                    {
                        vrg.get(j).getVRSubject().add(this);
                    }
                }
                else
                {
                    VRGroup c = new VRGroup();

                    c.setName(v.get(i));
                    c.getVRSubject().add(this);
                    vrgDao.persist(c);
                    VRGroupSubject.add(c);
                }

             }
             else
             {
                VRGroup c = new VRGroup();
                c.setName(v.get(i));
                c.getVRSubject().add(this);
                if (Constants.globalConcepts.contains(c))
                {
                    VRGroupSubject.add((VRGroup)Constants.globalConcepts.get(c.getName()));
                }
                else
                {
                    VRGroupSubject.add(c);
                    Constants.globalConcepts.put(c.getName(), c);
                }

             }
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Relation)) {
            return false;
        }
        Relation other = (Relation) object;
        try
        {
            if ((this.type!=null && this.obj!=null && this.subject!=null
                && this.type.equals(other.type) && this.obj.equals(other.obj) && this.subject.equals(other.subject))||
                (this.type!=null && this.obj!=null && this.subject!=null
                &&this.type.equals(other.type) && this.obj.equals(other.subject) && this.subject.equals(other.obj)))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String toString() {
        return this.getSubject() + " " + this.getType().getForwardName() + " " + this.getObj();
    }

    public void afterUnmarshal(Unmarshaller u, Object parent) {
        if (Globals.ToMergeAfterUnMarshalling)
        {
            TypeOfRelationDao rDao = new TypeOfRelationDaoImpl();
            this.type = rDao.getEntity(type);
            ConceptDao cDao = new ConceptDaoImpl();
            this.obj = cDao.getEntity(obj);
            obj.getObjOfRelations().add(this);
            this.subject = cDao.getEntity(subject);
        }
    }


}
