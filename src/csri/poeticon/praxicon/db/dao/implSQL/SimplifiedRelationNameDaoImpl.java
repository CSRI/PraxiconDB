/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.dao.implSQL;

import csri.poeticon.praxicon.db.dao.SimplifiedRelationNameDao;
import javax.persistence.Query;
import csri.poeticon.praxicon.db.entities.SimplifiedRelationName;
import java.util.List;


/**
 *
 * @author Erevodifwntas
 */
public class SimplifiedRelationNameDaoImpl extends JpaDao<Long, SimplifiedRelationName> implements SimplifiedRelationNameDao
{
    /**
     * Gives the simple relation name of a given complex relation name
     * @param complexName the complex relaiton name
     * @return a string
     */
    @Override
    public String getSimplifiedName(String complexName)
    {
        Query q = getEntityManager().createQuery("SELECT srn FROM SimplifiedRelationName srn " +
                "where UPPER(srn.ComplexName) = ?1"
                );
        q.setParameter(1, complexName.toUpperCase());
        List tmp = q.getResultList();
        if (tmp ==null || tmp.size() ==0)
        {
            return null;
        }
        return ((SimplifiedRelationName)tmp.get(0)).getSimpleName();
    }
}
