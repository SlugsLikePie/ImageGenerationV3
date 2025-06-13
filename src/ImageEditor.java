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

    // Used for rectangularBlur
    private int rectangularAreaColorAverage(int x, int y, int xRadius, int yRadius) {
        int rSum = 0;
        int gSum = 0;
        int bSum = 0;

        int numPx = 0;

        for (int xI = x - xRadius; xI <= x + xRadius; xI++)
        for (int yI = y - yRadius; yI <= y + yRadius; yI++) {
            if (xI >= 0 && xI < editingImgWidth && yI >= 0 && yI < editingImgHeight) {
                int rgb = editingBImg.getRGB(xI, yI);
                // System.out.println(rgb);

                rSum += (rgb >> 16) & 0xFF;
                gSum += (rgb >> 8) & 0xFF;
                bSum += rgb & 0xFF;
                
                numPx++;
            }
        }

        return ((rSum / numPx) << 16) + ((gSum / numPx) << 8) + (bSum / numPx);
    }

    public EditableImage rectangularBlur(int iterations, int xRadius, int yRadius) {
        Instant start = Instant.now();
        
        BufferedImage outputImg = new BufferedImage(
            editingBImg.getWidth(), 
            editingBImg.getHeight(), 
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

    // public EditableImage adaptiveTriangularMosaic(int iterations, int xScale, int yScale) {

    // }
}
