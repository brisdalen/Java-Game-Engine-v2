import Enteties.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.Serializable;
//TODO: Look into key bindings instead of key listener
public class Controller implements KeyListener, Serializable {

    private Client client;
    private Player player;
    public boolean left, up, right, down, k;

    public Controller(Client client, Player player) {
        this.client = client;
        this.player = player;
        left = false;
        up = false;
        right = false;
        down = false;
        k = false;

        System.out.println("Controller created.");
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            left = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            up = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            right = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            down = true;
        }
        /*
        if(player.getController().left) {
            player.changeDirection(0);
            System.out.println("left");
            newPos = new Point(player.getX() - player.getMovementSpeed(), player.getY());
            if(!checkPlayerCollision(newPos)) {
                player.setX(newPos.x);
            }
        }
        */
        // Sending movement info to the server
        if(left) {
            player.changeDirection(0);
            client.sendMessage("MOV(" + -player.getMovementSpeed() + ",0)");
            client.updatePlayerPosition();
        }
        if(up) {
            player.changeDirection(1);
            client.sendMessage("MOV(0," + -player.getMovementSpeed() + ")");
            client.updatePlayerPosition();
        }
        if(right) {
            player.changeDirection(2);
            client.sendMessage("MOV(" + player.getMovementSpeed() + ",0)");
            client.updatePlayerPosition();
        }
        if(down) {
            player.changeDirection(3);
            client.sendMessage("MOV(0," + player.getMovementSpeed() + ")");
            client.updatePlayerPosition();
        }

        switch(e.getKeyCode()) {
            case KeyEvent.VK_K:
                k = true;
                break;
            case KeyEvent.VK_SHIFT:
                player.setMovementSpeed(2);
                break;
            case KeyEvent.VK_ENTER:
                try {
                    client.sendMessage(client.getStdIn().readLine());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            left = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            up = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            right = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            down = false;
        }
        switch(e.getKeyCode()) {
            case KeyEvent.VK_K:
                k = false;
                break;
            case KeyEvent.VK_SHIFT:
                player.setMovementSpeed(player.getMovementSpeedStart());
                break;
        }
    }
}
