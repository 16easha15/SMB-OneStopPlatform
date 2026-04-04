package com.example.smbone.ui.controllers;

import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.event.ActionEvent;

public class EnableBlockchainController {

    @FXML
    private Button enableBtn;

    @FXML
    private Label statusLabel;

    @FXML
    private Label badgeLabel;

    @FXML
    private void handleEnableBlockchain(ActionEvent event) {

        // =========================
        // 1. OLD STATE SAVE KARO
        // =========================
        String oldStatus = statusLabel.getText();
        String oldBadge = badgeLabel.getText();
        String oldButtonText = enableBtn.getText();
        boolean oldButtonState = enableBtn.isDisable();

        // =========================
        // 2. PEHLE STATUS CHANGE KARO
        // =========================
        statusLabel.setText("Blockchain Status: Enabled");

        badgeLabel.setText("ACTIVE");
        badgeLabel.getStyleClass().remove("badge-disabled");
        if (!badgeLabel.getStyleClass().contains("badge-enabled")) {
            badgeLabel.getStyleClass().add("badge-enabled");
        }

        enableBtn.setDisable(true);
        enableBtn.setText("Blockchain Enabled");

        // =========================
        // 3. POPUP SHOW KARO
        // =========================
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Blockchain Status");
        alert.setHeaderText(null);
        alert.setContentText("Blockchain has been enabled successfully!");
        alert.getButtonTypes().setAll(ButtonType.OK);

        Optional<ButtonType> result = alert.showAndWait();

        // =========================
        // 4. AGAR OK TO NEXT SCREEN
        // =========================
        if (result.isPresent() && result.get() == ButtonType.OK) {
            openPeerSetup();
        } else {
            // =========================
            // 5. AGAR CROSS TO OLD STATE RESTORE
            // =========================
            statusLabel.setText(oldStatus);
            badgeLabel.setText(oldBadge);

            badgeLabel.getStyleClass().remove("badge-enabled");
            if (!badgeLabel.getStyleClass().contains("badge-disabled")) {
                badgeLabel.getStyleClass().add("badge-disabled");
            }

            enableBtn.setDisable(oldButtonState);
            enableBtn.setText(oldButtonText);

            System.out.println("Popup closed. State restored.");
        }
    }

    // ================= OPEN PEER SETUP =================
    private void openPeerSetup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PeerSetup.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) enableBtn.getScene().getWindow();
            Scene currentScene = stage.getScene();

            // Fade out
            FadeTransition fadeOut = new FadeTransition(Duration.millis(400), currentScene.getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            fadeOut.setOnFinished(e -> {
                root.setOpacity(0);
                currentScene.setRoot(root);

                // Fade in
                FadeTransition fadeIn = new FadeTransition(Duration.millis(600), root);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();

                stage.setTitle("Peer Setup");
                stage.setMaximized(true);
            });

            fadeOut.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}