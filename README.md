
# DiscoRead – Digital Library System

DiscoRead is a JavaFX + SQLite digital library that allows users to discover, organize and store books locally while also importing online book data via the Google Books API.  
The system includes User Authentication so access is controlled through Login and Sign-Up.

## 1. Features Overview

### 🧑‍💻 User Account System
- New users can **Sign Up / Register**
- Returning users **Log In with credentials**
- Password stored securely in SQLite
- Each user maintains their own session
- Prevents unauthorized access to the library

### Sign Up

<img width="890" height="721" alt="sign up" src="https://github.com/user-attachments/assets/34b4da2e-8329-4cb4-9d5a-a6f6df7b9de0" />

### Login

<img width="897" height="629" alt="sign in" src="https://github.com/user-attachments/assets/2a630782-598e-4a46-8b81-195eddff9252" />



### 📚 Library Management
- Load book database on startup
- Add books manually or through Google import
- Edit existing entries
- Delete selected books **(ID-based, safe deletion)**
- View detailed info including cover, metadata and PDF link

### 🔍 Search & View
- Search dynamically by:
  - Title
  - Author
  - ISBN
- TableView updates instantly while typing

### 🌐 Google Books API Integration
- Search for books online
- Auto-fetch:
  - Title  
  - Author  
  - Description  
  - Publication year  
  - Genre  
  - Cover image  
  - Google preview link
- One-click import → form filled automatically

### 📄 PDF Book Support
- Attach PDF file from local computer
- Open externally in system viewer or browser
- File path stored in database

## 2. System Architecture

### Book Model

| Field | Type | Description |
|------|------|-------------|
| title | String | Book title |
| author | String | Author name |
| year | int | Published year |
| genre | String | Genre/category |
| isbn | String | Book identifier |
| available | boolean | Availability |
| description | String | Summary text |
| coverImageURL | String | Cover link |
| addedDate | LocalDate | Date added |
| location | String | Shelf location |
| googleBooksLink | String | External web preview |
| pdfPath | String | Local PDF path |

### User Model

| Field | Type | Description |
|------|------|-------------|
| username | String | Unique login ID |
| password | String | Stored user password |
| created_date | String | Registration date |


## 3. User Story

1. New user opens DiscoRead → registers an account and logs in.
2. Library loads automatically and shows saved books.
3. User searches *The Alchemist*, edits its description and updates it.
4. User imports a new book using Google Books — metadata auto-fills.
5. User attaches a PDF, opens it to read externally.
6. Logs out — returns later, database is preserved exactly as before.

## 4. Tech Stack

| Component | Technology |
|----------|------------|
| Language | Java 17 |
| UI | JavaFX |
| Database | SQLite + JDBC |
| API | Google Books REST + Gson JSON |
| Persistence | Local DB with auto-migrations |
| Testing | JUnit 5 |
| Version Control | Git + GitHub |

## 5. Run the System

```bash
git clone https://github.com/YOUR-USERNAME/DiscoRead.git
cd DiscoRead
mvn clean install
mvn javafx:run

