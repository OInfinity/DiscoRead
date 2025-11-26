package com.discoread.controller;

import com.discoread.dao.BookDAO;
import com.discoread.model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

public class MainController {

    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, Integer> yearColumn;
    @FXML private TableColumn<Book, String> genreColumn;
    @FXML private TableColumn<Book, String> isbnColumn;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> genreFilter;
    @FXML private Button addButton;
    @FXML private Button deleteButton;

    private final BookDAO bookDAO = new BookDAO();
    private final ObservableList<Book> books = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // Table column setup
        titleColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));

        authorColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getAuthor()));

        yearColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getYear()));

        genreColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getGenre()));

        isbnColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getIsbn()));

        // Load data into table
        refreshTable();

        // Search listener
        searchField.textProperty().addListener((obs, oldValue, newValue) -> applyFilters());

        // Genre filter listener
        genreFilter.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());

        // Button listeners
        deleteButton.setOnAction(event -> deleteSelectedBook());
        addButton.setOnAction(event -> openAddBookDialog());

        // ✅ Double click -> open details window
        bookTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                openBookDetailsDialog();
            }
        });
    }

    private void refreshTable() {
        books.clear();
        books.addAll(bookDAO.getAllBooks());
        bookTable.setItems(books);
        updateGenreFilter();
    }

    private void applyFilters() {
        String search = searchField.getText() == null ? "" : searchField.getText().toLowerCase();
        String selectedGenre = genreFilter.getValue();

        ObservableList<Book> filtered = books.filtered(book -> {
            boolean matchesSearch =
                    book.getTitle().toLowerCase().contains(search) ||
                            book.getAuthor().toLowerCase().contains(search) ||
                            (book.getGenre() != null && book.getGenre().toLowerCase().contains(search)) ||
                            book.getIsbn().toLowerCase().contains(search);

            boolean matchesGenre =
                    selectedGenre == null || selectedGenre.equals("All") ||
                            (book.getGenre() != null && book.getGenre().equalsIgnoreCase(selectedGenre));

            return matchesSearch && matchesGenre;
        });

        bookTable.setItems(filtered);
    }

    private void updateGenreFilter() {
        genreFilter.getItems().clear();
        genreFilter.getItems().add("All");

        books.stream()
                .map(Book::getGenre)
                .filter(g -> g != null && !g.isBlank())
                .distinct()
                .sorted()
                .forEach(genreFilter.getItems()::add);

        if (genreFilter.getValue() == null)
            genreFilter.setValue("All");
    }

    private void deleteSelectedBook() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a book to delete.");
            return;
        }

        bookDAO.deleteBook(selected.getId());
        refreshTable();
    }

    private void openAddBookDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/discoread/add-book-view.fxml"));
            DialogPane pane = loader.load();

            AddBookController controller = loader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.setTitle("Add a New Book");

            dialog.showAndWait().ifPresent(result -> {
                if (result.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    Book newBook = controller.saveBook();
                    if (newBook != null) refreshTable();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to open Add Book form.");
        }
    }

    // ✅ NEW — Book Preview Window
    private void openBookDetailsDialog() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/discoread/book-details-view.fxml")
            );
            DialogPane pane = loader.load();

            BookDetailsController controller = loader.getController();
            controller.setBook(selected);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.setTitle("Book Details");

            dialog.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();  // ✅ ensure full stacktrace shows
            throw new RuntimeException(e); // ✅ force Maven to show it
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
