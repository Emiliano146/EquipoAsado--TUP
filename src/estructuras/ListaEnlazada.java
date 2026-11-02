package estructuras;

public class ListaEnlazada<T> implements Lista<T> {

    private class Nodo {
        T dato;
        Nodo siguiente;

        Nodo(T dato) {
            this.dato = dato;
        }
    }

    private Nodo inicio;
    private int cantidad;

    public void agregar(T dato) {
        insertar(cantidad, dato);
    }

    public void insertar(int i, T dato) {
        if (i < 0 || i > cantidad)
            throw new IndexOutOfBoundsException();

        Nodo nuevo = new Nodo(dato);

        if (i == 0) {
            nuevo.siguiente = inicio;
            inicio = nuevo;
        } else {
            Nodo actual = inicio;
            for (int j = 0; j < i - 1; j++)
                actual = actual.siguiente;

            nuevo.siguiente = actual.siguiente;
            actual.siguiente = nuevo;
        }

        cantidad++;
    }

    public T obtener(int i) {
        if (i < 0 || i >= cantidad)
            throw new IndexOutOfBoundsException();

        Nodo actual = inicio;
        for (int j = 0; j < i; j++)
            actual = actual.siguiente;

        return actual.dato;
    }

    public int indiceDe(T dato) {
        Nodo actual = inicio;
        int i = 0;

        while (actual != null) {
            if (dato == null ? actual.dato == null : dato.equals(actual.dato))
                return i;

            actual = actual.siguiente;
            i++;
        }

        return -1;
    }

    public int tamanio() {
        return cantidad;
    }

    public boolean estaVacia() {
        return cantidad == 0;
    }

    public T eliminar(int i) {
        if (i < 0 || i >= cantidad)
            throw new IndexOutOfBoundsException();

        Nodo actual = inicio;
        T dato;

        if (i == 0) {
            dato = inicio.dato;
            inicio = inicio.siguiente;
        } else {
            for (int j = 0; j < i - 1; j++)
                actual = actual.siguiente;

            dato = actual.siguiente.dato;
            actual.siguiente = actual.siguiente.siguiente;
        }

        cantidad--;
        return dato;
    }

    public void vaciar() {
        inicio = null;
        cantidad = 0;
    }
}
