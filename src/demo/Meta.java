package demo;

import java.awt.Color;
import java.awt.Graphics2D;

import entidades.Entidad;

public class Meta extends Entidad {

    private boolean visible = true;

    public Meta(int x, int y) {
        super(x, y);
    }

    public void alternarVisibilidad() {
        visible = !visible;
    }

    @Override
    public void dibujar(Graphics2D g) {
        if (!visible)
            return;

        g.setColor(Color.YELLOW);
        g.fillRect(getX(), getY(), TAM, TAM);
    }
}
