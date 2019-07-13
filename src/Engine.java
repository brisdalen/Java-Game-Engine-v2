import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Engine extends Timer {

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
     * @param player The player to affect and get movement from. (TODO: Multiplayer solution?)
     * @param beforeStart Should the player be able to move before the start method has been called.
     */
    public Engine(Window.DrawPanel drawPanel, Player player, boolean beforeStart) {
        this.drawPanel = drawPanel;
        this.currentWindowWidth = drawPanel.getWidth();
        this.currentWindowHeight = drawPanel.getHeight();
        this.randomer = new Random();

        this.player = player;
        player.addEngine(this);

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

    /*
    Player input
    Collision
    Spawn
    Clear
     */

    private void checkPlayer(Player player, Window.DrawPanel drawPanel) {
        player.getInputs();

        if (player.x <= 0) {
            player.x = 0;
        }
        if (player.x + player.width >= currentWindowWidth) {
            player.x = currentWindowWidth - player.width - 1;
        }
        if (player.y <= 0) {
            player.y = 0;
        }
        if (player.y + player.height >= currentWindowHeight) {
            player.y = currentWindowHeight - player.height - 1;
        }
        if(player.health <= 0) {
            stop();
        }

        drawPanel.repaint(getBigClipX1(), getBigClipY1(),
                getBigClipX2(), getBigClipY2());
    }

    public int getNewDirection(int x, int y, int width, int height) {
        int direction = 0;
        //Typ 1 og 2
        if(height > width) {
            if(x - width < currentWindowWidth/2) {
                direction = 2;
            } else {
                direction = 0;
            }
            //Typ 0
        } else {
            if(y - height < currentWindowHeight/2) {
                direction = 3;
            } else {
                direction = 1;
            }
        }

        return direction;
    }

    public void win() {
        System.out.println("You have won!");
    }

    public Player getPlayer() {
        return player;
    }

    public int getBigClipX1() {
        return player.x - player.width*2;
    }
    public int getBigClipX2() {
        return player.width*6;
    }
    public int getBigClipY1() {
        return player.y - player.height*2;
    }
    public int getBigClipY2() {
        return player.height*6;
    }
}