import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public Server(int port) throws IOException {
        System.out.println("Attempting to recieve client-connecting...");
        try (
            ServerSocket serverSocket = new ServerSocket(port);
            Socket clientSocket = serverSocket.accept();

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Client successfully connected to server.");
            String inputLine, outputLine;
            //while ((inputLine = in.readLine()) != null) {
            while(true) {
                inputLine = in.readLine();
                outputLine = "Message recieved.";

                // Calculate movements and such
                //TODO: Figure shit out

                // Return info for graphics and such
                out.println(outputLine);
                System.out.println(inputLine);
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + 6070 + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

}
