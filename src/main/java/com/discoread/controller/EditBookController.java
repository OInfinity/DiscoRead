package com.discoread.controller;

import com.discoread.Main;
import com.discoread.dao.BookDAO;
import com.discoread.model.Book;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class EditBookController {

    // UI Fields
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField yearField;
    @FXML private TextField genreField;
    @FXML private TextField isbnField;
    @FXML private TextField locationField;
    @FXML private TextArea descriptionField;
    @FXML private TextField coverURLField;
    @FXML private Label statusLabel;

    private final BookDAO bookDAO = new BookDAO();
    private static Book selectedBook; // book to edit

    /* =========================================================
                        RECEIVE BOOK FROM DETAILS VIEW
     ========================================================= */
    public static void setBook(Book book) { selectedBook = book; }

    /* =========================================================
                        LOAD BOOK DETAILS INTO FIELDS
     ========================================================= */
    @FXML
    public void initialize() {
        if (selectedBook != null) {
            titleField.setText(selectedBook.getTitle());
            authorField.setText(selectedBook.getAuthor());
            yearField.setText(String.valueOf(selectedBook.getYear()));
            genreField.setText(selectedBook.getGenre());
            isbnField.setText(selectedBook.getIsbn());
            isbnField.setDisable(true); // ISBN cannot change
            locationField.setText(selectedBook.getLocation());
            descriptionField.setText(selectedBook.getDescription());
            coverURLField.setText(selectedBook.getCoverImageURL());
        }
    }

    /* =========================================================
                          SAVE EDIT CHANGES
     ========================================================= */
    @FXML
    private void onSaveEdit() {

        if (titleField.getText().isBlank() || authorField.getText().isBlank()) {
            statusLabel.setText("⚠ Title and Author are required.");
            return;
        }

        try {
            selectedBook.setTitle(titleField.getText());
            selectedBook.setAuthor(authorField.getText());
            selectedBook.setYear(Integer.parseInt(yearField.getText()));
            selectedBook.setGenre(genreField.getText());
            selectedBook.setLocation(locationField.getText());
            selectedBook.setDescription(descriptionField.getText());
            selectedBook.setCoverImageURL(coverURLField.getText());
        } catch (Exception e) {
            statusLabel.setText("❌ Year must be a number.");
            return;
        }

        boolean success = bookDAO.updateBook(selectedBook);

        if (!success) {
            statusLabel.setText("❌ Failed to update book.");
            return;
        }

        statusLabel.setStyle("-fx-text-fill: green;");
        statusLabel.setText("✔ Saved! Updating list...");

        // 🔥 Load main view AND refresh table instantly
        javafx.application.Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/discoread/main-view.fxml"));
                Scene scene = new Scene(loader.load());

                MainController controller = loader.getController();
                controller.initialize(); // refresh books

                Stage stage = (Stage) titleField.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) { e.printStackTrace(); }
        });
    }


    /* =========================================================
                          CANCEL BUTTON
     ========================================================= */
    @FXML
    private void onCancel() throws IOException {
        loadScene("main-view.fxml");
    }

    private void loadScene(String fxml) throws IOException {
        Stage stage = (Stage) titleField.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/discoread/" + fxml));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(Main.class.getResource("/com/discoread/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
