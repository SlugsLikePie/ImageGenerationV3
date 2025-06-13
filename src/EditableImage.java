import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class EditableImage {
    public BufferedImage img;
    public int width = 100;
    public int height = 100;
    
    public EditableImage() {
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public EditableImage(int width, int height) {
        this.width = width;
        this.height = height;
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public EditableImage(int width, int height, int type) {
        this.width = width;
        this.height = height;
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public EditableImage(BufferedImage img) {
        this.img = img;
    }

    public EditableImage(String path) {
        try {
            img = ImageIO.read(new File(path));
            width = img.getWidth();
            height = img.getHeight();
        } catch (IOException e) {}
    }

    public BufferedImage getBufferedImage() {
        return img;
    }

    public void writeFile() throws Exception {
        File f = new File("Output.png");
        ImageIO.write(img, "PNG", f);
        System.out.println("Finished");
    }

    public void writeFile(String name) throws Exception {
        File f = new File(name + ".png");
        ImageIO.write(img, "PNG", f);
        System.out.println("Finished");
    }
}
