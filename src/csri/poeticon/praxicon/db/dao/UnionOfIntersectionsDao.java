/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao;

import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.UnionOfIntersections;
import java.util.List;

/**
 *
 * @author Erevodifwntas
 */
public interface UnionOfIntersectionsDao extends Dao<Long, UnionOfIntersections>{

    List<UnionOfIntersections> getAllUnions(Concept c);

    UnionOfIntersections transformUnion(UnionOfIntersections union, Concept c);

    UnionOfIntersections createUnion(String forwardName, String backwardName, Concept value, Concept owner);
}
