import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Block extends Entity implements Drawable {

    protected transient BufferedImage texture;

    public Block(int x, int y) {
        this(x, y, "res/block-default.png");
    }

    public Block(int x, int y, String texturePath) {
        this(x, y, texturePath, false);
    }

    public Block(int x, int y, String texturePath, boolean isKinematic) {
        this.x = x;
        this.y = y;
        this.width = 1 * Window.SCALE;
        this.height = 1 * Window.SCALE;
        this.isKinematic = isKinematic;
        try {
            this.texture = ImageUtility.resize(ImageIO.read(new File(texturePath)), width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTexture(String texturePath) {
        try {
            this.texture = ImageUtility.resize(ImageIO.read(new File(texturePath)), this.width, this.height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {
        if(texture == null) {
            setTexture("res/block-default.png");
        }
        g.drawImage(texture, x, y, null);
    }
}
