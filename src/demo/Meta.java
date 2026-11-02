package demo;

import java.awt.Color;
import java.awt.Graphics2D;

public class Meta {

    private int x;
    private int y;

    private boolean visible = true;

    private final int TAM = 100;

    public Meta(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void alternarVisibilidad() {
        visible = !visible;
    }

    public void dibujar(Graphics2D g) {

        if (!visible) {
            return;
        }

        g.setColor(Color.YELLOW);

        g.fillRect(
                x,
                y,
                TAM,
                TAM);
    }
}