package com.util;

import com.models.Category;
import com.models.WordPair;
import com.models.list.Lista;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Data {
    private static final Lista<Category> categories;
    private static final File fileData = new File("data.txt");

    static {
        categories = new Lista<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileData))) {
            String line;
            Category actualCategory = null;

            while ((line = br.readLine()) != null) {
                if (!line.contains(":")) {
                    actualCategory = new Category(line);
                    categories.insertNode(actualCategory);
                } else {
                    String[] parts = line.split(":");
                    WordPair word = new WordPair(parts[0], parts[1]);
                    assert actualCategory != null;
                    actualCategory.addWord(word);
                }
            }
        } catch (IOException e) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static Lista<Category> getCategories() {
        return categories;
    }

    public static Category searchCategory(String name) {
        for (Category category : categories.toArray(Category[].class)) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }

    public static void addCategories(Lista<Category> categories) {
        if (categories.isEmpty()) return;
        Data.categories.insertAll(categories.toArray(Category[].class));
        saveData();
    }

    public static void saveData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileData))) {
            for (Category category : categories.toArray(Category[].class)) {
                bw.write(category.getName());
                bw.newLine();
                for (WordPair word : category.getWords()) {
                    bw.write(word.toString());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static Category getRandomCategory() {
        if (categories.isEmpty()) return null;
        final int index = new Random().nextInt(categories.size());
        return categories.search(index);
    }

    public static void initClass() {
        // Initialize class
    }
}
