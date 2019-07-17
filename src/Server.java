import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    private ServerSocket[] sockets;

    private ServerSocket server;

    public Server(int port) throws IOException {
        server = new ServerSocket(port);
        server.setSoTimeout(15000);
    }

    public ServerSocket getServer() {
        return server;
    }

    /* TODO: Finne ut av multiple connections senere
    public Server(int numOfSockets, int port) {
        this.sockets = new ServerSocket[numOfSockets];
        for(int i = 0; i < numOfSockets; i++) {
            try {
                sockets[i] = new ServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(sockets[0].getLocalPort());
    }*/
}
