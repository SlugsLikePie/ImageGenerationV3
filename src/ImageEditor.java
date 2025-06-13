import java.awt.image.BufferedImage;
import java.awt.Color;

public class ImageEditor {
    EditableImage editingImg;
    BufferedImage editingBufferedImg;
    ImageEditorMode mode = ImageEditorMode.RETURN_ONLY;

    public ImageEditor() {
        editingImg = new EditableImage();
        editingBufferedImg = editingImg.getBufferedImage();
    }

    public ImageEditor(ImageEditorMode mode) {
        editingImg = new EditableImage();
        editingBufferedImg = editingImg.getBufferedImage();
        this.mode = mode;
    }

    public ImageEditor(EditableImage editingImage) {
        this.editingImg = editingImage;
        editingBufferedImg = editingImage.getBufferedImage();
    }

    public ImageEditor(EditableImage editingImage, ImageEditorMode mode) {
        this.editingImg = editingImage;
        editingBufferedImg = editingImage.getBufferedImage();
        this.mode = mode;
    }

    public EditableImage output(BufferedImage outputImg) {
        if (mode == ImageEditorMode.RETURN_AND_REPLACE) {
            editingImg = new EditableImage();
        }

        return new EditableImage(outputImg);
    }

    // private int rectangularAreaColorAverage(int x, int y, int xSize, int ySize) {
        
    // }

    public EditableImage rectangularBlur(int iterations, int xSize, int ySize) {
        BufferedImage outputImg = new BufferedImage(
            editingBufferedImg.getWidth(), 
            editingBufferedImg.getHeight(), 
            editingBufferedImg.getType()
        );

        for (int i = 0; i < iterations; i++)
        for (int x = 0; x < editingBufferedImg.getWidth(); x++)
        for (int y = 0; y < editingBufferedImg.getHeight(); y++) {
            outputImg.setRGB(
                x, y, 1324
            );
        }

        return output(outputImg);
    }

}
