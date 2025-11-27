package com.discoread.service;

import com.discoread.model.Book;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GoogleBooksService {

    public List<Book> searchBooks(String query) {
        List<Book> results = new ArrayList<>();

        try {
            String urlStr = "https://www.googleapis.com/books/v1/volumes?q=" +
                    query.replace(" ", "+") + "&maxResults=10";

            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) json.append(line);
            reader.close();

            JsonObject root = JsonParser.parseString(json.toString()).getAsJsonObject();
            JsonArray items = root.getAsJsonArray("items");
            if (items == null) return results;

            for (var item : items) {
                JsonObject volume = item.getAsJsonObject().getAsJsonObject("volumeInfo");

                Book book = new Book(
                        volume.has("title") ? volume.get("title").getAsString() : "Unknown Title",
                        volume.has("authors") ? volume.getAsJsonArray("authors").get(0).getAsString() : "Unknown Author",
                        volume.has("publishedDate") ? safeYear(volume.get("publishedDate").getAsString()) : 0,
                        volume.has("categories") ? volume.getAsJsonArray("categories").get(0).getAsString() : "Unknown",
                        extractISBN(volume),
                        true,
                        volume.has("description") ? volume.get("description").getAsString() : "",
                        volume.has("imageLinks") ? volume.getAsJsonObject("imageLinks").get("thumbnail").getAsString() : "",
                        LocalDate.now(),
                        "Imported",
                        volume.has("infoLink") ? volume.get("infoLink").getAsString() : ""
                );

                results.add(book);
            }

        } catch (Exception e) {
            System.out.println("❌ Google API Error: " + e.getMessage());
        }

        return results;
    }

    /** Extract ISBN safely */
    /** Extract best ISBN available (ISBN_13 → ISBN_10 → fallback) */
    private String extractISBN(JsonObject volume) {
        try {
            JsonArray ids = volume.getAsJsonArray("industryIdentifiers");
            String isbn10 = null, isbn13 = null;

            for (var id : ids) {
                JsonObject obj = id.getAsJsonObject();
                String type = obj.get("type").getAsString();
                String val = obj.get("identifier").getAsString();

                if (type.equals("ISBN_13")) isbn13 = val;
                if (type.equals("ISBN_10")) isbn10 = val;
            }
            // Prefer ISBN_13 > ISBN_10 > fallback
            return isbn13 != null ? isbn13 : (isbn10 != null ? isbn10 : "N/A");
        }
        catch (Exception e) { return "N/A"; }
    }


    /** Published year format fix (2020-05 → 2020) */
    private static int safeYear(String date) {
        try { return Integer.parseInt(date.substring(0,4)); }
        catch (Exception e) { return 0; }
    }
}
