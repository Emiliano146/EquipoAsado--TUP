package demo;

import java.awt.Color;
import java.awt.Graphics2D;

import entidades.Entidad;
import contratos.Movible;

public class Caja extends Entidad implements Movible {

    public Caja(int x, int y) {
        super(x, y);
    }

    @Override
    public void mover(int dx, int dy) {
        establecerPosicion(getX() + dx, getY() + dy);
    }

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(new Color(108, 59, 42));
        g.fillRect(getX(), getY(), TAM, TAM);
    }
}