package itmo.lab.data;

import java.io.Serializable;

/**
 * Трёхмерные координаты местоположения города.
 */
public class Location implements Serializable {
    private long x;
    private Integer y;
    private Integer z;

    public Location() {
    }

    /**
     * Создаёт местоположение по трём координатам.
     *
     * @param x координата X
     * @param y координата Y (не может быть null)
     * @param z координата Z (не может быть null)
     */
    public Location(long x, Integer y, Integer z) {
        if (y == null) throw new IllegalArgumentException("Location.y не может быть null");
        if (z == null) throw new IllegalArgumentException("Location.z не может быть null");
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long getX() { return x; }
    public void setX(long x) { this.x = x; }
    public Integer getY() { return y; }

    /**
     * Устанавливает координату Y.
     *
     * @param y значение (не может быть null)
     */
    public void setY(Integer y) {
        if (y == null) throw new IllegalArgumentException("Location.y не может быть null");
        this.y = y;
    }

    public Integer getZ() { return z; }

    /**
     * Устанавливает координату Z.
     *
     * @param z значение (не может быть null)
     */
    public void setZ(Integer z) {
        if (z == null) throw new IllegalArgumentException("Location.z не может быть null");
        this.z = z;
    }
}
