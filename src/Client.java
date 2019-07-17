import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    String hostName = "java-game-engine";
    int port = 6060;
    Socket socket;

    public Client() {
        try {
            socket = new Socket(hostName, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
