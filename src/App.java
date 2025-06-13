public class App {
    public static void main(String[] args) throws Exception {
        EditableImage img = new EditableImage("Background.jpg");
        ImageEditor imgE = new ImageEditor(img);
        
        img = imgE.rectangularBlur(1, 1, 1);

        img.writeFile();
    }
}
