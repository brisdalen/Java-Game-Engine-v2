import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    Engine engine;
    Server server;

    public Main() {

        engine = new Engine(new Player(Window.CANVAS_WIDTH / 2 - 10, Window.CANVAS_HEIGHT / 2 - 10, 20, 20),
                Window.CANVAS_WIDTH, Window.CANVAS_HEIGHT, true, true);

        try {
            server = new Server(6070, engine);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        /*SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Window();
                System.out.println("Window created.");
            }
        });*/
    }
}
