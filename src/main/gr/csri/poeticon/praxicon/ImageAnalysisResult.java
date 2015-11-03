/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon;

/**
 *
 * @author dmavroeidis
 */
public class ImageAnalysisResult {

    boolean image;
    boolean truncated;

    public void setImage(boolean image) {
        this.image = image;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }
}
