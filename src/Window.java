import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Window extends JFrame {

    public static final int CANVAS_WIDTH = 780;
    public static final int CANVAS_HEIGHT = 480;
    public int currentWindowWidth;
    public int currentWindowHeight;
    public static final Color CANVAS_BG_COLOR = new Color(120, 80, 120);

    private boolean first = true;

    public JButton startGameButton;

    private DrawPanel drawPanel;
    private Player player;

    private Font font;
    private Engine engine;

    public Window() {
        setResizable(false);

        startGameButton = new JButton("Start the game!");
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame(startGameButton);
            }
        });

        drawPanel = new DrawPanel(this);
        drawPanel.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        JPanel labelPanel = new JPanel(new FlowLayout());
        labelPanel.add(new JLabel("Use the Arrow Keys or WASD to move. Hold shift to move slow. " +
                "Stay on the healing pods to get points!"));
        labelPanel.add(startGameButton);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        //cp.add(startPanel);
        cp.add(drawPanel, BorderLayout.CENTER);
        cp.add(labelPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Rendering test");
        pack();

        currentWindowWidth = drawPanel.getWidth();
        currentWindowHeight = drawPanel.getHeight();

        engine = new Engine(drawPanel,
                new Player(CANVAS_WIDTH / 2 - 10, CANVAS_HEIGHT / 2 - 10, 20, 20));

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.getAllFonts();

        font = new Font("SukhumvitSet-Thin", Font.PLAIN, 35);

        addKeyListener(engine.getPlayer().controller);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Rectangle r = getBounds();
                currentWindowWidth = r.width;
                currentWindowHeight = r.height;
            }
        });

        setVisible(true);
        requestFocus();
    }

    /**
     * Constructor used when resetting the game. Opens the window in the location of the previous window.
     *
     * @param locationX The x-coordinate of the previous window.
     * @param locationY The y-coordinate of the previous window.
     */
    public Window(int locationX, int locationY) {
        this();
        setLocation(locationX, locationY);
    }

    public void startGame(JButton button) {
        System.out.println("Game has started.");
        button.setEnabled(false);
        engine.start();
        requestFocus();
    }

    public void resetGame() {
        System.out.println("Game reset");
        this.setVisible(false);
        this.dispose();
        engine.reset();
        new Window(this.getX(), this.getY());
    }

    public void setButtonToReset(JButton button) {
        button.setText("Go again?");
        for (ActionListener al : button.getActionListeners()) {
            button.removeActionListener(al);
        }
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });
        button.setEnabled(true);
        /**TODO: Add code to reset window
         *  - Endre ActionPerformed hos startGame ved reset
         *  - Set player to the centre
         *  - Clear all healpods and spawn 1 in the centre
         *  - Clear all lazers
         **/
    }

    class DrawPanel extends JPanel {

        public Window window;

        public DrawPanel(Window window) {
            this.window = window;
        }

        public void updateParentFrameButton() {
            if (engine.player.health <= 0) {
                setButtonToReset(window.startGameButton);
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(CANVAS_BG_COLOR);

            /**
             * Opptimalisere "Death message" ved å sette mest mulig med en gang
             * før man displayer det ved player.health == 0
             */
            boolean first = true;
            int fontX = 0;
            int fontY = 0;
            if (first) {
                g.setFont(font);
                FontMetrics metrics = g.getFontMetrics();
                fontX = ((currentWindowWidth / 2) - metrics.stringWidth("YOU'RE DEAD.") / 2);
                fontY = currentWindowHeight / 2 - metrics.getHeight() / 2 + metrics.getAscent();
                first = false;
            }

            engine.getPlayer().paint(g);
            engine.getPlayer().paintHealthbar(g, currentWindowWidth / 23, currentWindowHeight / 17,
                    currentWindowWidth / 4, currentWindowHeight / 13);

            repaint(currentWindowWidth / 23, currentWindowHeight / 17,
                    currentWindowWidth / 4, currentWindowHeight / 13);

            if (engine.getPlayer().health == 0) {
                g.setColor(Color.black);
                g.drawString("YOU'RE DEAD.", fontX, fontY);
                repaint();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Window();
            }
        });
    }
}
