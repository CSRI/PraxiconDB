/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao;

import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.RelationChain;
import java.util.List;

/**
 *
 * @author Erevodifwntas
 */
public interface RelationChainDao extends Dao<Long, RelationChain> {

    List<RelationChain> getRelationChainsContainingConcept(Concept concept);
}
