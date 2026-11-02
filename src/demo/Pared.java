package demo;

import java.awt.Color;
import java.awt.Graphics2D;

public class Pared {

    private int x;
    private int y;

    private final int TAM = 100;

    public Pared(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void dibujar(Graphics2D g) {
        g.setColor(Color.GRAY);

        g.fillRect(
                x,
                y,
                TAM,
                TAM);
    }
}
