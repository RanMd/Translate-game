package com.models;

import com.models.tree.ArbolBinario;

import java.util.ArrayList;
import java.util.List;

public class TranslateGame {
    private final List<Player> players;
    private int numPlayers;

    public TranslateGame() {
        this.players = new ArrayList<>();
    }

    public Player playerWinner() {
        if (players.isEmpty()) return null;

        final ArbolBinario arbol = new ArbolBinario();

        for (Player jugador : players) arbol.insertar(jugador);

        return arbol.encontrarMinimo();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        final int MAX_PLAYERS = 5;
        if (numPlayers == MAX_PLAYERS) return;
        players.add(player);
        numPlayers++;
    }
}
