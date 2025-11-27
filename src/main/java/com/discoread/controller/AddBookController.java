package com.discoread.controller;

import com.discoread.Main;
import com.discoread.dao.BookDAO;
import com.discoread.model.Book;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class AddBookController {

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField yearField;
    @FXML private TextField genreField;
    @FXML private TextField isbnField;
    @FXML private TextField locationField;
    @FXML private TextArea descriptionField;
    @FXML private TextField coverURLField;
    @FXML private TextField googleLinkField;   // Google Books URL
    @FXML private TextField pdfField;          // ⭐ NEW PDF Storage Field
    @FXML private Label statusLabel;

    private final BookDAO bookDAO = new BookDAO();



    // =====================================================
    // ⭐ SELECT PDF FROM DEVICE
    // =====================================================
    @FXML
    private void onChoosePDF() {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select PDF File");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf")
        );

        File file = chooser.showOpenDialog(null);

        if (file != null) {
            String pdfPath = file.toURI().toString();   // Convert local file to URI
            pdfField.setText(pdfPath);                 // Show to user
            statusLabel.setText("📄 PDF Added!");
            statusLabel.setStyle("-fx-text-fill: green;");
        }
    }



    // =====================================================
    // 🔍 GOOGLE SEARCH IMPORT (unchanged)
    // =====================================================
    @FXML
    private void openGoogleSearch() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/discoread/google-search-view.fxml"));
        Stage popup = new Stage();
        popup.setScene(new Scene(loader.load()));
        popup.setTitle("Search Google Books Online");
        popup.showAndWait();

        if (GoogleSearchController.selectedImportedBook != null) {
            Book b = GoogleSearchController.selectedImportedBook;

            titleField.setText(b.getTitle());
            authorField.setText(b.getAuthor());
            yearField.setText(String.valueOf(b.getYear()));
            genreField.setText(b.getGenre());
            descriptionField.setText(b.getDescription());
            coverURLField.setText(b.getCoverImageURL());
            googleLinkField.setText(b.getGoogleBooksLink());
        }
    }



    // =====================================================
    // SAVE NEW BOOK
    // =====================================================
    @FXML
    private void onSaveBook() throws IOException {

        if (titleField.getText().isBlank() || authorField.getText().isBlank()) {
            statusLabel.setText("⚠ Title & Author are required.");
            return;
        }

        int year;
        try { year = Integer.parseInt(yearField.getText()); }
        catch (NumberFormatException e) {
            statusLabel.setText("⚠ Year must be a number.");
            return;
        }

        // ⭐ UPDATED CONSTRUCTOR — now including PDF Path
        Book book = new Book(
                titleField.getText(),
                authorField.getText(),
                year,
                genreField.getText(),
                isbnField.getText(),
                true,
                descriptionField.getText(),
                coverURLField.getText(),
                LocalDate.now(),
                locationField.getText(),
                googleLinkField.getText(),
                pdfField.getText()          // ⭐ NEW
        );

        if (!bookDAO.addBook(book)) {
            statusLabel.setText("❌ Could not save. ISBN may already exist.");
            return;
        }

        statusLabel.setStyle("-fx-text-fill: green;");
        statusLabel.setText("✔ Book added! Returning...");

        new Thread(() -> {
            try { Thread.sleep(900); } catch (Exception ignored) {}
            javafx.application.Platform.runLater(() -> {
                try { loadScene("main-view.fxml"); }
                catch (IOException e) { throw new RuntimeException(e); }
            });
        }).start();
    }



    // =====================================================
    // CANCEL
    // =====================================================
    @FXML
    private void onCancel() throws IOException { loadScene("main-view.fxml"); }



    private void loadScene(String fxml) throws IOException {
        Stage stage = (Stage) titleField.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/discoread/" + fxml));
        Scene scene = new Scene(loader.load());

        var css = Main.class.getResource("/com/discoread/styles.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        stage.setScene(scene);
        stage.show();
    }
}
