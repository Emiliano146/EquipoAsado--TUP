package demo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import motor.Entrada;
import motor.Escena;
import motor.Temporizador;
import ui.Texto;

import estructuras.PilaEnlazada;

public class EscenaDemo extends Escena {

    // Atributos
    private final Temporizador parpadeo = new Temporizador(0.5);

    private double jugadorX;
    private double jugadorY;

    private double cajaX;
    private double cajaY;

    private double metaX;
    private double metaY;

    private boolean visible;
    private boolean gano;

    // Pila para guardar los movimientos
    private PilaEnlazada<Movimiento> movimientos = new PilaEnlazada<>();

    // Contador de movimientos
    private int contadorMovimientos;

    // Tamaños
    private final int TAM = 100;

    // Entrada a la escena
    @Override
    public void alEntrar() {
        jugadorX = 50;
        jugadorY = 50;

        cajaX = 200;
        cajaY = 200;

        metaX = 400;
        metaY = 400;

        visible = true;
        gano = false;

        contadorMovimientos = 0;
        movimientos.vaciar();

        parpadeo.reiniciar();
    }

    // Actualización
    @Override
    public void actualizar(double dt) {

        Entrada entrada = getEntrada();

        if (gano) {
            if (entrada.fuePresionada(KeyEvent.VK_ENTER)) {
                getContexto().irA(new EscenaDemoDos());
            }
            return;
        }

        // Deshacer movimiento
        if (entrada.fuePresionada(KeyEvent.VK_Z)) {

            if (!movimientos.estaVacia()) {

                Movimiento ultimo = movimientos.desapilar();

                // Restaurar jugador
                jugadorX = ultimo.getJugadorX();
                jugadorY = ultimo.getJugadorY();

                // Restaurar caja
                cajaX = ultimo.getCajaX();
                cajaY = ultimo.getCajaY();

                // Restar movimiento
                contadorMovimientos--;
            }

        } else {

            double nuevoX = jugadorX;
            double nuevoY = jugadorY;

            // Guardar posiciones anteriores
            double anteriorX = jugadorX;
            double anteriorY = jugadorY;

            double anteriorCajaX = cajaX;
            double anteriorCajaY = cajaY;

            // Detectar movimiento
            if (entrada.fuePresionada(KeyEvent.VK_LEFT)) {
                nuevoX -= TAM;

            } else if (entrada.fuePresionada(KeyEvent.VK_RIGHT)) {
                nuevoX += TAM;

            } else if (entrada.fuePresionada(KeyEvent.VK_UP)) {
                nuevoY -= TAM;

            } else if (entrada.fuePresionada(KeyEvent.VK_DOWN)) {
                nuevoY += TAM;
            }

            // Límites del escenario (500 x 500)
            nuevoX = Math.max(50, Math.min(450, nuevoX));
            nuevoY = Math.max(50, Math.min(450, nuevoY));

            // Comprobar si el jugador intenta entrar en la caja
            boolean chocaCaja = nuevoX + 50 > cajaX &&
                    nuevoX - 50 < cajaX + TAM &&
                    nuevoY + 50 > cajaY &&
                    nuevoY - 50 < cajaY + TAM;

            if (chocaCaja) {

                // Calcular desplazamiento de la caja
                double desplazamientoX = nuevoX - jugadorX;
                double desplazamientoY = nuevoY - jugadorY;

                double nuevaCajaX = cajaX + desplazamientoX;
                double nuevaCajaY = cajaY + desplazamientoY;

                // Comprobar límites de la caja
                boolean dentroLimites = nuevaCajaX >= 0 &&
                        nuevaCajaX <= 400 &&
                        nuevaCajaY >= 0 &&
                        nuevaCajaY <= 400;

                if (dentroLimites) {

                    // Mover caja
                    cajaX = nuevaCajaX;
                    cajaY = nuevaCajaY;

                    // Mover jugador
                    jugadorX = nuevoX;
                    jugadorY = nuevoY;
                }

            } else {

                // No hay colisión: mover normalmente
                jugadorX = nuevoX;
                jugadorY = nuevoY;
            }

            // Guardar movimiento válido
            if (jugadorX != anteriorX || jugadorY != anteriorY) {

                movimientos.apilar(
                        new Movimiento(
                                anteriorX,
                                anteriorY,
                                anteriorCajaX,
                                anteriorCajaY));

                // Sumar movimiento
                contadorMovimientos++;
            }
        }

        // Parpadeo de la meta
        if (parpadeo.avanzar(dt)) {
            visible = !visible;
        }

        // Comprobar si la caja llegó a la meta
        if (cajaX == metaX && cajaY == metaY) {
            gano = true;
        }

        // Salir
        if (entrada.fuePresionada(KeyEvent.VK_ESCAPE)) {
            getContexto().salir();
        }

        // Ir a la siguiente escena solamente si ganó
        if (gano && entrada.fuePresionada(KeyEvent.VK_ENTER)) {
            getContexto().irA(new EscenaDemoDos());
        }
    }

    // Dibujo
    @Override
    public void dibujar(Graphics2D g) {

        int ancho = getContexto().getAncho();
        int alto = getContexto().getAlto();

        // Fondo
        g.setColor(new Color(20, 24, 36));
        g.fillRect(0, 0, ancho, alto);

        // Meta
        if (visible) {

            g.setColor(new Color(255, 255, 0));

            g.fillRect(
                    (int) metaX,
                    (int) metaY,
                    TAM,
                    TAM);
        }

        // Jugador azul
        g.setColor(new Color(70, 140, 230));

        g.fillRect(
                (int) jugadorX - 50,
                (int) jugadorY - 50,
                TAM,
                TAM);

        // Caja marrón
        g.setColor(new Color(120, 55, 6));

        g.fillRect(
                (int) cajaX,
                (int) cajaY,
                TAM,
                TAM);

        // Contador de movimientos
        Texto.izquierda(
                g,
                "Movimientos: " + contadorMovimientos,
                16, 28, 18,
                Color.WHITE);

        // Instrucciones
        Texto.centrado(
                g,
                "Flechas: mover, Z: deshacer",
                ancho / 2.0,
                alto - 20,
                14,
                Color.LIGHT_GRAY);

        // Mensaje de victoria
        if (gano) {
            Texto.centrado(
                    g,
                    "YOU WIN!",
                    ancho / 2.0,
                    alto / 2.0,
                    40,
                    Color.GREEN);

            Texto.centrado(
                    g,
                    "Presiona ENTER para jugar el siguiente nivel",
                    ancho / 2.0,
                    alto / 2.0 + 50,
                    18,
                    Color.WHITE);
        }
    }
}
