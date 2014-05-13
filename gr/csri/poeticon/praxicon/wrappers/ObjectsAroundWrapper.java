/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.csri.poeticon.praxicon.wrappers;

import gr.csri.poeticon.praxicon.db.entities.Concept;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author panos
 */
public class ObjectsAroundWrapper implements Comparable{

    Concept object;
    String commonName;

    double level;

    public ObjectsAroundWrapper(Concept object, String commonName)
    {
        this.object = object;
        this.commonName = commonName;
        this.level = -1;
    }

    public ObjectsAroundWrapper(Concept object)
    {
        this.object = object;
        this.commonName = object.getLanguageRepresentationName();
        this.level = -1;
    }

    public ObjectsAroundWrapper(Concept object, int level)
    {
        this.object = object;
        this.commonName = object.getLanguageRepresentationName();
        this.level = level;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public Concept getObject() {
        return object;
    }

    public void setObject(Concept object) {
        this.object = object;
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ObjectsAroundWrapper other = (ObjectsAroundWrapper) obj;
        if (this.object != other.object && (this.object == null || !this.object.equals(other.object))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + (this.object != null ? this.object.hashCode() : 0);
        hash = 17 * hash + (this.commonName != null ? this.commonName.hashCode() : 0);
        return hash;
    }
    
    public static List<Concept> toConceptList(List<ObjectsAroundWrapper> list)
    {
        List<Concept> res = new ArrayList<Concept>();
        for (int i = 0; i < list.size(); i++)
        {
            res.add(list.get(i).object);
        }
        return res;
    }

    @Override
    public int compareTo(Object o)
    {
        if (o instanceof ObjectsAroundWrapper)
        {
            ObjectsAroundWrapper other = (ObjectsAroundWrapper)o;
            if (other.getLevel() > this.getLevel())
            {
                return 1;
            }
            else if (other.getLevel() < this.getLevel())
            {
                return -1;
            }
            return 0;
        }
        return -1;
    }
}
