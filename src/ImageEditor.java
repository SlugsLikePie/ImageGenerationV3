import java.awt.image.BufferedImage;


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

    public EditableImage outputImg(BufferedImage outputImg) {
        if (mode == ImageEditorMode.RETURN_AND_REPLACE) {
            editingImg = new EditableImage();
        }

        return new EditableImage(outputImg);
    }

    public EditableImage rectangularBlur() {
        BufferedImage outputImg = new BufferedImage(
            editingBufferedImg.getWidth(), 
            editingBufferedImg.getHeight(), 
            editingBufferedImg.getType()
        );

        // Blurring code goes here

        return outputImg(outputImg);
    }

}
