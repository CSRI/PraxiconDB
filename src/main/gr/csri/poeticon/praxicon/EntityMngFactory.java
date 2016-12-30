package gr.csri.poeticon.praxicon;

import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author dmavroeidis
 *
 * This class administrates everything that has to do with the Entity Manager
 */
public class EntityMngFactory {

    /**
     * The entity Manager Factory
     */
    private static EntityManagerFactory emf = null;

    /**
     * The Entity Manager
     */
    private static EntityManager em = null;

    private static void createEntityMngFactory() {
        /*
         * Check the environment (testing or production) and choose
         * the appropriate persistence unit.
         */
        String persistenceUnit;
        if (inUnitTest()) {
            persistenceUnit = Globals.JpaPUDerbyTest;
        } else {
            persistenceUnit = Globals.JpaPU;
        }

        emf = javax.persistence.Persistence.createEntityManagerFactory(
                persistenceUnit);
        em = emf.createEntityManager();
    }

    /**
     * The constructor
     */
    private EntityMngFactory() {
        createEntityMngFactory();
    }

    /**
     * It was used by the singleton pattern, but we didn't use this pattern
     * after all
     */
    public static void restart() {
        createEntityMngFactory();
    }

    public static EntityManager getEntityManager() {
        if (em == null || !em.isOpen()) {
            createEntityMngFactory();
        }
        return em;
    }

    /**
     * It closes the Entity Manager
     */
    public static void close() {
        em.close();
//        em = null;
    }

    public static boolean inUnitTest() {
        /*
         * Addopted from http://stackoverflow.com/questions/2341943/
         * how-can-i-find-out-if-code-is-running-inside-a-junit-test-or-not
         */
        StackTraceElement[] stackTrace = Thread.currentThread().
                getStackTrace();
        List<StackTraceElement> list = Arrays.asList(stackTrace);
        for (StackTraceElement element : list) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }
        return false;
    }
}
