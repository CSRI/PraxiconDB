package gr.csri.poeticon.praxicon.db.entities;

import gr.csri.poeticon.praxicon.db.dao.RelationDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationDaoImpl;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author dmavroeidis
 */
@XmlRootElement(name = "relations")
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlSeeAlso(Concept.class)

public class Relations {

    @XmlElement(name = "relation")
    List<Relation> relations = new ArrayList<>();

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    public Relations() {
        relations = new ArrayList<>();
    }

    /**
     * Stores all relations of the collection in the database updating
     * same name entries
     */
    public void storeRelations() {
        if (!relations.isEmpty()) {
            for (Relation relation : relations) {                
                RelationDao rDao = new RelationDaoImpl();
                relation = rDao.updatedRelation(relation);
                System.out.println("Relation: " + relation);
                rDao.merge(relation);
            }
        }
    }

}
