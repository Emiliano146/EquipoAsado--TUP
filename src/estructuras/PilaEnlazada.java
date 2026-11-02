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
    private int cantidad;

    @Override
    public void apilar(T elemento) {
        Nodo nuevo = new Nodo(elemento);
        nuevo.siguiente = tope;
        tope = nuevo;
        cantidad++;
    }

    @Override
    public T desapilar() {
        if (estaVacia()) {
            throw new IllegalStateException("La pila está vacía");
        }

        T elemento = tope.dato;
        tope = tope.siguiente;
        cantidad--;
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
        return cantidad == 0;
    }

    @Override
    public int tamanio() {
        return cantidad;
    }

    @Override
    public void vaciar() {
        tope = null;
        cantidad = 0;
    }
}
