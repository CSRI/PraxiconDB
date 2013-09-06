/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.logs;

import csri.poeticon.praxicon.logs.dao.Impl.SentenceToAnalyzeDAOImpl;
import csri.poeticon.praxicon.logs.dao.SentenceToAnalyzeDAO;

/**
 *
 * @author Erevodifwntas
 */
public class TestLogging {

    public static void main(String args[])
    {
        SentenceToAnalyze sentence = new SentenceToAnalyze();
        sentence.setSentence("What is this");

        SentenceToAnalyzeDAO sDao = new SentenceToAnalyzeDAOImpl();
        sDao.persist(sentence);
    }
}
