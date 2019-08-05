import javax.swing.text.Position;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Engine extends Timer {
    private ArrayList<Entity> entities;
    private ArrayList<Entity> previouslyRequestedEntities;

    private Player player;
    private Point previousPlayerPosition;
    private int currentWindowWidth;
    private int currentWindowHeight;
    private Random randomer;
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
        this(player, Window.CANVAS_WIDTH, Window.CANVAS_HEIGHT, false, true);
    }

    /**
     *
     * @param player The player to affect and get movement from. (TODO: Multiplayer solution?)
     * @param beforeStart Should the player be able to move before the start method has been called.
     */
    public Engine(Player player, boolean beforeStart) {
        this(player, Window.CANVAS_WIDTH, Window.CANVAS_HEIGHT, beforeStart, false);
    }
    /**
     *
     * @param player The player to affect and get movement from. (TODO: Multiplayer solution?)
     * @param beforeStart Should the player be able to move before the start method has been called.
     * @param gravity Should gravity be activated by default
     */
    public Engine(Player player, boolean beforeStart, boolean gravity) {
        this(player, Window.CANVAS_WIDTH, Window.CANVAS_HEIGHT, beforeStart, gravity);
    }

    public Engine(Player player, int windowWidth, int windowHeight, boolean beforeStart, boolean gravity) {
        entities = new ArrayList<>();
        previouslyRequestedEntities = new ArrayList<>();

        this.currentWindowWidth = windowWidth;
        this.currentWindowHeight = windowHeight;
        this.randomer = new Random();
        this.gravity = gravity;

        this.player = player;
        player.setKinematic(gravity);

        //entities.add(player);

        entities.add(new Block(0, Window.CANVAS_HEIGHT-100));
        entities.add(new Block(50, Window.CANVAS_HEIGHT-100));
        entities.add(new Block(100, Window.CANVAS_HEIGHT-100));

        entities.add(new SolidBlock(150, Window.CANVAS_HEIGHT-100));
        entities.add(new SolidBlock(200, Window.CANVAS_HEIGHT-100));
        entities.add(new SolidBlock(250, Window.CANVAS_HEIGHT-100));

        entities.add(new SolidBlock(370, Window.CANVAS_HEIGHT-100));

        entities.add(new SolidBlock(490, Window.CANVAS_HEIGHT-100));

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
        System.out.println("Engine RESET");
        /* TODO: Add code to reset engine
         *  - husk på forskjell på STOP og RESET
         *  - ny thread med totalTime ved hver reset?
         */
        resetPrompt = false;
        hasLost = false;
        totalTime = startTime;

        player.reset();

        running = true;

        //spawnNewHealpod(currentWindowWidth/2-20, currentWindowHeight/2-20);

    }

    public void stop() {
        System.out.println("Engine STOP");
        hasLost = true;
        running = false;
        //hasStarted = false;
    }

    public void saveLevel(String path) {
        //TODO: Lage metode for å lagre levels
    }

    public void loadLevel(String path) {
        //TODO: Lage metode for å loade levels
        System.out.println("engine load: " + path.substring(path.indexOf("/")+1, path.length()-4));
    }

    /*
    Player input
    Collision
     */

    private void checkPlayer(Player player) {
        getPlayerInputs();

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

    /*
     *  Player input
     */
    public void getPlayerInputs() {
        /* TODO: Refactor player-input to work through socket
        if(player.getController().left) {
            player.changeDirection(0);
            System.out.println("left");
            newPos = new Point(player.getX() - player.getMovementSpeed(), player.getY());
            // If the player does not collide with anything on the new position
            if(!checkPlayerCollision(newPos)) {
                // Set the player to this new position
                player.setX(newPos.x);
            }
        }
        if(player.getController().up) {
            player.changeDirection(1);
            newPos = new Point(player.getX(), player.getY() - player.getMovementSpeed());
            if(!checkPlayerCollision(newPos)) {
                player.setY(newPos.y);
            }
        }
        if(player.getController().right) {
            player.changeDirection(2);
            newPos = new Point(player.getX() + player.getMovementSpeed(), player.getY());
            if(!checkPlayerCollision(newPos)) {
                player.setX(newPos.x);
            }
        }
        if(player.getController().down) {
            player.changeDirection(3);
            newPos = new Point(player.getX(), player.getY() + player.getMovementSpeed());
            if(!checkPlayerCollision(newPos)) {
                player.setY(newPos.y);
            }
        }
        if(player.getController().k) {
            // Instantly kill the player
            player.setHealth(0);
            stop();
            //drawPanel.updateParentFrameButton();
        }*/
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
            entities.add(new SolidBlock(500 + randomer.nextInt(Window.CANVAS_WIDTH - 500), Window.CANVAS_HEIGHT-100));
        }
        previouslyRequestedEntities = entities;
        return entities;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }
}