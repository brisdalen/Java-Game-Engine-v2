import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Engine extends Timer {
    public ArrayList<Entity> entities;

    public Window.DrawPanel drawPanel;
    public Player player;
    public int currentWindowWidth;
    public int currentWindowHeight;
    public Random randomer;
    public int startTime = -20;
    public int totalTime = startTime;

    public boolean running = false;
    public boolean hasStarted = false;
    public static boolean hasLost = false;
    public boolean resetPrompt = false;

    /**
     *
     * @param drawPanel The panel to update and repaint.
     * @param player The player to affect and get movement from.
     */
    public Engine(Window.DrawPanel drawPanel, Player player) {
        this(drawPanel, player, false);
    }
    /**
     *
     * @param drawPanel The panel to update and repaint.
     * @param player The player to affect and get movement from. (TODO: Multiplayer solution?)
     * @param beforeStart Should the player be able to move before the start method has been called.
     */
    public Engine(Window.DrawPanel drawPanel, Player player, boolean beforeStart) {
        entities = new ArrayList<>();

        this.drawPanel = drawPanel;
        this.currentWindowWidth = drawPanel.getWidth();
        this.currentWindowHeight = drawPanel.getHeight();
        this.randomer = new Random();

        this.player = player;
        player.addEngine(this);

        entities.add(player);

        entities.add(new Block(0, drawPanel.getHeight()-100));
        entities.add(new Block(50, drawPanel.getHeight()-100));
        entities.add(new Block(100, drawPanel.getHeight()-100));

        entities.add(new SolidBlock(150, drawPanel.getHeight()-100));
        entities.add(new SolidBlock(200, drawPanel.getHeight()-100));
        entities.add(new SolidBlock(250, drawPanel.getHeight()-100));

        init(beforeStart);
    }

    public void init(boolean beforeStart) {
        running = true;
        /**
         * Get player input and check for out-of-bounds before the start method has been called.
         */
        if(beforeStart) {
            scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (!hasLost && !hasStarted) {
                        checkPlayer(player, drawPanel);
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
                    checkPlayer(player, drawPanel);
                }
                //If you lost, and resetPrompt has not been displayed yet
                if(hasLost && !resetPrompt) {
                    drawPanel.updateParentFrameButton();
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
                    System.out.println(totalTime);
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

    private void checkPlayer(Player player, Window.DrawPanel drawPanel) {
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

        drawPanel.repaint(getBigClipX1(), getBigClipY1(),
                getBigClipX2(), getBigClipY2());
    }

    /*
     *  Player input
     */
    public void getPlayerInputs() {
        if(player.getController().left) {
            player.changeDirection(0);
            if(!checkCollision()) {
                player.setX(player.getX() - player.getMovementSpeed());
            }
        }
        if(player.getController().up) {
            player.changeDirection(1);
            if(!checkCollision()) {
                player.setY(player.getY() - player.getMovementSpeed());
            }
        }
        if(player.getController().right) {
            player.changeDirection(2);
            // HUSK å sjekke collision med avstanden du planlegger å reise
            int distance = player.getX() + player.getMovementSpeed();
            if(!checkCollision()) {
                player.setX(player.getX() + player.getMovementSpeed());
            }
        }
        if(player.getController().down) {
            player.changeDirection(3);
            if(!checkCollision()) {
                player.setY(player.getY() + player.getMovementSpeed());
            }
        }
        if(player.getController().k) {
            // Instantly kill the player
            player.setHealth(0);
            stop();
            drawPanel.updateParentFrameButton();
        }
    }

    private boolean checkCollision() {
        for(Entity e : entities) {
            return player.isCollidingWith(e);
        }
    }

    public void win() {
        System.out.println("You have won!");
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public int getBigClipX1() {
        return player.getX() - player.getWidth()*2;
    }
    public int getBigClipX2() {
        return player.getWidth()*6;
    }
    public int getBigClipY1() {
        return player.getY() - player.getHeight()*2;
    }
    public int getBigClipY2() {
        return player.getHeight()*6;
    }
}