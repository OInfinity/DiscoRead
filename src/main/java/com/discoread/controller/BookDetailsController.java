package com.discoread.controller;

import com.discoread.model.Book;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.Desktop;
import java.io.File;

public class BookDetailsController {

    @FXML private Label titleLabel;
    @FXML private Label authorLabel;
    @FXML private Label yearLabel;
    @FXML private Label genreLabel;
    @FXML private Label isbnLabel;
    @FXML private Label locationLabel;
    @FXML private Label filePathLabel; // ✅ NEW — shows file path
    @FXML private TextArea descriptionArea;
    @FXML private ImageView coverImage;
    @FXML private Button openBookButton; // ✅ NEW — open book file button

    private Book currentBook; // ✅ store reference for access

    public void setBook(Book book) {

        this.currentBook = book;

        titleLabel.setText(book.getTitle());
        authorLabel.setText("Author: " + book.getAuthor());
        yearLabel.setText("Year: " + book.getYear());
        genreLabel.setText("Genre: " + book.getGenre());
        isbnLabel.setText("ISBN: " + book.getIsbn());
        locationLabel.setText("Location: " + book.getLocation());

        descriptionArea.setText(book.getDescription());

        // ✅ Display file path
        if (book.getFilePath() != null && !book.getFilePath().isBlank()) {
            filePathLabel.setText("File: " + book.getFilePath());
        } else {
            filePathLabel.setText("File: Not available");
        }

        // ✅ Load cover image safely
        if (book.getCoverImageURL() != null && !book.getCoverImageURL().isBlank()) {
            try {
                coverImage.setImage(new Image(book.getCoverImageURL(), true));
            } catch (Exception e) {
                coverImage.setImage(null);
            }
        }

        // ✅ Disable Open button if no file path
        boolean hasFile = book.getFilePath() != null && !book.getFilePath().isBlank();
        openBookButton.setDisable(!hasFile);

        // ✅ Handle button click
        openBookButton.setOnAction(event -> openBookFile());
    }

    // ✅ Opens PDF/EPUB in system default viewer
    private void openBookFile() {
        try {
            File file = new File(currentBook.getFilePath());

            if (!file.exists()) {
                showAlert("File Not Found",
                        "The attached file could not be located:\n" + currentBook.getFilePath());
                return;
            }

            Desktop.getDesktop().open(file);

        } catch (Exception e) {
            showAlert("Unable to Open File",
                    "An error occurred while opening the book file.");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
