package entidades;

import java.awt.Graphics2D;

public abstract class Entidad {

    private int x;
    private int y;
    protected final int TAM = 100;

    public Entidad(int x, int y) {
        establecerPosicion(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void establecerPosicion(int x, int y) {
        if (x < 0 || x > 400 || y < 0 || y > 400
                || x % TAM != 0 || y % TAM != 0)
            throw new IllegalArgumentException("Posición inválida");

        this.x = x;
        this.y = y;
    }

    public abstract void dibujar(Graphics2D g);
}