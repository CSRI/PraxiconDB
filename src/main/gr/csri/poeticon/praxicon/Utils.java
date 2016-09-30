/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon;

import gr.csri.poeticon.praxicon.db.dao.ConceptDao;
import gr.csri.poeticon.praxicon.db.dao.VisualRepresentationDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.VisualRepresentationDaoImpl;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.VisualRepresentation;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;

/**
 *
 * @author dmavroeidis
 */
public class Utils {

    public enum FileExtensions {

        jpeg, tiff, jpg, jpe, gif, tif, bmp
    }

    public static void main(String args[]) throws IOException,
            URISyntaxException, NoSuchAlgorithmException {
        importPhotosFromImagenet("/home/dmavroeidis/Desktop/ImageNet/");
        System.exit(0);
    }

    public static void importPhotosFromImagenet(String savePath) throws
            IOException, URISyntaxException, NoSuchAlgorithmException {
        /*
         * 1. Read file with image URLs
         * 2. For each line,
         * 2.1. Get the first column as the sysnset
         * 2.1.1. Replace the first digit from n to 1.
         * 2.1.2. Cut all the part from the last underscore.
         * 2.1.3. Save this as the sysnset ID.
         * 2.2. Get the second column as the URL
         * 2.3. Find the Concept according to the sysnset ID (externalSourceId)
         * 2.4. Add the Visual Representation with the following fields:
         * 2.4.1. MediaType = IMAGE
         * 2.4.2. Source = ImageNet_fall_2011
         * 2.4.3. Uri = URL
         * 2.4.4. Name = first column
         */

        ConceptDao cDao = new ConceptDaoImpl();
        Concept concept = new Concept();
        boolean corrupted = false;
        VisualRepresentationDao vrDao = new VisualRepresentationDaoImpl();

        try (BufferedReader br = new BufferedReader(new FileReader(
                "/home/dmavroeidis/Desktop/fall11_urls.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] columnDetail = new String[2];
                columnDetail = line.split("\t", -1);
                String url = columnDetail[1];
                String imageNetId = columnDetail[0];
                String synsetId = imageNetId.replace("n", "1").substring(0, 9);
                concept = cDao.getConceptByExternalSourceIdExact(synsetId);
                corrupted = false;
                // Only get the first 5 entries of BASIC_LEVEL and-below concepts
                if ((concept.getVisualRepresentationsEntries().size() < 5) &&
                        (concept.getSpecificityLevel() !=
                        Concept.SpecificityLevel.SUPERORDINATE)) {

                    // Check if file is an image
                    URL imageUrl = new URL(url);
                    URLConnection conn = imageUrl.openConnection();
                    conn.setConnectTimeout(3000);
                    conn.setReadTimeout(3000);
                    InputStream in = null;  //Initialize input stream
                    try {
                        in = conn.getInputStream();
                    } catch (IOException | IllegalArgumentException ioE) {
                        continue;
                    }

                    int beginIndex = url.lastIndexOf(".") + 1;
                    String saveFile;
                    String imageExtension;

                    imageExtension = url.substring(beginIndex).toLowerCase();

                    if (imageExtension.startsWith(FileExtensions.jpeg.
                            toString()) ||
                            imageExtension.startsWith(
                                    FileExtensions.tiff.toString())) {
                        saveFile = savePath + imageNetId + "." +
                                imageExtension.substring(0, 4);
                    } else if (imageExtension.
                            startsWith(FileExtensions.gif.toString()) ||
                            imageExtension.startsWith(FileExtensions.jpg.
                                    toString()) ||
                            imageExtension.startsWith(FileExtensions.tif.
                                    toString()) ||
                            imageExtension.startsWith(FileExtensions.jpe.
                                    toString()) ||
                            imageExtension.startsWith(FileExtensions.bmp.
                                    toString())) {
                        saveFile = savePath + imageNetId + "." +
                                imageExtension.substring(0, 3);
                    } else {
                        saveFile = savePath + imageNetId + "." + "jpg";
                    }

                    System.out.println("FILE: " + saveFile + "\tURL: " + url);

                    // Copy the image from the web.
                    try {
                        Files.copy(in, Paths.get(saveFile));
                    } catch (FileAlreadyExistsException | NoSuchFileException |
                            SocketTimeoutException | SocketException faeE) {
                        continue;
                    }

                    // Check the validity of the image.
                    ImageAnalysisResult iaResult = new ImageAnalysisResult();
                    try {
                        iaResult = analyzeImage(Paths.get(saveFile));
                    } catch (IllegalArgumentException iaE) {
                        corrupted = true;
                    }
                    if (iaResult.truncated) {
                        corrupted = true;
                    }

                    // Check size of the image.
                    ImageIcon img = new ImageIcon(saveFile);
                    int imageWidth = img.getIconWidth();
                    int imageHeight = img.getIconHeight();
                    if (imageWidth < 201 || imageHeight < 201) {
                        corrupted = true;
                    }

                    // Delete the image if it does not comply with standards.
                    if (corrupted) {
                        Files.delete(Paths.get(saveFile));
                        continue;
                    }

                    VisualRepresentation vr = new VisualRepresentation(
                            VisualRepresentation.MediaType.IMAGE, imageNetId);
                    vr.setURI(url);
                    vr.setConcept(concept);
                    vr.setSource("ImageNet_fall_2011");
                    vrDao.merge(vr);
                    cDao.clearManager();
                }
            }
        }
    }

    /**
     * Analyze the image, checking if it is truncated or not.
     *
     * @param file - The path to the image file
     *
     * @return whether the image is truncated or not
     *
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static ImageAnalysisResult analyzeImage(final Path file)
            throws NoSuchAlgorithmException, IOException {
        final ImageAnalysisResult result = new ImageAnalysisResult();
        try (InputStream digestInputStream = Files.newInputStream(file)) {
            final ImageInputStream imageInputStream = ImageIO
                    .createImageInputStream(digestInputStream);
            final Iterator<ImageReader> imageReaders = ImageIO
                    .getImageReaders(imageInputStream);
            if (!imageReaders.hasNext()) {
                result.setImage(false);
                return result;
            }
            final ImageReader imageReader = imageReaders.next();
            imageReader.setInput(imageInputStream);
            final BufferedImage image = imageReader.read(0);
            if (image == null) {
                return result;
            }
            image.flush();
            if (imageReader.getFormatName().equals("JPEG")) {
                imageInputStream.seek(imageInputStream.getStreamPosition() - 2);
                final byte[] lastTwoBytes = new byte[2];
                imageInputStream.read(lastTwoBytes);
                if (lastTwoBytes[0] != (byte)0xff || lastTwoBytes[1] !=
                        (byte)0xd9) {
                    result.setTruncated(true);
                } else {
                    result.setTruncated(false);
                }
            }
            result.setImage(true);
        } catch (final IndexOutOfBoundsException e) {
            result.setTruncated(true);
        } catch (final IIOException e) {
            if (e.getCause() instanceof EOFException) {
                result.setTruncated(true);
            }
        }
        return result;
    }
}
