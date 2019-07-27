import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    ServerSocket serverSocket;
    Socket clientSocket;

    DataOutputStream out;
    DataInputStream in;
    BufferedReader stdIn;

    public Server(int port) throws IOException {
        System.out.println("Attempting to recieve client-connecting...");

        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();

            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());
            stdIn = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Client successfully connected to server.");
            String inputLine, outputLine;
            //while ((inputLine = in.readLine()) != null) {
            while(true) {
                inputLine = in.readUTF();

                System.out.println("input: " + inputLine);
                outputLine = inputLine;

                // Calculate movements
                // Return info for graphics
                outputLine = processInput(inputLine);
                out.writeUTF(outputLine);
                System.out.println("send to client: " + outputLine + "\n");
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + 6070 + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    public String processInput(String input) {
        String output;
        int leftPar = input.indexOf("(");

        switch(input.substring(0, leftPar)) {
            case "MOV": output = "x + " + input.substring(leftPar+1, input.indexOf(","));
                break;

            case "REQ_ENT": output = "Attempting to send entities...";
                break;

            default: output = "Unrecognized input.";
                break;
        }

        return output;
    }

}
