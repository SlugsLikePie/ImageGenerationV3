import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import java.awt.Color;
import java.awt.Graphics2D;

public class ImageEditor {
    EditableImage editingImg;
    BufferedImage editingBImg;

    int editingImgWidth;
    int editingImgHeight;

    ImageEditorOutputMode mode = ImageEditorOutputMode.RETURN_AND_REPLACE;

    public ImageEditor() {
        editingImg = new EditableImage();
        editingBImg = editingImg.getBufferedImage();
        editingImgWidth = editingBImg.getWidth();
        editingImgHeight = editingBImg.getHeight();
    }

    public ImageEditor(ImageEditorOutputMode mode) {
        editingImg = new EditableImage();
        editingBImg = editingImg.getBufferedImage();
        editingImgWidth = editingBImg.getWidth();
        editingImgHeight = editingBImg.getHeight();
        this.mode = mode;
    }

    public ImageEditor(EditableImage editingImage) {
        this.editingImg = editingImage;
        editingBImg = editingImage.getBufferedImage();
        editingImgWidth = editingBImg.getWidth();
        editingImgHeight = editingBImg.getHeight();
    }

    public ImageEditor(EditableImage editingImage, ImageEditorOutputMode mode) {
        this.editingImg = editingImage;
        editingBImg = editingImage.getBufferedImage();
        editingImgWidth = editingBImg.getWidth();
        editingImgHeight = editingBImg.getHeight();
        this.mode = mode;
    }

    public EditableImage output(BufferedImage outputImg) {
        if (mode == ImageEditorOutputMode.RETURN_AND_REPLACE) {
            editingImg = new EditableImage(outputImg);
            editingBImg = outputImg;
        }

        return new EditableImage(outputImg);
    }

    public EditableImage getEditableImage() {
        return editingImg;
    }

    public BufferedImage getBufferedImage() {
        return editingBImg;
    }

    // Used for rectangularBlur. Takes the average of the pixels in a rectangle around a pixel
    private int rectangularAreaColorAverage(BufferedImage outputImage, int x, int y, int xRadius, int yRadius) {
        int rSum = 0;
        int gSum = 0;
        int bSum = 0;

        int numPx = 0;

        for (int xI = x - xRadius; xI <= x + xRadius; xI++)
        for (int yI = y - yRadius; yI <= y + yRadius; yI++) {
            if (xI >= 0 && xI < editingImgWidth && yI >= 0 && yI < editingImgHeight) {
                int rgb = outputImage.getRGB(xI, yI);

                rSum += (rgb >> 16) & 0xFF;
                gSum += (rgb >> 8) & 0xFF;
                bSum += rgb & 0xFF;
                
                numPx++;
            }
        }

        return ((rSum / numPx) << 16) | ((gSum / numPx) << 8) | (bSum / numPx);
    }

    // Simple unweighted blur based on the rectangle of pixels that surround a pixel
    public EditableImage rectangularBlur(int iterations, int xRadius, int yRadius) {
        Instant start = Instant.now();
        
        BufferedImage outputImg = editingBImg; // PROBABLY CHANGE TO BufferedImage outputImg = editingImg.getBufferedImage(); // NEVERMIND, BUT IS STILL BROKEN BECAUSE OF THE WAY OBJECT ASSIGNMENTS WORK

        for (int i = 0; i < iterations; i++)
        for (int x = 0; x < editingBImg.getWidth(); x++)
        for (int y = 0; y < editingBImg.getHeight(); y++) {
            outputImg.setRGB(
                x, y, rectangularAreaColorAverage(outputImg, x, y, xRadius, yRadius)
            );
        }

        Instant end = Instant.now();
        System.out.println("Rectangular blurring completed in " + Duration.between(start, end).toMillis() + " milliseconds");

        return output(outputImg);
    }

    // Fills an image with random noise, overwriting all previous data
    public EditableImage replaceImageWithRandomNoise() throws Exception {
        Instant start = Instant.now();
        
        BufferedImage outputImg = new BufferedImage(
            editingImgWidth, 
            editingImgHeight, 
            editingBImg.getType()
        );

        for (int x = 0; x < editingImgWidth; x++)
        for (int y = 0; y < editingImgHeight; y++) {
            outputImg.setRGB(
                x, y, (int) (Math.random() * 0x1000000)
            );
        }

        Instant end = Instant.now();
        System.out.println("Noise replacement completed in " + Duration.between(start, end).toMillis() + " milliseconds");

        return output(outputImg);
    }

    // Multiplies the color values in an image by random noise
    public EditableImage sumImageWithRandomNoise(double noiseWeight) throws Exception { // NEED TO IMPLEMENT WEIGHT
        Instant start = Instant.now();
        
        BufferedImage outputImg = new BufferedImage(
            editingImgWidth, 
            editingImgHeight, 
            editingBImg.getType()
        );

        for (int x = 0; x < editingImgWidth; x++)
        for (int y = 0; y < editingImgHeight; y++) {
            int rgb = editingBImg.getRGB(x, y);

            outputImg.setRGB(
                x, y, (int) (((Math.random() * 0x100 * noiseWeight) + (rgb >> 16 & 0xFF) * (1 - noiseWeight))) << 16 | 
                      (int) (((Math.random() * 0x100 * noiseWeight) + (rgb >> 8 & 0xFF) * (1 - noiseWeight))) << 8 | 
                      (int) ((Math.random() * 0x100 * noiseWeight) + (rgb & 0xFF) * (1 - noiseWeight))
            );
        }

        Instant end = Instant.now();
        System.out.println("Noise sum completed in " + Duration.between(start, end).toMillis() + " milliseconds");

        return output(outputImg);
    }

    private int[] rectangularAreaFindLeastSimilar(BufferedImage editingBImage, int x, int y, int xScale, int yScale) {
        int rgbO = editingBImage.getRGB(x, y);
        int rO = (rgbO >> 16) & 0xFF;
        int gO = (rgbO >> 8) & 0xFF;
        int bO = rgbO & 0xFF;

        int[] leastSimilarCoordinatesAndColor = {x, y, Integer.MIN_VALUE};

        for (int xI = x - xScale; xI <= x + xScale; xI++)
        for (int yI = y - yScale; yI <= y + yScale; yI++) {
            if (xI >= 0 && xI < editingImgWidth && yI >= 0 && yI < editingImgHeight && xI != x && xI != y) {
                int rgbI = editingBImage.getRGB(xI, yI);
                int rI = (rgbI >> 16) & 0xFF;
                int gI = (rgbI >> 8) & 0xFF;
                int bI = rgbI & 0xFF;

                // if (Math.sqrt(Math.pow(rI - rO, 2) + Math.pow(gI - gO, 2) + Math.pow(bI - bO, 2)) > leastSimilarCoordinatesAndColor[2]) {
                if (Math.random() * 5 > 2.5) {
                    leastSimilarCoordinatesAndColor[0] = xI;
                    leastSimilarCoordinatesAndColor[1] = yI;
                    leastSimilarCoordinatesAndColor[2] = rgbI;
                }
            }
        }

        return leastSimilarCoordinatesAndColor;
    }

    public EditableImage adaptiveTriangularMosaic(int xScale, int yScale) {
        Instant start = Instant.now();
        
        BufferedImage outputImg = new BufferedImage(
            editingImgWidth, 
            editingImgHeight, 
            editingBImg.getType()
        );

        Graphics2D outputImgG2D = outputImg.createGraphics();

        for (int x = 0; x < editingImgWidth; x++)
        for (int y = 0; y < editingImgHeight; y++) {
            int rgb = editingBImg.getRGB(x, y);
            
            int[] leastSimilarCoordinatesAndColor = rectangularAreaFindLeastSimilar(editingBImg, x, y, xScale, yScale);

            outputImgG2D.setColor(new Color(rgb));
            outputImgG2D.drawLine(x, y, leastSimilarCoordinatesAndColor[0], leastSimilarCoordinatesAndColor[1]);
        }

        Instant end = Instant.now();
        System.out.println("Adaptive triangular mosaic overlay completed in " + Duration.between(start, end).toMillis() + " milliseconds");

        return output(outputImg);
    }
}
