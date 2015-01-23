/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao;

import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
import gr.csri.poeticon.praxicon.db.entities.RelationSet;
import java.util.List;

/**
 *
 * @author dmavroeidis
 */
public interface RelationSetDao extends Dao<Long, RelationSet> {

    List<RelationSet> getRelationSetsByRelationArgument(RelationArgument relationArgument);
            
    List<RelationSet> getRelationSetsByConcept(Concept concept);
}
