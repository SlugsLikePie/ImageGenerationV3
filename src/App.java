public class App {
    public static void main(String[] args) throws Exception {
        EditableImage img = new EditableImage("Background.jpg");
        ImageEditor imgE = new ImageEditor(img, ImageEditorOutputMode.RETURN_AND_REPLACE);
        
        // imgE.sumImageWithRandomNoise(0.1);
        // imgE.rectangularBlur(5, 1, 1);
        imgE.adaptiveTriangularMosaic(5, 5);

        img = imgE.getEditableImage();

        img.writeFile();
    }
}
