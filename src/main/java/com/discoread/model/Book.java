package com.discoread.model;

import java.time.LocalDate;

public class Book {

    private int id;                   // primary key
    private String title;
    private String author;
    private int year;
    private String genre;
    private String isbn;
    private boolean available;
    private String description;
    private String coverImageURL;
    private LocalDate addedDate;
    private String location;

    // ✅ NEW — local file path to PDF/EPUB book
    private String filePath;

    public Book() {
        // required for JavaFX & DAO
    }

    public Book(String title, String author, int year, String genre, String isbn,
                boolean available, String description, String coverImageURL,
                LocalDate addedDate, String location) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.genre = genre;
        this.isbn = isbn;
        this.available = available;
        this.description = description;
        this.coverImageURL = coverImageURL;
        this.addedDate = addedDate;
        this.location = location;
    }

    // ✅ OPTIONAL — overloaded constructor including filePath
    public Book(String title, String author, int year, String genre, String isbn,
                boolean available, String description, String coverImageURL,
                LocalDate addedDate, String location, String filePath) {
        this(title, author, year, genre, isbn, available, description,
                coverImageURL, addedDate, location);
        this.filePath = filePath;
    }

    // Getters & Setters

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImageURL() {
        return coverImageURL;
    }
    public void setCoverImageURL(String coverImageURL) {
        this.coverImageURL = coverImageURL;
    }

    public LocalDate getAddedDate() {
        return addedDate;
    }
    public void setAddedDate(LocalDate addedDate) {
        this.addedDate = addedDate;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    // ✅ NEW getters & setters for PDF/EPUB file
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return title + " by " + author;
    }
}
