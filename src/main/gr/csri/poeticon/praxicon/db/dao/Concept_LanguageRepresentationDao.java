/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao;

import gr.csri.poeticon.praxicon.db.entities.Concept_LanguageRepresentation;
import java.util.List;

/**
 *
 * @author dmavroeidis
 */
public interface Concept_LanguageRepresentationDao extends
        Dao<Long, Concept_LanguageRepresentation> {

    List<Concept_LanguageRepresentation> 
        getAllConcept_LanguageRepresentations();

}
