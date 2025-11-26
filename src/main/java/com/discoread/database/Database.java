package com.discoread.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:discoread.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Creates table if not exists
    public static void initialize() {
        String sql = """
                CREATE TABLE IF NOT EXISTS books (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    author TEXT NOT NULL,
                    year INTEGER,
                    genre TEXT,
                    isbn TEXT UNIQUE,
                    available BOOLEAN DEFAULT 1,
                    description TEXT,
                    coverImageURL TEXT,
                    addedDate TEXT,
                    location TEXT,
                    
                    -- ✅ NEW column for opening PDF/EPUB
                    filePath TEXT
                );
                """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ Database initialized successfully");
        } catch (SQLException e) {
            System.out.println("❌ Database initialization failed");
            e.printStackTrace();
        }
    }
}
