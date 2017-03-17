/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.dao.implSQL;

import gr.csri.poeticon.praxicon.db.dao.Concept_LanguageRepresentationDao;
import gr.csri.poeticon.praxicon.db.entities.Concept_LanguageRepresentation;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author dmavroeidis
 */
public class Concept_LanguageRepresentationDaoImpl extends
        JpaDao<Long, Concept_LanguageRepresentation> implements
        Concept_LanguageRepresentationDao {

    /**
     * Finds all the Language Representations.
     *
     * @return a list of all Language Representations in the database
     */
    @Override
    public List<Concept_LanguageRepresentation>
            getAllConcept_LanguageRepresentations() {
        Query query = getEntityManager().createNamedQuery(
                "findAllConcept_LanguageRepresentations");
        List<Concept_LanguageRepresentation> retrievedConcept_LanguageRepresentationsList =
                new ArrayList<>(query.getResultList());
        return retrievedConcept_LanguageRepresentationsList;
    }
}
