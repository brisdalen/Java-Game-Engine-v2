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

            default: output = "Unrecognized input.";
                break;
        }

        return output;
    }

}
