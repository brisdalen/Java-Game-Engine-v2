package LevelParsing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class LevelParser {

    Scanner levelScanner;
    File level;

    public LevelParser() { }

    public Level parseLevelFromString(String filePath) {

        String levelDetails = "";

        level = new File(filePath);
        int levelWidth = 0;
        int levelHeight = 0;
        try {
            levelScanner = new Scanner(level);
            while (levelScanner.hasNextLine()) {
                levelHeight++;
                String nextLine = levelScanner.nextLine();
                levelDetails = levelDetails + nextLine;
                if(levelWidth == 0) {
                    levelWidth = levelDetails.trim().length();
                }
            }
            levelScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return new Level(lastElementOfPath(filePath), levelWidth, levelHeight, levelDetails);
    }

    public Level parseLevelFromImage(String filePath) {

        BufferedImage image = null;

        level = new File(filePath);
        int levelWidth = 0;
        int levelHeight = 0;

        try {
            image = ImageIO.read(level);
            levelWidth = image.getWidth();
            levelHeight = image.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Level(lastElementOfPath(filePath), levelWidth, levelHeight, image);
    }

    public String lastElementOfPath(String input) {
        return input.substring(input.indexOf('/'));
    }
}
