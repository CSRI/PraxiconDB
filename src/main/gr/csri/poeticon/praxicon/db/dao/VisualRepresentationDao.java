/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao;

import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.VisualRepresentation;
import java.util.List;

/**
 *
 * @author dmavroeidis
 */
public interface VisualRepresentationDao extends Dao<Long, VisualRepresentation> {

    List<VisualRepresentation> getEntries(Concept concept);
}
