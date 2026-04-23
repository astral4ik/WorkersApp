package itmo.lab.data;

import java.io.Serializable;

/**
 * Географические координаты (локация).
 *
 * Используется для указания местоположения города.
 * Y и Z могут быть null при загрузке из XML.
 */
public class Location implements Serializable {
    private long x;
    private Integer y;
    private Integer z;

    public Location() {
    }

    public Location(long x, Integer y, Integer z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long getX() { return x; }
    public void setX(long x) { this.x = x; }
    public Integer getY() { return y; }
    public void setY(Integer y) { this.y = y; }
    public Integer getZ() { return z; }
    public void setZ(Integer z) { this.z = z; }
}