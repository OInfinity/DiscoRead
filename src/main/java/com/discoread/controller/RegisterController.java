package com.discoread.controller;

import com.discoread.Main;
import com.discoread.dao.UserDAO;
import com.discoread.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label statusLabel;

    private final UserDAO userDAO = new UserDAO();  // DB Access

    @FXML
    private void onRegisterClick() throws IOException {

        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        // ================= VALIDATION =================
        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            statusLabel.setText("All fields are required.");
            return;
        }

        if (!password.equals(confirm)) {
            statusLabel.setText("Passwords do not match.");
            return;
        }

        // ================== CREATE NEW USER ==================
        User newUser = new User(username, password, LocalDate.now().toString());
        boolean success = userDAO.addUser(newUser);

        if (success) {
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Account created! Redirecting...");

            // Run delayed redirect SAFELY inside UI thread
            new Thread(() -> {
                try { Thread.sleep(1200); } catch (Exception ignored) {}

                Platform.runLater(() -> {
                    try { switchScene("login-view.fxml"); }
                    catch (IOException e) { e.printStackTrace(); }
                });
            }).start();

        } else {
            statusLabel.setText("Username already exists!");
        }
    }

    @FXML
    private void onLoginClick() throws IOException {
        switchScene("login-view.fxml");
    }

    private void switchScene(String fxml) throws IOException {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/discoread/" + fxml));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(Main.class.getResource("/com/discoread/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
