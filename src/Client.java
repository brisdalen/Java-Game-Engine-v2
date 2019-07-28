import java.io.*;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {

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

    PlayerInputThread playerInputThread;

    public Client(String hostName, int port) {

        try {
            clientSocket = new Socket(hostName, port);
            System.out.println("Successfully connected to server.");

            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());

            objectOut = new ObjectOutputStream(clientSocket.getOutputStream());
            objectIn = new ObjectInputStream(clientSocket.getInputStream());

            stdIn = new BufferedReader(new InputStreamReader(System.in));

            player = new Player(Window.CANVAS_WIDTH / 2 - 10, Window.CANVAS_HEIGHT / 2 - 10, 20, 20);
            controller = new Controller(player);
            window = new Window(player);
            window.addKeyListener(controller);

            playerInputThread = new PlayerInputThread(controller);
            window.setEntities(requestEntities());
            playerInputThread.setRunning(true);
            playerInputThread.start();

            String userInput;
            //while ((userInput = stdIn.readLine()) != null) {
            while(true) {
                // Get player info and movement, send it to the output stream
                userInput = stdIn.readLine();

                // Lage maps med kommandoer??
                if(userInput.trim().equalsIgnoreCase("bye")) {
                    playerInputThread.setRunning(false);
                    break;
                } else if(userInput.trim().equalsIgnoreCase("req_ent()")) {
                    entities = requestEntities();
                } else {
                    System.out.println("Trying to write...");
                    out.writeUTF(userInput);

                    // Read the server data and do things accordingly.
                    //TODO: figure this out
                    String result = in.readUTF();
                    System.out.println("recieved from server: " + result + "\n");
                }
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

    private ArrayList<Entity> requestEntities() {
        try {
            System.out.println("Attempting to request entities.");
            out.writeUTF("REQ_ENT()");
            System.out.println("Waiting for requested entities.");
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

    public static void main(String[] args) {
        Client client = new Client("localHost", 6070);
    }
}
