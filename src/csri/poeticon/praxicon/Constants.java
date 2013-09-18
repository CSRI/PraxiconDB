package csri.poeticon.praxicon;

import csri.poeticon.praxicon.db.entities.CollectionOfConcepts;
import csri.poeticon.praxicon.db.entities.Concept;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

/**
 * All the constants and globals of the project. 
 * All properties taken from "settings.properties" file.
 * 
 * @author Dimitris Mavroeidis
 * @date 18/09/2013
 * 
 */
public class Constants {

    // DB settings constants
    public static String dbHost;
    public static String dbPort;
    public static String dbName;
    public static String dbUser;
    public static String dbPass;

    // Path constants
    public static String imagePath;
    public static String imagePathLabelMe;
    public static String imagePathImageNet;
    public static String videoPath;
    public static String soundPath;

    // URL constants
    public static String LabelMeURL;
    public static String LabelMeImagesURL;
    public static String ImageNetURL;

    // Other constants
    public static double weightForVariableSolver;


    /**
     * This is used by the similarity functions, to store which objects we have already visited
     */
    public static Hashtable<String, Concept> conceptsVisited = new Hashtable();
    
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


    public static void Constants() throws FileNotFoundException, IOException{

        Properties props = new Properties();
        FileInputStream fis = new FileInputStream("settings.properties");
        props.load(fis);

        /**
         * Get database settings.
         */
        dbHost = props.getProperty("db.host");
        dbPort = props.getProperty("db.port");
        dbName = props.getProperty("db.name");
        dbUser = props.getProperty("db.username");
        dbPass = props.getProperty("db.password");

        /**
         * The path to the image resources
         */
        imagePath = props.getProperty("path.images");

        /**
         * The path to the LabelMe image resources
         */
        imagePathLabelMe = props.getProperty("path.LabelMe");

        /**
         * The path to the ImageNet image resources
         */
        imagePathImageNet = props.getProperty("path.ImageNet");

        /**
         * The path to the video resources
         */
        videoPath = props.getProperty("path.videos");

        /**
         * * The path to the sound resources
         */
        soundPath = props.getProperty("path.sounds");

        /**
         * The URL string to labelMe
         */
        LabelMeURL = props.getProperty("url.LabelMe");

        /**
         * The URL string to labelMe actual images
         */
        LabelMeImagesURL = props.getProperty("url.LabelMeImages");

        /**
         * The URL string to ImageNet
         */
        ImageNetURL = props.getProperty("url.ImageNet");

        /**
         * A weight that it is being used by the variable solver (the weight of an inherent relation)
         */
        weightForVariableSolver = Double.parseDouble(props.getProperty("const.variableSolverWeight"));
        System.out.println("db.username: " + dbName);
        System.out.println("db.host: " + dbHost);

    }

}
