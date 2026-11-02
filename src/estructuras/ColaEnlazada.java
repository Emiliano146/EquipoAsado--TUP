package estructuras;

public class ColaEnlazada<T> implements Cola<T> {

    private class Nodo {
        T dato;
        Nodo siguiente;

        Nodo(T dato) {
            this.dato = dato;
        }
    }

    private Nodo frente;
    private Nodo fin;
    private int cantidad;

    public void encolar(T dato) {
        Nodo nuevo = new Nodo(dato);

        if (estaVacia())
            frente = nuevo;
        else
            fin.siguiente = nuevo;

        fin = nuevo;
        cantidad++;
    }

    public T desencolar() {
        if (estaVacia())
            throw new IllegalStateException("Cola vacía");

        T dato = frente.dato;
        frente = frente.siguiente;
        cantidad--;

        if (frente == null)
            fin = null;

        return dato;
    }

    public T frente() {
        if (estaVacia())
            throw new IllegalStateException("Cola vacía");

        return frente.dato;
    }

    public boolean estaVacia() {
        return cantidad == 0;
    }

    public int tamanio() {
        return cantidad;
    }

    public void vaciar() {
        frente = null;
        fin = null;
        cantidad = 0;
    }
}