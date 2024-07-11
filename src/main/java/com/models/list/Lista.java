package com.models.list;

public class Lista<T> {

    private Nodo<T> primero;
    private Nodo<T> ultimo;
    private int cantidad;

    public int getSize() {
        return cantidad;
    }
    
    public void clear() {
        primero = null;
        ultimo = null;
        cantidad = 0;
    }

    public void insertNode(T dato) {
        if (primero == null) {
            primero = new Nodo<>(dato);
            ultimo = primero;
        } else {
            this.ultimo.siguiente = new Nodo<>(dato);
            ultimo = ultimo.siguiente;
        }
        cantidad++;
    }

    public void insertAll(T[] datos) {
        for (T dato : datos) {
            insertNode(dato);
        }
    }

    public boolean isEmpty() {
        return primero == null;
    }

    public int size() {
        return cantidad;
    }

    public T getUltimo() {
        return ultimo.dato;
    }

    public T search(T dato) {
        if (primero == null) {
            return null;
        }

        Nodo<T> actual = primero;

        while (actual != null) {
            if (actual.dato.equals(dato)) {
                return actual.dato;
            }

            actual = actual.siguiente;
        }

        return null;
    }

    public T search(int index) {
        if (index < 0 || index >= cantidad) {
            return null;
        }

        Nodo<T> actual = primero;

        for (int i = 0; i < index; i++) {
            actual = actual.siguiente;
        }

        return actual.dato;
    }

    public T search(Comparator<T> comparador) {
        if (primero == null) {
            return null;
        }

        Nodo<T> actual = primero;

        while (actual != null) {
            if (comparador.compare(actual.dato)) {
                return actual.dato;
            }

            actual = actual.siguiente;
        }

        return null;
    }

    public void removeIf(Condition<T> condition) {
        if (primero == null) {
            return;
        }

        Nodo<T> actual = primero;
        Nodo<T> anterior = null;

        while (actual != null) {
            if (condition.test(actual.dato)) {
                if (anterior == null) {
                    primero = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }
                cantidad--;
            } else {
                anterior = actual;
            }

            actual = actual.siguiente;
        }
    }
    
    public T[] toArray(Class<T[]> type) {
        if (getSize() == 0) {
            return null;
        }

        int size = getSize();
        T[] array = type.cast(java.lang.reflect.Array.newInstance(type.getComponentType(), size));

        Nodo<T> actual = primero;

        for (int i = 0; i < cantidad; i++) {
            array[i] = actual.dato;

            actual = actual.siguiente;
        }

        return array;
    }

    public T[] toArrayFiltered(Class<T[]> type, Condition<T> condition) {
        if (getSize() == 0) {
            return null;
        }

        Lista<T> filteredList = new Lista<>();
        Nodo<T> actual = primero;

        for (int i = 0; i < cantidad; i++) {
            if (condition.test(actual.dato)) {
                filteredList.insertNode(actual.dato);
            }
            actual = actual.siguiente;
        }
        
        return filteredList.toArray(type);
    }

    @FunctionalInterface
    public interface Condition<T> {
        boolean test(T obj);
    }

}
