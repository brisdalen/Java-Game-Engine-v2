import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {

    private boolean running;
    private Player player;
    private Controller controller;
    private Window window;
    private Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;
    private ObjectOutputStream objectOut;
    private ObjectInputStream objectIn;
    private BufferedReader stdIn;
    private ArrayList<Entity> entities;

    public Client(String hostName, int port) {

        try {
            running = true;
            clientSocket = new Socket(hostName, port);
            System.out.println("Successfully connected to server.");

            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());

            objectOut = new ObjectOutputStream(clientSocket.getOutputStream());
            objectIn = new ObjectInputStream(clientSocket.getInputStream());

            stdIn = new BufferedReader(new InputStreamReader(System.in));

            player = new Player(Window.CANVAS_WIDTH / 2 - 10, Window.CANVAS_HEIGHT / 2 - 10, 20, 20);
            controller = new Controller(this, player);
            window = new Window(player);
            window.addKeyListener(controller);

            window.setEntities(requestEntities());
            window.getDrawPanel().repaint();

            while(running) {
                //entities = requestEntities();
            }

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }

    public void sendMessage(String message) {
        System.out.println("Attempting to send message...");
        try {
            // Exit the program if the user types "bye"
            if(message.trim().equalsIgnoreCase("bye")) {
                exit();
            // Send the message otherwise
            } else {
                //System.out.println("Trying to write...");
                out.writeUTF(message);
                System.out.println(in.readUTF());
                //entities = requestEntities();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Entity> requestEntities() {
        try {
            out.writeUTF("REQ_ENT()");
            ArrayList<Entity> entitiesReceived = (ArrayList<Entity>) objectIn.readObject();
            in.readUTF();
            return entitiesReceived;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Point requestPlayerPosition() {
        System.out.println("Requesting player position...");
        try {
            out.writeUTF("REQ_PLAYER_POSITION()");
            Point newPosition = (Point) objectIn.readObject();
            in.readUTF();
            entities = requestEntities();
            return newPosition;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updatePlayerPosition() {
        Point playerPosition = requestPlayerPosition();
        if(playerPosition != null) {
            player.setPosition(playerPosition);
        }

        updateEntities();
    }

    private void updateEntities() {
        window.setEntities(requestEntities());
    }

    private void exit() {

        String result = null;
        try {
            out.writeUTF("bye");
            result = in.readUTF();
            objectIn.close();
            objectOut.close();
            in.close();
            out.close();
            clientSocket.close();
            running = false;

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("recieved from server: " + result + "\n");

        System.exit(0);
    }

    public BufferedReader getStdIn() {
        return stdIn;
    }

    public Window getWindow() {
        return window;
    }

    public static void main(String[] args) {
        Client client = new Client("localHost", 6070);
    }
}
