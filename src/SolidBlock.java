import java.awt.*;

public class SolidBlock extends Block {

    public SolidBlock(int x, int y) {
        this(x, y, "res/solid-block-default.png");
    }

    public SolidBlock(int x, int y, String texturePath) {
        super(x, y, texturePath);
    }

    @Override
    public void draw(Graphics g) {
        if(texture == null) {
            setTexture("res/solid-block-default.png");
        }
        g.drawImage(texture, x, y, null);
    }
}
