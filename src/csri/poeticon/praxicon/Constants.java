package csri.poeticon.praxicon;

import csri.poeticon.praxicon.db.entities.CollectionOfConcepts;
import csri.poeticon.praxicon.db.entities.Concept;
import java.util.Hashtable;

/**
 *
 * @author Erevodifwntas
 *
 * All the constants and globals of the project
 */
public class Constants {
    
    /**
     * These paths are used by the server version of the praxicon
     */
    /*
    public static String ImagePath = "http://10.1.2.9:8080/Praxicon/resources/images/";
    public static String VideoPath = "http://10.1.2.9:8080/Praxicon/resources/videos/";
    public static String SoundPath = "http://10.1.2.9:8080/Praxicon/resources/sounds/";*/

    /**
     * A global variable that contains all the concepts that have been loaded from an xml.
     * It is used in the XML mode (where we do not have any db)
     */
    public static Hashtable globalConcepts = new Hashtable();

    /**
     * A global variable that contains all the concepts that have been loaded from Wordnet.
     * It is used in the Wordnet mode (where we do not have any db)
     */
    public static CollectionOfConcepts wordNetConcepts;

    /**
     * The path to the image resources
     */
    public static String ImagePath = "file:c:/Praxicon/resources/images/";

    /**
     * The path to the LabelMe image resources
     */
    public static String ImagePathLabelMe = "C:/Praxicon/resources/images/LabelMe/";

    /**
     * The path to the ImageNet image resources
     */
    public static String ImagePathImageNet = "C:/Praxicon/resources/images/ImageNet/";
    /**
     * The path to the video resources
     */
    public static String VideoPath = "file:c:/Praxicon/resources/videos/";
    /**
     * * The path to the sound resources
     */
    public static String SoundPath = "file:c:/Praxicon/resources/sounds/";

    /**
     * This is used by the similarity functions, to store which objects we have already visited
     */
    public static Hashtable<String, Concept> conceptsVisited = new Hashtable();
    /**
     * A weight that it is being used by the variable solver (the weight of an inherent relation)
     */
    public static double weightForVariableSolver = 100;

    /**
     * The url string to labelMe
     */
    public static String LABELME="http://people.csail.mit.edu/torralba/research/LabelMe/js/LabelMeQueryObjectFast.cgi?query=";

    /**
     * The url string to labelMe actual images
     */
    public static String LABELME_IMAGES="http://labelme.csail.mit.edu/Images/";

    /**
     * The url string to ImageNet
     */
    public static String IMAGENET="http://www.image-net.org/api/text/imagenet.synset.geturls?wnid=";

}
