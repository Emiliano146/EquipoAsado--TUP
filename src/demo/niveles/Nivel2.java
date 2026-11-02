package demo.niveles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import demo.Caja;
import demo.Jugador;
import demo.Meta;
import demo.Pared;
import motor.Escena;
import motor.Entrada;
import motor.Temporizador;
import ui.Texto;

import estructuras.Lista;
import estructuras.ListaEnlazada;
import estructuras.Pila;
import estructuras.PilaEnlazada;

public class Nivel2 extends Escena {

    private final int TAM = 100;

    private Jugador jugador;

    private Lista<Caja> cajas = new ListaEnlazada<>();
    private Lista<Pared> paredes = new ListaEnlazada<>();
    private Lista<Meta> metas = new ListaEnlazada<>();

    private Pila<Estado> movimientos = new PilaEnlazada<>();

    private final Temporizador parpadeo = new Temporizador(0.5);

    private boolean gano;
    private int contadorMovimientos;

    private class PosicionCaja {
        int x, y;

        PosicionCaja(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private class Estado {
        int jugadorX, jugadorY;
        Lista<PosicionCaja> posiciones = new ListaEnlazada<>();

        Estado() {
            jugadorX = jugador.getX();
            jugadorY = jugador.getY();

            for (int i = 0; i < cajas.tamanio(); i++) {
                Caja c = cajas.obtener(i);
                posiciones.agregar(new PosicionCaja(c.getX(), c.getY()));
            }
        }
    }

    @Override
    public void alEntrar() {
        reiniciarNivel();
    }

    private void reiniciarNivel() {
        jugador = new Jugador(400, 200);

        cajas.vaciar();
        paredes.vaciar();
        metas.vaciar();
        movimientos.vaciar();

        contadorMovimientos = 0;
        gano = false;

        parpadeo.reiniciar();

        cajas.agregar(new Caja(100, 200));
        cajas.agregar(new Caja(200, 100));
        cajas.agregar(new Caja(300, 100));
        cajas.agregar(new Caja(300, 200));

        metas.agregar(new Meta(0, 300));
        metas.agregar(new Meta(0, 400));
        metas.agregar(new Meta(100, 300));
        metas.agregar(new Meta(100, 400));

        paredes.agregar(new Pared(400, 300));
        paredes.agregar(new Pared(300, 300));
        paredes.agregar(new Pared(200, 300));
        paredes.agregar(new Pared(200, 400));
        paredes.agregar(new Pared(300, 400));
        paredes.agregar(new Pared(400, 400));

    }

    private boolean dentroLimites(int x, int y) {
        return x >= 0 && x <= 400 && y >= 0 && y <= 400;
    }

    private boolean hayPared(int x, int y) {
        for (int i = 0; i < paredes.tamanio(); i++) {
            Pared p = paredes.obtener(i);

            if (p.getX() == x && p.getY() == y)
                return true;
        }

        return false;
    }

    private Caja obtenerCaja(int x, int y) {
        for (int i = 0; i < cajas.tamanio(); i++) {
            Caja c = cajas.obtener(i);

            if (c.getX() == x && c.getY() == y)
                return c;
        }

        return null;
    }

    private void moverJugador(int dx, int dy) {
        int nx = jugador.getX() + dx;
        int ny = jugador.getY() + dy;

        if (!dentroLimites(nx, ny) || hayPared(nx, ny))
            return;

        Caja caja = obtenerCaja(nx, ny);

        if (caja != null) {
            int cx = caja.getX() + dx;
            int cy = caja.getY() + dy;

            if (!dentroLimites(cx, cy)
                    || hayPared(cx, cy)
                    || obtenerCaja(cx, cy) != null)
                return;
        }

        movimientos.apilar(new Estado());

        if (caja != null)
            caja.mover(dx, dy);

        jugador.mover(dx, dy);
        contadorMovimientos++;
    }

    private void deshacerMovimiento() {
        if (movimientos.estaVacia())
            return;

        Estado anterior = movimientos.desapilar();

        jugador.establecerPosicion(
                anterior.jugadorX, anterior.jugadorY);

        for (int i = 0; i < cajas.tamanio(); i++) {
            PosicionCaja p = anterior.posiciones.obtener(i);
            cajas.obtener(i).establecerPosicion(p.x, p.y);
        }

        if (contadorMovimientos > 0)
            contadorMovimientos--;
    }

    private boolean todasLasCajasEnMetas() {
        if (cajas.tamanio() != metas.tamanio())
            return false;

        for (int i = 0; i < cajas.tamanio(); i++) {
            Caja c = cajas.obtener(i);
            boolean encontrada = false;

            for (int j = 0; j < metas.tamanio(); j++) {
                Meta m = metas.obtener(j);

                if (c.getX() == m.getX() && c.getY() == m.getY()) {
                    encontrada = true;
                    break;
                }
            }

            if (!encontrada)
                return false;
        }

        return true;
    }

    @Override
    public void actualizar(double dt) {
        Entrada entrada = getEntrada();

        if (entrada.fuePresionada(KeyEvent.VK_R)) {
            reiniciarNivel();
            return;
        }

        if (gano) {
            if (entrada.fuePresionada(KeyEvent.VK_ENTER))
                getContexto().irA(new Nivel3());
            return;
        }

        if (entrada.fuePresionada(KeyEvent.VK_Z)) {
            deshacerMovimiento();
        } else if (entrada.fuePresionada(KeyEvent.VK_LEFT)) {
            moverJugador(-TAM, 0);
        } else if (entrada.fuePresionada(KeyEvent.VK_RIGHT)) {
            moverJugador(TAM, 0);
        } else if (entrada.fuePresionada(KeyEvent.VK_UP)) {
            moverJugador(0, -TAM);
        } else if (entrada.fuePresionada(KeyEvent.VK_DOWN)) {
            moverJugador(0, TAM);
        }

        if (parpadeo.avanzar(dt)) {
            for (int i = 0; i < metas.tamanio(); i++)
                metas.obtener(i).alternarVisibilidad();
        }

        if (todasLasCajasEnMetas())
            gano = true;

        if (entrada.fuePresionada(KeyEvent.VK_ESCAPE))
            getContexto().salir();
    }

    @Override
    public void dibujar(Graphics2D g) {
        int ancho = getContexto().getAncho();
        int alto = getContexto().getAlto();

        g.setColor(new Color(20, 24, 36));
        g.fillRect(0, 0, ancho, alto);

        for (int i = 0; i < metas.tamanio(); i++)
            metas.obtener(i).dibujar(g);

        for (int i = 0; i < paredes.tamanio(); i++)
            paredes.obtener(i).dibujar(g);

        for (int i = 0; i < cajas.tamanio(); i++)
            cajas.obtener(i).dibujar(g);

        jugador.dibujar(g);

        Texto.izquierda(g,
                "Movimientos: " + contadorMovimientos,
                16, 28, 18, Color.WHITE);

        Texto.centrado(g,
                "[Nivel 2] - Flechas: mover | Z: deshacer | R: reiniciar",
                ancho / 2.0, alto - 20, 14, Color.LIGHT_GRAY);

        if (gano) {
            Texto.centrado(g, "YOU WIN!",
                    ancho / 2.0, alto / 2.0, 40, Color.GREEN);

            Texto.centrado(g, "ENTER: continuar",
                    ancho / 2.0, alto / 2.0 + 50, 18, Color.WHITE);
        }
    }
}