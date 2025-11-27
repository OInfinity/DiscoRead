package com.discoread.dao;

import com.discoread.database.Database;
import com.discoread.model.Book;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    private final Connection conn = Database.getConnection();


    /* =========================================================
                     ADD NEW BOOK (supports PDF)
     ========================================================= */
    public boolean addBook(Book book) {

        String sql = """
                INSERT INTO books (title, author, year, genre, isbn, available,
                                   description, cover_url, added_date,
                                   location, google_link, pdf_path)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setInt(3, book.getYear());
            stmt.setString(4, book.getGenre());
            stmt.setString(5, book.getIsbn());
            stmt.setBoolean(6, book.isAvailable());
            stmt.setString(7, book.getDescription());
            stmt.setString(8, book.getCoverImageURL());
            stmt.setString(9, book.getAddedDate() != null ? book.getAddedDate().toString() : null);
            stmt.setString(10, book.getLocation());
            stmt.setString(11, book.getGoogleBooksLink());
            stmt.setString(12, book.getPdfPath());

            stmt.executeUpdate();
            System.out.println("✔ Book added: " + book.getTitle());
            return true;

        } catch (SQLException e) {
            System.out.println("❌ Add book failed: " + e.getMessage());
            return false;
        }
    }



    /* =========================================================
                   GET ALL BOOKS (Loads ID + PDF)
     ========================================================= */
    public List<Book> getAllBooks() {

        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY added_date DESC";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                LocalDate addedDate;
                try { addedDate = LocalDate.parse(rs.getString("added_date")); }
                catch (Exception e) { addedDate = LocalDate.now(); }

                Book book = new Book(
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("year"),
                        rs.getString("genre"),
                        rs.getString("isbn"),
                        rs.getBoolean("available"),
                        rs.getString("description"),
                        rs.getString("cover_url"),
                        addedDate,
                        rs.getString("location"),
                        rs.getString("google_link"),
                        rs.getString("pdf_path")
                );

                // <<< NEW — store DB ID >>>
                book.setId(rs.getInt("id"));

                list.add(book);
            }

            System.out.println("📚 Books loaded → " + list.size());

        } catch (SQLException e) {
            System.out.println("❌ Load books failed → " + e.getMessage());
        }

        return list;
    }



    /* =========================================================
                        UPDATE BOOK BY ID (SAFE)
     ========================================================= */
    public boolean updateBook(Book book) {

        String sql = """
                UPDATE books SET title=?, author=?, year=?, genre=?, available=?,
                                 description=?, cover_url=?, location=?,
                                 google_link=?, pdf_path=?
                WHERE id=?          -- < SAFE update >
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setInt(3, book.getYear());
            stmt.setString(4, book.getGenre());
            stmt.setBoolean(5, book.isAvailable());
            stmt.setString(6, book.getDescription());
            stmt.setString(7, book.getCoverImageURL());
            stmt.setString(8, book.getLocation());
            stmt.setString(9, book.getGoogleBooksLink());
            stmt.setString(10, book.getPdfPath());

            stmt.setInt(11, book.getId());  // <<< NEW

            stmt.executeUpdate();
            System.out.println("✏ Updated: " + book.getTitle());
            return true;

        } catch (SQLException e) {
            System.out.println("❌ Update failed: " + e.getMessage());
            return false;
        }
    }



    /* =========================================================
                        DELETE BOOK BY ID (SAFE)
     ========================================================= */
    public boolean deleteBook(int id) {

        String sql = "DELETE FROM books WHERE id=?";  // <<< FIXED

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("🗑 Deleted book ID → " + id);
            return true;

        } catch (SQLException e) {
            System.out.println("❌ Delete failed: " + e.getMessage());
            return false;
        }
    }
}
