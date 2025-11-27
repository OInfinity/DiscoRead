package com.discoread.controller;

import com.discoread.Main;
import com.discoread.model.Book;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.awt.Desktop;
import java.net.URI;

public class BookDetailsController {

    /** Receives selected book from MainController */
    private static Book selectedBook;

    @FXML private ImageView coverImage;
    @FXML private Label titleLabel;
    @FXML private Label authorLabel;
    @FXML private Label isbnLabel;
    @FXML private Label yearLabel;
    @FXML private Label genreLabel;
    @FXML private Label locationLabel;
    @FXML private TextArea descriptionArea;
    @FXML private Label googleLinkLabel;

    // ⭐ NEW — Button for PDF open feature
    @FXML private Button openPdfButton;


    /* ===========================
          Setter from MainPage
    =========================== */
    public static void setBook(Book book) { selectedBook = book; }


    @FXML
    public void initialize() {
        if (selectedBook == null) return;

        titleLabel.setText(selectedBook.getTitle());
        authorLabel.setText("Author: " + selectedBook.getAuthor());
        isbnLabel.setText("ISBN: " + selectedBook.getIsbn());
        yearLabel.setText("Year: " + selectedBook.getYear());
        genreLabel.setText("Genre: " + selectedBook.getGenre());
        locationLabel.setText("Location: " + selectedBook.getLocation());
        descriptionArea.setText(selectedBook.getDescription());

        /* ================= COVER IMAGE ================= */
        try {
            if (selectedBook.getCoverImageURL() != null && !selectedBook.getCoverImageURL().isBlank())
                coverImage.setImage(new Image(selectedBook.getCoverImageURL(), true));
        } catch (Exception ignored) {}


        /* ================= GOOGLE LINK ================= */
        if (selectedBook.getGoogleBooksLink() != null && !selectedBook.getGoogleBooksLink().isBlank()) {
            googleLinkLabel.setText("🔗 View on Google Books");
            googleLinkLabel.setStyle("-fx-text-fill: #1976D2; -fx-underline: true; -fx-font-size: 14px;");
            googleLinkLabel.setOnMouseClicked(e -> openGoogleLink());
        } else {
            googleLinkLabel.setText("");
        }


        /* ================= PDF OPEN BUTTON (NEW) ================= */
        if (selectedBook.getPdfPath() != null && !selectedBook.getPdfPath().isBlank()) {

            openPdfButton.setVisible(true);
            openPdfButton.setOnAction(e -> openPDF());

        } else {
            openPdfButton.setVisible(false);   // No PDF → hide button
        }
    }


    /* ===================== OPEN PDF EXTERNAL ====================== */
    private void openPDF() {
        try {
            String pdf = selectedBook.getPdfPath();

            if (pdf.startsWith("file:/") || pdf.startsWith("file:/")) {
                Desktop.getDesktop().browse(new URI(pdf));  // open local PDF
            } else {
                Desktop.getDesktop().browse(new URI("file:///" + pdf)); // fallback
            }
        }
        catch (Exception ex) {
            System.out.println("❌ Failed to open PDF: " + ex.getMessage());
        }
    }


    /* ===================== OPEN GOOGLE BOOKS ===================== */
    private void openGoogleLink() {
        try { Desktop.getDesktop().browse(new URI(selectedBook.getGoogleBooksLink())); }
        catch (Exception ignored) {}
    }


    /* ========== GO TO EDIT ========== */
    @FXML private void onEditBook() throws IOException {
        EditBookController.setBook(selectedBook);
        switchScene("edit-book-view.fxml");
    }

    /* ========== CLOSE ========== */
    @FXML private void onClose() throws IOException { switchScene("main-view.fxml"); }

    private void switchScene(String file) throws IOException {
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/discoread/" + file));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(Main.class.getResource("/com/discoread/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
