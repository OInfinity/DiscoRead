package com.discoread.dao;

import com.discoread.database.Database;
import com.discoread.model.Book;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public BookDAO() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS books (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                author TEXT NOT NULL,
                year INTEGER,
                genre TEXT,
                isbn TEXT UNIQUE,
                available INTEGER,
                description TEXT,
                coverImageURL TEXT,
                addedDate TEXT,
                location TEXT,
                filePath TEXT
            );
        """;

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("✅ Books table ready");

        } catch (SQLException e) {
            System.out.println("❌ Failed creating books table");
            e.printStackTrace();
        }
    }

    // Insert a new book
    public void addBook(Book book) {
        String sql = """
            INSERT INTO books(title, author, year, genre, isbn, available, description, 
                              coverImageURL, addedDate, location, filePath)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setInt(3, book.getYear());
            pstmt.setString(4, book.getGenre());
            pstmt.setString(5, book.getIsbn());
            pstmt.setBoolean(6, book.isAvailable());
            pstmt.setString(7, book.getDescription());
            pstmt.setString(8, book.getCoverImageURL());

            if (book.getAddedDate() != null) {
                pstmt.setString(9, book.getAddedDate().toString());
            } else {
                pstmt.setNull(9, Types.VARCHAR);
            }

            pstmt.setString(10, book.getLocation());
            pstmt.setString(11, book.getFilePath());

            pstmt.executeUpdate();
            System.out.println("✅ Book added");

        } catch (SQLException e) {
            System.out.println("❌ Failed to add book");
            e.printStackTrace();
        }
    }

    // Retrieve all books
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                LocalDate addedDate = null;
                String dateStr = rs.getString("addedDate");
                if (dateStr != null) {
                    addedDate = LocalDate.parse(dateStr);
                }

                Book book = new Book(
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("year"),
                        rs.getString("genre"),
                        rs.getString("isbn"),
                        rs.getBoolean("available"),
                        rs.getString("description"),
                        rs.getString("coverImageURL"),
                        addedDate,
                        rs.getString("location")
                );

                book.setId(rs.getInt("id"));
                book.setFilePath(rs.getString("filePath"));  // ✅ load file path

                books.add(book);
            }

        } catch (SQLException e) {
            System.out.println("❌ Failed loading books");
            e.printStackTrace();
        }

        return books;
    }

    // Update existing book
    public void updateBook(Book book) {
        String sql = """
            UPDATE books SET title=?, author=?, year=?, genre=?, isbn=?, 
                             available=?, description=?, coverImageURL=?, 
                             location=?, filePath=?
            WHERE id=?
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setInt(3, book.getYear());
            pstmt.setString(4, book.getGenre());
            pstmt.setString(5, book.getIsbn());
            pstmt.setBoolean(6, book.isAvailable());
            pstmt.setString(7, book.getDescription());
            pstmt.setString(8, book.getCoverImageURL());
            pstmt.setString(9, book.getLocation());
            pstmt.setString(10, book.getFilePath());
            pstmt.setInt(11, book.getId());

            pstmt.executeUpdate();
            System.out.println("✏️ Book updated");

        } catch (SQLException e) {
            System.out.println("❌ Failed updating book");
            e.printStackTrace();
        }
    }

    // Delete book by ID
    public void deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("🗑️ Book deleted");

        } catch (SQLException e) {
            System.out.println("❌ Failed deleting book");
            e.printStackTrace();
        }
    }
}
