package com.models.tree;

import com.models.Player;

public class Nodo {
    Player jugador;
    Nodo izquierdo, derecho;

    public Nodo(Player jugador) {
        this.jugador = jugador;
        izquierdo = derecho = null;
    }
}