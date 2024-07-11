// Palabra.java
package com.models;

import java.util.Random;

public class WordPair {
    private final String spanish;
    private final String english;
    private final String[] words;
    private boolean isSpanish;

    public WordPair(String spanish, String english) {
        this.spanish = spanish;
        this.english = english;
        this.words = new String[]{spanish, english};
    }

    public String getWord() {
        final int index = new Random().nextInt(words.length);
        isSpanish = index == 0;
        return words[index];
    }

    public String getCorrectWord() {
        return isSpanish ? english : spanish;
    }

    @Override
    public String toString() {
        return spanish + ":" + english;
    }

    public boolean checkTranslate(String word) {
        return isSpanish ? this.english.equalsIgnoreCase(word) : this.spanish.equalsIgnoreCase(word);
    }
}
