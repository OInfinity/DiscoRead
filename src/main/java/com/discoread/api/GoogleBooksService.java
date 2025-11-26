package com.discoread.api;

import com.discoread.model.Book;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

public class GoogleBooksService {

    private static final String API_URL =
            "https://www.googleapis.com/books/v1/volumes?q=isbn:";

    public Book searchByISBN(String isbn) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + isbn))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonArray items = json.getAsJsonArray("items");

            if (items == null || items.size() == 0) return null;

            JsonObject volumeInfo = items.get(0).getAsJsonObject()
                    .getAsJsonObject("volumeInfo");

            String title = volumeInfo.get("title").getAsString();
            String author = volumeInfo.has("authors")
                    ? volumeInfo.getAsJsonArray("authors").get(0).getAsString()
                    : "Unknown";

            int year = volumeInfo.has("publishedDate")
                    ? Integer.parseInt(volumeInfo.get("publishedDate")
                    .getAsString().substring(0, 4))
                    : 0;

            String description = volumeInfo.has("description")
                    ? volumeInfo.get("description").getAsString()
                    : "";

            String thumbnail = volumeInfo.has("imageLinks")
                    ? volumeInfo.getAsJsonObject("imageLinks")
                    .get("thumbnail").getAsString()
                    : "";

            return new Book(title, author, year, "", isbn,
                    true, description, thumbnail, LocalDate.now(), "");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
