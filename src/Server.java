import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public Server(int port) throws IOException {
        System.out.println("Attempting to recieve client-connecting...");
        try (
                ServerSocket serverSocket = new ServerSocket(port);
                Socket clientSocket = serverSocket.accept();

                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Client successfully connected to server.");
            int inputLine = 0;
            int outputLine;
            //while ((inputLine = in.readLine()) != null) {
            while(true) {
                System.out.println("Attempting to read ints...");
                int length = in.readInt();
                System.out.println("length: " + length);
                for(int i = 0; i < length; i++) {
                    int temp = in.readInt();
                    System.out.println(i + " : " + temp);
                    inputLine += temp;
                }

                System.out.println("input: " + inputLine);
                outputLine = inputLine;

                // Calculate movements and such
                //TODO: Figure this out
                // Return info for graphics and such
                out.writeInt(outputLine);
                System.out.println("send to client: " + outputLine + "\n");
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + 6070 + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

}
