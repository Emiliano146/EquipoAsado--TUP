package demo;

import java.awt.Color;
import java.awt.Graphics2D;

public class Caja {

    private int x;
    private int y;

    private final int TAM = 100;

    public Caja(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void mover(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public void establecerPosicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void dibujar(Graphics2D g) {
        g.setColor(new Color(120, 55, 6));

        g.fillRect(
                x,
                y,
                TAM,
                TAM);
    }
}
