public class SolidBlock extends Block {

    public SolidBlock(int x, int y) {
        super(x, y, "res/solid-block-default.png");
    }

    public SolidBlock(int x, int y, String texturePath) {
        super(x, y, texturePath);
    }

    public void checkCollisionWith(Entity e) {

    }
}
