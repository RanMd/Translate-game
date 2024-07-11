package com.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Category {
    private final String name;
    private final List<WordPair> words;

    public Category(String name) {
        this.name = name;
        this.words = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<WordPair> getWords() {
        return words;
    }

    public WordPair[] getWordsArray() {
        return words.toArray(new WordPair[0]);
    }

    public void addWord(WordPair palabra) {
        words.add(palabra);
    }

    public WordPair getRandomWord() {
        if (words.isEmpty()) {
            return null;
        }
        int indexRnd = new Random().nextInt(words.size());
        return words.get(indexRnd);
    }

    @Override
    public String toString() {
        return name;
    }
}
