import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public Client(String hostName, int port) {
        try (
            Socket echoSocket = new Socket(hostName, port);
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Successfully connected to server.");
            String userInput;
            //while ((userInput = stdIn.readLine()) != null) {
            while(true) {
                // Get player info and movement, send it to the output stream
                userInput = stdIn.readLine();
                out.println(userInput);

                // Read the server data and do things accordingly.
                //TODO: figure this out
                System.out.println(in.readLine());

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

    public static void main(String[] args) {
        Client client = new Client("localHost", 6070);
    }
}
