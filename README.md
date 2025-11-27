🔧
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

### Add a Book Manually
<img width="486" height="505" alt="image" src="https://github.com/user-attachments/assets/dba57ac7-0a9b-431b-aaf6-226e58e9452e" />

<img width="1008" height="545" alt="image" src="https://github.com/user-attachments/assets/71e518bb-5798-4388-a8bb-8bd498c2c896" />

<img width="1017" height="550" alt="image" src="https://github.com/user-attachments/assets/5deee221-cb6d-484c-9950-87ad4833f4f5" />


<img width="505" height="500" alt="image" src="https://github.com/user-attachments/assets/79b92ac4-3ceb-48ea-a8a9-ca3f0236ca07" />

<img width="509" height="508" alt="image" src="https://github.com/user-attachments/assets/6f5df70a-339c-46f6-b97c-0728e680aee5" />

<img width="524" height="450" alt="image" src="https://github.com/user-attachments/assets/13ac1429-d480-4347-bf7f-1a07e22c14af" />

<img width="820" height="414" alt="image" src="https://github.com/user-attachments/assets/06b6539d-e388-4a6e-9bd2-7c98fbf59b7b" />

### Edit a Book Entry
<img width="736" height="516" alt="image" src="https://github.com/user-attachments/assets/5a9240e0-698d-4d66-a826-085df12341c3" />

### Delete Book (ID-based safe delete)
<img width="814" height="561" alt="image" src="https://github.com/user-attachments/assets/50504a47-fec3-4884-9038-a764a317a07c" />

### PDF Upload & Open
<img width="872" height="580" alt="image" src="https://github.com/user-attachments/assets/b4e2741c-77b0-4a74-98cd-def2adc81677" />



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
git clone https://github.com/OInfinity/DiscoRead.git
cd DiscoRead
mvn clean install
mvn javafx:run

