package KohonenMap.gui;

import javafx.scene.shape.Polygon;

public class Hexagon {
    public int i;
    public int j;
    public Polygon hex;

    public Hexagon(int i, int j, Polygon hex) {
        this.i = i;
        this.j = j;
        this.hex = hex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hexagon hexagon = (Hexagon) o;

        if (i != hexagon.i) return false;
        return j == hexagon.j;
    }

    @Override
    public int hashCode() {
        int result = i;
        result = 31 * result + j;
        return result;
    }

    public static int getHash(int i, int j) {
        int result = i;
        result = 31 * result + j;
        return result;
    }
}
