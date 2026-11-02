package demo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import demo.niveles.Nivel1;
import demo.niveles.Tutorial;
import motor.Escena;
import ui.Texto;
import estructuras.Cola;
import estructuras.ColaEnlazada;

public class EscenaMenu extends Escena {

    @Override
    public void actualizar(double dt) {
        if (getEntrada().fuePresionada(KeyEvent.VK_ENTER)) {

            Cola<Integer> niveles = new ColaEnlazada<>();

            niveles.encolar(1);
            niveles.encolar(2);

            getContexto().irA(new Nivel1());
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        int ancho = getContexto().getAncho();
        int alto = getContexto().getAlto();

        g.setColor(new Color(25, 20, 32));
        g.fillRect(0, 0, ancho, alto);

        g.setColor(new Color(75, 55, 90));
        g.fillRect(ancho / 2 - 180, alto / 2 - 130, 360, 250);

        Texto.centrado(g, "SOKOBAN 5x5",
                ancho / 2.0, alto / 2.0 - 65, 48, Color.WHITE);

        Texto.centrado(g, "Cada nivel más difícil que el anterior",
                ancho / 2.0, alto / 2.0 - 20, 18, Color.LIGHT_GRAY);

        Texto.centrado(g, "ENTER: empezar",
                ancho / 2.0, alto / 2.0 + 55, 24,
                new Color(255, 220, 120));

        Texto.centrado(g, "Hecho por el Equipo Asado",
                ancho / 2.0, alto - 35, 18, Color.GRAY);
    }
}