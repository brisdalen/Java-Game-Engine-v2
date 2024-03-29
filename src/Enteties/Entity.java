package Enteties;

import java.awt.*;
import java.io.Serializable;

public abstract class Entity implements Serializable {

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    protected boolean isKinematic = false;

    public Entity() { }

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

    public Point getPosition() {
        return new Point(x, y);
    }

    public void setPosition(Point newPosition) {
        setX(newPosition.x);
        setY(newPosition.y);
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
}
