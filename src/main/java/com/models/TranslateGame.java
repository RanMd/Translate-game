package com.models;

import com.models.tree.ArbolBinario;
import com.util.Assets;

import java.util.Random;

public class TranslateGame {
    private final Player[] players;
    private final WordPair[] words;
    private final Random rnd = new Random();
    private final int numPlayers;
    private final int maxRounds;

    private int currentPLayer;
    private WordPair currentWord;
    private int cantRounds;

    public TranslateGame(Player[] players, Category category, int maxRounds) {
        this.players = players;
        this.words = category.getWordsArray();
        this.numPlayers = players.length;
        this.maxRounds = maxRounds;
        this.currentWord = category.getRandomWord();
    }

    public Player playerWinner() {
        if (players.length == 0) return null;

        final ArbolBinario arbol = new ArbolBinario();

        for (Player jugador : players) arbol.insertar(jugador);

        return arbol.encontrarMinimo();
    }

    public void nextPlayerTurn() {
        currentPLayer = (currentPLayer + 1) % numPlayers;

        if (currentPLayer == 0) cantRounds++;
    }

    public WordPair getCurrentWord() {
        return currentWord;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public Player getCurrentPlayer() {
        return players[currentPLayer];
    }

    public void changeWord() {
        currentWord = words[rnd.nextInt(words.length)];
    }

    public boolean isEndGame() {
        return cantRounds == maxRounds;
    }

    public void playerAnswered(boolean isCorrect, int time) {
        if (!isCorrect) {
            getCurrentPlayer().incrementarErrores();
            Assets.incorrectSound.play();
            return;
        }

        getCurrentPlayer().setPuntajeTiempo(time);
    }


}
