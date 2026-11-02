package demo;

import java.awt.Color;
import java.awt.Graphics2D;

import entidades.Entidad;

public class Pared extends Entidad {

    public Pared(int x, int y) {
        super(x, y);
    }

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(Color.GRAY);
        g.fillRect(getX(), getY(), TAM, TAM);
    }
}