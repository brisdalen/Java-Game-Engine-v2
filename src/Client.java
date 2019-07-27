import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    Player player;

    public Client(String hostName, int port) {

        player = new Player(Window.CANVAS_WIDTH / 2 - 10, Window.CANVAS_HEIGHT / 2 - 10, 20, 20);

        try (
            Socket clientSocket = new Socket(hostName, port);
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Successfully connected to server.");
            String userInput;
            //while ((userInput = stdIn.readLine()) != null) {
            while(true) {
                // Get player info and movement, send it to the output stream
                userInput = stdIn.readLine().toUpperCase();

                if(userInput.trim().equalsIgnoreCase("bye")) {
                    break;
                }

                System.out.println("Trying to write...");
                out.writeUTF(userInput);

                // Read the server data and do things accordingly.
                //TODO: figure this out
                String result = in.readUTF();
                System.out.println("recieved from server: " + result + "\n");
            }

            System.exit(0);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localHost", 6070);
    }
}
