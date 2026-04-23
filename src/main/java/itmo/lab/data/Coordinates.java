package itmo.lab.data;

import java.io.Serializable;

/**
 * Координаты работника.
 *
 * X должен быть не больше 717, Y - дробное число.
 */
public class Coordinates implements Serializable {
    private int x;
    private float y;

    public Coordinates() {
        
    }

    public Coordinates(int x, float y) {
        if (x > 717) {
            throw new IllegalArgumentException("X должен быть <= 717");
        }
        this.x = x;
        this.y = y;
    }
    public int getX() { return x; }
    public void setX(int x) {
        if (x > 717) {
            throw new IllegalArgumentException("X должен быть <= 717");
        }
        this.x = x;
    }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
}