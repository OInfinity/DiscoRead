package com.discoread.controller;

import com.discoread.api.GoogleBooksService;
import com.discoread.dao.BookDAO;
import com.discoread.model.Book;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;

public class AddBookController {

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField yearField;
    @FXML private TextField genreField;
    @FXML private TextField isbnField;
    @FXML private CheckBox availableCheck;
    @FXML private TextArea descriptionField;
    @FXML private TextField coverUrlField;
    @FXML private TextField locationField;
    @FXML private Button lookupButton;
    @FXML private TextField filePathField;
    @FXML private Button browseFileButton;

    private final BookDAO bookDAO = new BookDAO();
    private final GoogleBooksService googleService = new GoogleBooksService();

    @FXML
    public void initialize() {
        lookupButton.setOnAction(event -> lookupISBN());
        browseFileButton.setOnAction(event -> chooseBookFile());
    }

    private void chooseBookFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Book File");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Book Files (*.pdf, *.epub, *.txt)", "*.pdf", "*.epub", "*.txt")
        );

        File file = chooser.showOpenDialog(null);
        if (file != null) {
            filePathField.setText(file.getAbsolutePath());
            availableCheck.setSelected(true); // auto available
        }
    }

    private void lookupISBN() {
        String isbn = isbnField.getText().trim();

        if (isbn.isEmpty()) {
            showAlert("Missing ISBN", "Please enter an ISBN before searching.");
            return;
        }

        Book found = googleService.searchByISBN(isbn);

        if (found == null) {
            showAlert("Not Found", "No book found for ISBN: " + isbn);
            return;
        }

        titleField.setText(found.getTitle());
        authorField.setText(found.getAuthor());
        yearField.setText(found.getYear() == 0 ? "" : String.valueOf(found.getYear()));
        genreField.setText(found.getGenre());
        descriptionField.setText(found.getDescription());
        coverUrlField.setText(found.getCoverImageURL());
        availableCheck.setSelected(true);

        if (locationField.getText().isBlank()) {
            locationField.setText("Online");
        }
    }

    public Book saveBook() {
        if (titleField.getText().isBlank() || authorField.getText().isBlank()) {
            showAlert("Missing Data", "Title and Author are required.");
            return null;
        }

        int year = 0;
        try {
            year = Integer.parseInt(yearField.getText());
        } catch (NumberFormatException ignored) {}

        Book book = new Book(
                titleField.getText(),
                authorField.getText(),
                year,
                genreField.getText(),
                isbnField.getText(),
                availableCheck.isSelected(),
                descriptionField.getText(),
                coverUrlField.getText(),
                LocalDate.now(),
                locationField.getText()
        );

        // ✅ validate file path if provided
        String path = filePathField.getText();
        if (path != null && !path.isBlank()) {
            File f = new File(path);
            if (!f.exists()) {
                showAlert("Invalid File", "Selected file does not exist.");
                return null;
            }
            book.setFilePath(path);
        }

        bookDAO.addBook(book);
        return book;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
