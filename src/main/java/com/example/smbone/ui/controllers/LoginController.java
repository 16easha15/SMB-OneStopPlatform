package com.example.smbone.ui.controllers;

import com.example.smbone.DTOs.LoginRequestDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;


public class LoginController {

    // ✅ Login Fields
    @FXML
    private TextField logUser;
    @FXML
    private PasswordField logPass;
    @FXML
    private Label stageLabel;
    @FXML
    private TextField visiblePass;
    @FXML
    private Button toggleEye;

    @FXML
    public void initialize() {
        addButtonHoverAnimation();
    }

    // ================= LOGIN API =================
    @FXML
    private void handleLogin() {

        try {

            WebClient client = WebClient.create("http://localhost:8000");

            LoginRequestDTO request =
                    new LoginRequestDTO(logUser.getText().trim(), logPass.getText().trim());

            ResponseEntity<String> response = client.post()
                    .uri("/api/auth/login")
                    .bodyValue(request)
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            System.out.println(response);

            if (response.getStatusCode().is2xxSuccessful()) {

                System.out.println("API RESPONSE = " + response);
                boolean confirmed = showAlert("Success", "Login Successful --> Enabling Blockchain");
                if (confirmed) {
                    openEnableBlockchain();
                }

            } else {
                showAlert("Error", "Server Error");
            }

        } catch (
                Exception e) {
            e.printStackTrace();

            showAlert("Error", "Server not reachable");
        }
    }

    // ================= PeerSetup =================
    private void openPeerSetup() {

        try {

            Parent root =
                    FXMLLoader.load(getClass()
                            .getResource("/PeerSetup.fxml"));

            Stage stage =
                    (Stage) logUser.getScene().getWindow();

            Scene scene = new Scene(
                    root,
                    javafx.stage.Screen.getPrimary()
                            .getVisualBounds().getWidth(),
                    javafx.stage.Screen.getPrimary()
                            .getVisualBounds().getHeight()
            );

            stage.setScene(scene);
            stage.setMaximized(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= DASHBOARD =================
    private void openDashboard() {
        Platform.runLater(() -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/Dashboard.fxml"));
                Stage stage = (Stage) stageLabel.getScene().getWindow();
                Scene scene = new Scene(
                        root,
                        javafx.stage.Screen.getPrimary()
                                .getVisualBounds().getWidth(),
                        javafx.stage.Screen.getPrimary()
                                .getVisualBounds().getHeight());

                stage.setScene(scene);
                stage.setFullScreen(false);
                stage.setMaximized(true);
                stage.centerOnScreen();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // ================= ENABLE BLOCKCHAIN =================

    private void openEnableBlockchain() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/EnableBlockchain.fxml"));

            Stage stage = (Stage) logUser.getScene().getWindow();

            Scene scene = new Scene(
                    root,
                    javafx.stage.Screen.getPrimary().getVisualBounds().getWidth(),
                    javafx.stage.Screen.getPrimary().getVisualBounds().getHeight()
            );

            stage.setScene(scene);
            stage.setMaximized(true);

            // Smooth Fade In
            javafx.animation.FadeTransition fade =
                    new javafx.animation.FadeTransition(javafx.util.Duration.millis(700), root);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // ================= FORGOT PASSWORD =================
    @FXML
    private void showForgot() {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Password Reset");
        dialog.setContentText("Email :");

        dialog.showAndWait()
                .ifPresent(email ->
                        showAlert("Sent", "Reset link sent"));
    }

    // ================= ALERT =================
    private boolean showAlert(String title, String msg) {

        Alert alert =
                new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        return alert.showAndWait().isPresent();
    }

    @FXML
    private void togglePassword() {

        if (visiblePass.isVisible()) {

            logPass.setText(visiblePass.getText());

            visiblePass.setVisible(false);
            visiblePass.setManaged(false);

            logPass.setVisible(true);
            logPass.setManaged(true);

            toggleEye.setText("👁");

        } else {

            visiblePass.setText(logPass.getText());

            visiblePass.setVisible(true);
            visiblePass.setManaged(true);

            logPass.setVisible(false);
            logPass.setManaged(false);

            toggleEye.setText("🙈");
        }
    }

    @FXML
    private Button loginButton;

    private void addButtonHoverAnimation() {

        loginButton.setOnMouseEntered(e -> {
            loginButton.setStyle(
                    "-fx-background-color: linear-gradient(to right, #1c3b4d, #2a6f85);" +
                            "-fx-border-color: #00d4ff;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 40;" +
                            "-fx-background-radius: 40;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;" +
                            "-fx-font-weight: 600;"
            );

        });

        loginButton.setOnMouseExited(e -> {
            loginButton.setStyle(null);
        });
    }
}