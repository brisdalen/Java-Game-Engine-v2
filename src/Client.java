import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public Client(String hostName, int port) {
        try (
            Socket echoSocket = new Socket(hostName, port);
            DataOutputStream out = new DataOutputStream(echoSocket.getOutputStream());
            DataInputStream in = new DataInputStream(echoSocket.getInputStream());
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Successfully connected to server.");
            int[] userInput;
            //while ((userInput = stdIn.readLine()) != null) {
            while(true) {
                // Get player info and movement, send it to the output stream
                System.out.println("Enter int 1");
                int int1 = Integer.parseInt(stdIn.readLine());
                System.out.println("Enter int 2");
                int int2 = Integer.parseInt(stdIn.readLine());


                userInput = new int[]{int1, int2};
                System.out.println("Trying to write...");
                out.writeInt(userInput.length);
                for(int i = 0; i < userInput.length; i++) {
                    System.out.println("int: " + userInput[i]);
                    out.writeInt(userInput[i]);
                }

                // Read the server data and do things accordingly.
                //TODO: figure this out
                int result = in.readInt();
                System.out.println("recieved from server: " + result + "\n");

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
