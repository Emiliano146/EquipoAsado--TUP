package estructuras;

public class PilaEnlazada<T> implements Pila<T> {

    private class Nodo {
        T dato;
        Nodo siguiente;

        Nodo(T dato) {
            this.dato = dato;
        }
    }

    private Nodo tope;
    private int tamanio;

    public PilaEnlazada() {
        tope = null;
        tamanio = 0;
    }

    @Override
    public void apilar(T elemento) {
        Nodo nuevo = new Nodo(elemento);

        nuevo.siguiente = tope;
        tope = nuevo;

        tamanio++;
    }

    @Override
    public T desapilar() {
        if (estaVacia()) {
            throw new IllegalStateException("La pila está vacía");
        }

        T elemento = tope.dato;

        tope = tope.siguiente;

        tamanio--;

        return elemento;
    }

    @Override
    public T tope() {
        if (estaVacia()) {
            throw new IllegalStateException("La pila está vacía");
        }

        return tope.dato;
    }

    @Override
    public boolean estaVacia() {
        return tamanio == 0;
    }

    @Override
    public int tamanio() {
        return tamanio;
    }

    @Override
    public void vaciar() {
        tope = null;
        tamanio = 0;
    }
}