package com.discoread.controller;

import com.discoread.Main;
import com.discoread.dao.BookDAO;
import com.discoread.model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, Integer> colYear;
    @FXML private TableColumn<Book, String> colGenre;
    @FXML private TableColumn<Book, String> colISBN;
    @FXML private TableColumn<Book, Boolean> colAvailable;

    @FXML private TextField searchField;
    @FXML private Label statusLabel;

    private final BookDAO bookDAO = new BookDAO();
    private ObservableList<Book> bookList;

    @FXML
    public void initialize() {
        colTitle.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTitle()));
        colAuthor.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAuthor()));
        colYear.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getYear()));
        colGenre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getGenre()));
        colISBN.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getIsbn()));
        colAvailable.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().isAvailable()));

        loadBooks();
    }

    private void loadBooks() {
        List<Book> books = bookDAO.getAllBooks();
        bookList = FXCollections.observableArrayList(books);
        bookTable.setItems(bookList);

        System.out.println("📚 Books Loaded: " + bookList.size());
        bookList.forEach(b -> System.out.println("→ " + b.getTitle()));
    }

    @FXML
    private void onSearch() {
        String keyword = searchField.getText().toLowerCase();

        ObservableList<Book> filtered = bookList.filtered(book ->
                book.getTitle().toLowerCase().contains(keyword) ||
                        book.getAuthor().toLowerCase().contains(keyword) ||
                        book.getIsbn().toLowerCase().contains(keyword)
        );

        bookTable.setItems(filtered);
    }

    @FXML
    private void onAddBook() throws IOException {
        switchScene("add-book-view.fxml");
    }

    @FXML
    private void onViewBook() throws IOException {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("⚠ Select a book first.");
            return;
        }

        BookDetailsController.setBook(selected);
        switchScene("book-details-view.fxml");
    }

    @FXML
    private void onEditBook() throws IOException {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("⚠ Select a book to edit.");
            return;
        }

        EditBookController.setBook(selected);
        switchScene("edit-book-view.fxml");
    }

    // 🔥 FIXED — DELETE NOW USES ID, NOT ISBN
    @FXML
    private void onDeleteBook() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("⚠ Select a book to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Book");
        confirm.setHeaderText("Are you sure you want to delete?");
        confirm.setContentText("Book: " + selected.getTitle());

        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {

                boolean removed = bookDAO.deleteBook(selected.getId()); // 🔥 SAFE DELETE BY ID

                if (removed) {
                    statusLabel.setStyle("-fx-text-fill: green;");
                    statusLabel.setText("✔ Book deleted successfully.");
                    loadBooks(); // refresh table list
                } else {
                    statusLabel.setStyle("-fx-text-fill: red;");
                    statusLabel.setText("❌ Delete failed.");
                }
            }
        });
    }

    private void switchScene(String fxml) throws IOException {
        var file = Main.class.getResource("/com/discoread/" + fxml);

        if (file == null) {
            statusLabel.setText("❌ FXML not found: " + fxml);
            System.err.println("Missing: " + fxml);
            return;
        }

        FXMLLoader loader = new FXMLLoader(file);
        Scene scene = new Scene(loader.load());

        var css = Main.class.getResource("/com/discoread/styles.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        Stage stage = (Stage) bookTable.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
