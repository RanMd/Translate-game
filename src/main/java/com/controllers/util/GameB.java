package com.controllers.util;

import com.controllers.components.PlayerNode;
import com.models.Category;
import com.models.WordPair;
import javafx.application.Platform;
import javafx.scene.control.TextField;

import java.util.Random;

public class GameB {
    private final PlayerNode[] players;
    private final WordPair[] words;
    private final Random rnd;
    private final int maxWordTurns;

    private WordPair currentWord;
    private int survivors;
    private int wordTurns;
    private int currentPlayer;
    private int deadSpaces;

    public GameB(PlayerNode[] players, Category category, int maxWordTurns) {
        this.players = players;
        this.words = category.getWordsArray();
        this.survivors = players.length;
        this.maxWordTurns = maxWordTurns;
        this.wordTurns = 0;
        this.rnd = new Random();
        this.currentPlayer = 0;
        this.deadSpaces = 0;
    }

    public WordPair getCurrentWord() {
        return currentWord;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getNumPlayers() {
        return players.length;
    }

    public int getDeadSpaces() {
        return deadSpaces;
    }

    public PlayerNode getWinner() {
        calcDeadSpaces();
        currentPlayer = (currentPlayer + deadSpaces) % players.length;
        return players[currentPlayer];
    }

    public boolean someSurvivor() {
        return survivors > 1;
    }

    public void chosePlayer() {
        currentPlayer = rnd.nextInt(players.length);
        players[currentPlayer].setActive(true);
    }

    public void changeWord() {
        if (wordTurns < maxWordTurns) {
            currentWord = words[rnd.nextInt(words.length)];
            wordTurns++;
        }
    }

    public void playerLoseLife() {
        players[currentPlayer].loseLife();
        if (players[currentPlayer].isDead()) survivors--;
    }

    public void playerAnswered(boolean isCorrect) {
        players[currentPlayer].playAnswerAnimation(isCorrect);
    }

    public void nextPlayerTurn(boolean loseTurn, Runnable onChange) {
        if (loseTurn) {
            wordTurns++;
            if (wordTurns >= maxWordTurns) {
                wordTurns = 0;
                onChange.run();
            }
        } else onChange.run();

        players[currentPlayer].setActive(false);
        currentPlayer = (currentPlayer + 1) % players.length;
        calcDeadSpaces();
        players[currentPlayer].setActive(true);
    }

    public void calcDeadSpaces() {
        deadSpaces = 1;
        while (players[currentPlayer].isDead()) {
            deadSpaces++;
            currentPlayer = (currentPlayer + 1) % players.length;
        }
    }

    public void reset() {
        for (PlayerNode player : players) player.reset();

        survivors = players.length;
        wordTurns = 0;
        currentPlayer = 0;
        deadSpaces = 0;
    }

    public void postInitPlayers(TextField input) {
        for (PlayerNode player : players) {
            player.postInit(input);
        }
    }

    public void posWordPlayer() {
        for (PlayerNode player : players) {
            Platform.runLater(player::posWord);
        }
    }
}
