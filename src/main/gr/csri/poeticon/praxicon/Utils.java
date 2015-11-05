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
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.stream.Stream;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 *
 * @author dmavroeidis
 */
public class Utils {

    public static void main(String args[]) throws IOException,
            URISyntaxException, NoSuchAlgorithmException {
        importPhotosFromImagenet();
        System.exit(0);
    }

    public static void importPhotosFromImagenet() throws IOException,
            URISyntaxException, NoSuchAlgorithmException {
        /*
         1. Read file with image URLs
         2. For each line,
         2.1. Get the first column as the sysnset
         2.1.1. Replace the first digit from n to 1.
         2.1.2. Cut all the part from the last underscore.
         2.1.3. Save this as the sysnset ID.
         2.2. Get the second column as the URL
         2.3. Find the Concept according to the sysnset ID (externalSourceId)
         2.4. Add the Visual Representation with the following fields:
         2.4.1. MediaType = IMAGE
         2.4.2. Source = ImageNet_fall_2011
         2.4.3. Uri = URL
         2.4.4. Name = first column
         */

        ConceptDao cDao = new ConceptDaoImpl();
        Concept concept = new Concept();
        VisualRepresentationDao vrDao = new VisualRepresentationDaoImpl();
        java.nio.file.Path path = Paths.get(
                "/home/dmavroeidis/Desktop/fall11_urls.txt");
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            for (String line : (Iterable<String>)lines::iterator) {
                String[] columnDetail = new String[2];
                columnDetail = line.split("\t", -1);
                String synsetId = columnDetail[0].replace("n", "1").substring(
                        0, 9);
                concept = cDao.getConceptByExternalSourceIdExact(synsetId);

                // Only get the first 5 entries of BASIC_LEVEL and-below concepts
                if ((concept.getVisualRepresentationsEntries().size() < 5) &&
                        (concept.getSpecificityLevel() !=
                        Concept.SpecificityLevel.SUPERORDINATE)) {
                    boolean corrupted = false;
                    int begin_index = columnDetail[1].lastIndexOf(".");
                    String save_path = "";
                    String image_extension = "";

                    image_extension = columnDetail[1].substring(
                            begin_index, begin_index + 4);
                    save_path = "/home/dmavroeidis/Desktop/ImageNet/" +
                            columnDetail[0] + image_extension;

                    System.out.println("FILE: " + save_path + "\tURL: " +
                            columnDetail[1]);

                    // Check if file is an image
                    Image image = null;
                    URL image_url = new URL(columnDetail[1]);
                    URLConnection conn = image_url.openConnection();
                    conn.setConnectTimeout(3000);
                    conn.setReadTimeout(3000);
                    InputStream in = null;  //Initialize input stream
                    try {
                        in = conn.getInputStream();
                    } catch (IOException | IllegalArgumentException ioE) {
                        continue;
                    }

                    // Copy the image from the web
                    try {
                        Files.copy(in, Paths.get(save_path));
                    } catch (FileAlreadyExistsException | NoSuchFileException |
                            SocketTimeoutException faeE) {
                        continue;
                    }

                    image = ImageIO.read(in);
                    System.out.println("IMAGE: " + image);
                    if (image == null || image.getWidth(null) < 201 ||
                            image.getHeight(null) < 201) {
                        System.err.println("File not an image or small in size");
                        corrupted = true;
                    }

                    ImageAnalysisResult iaResult = new ImageAnalysisResult();
                    try {
                        iaResult = analyzeImage(Paths.get(save_path));
                    } catch (IllegalArgumentException iaE) {
                        corrupted = true;
                    }

                    System.out.print("RESULT: \t");
                    if (iaResult.truncated) {
                        System.out.println("TRUNCATED");
                        corrupted = true;
                    } else if (iaResult.image) {
                        System.out.println("IMAGE");
                        corrupted = false;
                    } else {
                        System.out.println("[Result is not available]");
                    }

                    if (corrupted) {
                        Files.delete(Paths.get(save_path));
                        continue;
                    }
                    
                    VisualRepresentation vr = new VisualRepresentation(
                            VisualRepresentation.MediaType.IMAGE,
                            columnDetail[0]);
                    vr.setURI(columnDetail[1]);
                    vr.setConcept(concept);
                    vr.setSource("ImageNet_fall_2011");
                    vrDao.merge(vr);
                    cDao.clearManager();
                }
            }
        }
    }

    public static ImageAnalysisResult analyzeImage(final Path file)
            throws NoSuchAlgorithmException, IOException {
        final ImageAnalysisResult result = new ImageAnalysisResult();
        final InputStream digestInputStream = Files.newInputStream(file);

        try {
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
        } finally {
            digestInputStream.close();
        }
        return result;
    }
}
