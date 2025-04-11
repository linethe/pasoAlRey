package model;

import java.util.Objects;

public class Posicion {
    int x, y;

    public Posicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

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

    @Override
    public boolean equals(Object o) {
        if (o instanceof Posicion p)
            return this.x == p.x && this.y == p.y;
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}