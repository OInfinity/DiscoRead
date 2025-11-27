package com.discoread.model;

import java.time.LocalDate;

public class Book {

    // <<< NEW UNIQUE PRIMARY KEY >>>
    private int id;

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

    private String googleBooksLink;  // Google URL
    private String pdfPath;          // PDF file path


    // ===== Constructors =====

    public Book() {}

    // OLD constructor (still needed for GoogleBooksService)
    public Book(String title, String author, int year, String genre, String isbn,
                boolean available, String description, String coverImageURL,
                LocalDate addedDate, String location, String googleBooksLink) {

        this(title, author, year, genre, isbn, available, description,
                coverImageURL, addedDate, location, googleBooksLink, null);
    }

    // FULL constructor including PDF
    public Book(String title, String author, int year, String genre, String isbn,
                boolean available, String description, String coverImageURL,
                LocalDate addedDate, String location, String googleBooksLink,
                String pdfPath) {

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
        this.googleBooksLink = googleBooksLink;
        this.pdfPath = pdfPath;
    }


    // ===== Getters & Setters =====

    // <<< NEW >>> used for safe delete
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCoverImageURL() { return coverImageURL; }
    public void setCoverImageURL(String coverImageURL) { this.coverImageURL = coverImageURL; }

    public LocalDate getAddedDate() { return addedDate; }
    public void setAddedDate(LocalDate addedDate) { this.addedDate = addedDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getGoogleBooksLink() { return googleBooksLink; }
    public void setGoogleBooksLink(String googleBooksLink) { this.googleBooksLink = googleBooksLink; }

    public String getPdfPath() { return pdfPath; }
    public void setPdfPath(String pdfPath) { this.pdfPath = pdfPath; }


    @Override
    public String toString() {
        return title + " by " + author;
    }
}
