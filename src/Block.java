import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Block extends Entity implements Drawable {
    private int x;
    private int y;
    private int width;
    private int height;
    private BufferedImage texture;

    public Block(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 1;
        this.height = 1;
        try {
            this.texture = ImageIO.read(new File("../res/block-default.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(texture, x, y, null);
    }
}
