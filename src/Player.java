import java.awt.*;

public class Player implements Drawable {
    private int x, y, width, height, startX, startY;
    private int direction = 2; // 0 = left, 1 = up, 2 = right, 3 = down
    private int startHealth = 1000;
    private int health = startHealth;
    private int movementSpeedStart = 4;
    private int movementSpeed = 4;
    // Is this bad coupling?
    private Controller controller;
    private Engine engine;
    private int maxHealth = 1000;

    private int fixedHealAmount = 50;
    private int fixedDamageAmount = 50;

    private Color color = new Color(180, 45, 50);

    public Player(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.width = width;
        this.height = height;
        this.controller = new Controller(this);

        if(health > maxHealth) {
            health = maxHealth;
        }
    }

    /*
     * Getters and setters
     */
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getDirection() {
        return direction;
    }

    public void changeDirection(int direction) {
        if (direction > 0 && direction <= 3) {
            this.direction = direction;
        }
    }

    public int getStartHealth() {
        return startHealth;
    }

    public void setStartHealth(int startHealth) {
        this.startHealth = startHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMovementSpeedStart() {
        return movementSpeedStart;
    }

    public void setMovementSpeedStart(int movementSpeedStart) {
        this.movementSpeedStart = movementSpeedStart;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(int movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    /*
     *  Player input
     *  TODO: Burde dette håndteres i Engine?
     */
    public void getInputs() {
        if(controller.left) {
            changeDirection(0);
            x -= movementSpeed;
        }
        if(controller.up) {
            changeDirection(1);
            y -= movementSpeed;
        }
        if(controller.right) {
            changeDirection(2);
            x += movementSpeed;
        }
        if(controller.down) {
            changeDirection(3);
            y += movementSpeed;
        }
        if(controller.k) {
            // Instantly kill the player
            health = 0;
            engine.stop();
            engine.drawPanel.updateParentFrameButton();
        }
    }

    public void reset() {
        x = startX;
        y = startY;
        direction = 2;
        health = startHealth;
    }

    public void takeDamage(int damage) {
        if(health - damage >= 0 + damage) {
            health -= damage;
        } else {
            health = 0;
        }
    }
    public void takeDamage() {
        if(health - fixedDamageAmount >= 0 + fixedDamageAmount) {
            health -= fixedDamageAmount;
        } else {
            health = 0;
        }
    }

    public void heal(int healAmount) {
        if(health + healAmount <= maxHealth - healAmount) {
            health += healAmount;
        } else {
            health = 1000;
        }
    }
    public void heal() {
        if(health + fixedHealAmount <= maxHealth - fixedHealAmount) {
            health += fixedHealAmount;
        } else {
            health = 1000;
        }
    }


    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);

        if(direction == 0) {
            g.fillRect(x-width, y+(height/4), width, width/2);
        }
        if(direction == 1) {
            g.fillRect(x+(width/4), y-height, width/2, width);
        }
        if(direction == 2) {
            g.fillRect(x+width, y+(height/4), width, width/2);
        }
        if(direction == 3) {
            g.fillRect(x+(width/4), y+height, width/2, width);
        }

        g.setColor(Color.black);
        g.drawRect(x, y, width, height);
    }

    public void paintHealthbar(Graphics g, int x, int y, int width, int height) {
        double percentage = width;
        percentage /= 100;
        percentage *= (health/10);
        /*
         * Percentages:       1% = (width/400)
         *                  100% = (width/400) * 100
         */
        g.setColor(Color.black);
        g.fillRect(x-2, y-2, width+4, height+4);
        g.setColor(Color.red);
        g.fillRect(x, y, width, height);
        g.setColor(Color.green);
        g.fillRect(x, y, (int)Math.ceil(percentage), height);
    }
    /*
    public void paintScore(Graphics g, int x, int y) {
        g.setColor(Color.black);
        g.drawString("Score: ", x -100, y);
        g.drawString(Integer.toString(score/5), x, y+1);
    }*/
}