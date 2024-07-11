package com.models;

import com.models.tree.ArbolBinario;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TranslateGame {
    private final List<Category> categories;
    private List<Player> players;
    private int numPlayers;

    public TranslateGame() {
        this.categories = new ArrayList<>();
        this.players = new ArrayList<>();
        this.numPlayers = 0;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void addCategory(Category categoria) {
        categories.add(categoria);
    }

    public Category buscarCategoria(String name) {
        for (Category categoria : categories) {
            if (categoria.getName().equals(name)) {
                return categoria;
            }
        }
        return null;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Category getRandomCategory() {
        if (categories.isEmpty()) return null;
        final int index = new Random().nextInt(categories.size());
        return categories.get(index);
    }

    public Player playerWinner() {
        if (players.isEmpty()) {
            return null;
        }

        ArbolBinario arbol = new ArbolBinario();
        for (Player jugador : players) {
            arbol.insertar(jugador);
        }
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
