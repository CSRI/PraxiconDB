/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.logs.dao.Impl;

import csri.poeticon.praxicon.db.dao.implSQL.JpaDao;
import csri.poeticon.praxicon.logs.SentenceToAnalyze;
import csri.poeticon.praxicon.logs.dao.SentenceToAnalyzeDAO;

/**
 *
 * @author Erevodifwntas
 */
public class SentenceToAnalyzeDAOImpl extends JpaDao<Long, SentenceToAnalyze> implements SentenceToAnalyzeDAO{

    @Override
    public void persist(SentenceToAnalyze entity) {
        if(!entityManager.getTransaction().isActive())
        {
            entityManager.getTransaction().begin();
        }
        try
        {
            entityManager.persist(entity);
            entityManager.flush();
            entityManager.getTransaction().commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        }
        finally
        {

        }
    }
}
