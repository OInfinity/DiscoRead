package com.discoread;

import com.discoread.database.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // ✅ Initialize SQLite database and create table if missing
        Database.initialize();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/discoread/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        stage.setTitle("DiscoRead — Digital Library");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
