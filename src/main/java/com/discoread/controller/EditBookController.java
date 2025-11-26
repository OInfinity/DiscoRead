package com.discoread.controller;

import com.discoread.dao.BookDAO;
import com.discoread.model.Book;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EditBookController {

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField yearField;
    @FXML private TextField genreField;
    @FXML private TextField isbnField;
    @FXML private CheckBox availableCheck;
    @FXML private TextArea descriptionField;
    @FXML private TextField coverUrlField;
    @FXML private TextField locationField;

    private Book book;
    private final BookDAO dao = new BookDAO();

    public void setBook(Book book) {
        this.book = book;

        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        yearField.setText(String.valueOf(book.getYear()));
        genreField.setText(book.getGenre());
        isbnField.setText(book.getIsbn());
        availableCheck.setSelected(book.isAvailable());
        descriptionField.setText(book.getDescription());
        coverUrlField.setText(book.getCoverImageURL());
        locationField.setText(book.getLocation());
    }

    public void saveChanges() {
        book.setTitle(titleField.getText());
        book.setAuthor(authorField.getText());

        try {
            book.setYear(Integer.parseInt(yearField.getText()));
        } catch (NumberFormatException ignored) {}

        book.setGenre(genreField.getText());
        book.setIsbn(isbnField.getText());
        book.setAvailable(availableCheck.isSelected());
        book.setDescription(descriptionField.getText());
        book.setCoverImageURL(coverUrlField.getText());
        book.setLocation(locationField.getText());

        dao.updateBook(book);
    }
}
