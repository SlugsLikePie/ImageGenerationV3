import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import java.awt.Color;

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
    private int rectangularAreaColorAverage(int x, int y, int xRadius, int yRadius) {
        int rSum = 0;
        int gSum = 0;
        int bSum = 0;

        int numPx = 0;

        for (int xI = x - xRadius; xI <= x + xRadius; xI++)
        for (int yI = y - yRadius; yI <= y + yRadius; yI++) {
            if (xI >= 0 && xI < editingImgWidth && yI >= 0 && yI < editingImgHeight) {
                int rgb = editingBImg.getRGB(xI, yI);

                rSum += (rgb >> 16) & 0xFF;
                gSum += (rgb >> 8) & 0xFF;
                bSum += rgb & 0xFF;
                
                numPx++;
            }
        }

        return ((rSum / numPx) << 16) + ((gSum / numPx) << 8) + (bSum / numPx);
    }

    // Simple unweighted blur based on the rectangle of pixels that surround a pixel
    public EditableImage rectangularBlur(int iterations, int xRadius, int yRadius) {
        Instant start = Instant.now();
        
        BufferedImage outputImg = new BufferedImage(
            editingImgWidth, 
            editingImgHeight, 
            editingBImg.getType()
        );

        for (int i = 0; i < iterations; i++)
        for (int x = 0; x < editingBImg.getWidth(); x++)
        for (int y = 0; y < editingBImg.getHeight(); y++) {
            outputImg.setRGB(
                x, y, rectangularAreaColorAverage(x, y, xRadius, yRadius)
            );
        }

        Instant end = Instant.now();
        System.out.println("Rectangular blurring completed in " + Duration.between(start, end).toMillis() + " milliseconds");

        return output(outputImg);
    }

    // Fills an image with random noise // FIX
    public EditableImage replaceImageWithRandomNoise() throws Exception {
        Instant start = Instant.now();
        
        BufferedImage outputImg = new BufferedImage(
            editingImgWidth, 
            editingImgHeight, 
            editingBImg.getType()
        );

        for (int x = 0; x < editingImgWidth; x++)
        for (int y = 0; y < editingImgHeight; y++) {
            editingBImg.setRGB(
                x, y, new Color(
                (int)(Math.random() * 256),
                (int)(Math.random() * 256),
                (int)(Math.random() * 256)
                ).getRGB()
            );
        }

        Instant end = Instant.now();
        System.out.println("Noise replacement completed in " + Duration.between(start, end).toMillis() + " milliseconds");

        return output(outputImg);
    }

    // Multiplies the color values in an image by random noise
    // public void multiplyImageByRandomNoise(double iterations) throws Exception {
    //     for (int i = 0; i < iterations; i++)
    //     for (int x = 0; x < img.getWidth(); x++)
    //     for (int y = 0; y < img.getHeight(); y++) {
    //         Color o = new Color(img.getRGB(x, y));
    //         img.setRGB(
    //             x, y, new Color(
    //             (int)(o.getRed() * (Math.random())),
    //             (int)(o.getGreen() * (Math.random())),
    //             (int)(o.getBlue() * (Math.random()))
    //             ).getRGB()
    //         );
    //     }
    //     writeFile();
    // }

    // public EditableImage adaptiveTriangularMosaic(int iterations, int xScale, int yScale) {

    // }
}
