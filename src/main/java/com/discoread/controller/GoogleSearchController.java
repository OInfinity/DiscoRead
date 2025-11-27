package com.discoread.controller;

import com.discoread.model.Book;
import com.discoread.service.GoogleBooksService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;   // <-- ADD THIS

import java.util.List;

public class GoogleSearchController {

    @FXML private TextField searchField;
    @FXML private ListView<Book> resultsList;

    private final GoogleBooksService api = new GoogleBooksService();
    public static Book selectedImportedBook = null;

    @FXML
    private void onSearch() {
        List<Book> found = api.searchBooks(searchField.getText());
        resultsList.setItems(FXCollections.observableArrayList(found));
    }

    @FXML
    private void onImport() {
        selectedImportedBook = resultsList.getSelectionModel().getSelectedItem();
        searchField.getScene().getWindow().hide(); // close pop-up
    }
}
