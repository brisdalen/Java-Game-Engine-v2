package LevelParsing;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;

public class Level {
    private String id;
    private int width;
    private int height;
    private String levelDetails;
    private BufferedImage image;

    public Level(String id, int width, int height, String levelDetails) {
        this(id, width, height);
        this.levelDetails = levelDetails;
    }

    public Level(String id, int width, int height, BufferedImage image) {
        this(id, width, height);
        this.image = image;
    }

    public Level(int width, int height, String levelDetails) {
        this("unknown", width, height);
        this.levelDetails = levelDetails;
    }

    public Level(int width, int height, BufferedImage image) {
        this("unknown", width, height);
        this.image = image;
    }

    public Level(String id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getLevelDetails() {
        return levelDetails;
    }

    public BufferedImage getImage() {
        return image;
    }

    @Override
    public String toString() {
        return levelDetails;
    }
}
