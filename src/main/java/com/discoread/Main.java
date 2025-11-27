package com.discoread;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        URL fxml = getClass().getResource("/com/discoread/login-view.fxml");

        if (fxml == null) {
            System.err.println("❌ FXML NOT FOUND ❌");
            System.err.println("Expected at: src/main/resources/com/discoread/login-view.fxml");
            return;
        }

        FXMLLoader loader = new FXMLLoader(fxml);
        Scene scene = new Scene(loader.load());

        URL css = getClass().getResource("/com/discoread/styles.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        stage.setScene(scene);
        stage.setTitle("DiscoRead Library");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
