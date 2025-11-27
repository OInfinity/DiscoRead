package com.discoread.controller;

import com.discoread.Main;
import com.discoread.dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    private final UserDAO userDAO = new UserDAO(); // <— DB connection enabled

    @FXML
    private void onLoginClick(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter username & password");
            return;
        }

        boolean success = userDAO.validateLogin(username, password);

        if (success) {
            loadScene("main-view.fxml");
        } else {
            statusLabel.setText("Invalid username or password!");
        }
    }

    @FXML
    private void onRegisterClick(ActionEvent event) throws IOException {
        loadScene("register-view.fxml");
    }

    private void loadScene(String fxml) throws IOException {

        var fxmlFile = Main.class.getResource("/com/discoread/" + fxml);
        if (fxmlFile == null) {
            statusLabel.setText("❌ Missing FXML: " + fxml);
            return;
        }

        FXMLLoader loader = new FXMLLoader(fxmlFile);
        Scene scene = new Scene(loader.load());

        var css = Main.class.getResource("/com/discoread/styles.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());
        else System.out.println("⚠ styles.css not found — UI will load without styling.");

        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
