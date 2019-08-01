import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    JFrame frame;

    Engine engine;

    ServerSocket serverSocket;
    Socket clientSocket;

    DataOutputStream out;
    DataInputStream in;

    ObjectOutputStream objectOut;
    ObjectInputStream objectIn;

    BufferedReader stdIn;

    public Server(int port, Engine engine) throws IOException {

        frame = new JFrame("Java game engine server");
        frame.setSize(300, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addEngine(engine);
        frame.setVisible(true);
        System.out.println("Waiting for client-connecting...");

        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();

            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());

            objectOut = new ObjectOutputStream(clientSocket.getOutputStream());
            objectIn = new ObjectInputStream(clientSocket.getInputStream());
            stdIn = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Client successfully connected to server.");
            String inputLine, outputLine;

            engine.start();


            //while ((inputLine = in.readLine()) != null) {
            while(true) {
                System.out.println("Waiting for client input...");
                inputLine = in.readUTF().trim().toUpperCase();

                if(inputLine.trim().equalsIgnoreCase("bye")) {
                    break;
                }

                System.out.println("Client input: " + inputLine);
                outputLine = inputLine;

                // Calculate movements
                // Return info for graphics
                outputLine = processInput(inputLine);
                out.writeUTF(outputLine);
                System.out.println("send to client: " + outputLine + "\n");
            }

            close();

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + 6070 + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    public String processInput(String input) {
        System.out.println("Processing \"" + input + "\"");
        String output;
        int leftPar = input.indexOf("(");
        int comma = input.indexOf(",");

        if(input.length() <= 2) {
            return "Command needs to be 3 characters long.";
        }

        switch(input.substring(0, leftPar)) {
            // TODO: kommandoer burde hentes fra en klasse, slik at de er like hvor enn jeg endrer dem
            // for client-side movement TODO
            case "MOV": //TODO: Skille på retninger
                Player player = engine.getPlayer();
                int x = Integer.parseInt(input.substring(leftPar+1, comma));
                int y = Integer.parseInt(input.substring(comma+1, input.length()-1));
                player.setX(player.getX() + x);
                player.setY(player.getY() + y);
                System.out.println(player.getX());
                output = "x: " + x + "\n" + "y: " + y;
                break;

            case "REQ_PLAYER_POSITION":
                output = "Attempting to send player data.";
                sendPlayerPosition();
                break;

            // for requesting entities from the engine
            case "REQ_ENT":
                output = "Attempting to send entities.";
                sendEntities();
                break;

            default: output = "Unrecognized input.";
                break;
        }

        return output;
    }

    private void sendEntities() {
        try {
            ArrayList<Entity> entities = engine.requestEntities();
            System.out.println(entities.size());
            objectOut.writeObject(entities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendPlayerPosition() {
        try {
            Point playerPosition = engine.getPlayer().getPosition();
            objectOut.writeObject(playerPosition);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        System.out.println("Attempting to close all resources.");
        try {
            stdIn.close();

            in.close();
            out.close();

            clientSocket.close();
            serverSocket.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void addEngine(Engine engine) {
        System.out.println("Adding engine.");
        this.engine = engine;
    }

}
