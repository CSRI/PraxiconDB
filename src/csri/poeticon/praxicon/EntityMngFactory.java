package csri.poeticon.praxicon;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
/**
 *
 * @author Erevodifwntas
 *
 * This class administrates everything that have to do with the Entity Manager
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

    /**
     * It was used by the singleton pattern, but we didn't use this pattern after all
     */
    private static void createEntityMngFactory()
    {
        emf = javax.persistence.Persistence.createEntityManagerFactory(Globals.JpaPU);
        em = emf.createEntityManager();
    }

    /**
     * The constructor
     */
    private EntityMngFactory()
    {
        createEntityMngFactory();
    }

    /**
     * It was used by the singleton pattern, but we didn't use this pattern after all
     */
    public static void restart()
    {
        createEntityMngFactory();
    }

    public static EntityManager getEntityManager()
    {
        if (em == null || !em.isOpen())
        {
            createEntityMngFactory();
        }
        return em;
    }

    /**
     * It closes the Entity Manager
     */
    public static void close()
    {
        em = null;
    }
}
