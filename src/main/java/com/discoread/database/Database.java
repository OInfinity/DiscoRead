package com.discoread.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:discoread.db";
    private static Connection connection = null;

    // ================= CONNECT =================
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL);
                System.out.println("✔ Connected to SQLite database.");
                createTables();
                updateSchema(); // Auto-update DB without breaking existing data
            } catch (SQLException e) {
                System.out.println("❌ Database connection failed: " + e.getMessage());
            }
        }
        return connection;
    }

    // ================= TABLE CREATION (first-time runs only) =================
    private static void createTables() {

        String createUsers = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    created_date TEXT NOT NULL
                );
                """;

        // ✔ Fresh install already supports PDF storage
        String createBooks = """
                CREATE TABLE IF NOT EXISTS books (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    author TEXT NOT NULL,
                    year INTEGER,
                    genre TEXT,
                    isbn TEXT,
                    available BOOLEAN,
                    description TEXT,
                    cover_url TEXT,
                    added_date TEXT,
                    location TEXT,
                    google_link TEXT,
                    pdf_path TEXT                 -- <★ added for PDF import feature
                );
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createUsers);
            stmt.execute(createBooks);
            System.out.println("✔ Tables verified/created");
        }
        catch (SQLException e) {
            System.out.println("❌ Failed to create tables: " + e.getMessage());
        }
    }


    // ================= AUTO-MIGRATION =================
    private static void updateSchema() {
        try (Statement stmt = connection.createStatement()) {

            // -------------------- 1️⃣ CHECK IF pdf_path EXISTS --------------------
            boolean hasPdf = false;
            ResultSet rs = stmt.executeQuery("PRAGMA table_info(books)");
            while (rs.next()) {
                if ("pdf_path".equals(rs.getString("name"))) {
                    hasPdf = true;
                }
            }

            // -------------------- 2️⃣ ADD COLUMN IF MISSING --------------------
            if (!hasPdf) {
                stmt.execute("ALTER TABLE books ADD COLUMN pdf_path TEXT;");
                System.out.println("🆕 Added column: pdf_path");
            }

            // -------------------- 3️⃣ CHECK IF ISBN IS STILL UNIQUE --------------------
            boolean isbnUnique = false;
            ResultSet rs2 = stmt.executeQuery("PRAGMA table_info(books)");
            while (rs2.next()) {
                if ("isbn".equals(rs2.getString("name")) && rs2.getInt("pk") == 0) {
                    isbnUnique = true;
                }
            }

            // -------------------- 4️⃣ AUTO-MIGRATE IF UNIQUE STILL PRESENT --------------------
            if (isbnUnique) {
                System.out.println("⚠ Migrating table → Removing UNIQUE on ISBN...");

                stmt.execute("ALTER TABLE books RENAME TO books_old;");

                stmt.execute("""
                    CREATE TABLE books (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        title TEXT NOT NULL,
                        author TEXT NOT NULL,
                        year INTEGER,
                        genre TEXT,
                        isbn TEXT,
                        available BOOLEAN,
                        description TEXT,
                        cover_url TEXT,
                        added_date TEXT,
                        location TEXT,
                        google_link TEXT,
                        pdf_path TEXT
                    );
                """);

                stmt.execute("""
                    INSERT INTO books (id, title, author, year, genre, isbn,
                        available, description, cover_url, added_date,
                        location, google_link, pdf_path)
                    SELECT id, title, author, year, genre, isbn,
                        available, description, cover_url, added_date,
                        location, google_link, pdf_path
                    FROM books_old;
                """);

                stmt.execute("DROP TABLE books_old;");
                System.out.println("✔ ISBN UNIQUE removed + PDF support active 🎉");
            }

        } catch (SQLException e) {
            System.out.println("❌ Schema update failed: " + e.getMessage());
        }
    }
}
