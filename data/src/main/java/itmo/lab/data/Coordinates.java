package itmo.lab.data;

import java.io.Serializable;

/**
 * Координаты работника на плоскости (x не более 717, y — дробное).
 */
public class Coordinates implements Serializable {
    private int x;
    private float y;

    public Coordinates() {
    }

    /**
     * Создаёт координаты с проверкой ограничения по X.
     *
     * @param x горизонтальная координата (не более 717)
     * @param y вертикальная координата
     */
    public Coordinates(int x, float y) {
        if (x > 717) {
            throw new IllegalArgumentException("X должен быть <= 717");
        }
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }

    /**
     * Устанавливает горизонтальную координату.
     *
     * @param x значение не более 717
     */
    public void setX(int x) {
        if (x > 717) {
            throw new IllegalArgumentException("X должен быть <= 717");
        }
        this.x = x;
    }

    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
}
