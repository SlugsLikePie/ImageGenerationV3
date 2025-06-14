public class App {
    public static void main(String[] args) throws Exception {
        EditableImage img = new EditableImage("Background.jpg");
        ImageEditor imgE = new ImageEditor(img, ImageEditorOutputMode.RETURN_AND_REPLACE);
        
        imgE.rectangularBlur(1, 1, 1);
        // imgE.replaceImageWithRandomNoise();

        img = imgE.getEditableImage();

        img.writeFile();
    }
}
