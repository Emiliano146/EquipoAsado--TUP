package demo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import motor.Entrada;
import motor.Escena;
import motor.Temporizador;
import ui.Texto;

import estructuras.PilaEnlazada;

public class Tutorial extends Escena {

    // Tamaño de cada casilla
    private final int TAM = 100;

    // Jugador
    private Jugador jugador;

    // Entidades
    private ArrayList<Caja> cajas = new ArrayList<>();
    private ArrayList<Pared> paredes = new ArrayList<>();
    private ArrayList<Meta> metas = new ArrayList<>();

    // Temporizador de parpadeo
    private final Temporizador parpadeo = new Temporizador(0.5);

    private boolean gano;
    private boolean visible = true;

    // Contador
    private int contadorMovimientos;

    // Pila para deshacer
    private PilaEnlazada<Estado> movimientos = new PilaEnlazada<>();

    // =====================================
    // ESTADO DEL TABLERO
    // =====================================

    private class Estado {

        int jugadorX;
        int jugadorY;

        ArrayList<int[]> posicionesCajas = new ArrayList<>();

        Estado() {

            jugadorX = jugador.getX();
            jugadorY = jugador.getY();

            for (Caja caja : cajas) {

                posicionesCajas.add(
                        new int[] {
                                caja.getX(),
                                caja.getY()
                        });
            }
        }
    }

    // =====================================
    // ENTRADA A LA ESCENA
    // =====================================

    @Override
    public void alEntrar() {

        jugador = new Jugador(0, 0);

        cajas.clear();
        paredes.clear();
        metas.clear();

        movimientos.vaciar();

        contadorMovimientos = 0;

        gano = false;
        visible = true;

        parpadeo.reiniciar();

        /// JUGADOR
        jugador.establecerPosicion(0, 0);

        // CAJAS
        cajas.add(new Caja(200, 100));
        cajas.add(new Caja(300, 200));

        // METAS
        metas.add(new Meta(300, 100));
        metas.add(new Meta(300, 300));

        // PAREDES
        paredes.add(new Pared(100, 200));
        paredes.add(new Pared(200, 300));
    }

    // =====================================
    // COMPROBAR SI UNA POSICIÓN ESTÁ OCUPADA
    // =====================================

    private boolean hayPared(int x, int y) {

        for (Pared pared : paredes) {

            if (pared.getX() == x &&
                    pared.getY() == y) {

                return true;
            }
        }

        return false;
    }

    private Caja obtenerCaja(int x, int y) {

        for (Caja caja : cajas) {

            if (caja.getX() == x &&
                    caja.getY() == y) {

                return caja;
            }
        }

        return null;
    }

    private boolean dentroLimites(int x, int y) {

        return x >= 0 &&
                x <= 400 &&
                y >= 0 &&
                y <= 400;
    }

    // =====================================
    // MOVIMIENTO DEL JUGADOR
    // =====================================

    private void moverJugador(int dx, int dy) {

        int nuevoX = jugador.getX() + dx;
        int nuevoY = jugador.getY() + dy;

        // Comprobar límites
        if (!dentroLimites(nuevoX, nuevoY)) {
            return;
        }

        // Comprobar paredes
        if (hayPared(nuevoX, nuevoY)) {
            return;
        }

        // Comprobar si hay una caja
        Caja caja = obtenerCaja(nuevoX, nuevoY);

        if (caja != null) {

            int cajaNuevaX = caja.getX() + dx;
            int cajaNuevaY = caja.getY() + dy;

            // Comprobar límites de la caja
            if (!dentroLimites(cajaNuevaX, cajaNuevaY)) {
                return;
            }

            // No empujar contra una pared
            if (hayPared(cajaNuevaX, cajaNuevaY)) {
                return;
            }

            // No empujar contra otra caja
            if (obtenerCaja(cajaNuevaX, cajaNuevaY) != null) {
                return;
            }

            // Guardar estado anterior
            movimientos.apilar(new Estado());

            // Mover caja
            caja.mover(dx, dy);

        } else {

            // Guardar estado anterior
            movimientos.apilar(new Estado());
        }

        // Mover jugador
        jugador.mover(dx, dy);

        contadorMovimientos++;
    }

    // =====================================
    // DESHACER MOVIMIENTO
    // =====================================

    private void deshacerMovimiento() {

        if (movimientos.estaVacia()) {
            return;
        }

        Estado anterior = movimientos.desapilar();

        // Restaurar jugador
        jugador.establecerPosicion(
                anterior.jugadorX,
                anterior.jugadorY);

        // Restaurar cajas
        for (int i = 0; i < cajas.size(); i++) {

            int[] posicion = anterior.posicionesCajas.get(i);

            cajas.get(i).establecerPosicion(
                    posicion[0],
                    posicion[1]);
        }

        contadorMovimientos--;
    }

    // =====================================
    // COMPROBAR VICTORIA
    // =====================================

    private boolean todasLasCajasEnMetas() {

        // Debe haber la misma cantidad
        if (cajas.size() != metas.size()) {
            return false;
        }

        for (Caja caja : cajas) {

            boolean estaEnMeta = false;

            for (Meta meta : metas) {

                if (caja.getX() == meta.getX() &&
                        caja.getY() == meta.getY()) {

                    estaEnMeta = true;
                    break;
                }
            }

            if (!estaEnMeta) {
                return false;
            }
        }

        return true;
    }

    // =====================================
    // ACTUALIZACIÓN
    // =====================================

    @Override
    public void actualizar(double dt) {

        Entrada entrada = getEntrada();

        // REINICIAR NIVEL
        if (entrada.fuePresionada(KeyEvent.VK_R)) {
            alEntrar();
            return;
        }

        if (gano) {

            if (entrada.fuePresionada(KeyEvent.VK_ENTER)) {
                getContexto().irA(new EscenaDemoDos());
            }

            return;
        }

        // DESHACER
        if (entrada.fuePresionada(KeyEvent.VK_Z)) {

            deshacerMovimiento();

        } else {

            // MOVIMIENTO
            if (entrada.fuePresionada(KeyEvent.VK_LEFT)) {

                moverJugador(-TAM, 0);

            } else if (entrada.fuePresionada(KeyEvent.VK_RIGHT)) {

                moverJugador(TAM, 0);

            } else if (entrada.fuePresionada(KeyEvent.VK_UP)) {

                moverJugador(0, -TAM);

            } else if (entrada.fuePresionada(KeyEvent.VK_DOWN)) {

                moverJugador(0, TAM);
            }
        }

        // PARPADEO DE LAS METAS
        if (parpadeo.avanzar(dt)) {

            visible = !visible;

            for (Meta meta : metas) {
                meta.alternarVisibilidad();
            }
        }

        // VICTORIA
        if (todasLasCajasEnMetas()) {
            gano = true;
        }

        // SALIR
        if (entrada.fuePresionada(KeyEvent.VK_ESCAPE)) {
            getContexto().salir();
        }
    }

    // =====================================
    // DIBUJO
    // =====================================

    @Override
    public void dibujar(Graphics2D g) {

        int ancho = getContexto().getAncho();
        int alto = getContexto().getAlto();

        // FONDO
        g.setColor(new Color(20, 24, 36));

        g.fillRect(0, 0, ancho, alto);

        // METAS
        for (Meta meta : metas) {
            meta.dibujar(g);
        }

        // PAREDES
        for (Pared pared : paredes) {
            pared.dibujar(g);
        }

        // CAJAS
        for (Caja caja : cajas) {
            caja.dibujar(g);
        }

        // JUGADOR
        jugador.dibujar(g);

        // CONTADOR
        Texto.izquierda(
                g,
                "Movimientos: " + contadorMovimientos,
                16, 28, 18,
                Color.WHITE);

        // INSTRUCCIONES
        Texto.centrado(
                g,
                "TUTORIAL /// Flechas: mover | Z: deshacer | R: reiniciar",
                ancho / 2.0,
                alto - 20,
                14,
                Color.LIGHT_GRAY);

        // VICTORIA
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