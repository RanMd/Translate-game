package com.models.list;

public class Nodo<T> {
    public T dato;
    public Nodo<T> siguiente;
    
    public Nodo(Nodo<T> siguiente, T dato){
        this.dato = dato;
        this.siguiente = siguiente;
    }
    
    public Nodo(T dato){
        this.dato = dato;
        this.siguiente = null;
    }
}
