package com.controllers;

import com.controllers.components.TextFieldNode;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.models.Category;
import com.models.WordPair;
import com.models.list.Lista;
import com.util.Data;
import com.util.interfaces.InitFlowController;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.scene.control.TextArea;

public class OptionsViewController implements Initializable, InitFlowController {
    private Category actualCategory;
    private boolean someWordAdded = false;
    private Runnable returnToMenu;

    private final Lista<Category> tempCategories = new Lista<>();

    @FXML
    private JFXButton addCategory;

    @FXML
    private JFXButton addWord;

    @FXML
    private JFXButton btnReturn;

    @FXML
    private JFXComboBox<String> comboSelectCat;

    @FXML
    private TextField fieldAddCategory;

    @FXML
    private TextField addWordENField;

    @FXML
    private TextField addWordSPField;

    @FXML
    private TextArea categoriesArea;

    @Override
    public void initController(Runnable returnToMenu) {
        this.returnToMenu = returnToMenu;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDataToCombo();
        addCategory.setOnAction(event -> addCategory());
        addWord.setOnAction(event -> addWordToCategory());
        btnReturn.setOnAction(event -> changeToMenu());

        TextFieldNode.addTextFormatterAll(addWordENField, addWordSPField, fieldAddCategory);

        comboSelectCat.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                addWord.setDisable(false);
                addWordENField.setDisable(false);
                addWordSPField.setDisable(false);
                setActualCategory(newValue);
                updateTextArea();
            }
        });

        CSSFX.start();
    }

    private void changeToMenu() {
        if (saveData()) {
            reset();
            returnToMenu.run();
        }
    }

    private void addCategory() {
        String categoryName = fieldAddCategory.getText();
        if (categoryName.isEmpty()) return;

        Category category = new Category(categoryName);
        tempCategories.insertNode(category);
        fieldAddCategory.clear();
        updateComboBox();
    }

    public void loadDataToCombo() {
        comboSelectCat.getItems().clear();
        for (Category category : Data.getCategories().toArray(Category[].class)) {
            comboSelectCat.getItems().addAll(category.getName());
        }
    }

    private void addWordToCategory() {
        String spanishWord = addWordSPField.getText();
        String englishWord = addWordENField.getText();

        if (!spanishWord.isEmpty() && !englishWord.isEmpty()) {
            WordPair word = new WordPair(spanishWord, englishWord);
            actualCategory.addWord(word);
            addWordSPField.clear();
            addWordENField.clear();
            updateTextArea();
            someWordAdded = true;
        }
    }

    private boolean saveData() {
        if (tempCategories.isEmpty() && !someWordAdded) return true;

        boolean someEmpty = false;
        for (Category tempCategory : tempCategories.toArray(Category[].class)) {
            if (tempCategory.getWords().isEmpty()) {
                someEmpty = true;
                break;
            }
        }

        if (!someEmpty) {
            Data.addCategories(tempCategories);
            return true;
        }

        AtomicBoolean save = new AtomicBoolean(false);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("Una o mas categorias se encuentran sin palabras. Si continua las categorias seran eliminadas" + "\n¿Desea continuar?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                tempCategories.removeIf(tempCategory -> tempCategory.getWords().isEmpty());
                Data.addCategories(tempCategories);
                save.set(true);
            } else {
                save.set(false);
            }
        });
        return save.get();
    }

    private void updateComboBox() {
        comboSelectCat.getItems().add(tempCategories.search(tempCategories.size() - 1).getName());
    }

    private void updateTextArea() {
        final StringBuilder sb = new StringBuilder();
        sb.append(actualCategory.getName()).append(":\n");
        for (WordPair word : actualCategory.getWords()) {
            sb.append(" - ").append(word).append("\n");
        }
        categoriesArea.setText(sb.toString());
    }

    private void setActualCategory(String categoryName) {
        actualCategory = Data.searchCategory(categoryName);
        if (actualCategory == null) {
            for (Category tempCategory : tempCategories.toArray(Category[].class)) {
                if (tempCategory.getName().equals(categoryName)) {
                    actualCategory = tempCategory;
                    break;
                }
            }
        }
    }

    private void reset() {
        tempCategories.clear();
        someWordAdded = false;
        loadDataToCombo();
        categoriesArea.setText("Seleccione una categoria");
        addWordSPField.clear();
        addWordENField.clear();
        addWordSPField.setDisable(true);
        addWordENField.setDisable(true);
    }
}
