package demo;

import motor.Ventana;

public class Main {

    public static void main(String[] args) {
        Ventana.abrir(
                "Sokoban 5x5",
                500,
                500,
                new EscenaMenu());
    }
}
