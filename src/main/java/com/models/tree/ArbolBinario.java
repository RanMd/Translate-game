package com.models.tree;

import com.models.Player;

public class ArbolBinario {
    Nodo raiz;

    public ArbolBinario() {
        raiz = null;
    }

    public void insertar(Player jugador) {
        raiz = insertarRec(raiz, jugador);
    }

    Nodo insertarRec(Nodo raiz, Player jugador) {
        if (raiz == null) {
            raiz = new Nodo(jugador);
            return raiz;
        }

        if (jugador.getPuntajeTiempo() < raiz.jugador.getPuntajeTiempo() ||
                (jugador.getPuntajeTiempo() == raiz.jugador.getPuntajeTiempo() && jugador.getErrores() < raiz.jugador.getErrores())) {
            raiz.izquierdo = insertarRec(raiz.izquierdo, jugador);
        } else {
            raiz.derecho = insertarRec(raiz.derecho, jugador);
        }

        return raiz;
    }

    public Player encontrarMinimo() {
        if (raiz == null) {
            return null;
        }
        Nodo actual = raiz;
        while (actual.izquierdo != null) {
            actual = actual.izquierdo;
        }
        return actual.jugador;
    }
}