import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    String hostName = "java-game-engine";
    int port = 6060;

    public Client() {
        try {
            Socket kkSocket = new Socket(hostName, port);

        }
    }
}
