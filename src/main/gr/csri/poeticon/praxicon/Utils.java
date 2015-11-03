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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public static void main(String args[]) throws IOException {
        importPhotosFromImagenet();
        System.exit(0);
    }

    public static void importPhotosFromImagenet() throws IOException {
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
                    VisualRepresentation vr = new VisualRepresentation(
                            VisualRepresentation.MediaType.IMAGE,
                            columnDetail[0]);
                    int begin_index = columnDetail[1].lastIndexOf(".");
                    String save_path = "";
                    String image_extension = "";

                    //
                    try {
                        image_extension = columnDetail[1].substring(
                                begin_index, begin_index + 4);
                        save_path = "/home/dmavroeidis/ImageNet/" +
                                columnDetail[0] + image_extension;
                    } catch (StringIndexOutOfBoundsException e) {
                        continue;
                    }

                    System.out.println("FILE: " + save_path + "\tURL: " +
                            columnDetail[1]);
                    // Check if file is an image
                    Image image = null;
                    try {
                        URL url = new URL(columnDetail[1]);
                        image = ImageIO.read(url);
                        if (image == null) {
                            corrupted = true;
                            continue;
                        }
                    } catch (IIOException e) {
                        corrupted = true;
                        continue;
                    }

                    if (!corrupted) {
                        try (InputStream in = new URL(columnDetail[1]).
                                openStream()) {
                            Files.copy(in, Paths.get(save_path));
                        } catch (IllegalArgumentException | ConnectException |
                                UnknownHostException |
                                FileAlreadyExistsException |
                                FileNotFoundException ex) {
                            corrupted = true;
                            continue;
                        } catch (IOException ioE) {
                            corrupted = true;
                            continue;
                        }

                        try {
                            ImageAnalysisResult iaResult = analyzeImage(
                                    Paths.get(save_path));
                            System.out.print("RESULT: \t");
                            if (iaResult.truncated) {
                                System.out.println("TRUNCATED");
                                corrupted = true;
                                continue;
                            } else if (iaResult.image) {
                                System.out.println("IMAGE");
                                corrupted = false;
                            }
                        } catch (IOException eofE) {
                            corrupted = true;
                            continue;
                        } catch (NoSuchAlgorithmException eee) {
                            corrupted = true;
                            continue;
                        }

                    } else {
                        System.err.println("File " + save_path +
                                " is corrupt! ");
                        corrupted = true;
                        continue;
                    }

                    try {
                        if (corrupted) {
                            Files.delete(Paths.get(save_path));
                            continue;
                        }
                        vr.setURI(columnDetail[1]);
                        vr.setConcept(concept);
                        vr.setSource("ImageNet_fall_2011");
                        vrDao.merge(vr);
                        cDao.clearManager();
                    } catch (URISyntaxException | MalformedURLException ex) {
                        cDao.clearManager();
                        Logger.getLogger(SimpleTest.class.getName()).
                                log(Level.SEVERE, null, ex);
                    } catch (IOException e) {
                        Logger.getLogger(SimpleTest.class.getName()).
                                log(Level.SEVERE, null, e);
                        cDao.clearManager();
                    }
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
