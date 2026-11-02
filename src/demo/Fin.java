package demo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import motor.Escena;
import ui.Texto;

public class Fin extends Escena {

    @Override
    public void actualizar(double dt) {
        if (getEntrada().fuePresionada(KeyEvent.VK_ESCAPE)) {
            getContexto().salir();
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        int ancho = getContexto().getAncho();
        int alto = getContexto().getAlto();

        g.setColor(new Color(25, 20, 32));
        g.fillRect(0, 0, ancho, alto);

        Texto.centrado(g, "¡Gracias por jugar!",
                ancho / 2.0, alto / 2.0 - 20, 38, Color.GREEN);

        Texto.centrado(g, "Presioná ESC para salir del juego",
                ancho / 2.0, alto / 2.0 + 40, 20, Color.WHITE);
    }
}