/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csri.poeticon.praxicon.db.dao;

import csri.poeticon.praxicon.db.entities.Concept;
import csri.poeticon.praxicon.db.entities.RelationChain;
import java.util.List;

/**
 *
 * @author Erevodifwntas
 */
public interface RelationChainDao extends Dao<Long, RelationChain> {

    List<RelationChain> getRelationChainsContainingConcept(Concept c);
}
