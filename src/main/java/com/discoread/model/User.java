package com.discoread.model;

public class User {

    private int id;
    private String username;
    private String password;
    private String createdDate;

    // ===== Constructors =====
    public User() {}

    public User(int id, String username, String password, String createdDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.createdDate = createdDate;
    }

    public User(String username, String password, String createdDate) {
        this.username = username;
        this.password = password;
        this.createdDate = createdDate;
    }

    // ===== Getters & Setters =====
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    // ===== Utility =====
    @Override
    public String toString() {
        return username;
    }
}
