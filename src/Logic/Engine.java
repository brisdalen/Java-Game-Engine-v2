package Logic;

import Enteties.Block;
import Enteties.Entity;
import Enteties.Player;
import Enteties.SolidBlock;
import LevelParsing.Level;
import LevelParsing.LevelParser;
import Rendering.Window;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Engine extends Timer {
    private ArrayList<Entity> entities;
    private ArrayList<Entity> previouslyRequestedEntities;

    private Player player;
    private Point previousPlayerPosition;
    private LevelParser levelParser;
    private Level level;
    private Level currentLevel;
    private Random randomer;
    public static final int SCALE_FACTOR = 25;

    private int currentWindowWidth;
    private int currentWindowHeight;
    private int startTime = -20;
    private int totalTime = startTime;

    private boolean gravity = false;

    private boolean running = false;
    private boolean hasStarted = false;
    private static boolean hasLost = false;
    private boolean resetPrompt = false;

    public static final float GRAVITY = 2f;

    private Point newPos;

    /**
     *
     * @param player The player to affect and get movement from.
     */
    public Engine(Player player) {
        this(player, Rendering.Window.CANVAS_WIDTH, Rendering.Window.CANVAS_HEIGHT, false, true);
    }

    /**
     *
     * @param player The player to affect and get movement from. (TODO: Multiplayer solution?)
     * @param beforeStart Should the player be able to move before the start method has been called.
     */
    public Engine(Player player, boolean beforeStart) {
        this(player, Window.CANVAS_WIDTH, Rendering.Window.CANVAS_HEIGHT, beforeStart, false);
    }
    /**
     *
     * @param player The player to affect and get movement from. (TODO: Multiplayer solution?)
     * @param beforeStart Should the player be able to move before the start method has been called.
     * @param gravity Should gravity be activated by default
     */
    public Engine(Player player, boolean beforeStart, boolean gravity) {
        this(player, Rendering.Window.CANVAS_WIDTH, Rendering.Window.CANVAS_HEIGHT, beforeStart, gravity);
    }

    public Engine(Player player, int windowWidth, int windowHeight, boolean beforeStart, boolean gravity) {

        entities = new ArrayList<>();
        previouslyRequestedEntities = new ArrayList<>();

        this.currentWindowWidth = windowWidth;
        this.currentWindowHeight = windowHeight;
        this.randomer = new Random();
        this.gravity = gravity;

        levelParser = new LevelParser();
        //level = levelParser.parseLevelFromImage("levels/level_3.png");
        //currentLevel = level;
        //loadLevel(currentLevel);

        level = levelParser.parseLevelFromImage("levels/level_4.png");
        currentLevel = level;
        loadLevel(currentLevel);

        this.player = player;
        player.setKinematic(gravity);

        //entities.add(player);

        /*entities.add(new Block(0, Rendering.Window.CANVAS_HEIGHT-100));
        entities.add(new Block(50, Rendering.Window.CANVAS_HEIGHT-100));
        entities.add(new Block(100, Rendering.Window.CANVAS_HEIGHT-100));

        entities.add(new SolidBlock(150, Rendering.Window.CANVAS_HEIGHT-100));
        entities.add(new SolidBlock(200, Rendering.Window.CANVAS_HEIGHT-100));
        entities.add(new SolidBlock(250, Rendering.Window.CANVAS_HEIGHT-100));

        entities.add(new SolidBlock(370, Rendering.Window.CANVAS_HEIGHT-100));

        entities.add(new SolidBlock(490, Rendering.Window.CANVAS_HEIGHT-100));
        */

        init(beforeStart);
    }

    public void init(boolean beforeStart) {
        running = true;
        System.out.println("Gravity: " + gravity);
        /**
         * Get player input and check for out-of-bounds before the start method has been called.
         */
        if(beforeStart) {
            scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (!hasLost && !hasStarted) {
                        checkPlayer(player);
                        if(gravity) {
                            playerGravity();
                        }
                    } else {
                        // Cancel thread when no longer needed
                        this.cancel();
                    }
                }
            }, 0, 40);
        }

    }

    public void start() {
        hasStarted = true;

        scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(!hasLost) {
                    checkPlayer(player);
                    if(gravity) {
                        playerGravity();
                    }
                }
                //If you lost, and resetPrompt has not been displayed yet
                if(hasLost && !resetPrompt) {
                    // TODO: Figure out how to update the button?
                    //drawPanel.updateParentFrameButton();
                    resetPrompt = true;
                    this.cancel();
                }
            }
        }, 0, 40);
        // Timings
        scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(!hasLost) {
                    //System.out.println(totalTime);
                    totalTime++;

                    if (totalTime % 10 == 0 && totalTime >= 0) {
                        // Do something
                    }

                } else {
                    this.cancel();
                }

            }
        }, 1000, 1000);
    }

    public void reset() {
        System.out.println("Logic.Engine RESET");
        /* TODO: Add code to reset engine
         *  - husk på forskjell på STOP og RESET
         *  - ny thread med totalTime ved hver reset?
         */
        resetPrompt = false;
        hasLost = false;
        totalTime = startTime;

        player.reset();

        running = true;

    }

    public void stop() {
        System.out.println("Logic.Engine STOP");
        hasLost = true;
        running = false;
        //hasStarted = false;
    }

    public void saveLevel(String path) {
        //TODO: Lage metode for å lagre levels
    }

    public void loadLevel(Level level) {
        int scale = SCALE_FACTOR;
        //TODO: Lage metode for å loade levels
        if(level.getImage() == null) {
            String levelDetails = level.getLevelDetails();
            levelDetails = levelDetails.replace(System.getProperty("line.separator"), "");
            int levelWidth = level.getWidth();
            int y = 0;

            for(int x = 0; x < levelDetails.length(); x++) {

                if(x > 0 && x % levelWidth == 0) {
                    y++;
                }

                switch(levelDetails.charAt(x)) {
                    case 'X':
                        entities.add(new SolidBlock((x % levelWidth) * scale, y * scale));
                        break;

                    case 'O':
                        // The default thing to do
                        break;
                }
            }
        } else {

            BufferedImage image = level.getImage();
            int levelWidth = level.getWidth();
            int levelHeight = level.getHeight();

            for(int y = 0; y < levelHeight; y++) {
                for(int x = 0; x < levelWidth; x++) {
                    int p = image.getRGB(x, y);
                    // Get the pixel values
                    int a = (p>>24) & 0xff;
                    int r = (p>>16) & 0xff;
                    int gee = (p>>8) & 0xff;
                    int b = p & 0xff;
                    //set the pixel value
                    //p = (a<<24) | (r<<16) | (g<<8) | b;
                    if(a == 255 && r == 0 && gee == 0 && b == 0) {
                        entities.add(new SolidBlock(x * scale, y * scale));
                    } else if(a == 255 && r == 255 && gee == 0 && b == 0) {
                        entities.add(new Block(x * scale, y * scale));
                    } else {
                        // The default thing to do
                    }
                }
            }

        }
    }

    /*
    Enteties.Player input
    Collision
     */

    private void checkPlayer(Player player) {

        int x = player.getX();
        int y = player.getY();
        int width = player.getWidth();
        int height = player.getHeight();

        if (x <= 0) {
            player.setX(0);
        }
        if (x + width >= currentWindowWidth) {
            player.setX(currentWindowWidth - width - 1);
        }
        if (y <= 0) {
            player.setY(0);
        }
        if (y + height >= currentWindowHeight) {
            player.setHeight(currentWindowHeight - height - 1);
        }
        if(player.getHealth() <= 0) {
            stop();
        }

    }

    private void playerGravity() {
        Point newPos = new Point(player.getX(), player.getY() + (int) GRAVITY);
        if(!checkPlayerCollision(newPos)) {
            player.setY(newPos.y);
        }
    }

    private boolean checkPlayerCollision(Point newPos) {
        return player.isCollidingWithNewPos(newPos, entities);
    }

    public Point requestNewPlayerPosition(Point newPos) {
        Point oldPos = player.getPosition();
        // If the player does not collide with anything on the new position
        if(!checkPlayerCollision(newPos)) {
            // Set the player to this new position
            return newPos;
        }

        return oldPos;
    }

    public void win() {
        System.out.println("You have won!");
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Entity> requestEntities() {
        System.out.println("Entities requested.");
        if(player.getX() > 460) {
            System.out.println("spawning entity");
            entities.add(new SolidBlock(500 + randomer.nextInt(Rendering.Window.CANVAS_WIDTH - 500), Rendering.Window.CANVAS_HEIGHT-100));
        }
        previouslyRequestedEntities = entities;
        return entities;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }
}