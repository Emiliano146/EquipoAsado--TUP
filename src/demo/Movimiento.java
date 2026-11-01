
package demo;

public class Movimiento {

    private final double jugadorX;
    private final double jugadorY;

    private final double cajaX;
    private final double cajaY;

    // Constructor
    public Movimiento(double jugadorX, double jugadorY, double cajaX, double cajaY) {

        this.jugadorX = jugadorX;
        this.jugadorY = jugadorY;

        this.cajaX = cajaX;
        this.cajaY = cajaY;
    }

    // No necesitamos setters, porque los valores no cambian

    // Getters
    public double getJugadorX() {
        return jugadorX;
    }

    public double getJugadorY() {
        return jugadorY;
    }

    public double getCajaX() {
        return cajaX;
    }

    public double getCajaY() {
        return cajaY;
    }
}