/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao;

import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
import gr.csri.poeticon.praxicon.db.entities.RelationSet;
import java.util.Set;

/**
 *
 * @author dmavroeidis
 */
public interface RelationArgumentDao extends Dao<Long, RelationArgument> {

    Set<RelationArgument> getAllRelationArguments();

    RelationArgument getRelationArgument(Concept concept);

    RelationArgument getRelationArgument(RelationSet relationSet);

}
