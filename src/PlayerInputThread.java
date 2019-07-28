public class PlayerInputThread extends Thread {

    private Controller controller;
    private boolean running = false;

    PlayerInputThread(Controller controller) {
        System.out.println("PlayerInputThread created.");
        this.controller = controller;
    }

    public void run() {
        System.out.println("PIT started.");
        /*
        TODO:
            - Get controller input
            - perform action on local player
            - send update and request to the server
         */
        while(running) {
            if (controller.left) {

            }
            if (controller.up) {

            }
            if (controller.right) {

            }
            if (controller.down) {

            }
        }
    }

    public void getPlayerInputs() {
        /* TODO: Refactor player-input to work through socket
        if(player.getController().left) {
            player.changeDirection(0);
            System.out.println("left");
            newPos = new Point(player.getX() - player.getMovementSpeed(), player.getY());
            if(!checkPlayerCollision(newPos)) {
                player.setX(newPos.x);
            }
        }

        if(player.getController().k) {
            // Instantly kill the player
            player.setHealth(0);
            stop();
            //drawPanel.updateParentFrameButton();
        }*/
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
