package Loginapplication;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONObject;

public class LoginController {

    // ✅ Login Fields
    @FXML private TextField logUser;
    @FXML private PasswordField logPass;
    @FXML private Label stageLabel;
    @FXML
    public void initialize() {
    }

    // ================= LOGIN API =================
    @FXML
    private void handleLogin() {

        try {

            URL url = new URL(
                    "http://localhost:8080/users?email="
                            + logUser.getText().trim()
            );

            System.out.println(url);

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            int statusCode = conn.getResponseCode();

            if (statusCode == 200) {

                BufferedReader br =
                        new BufferedReader(
                                new InputStreamReader(conn.getInputStream()));

                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    responseBuilder.append(line);
                }
                String response = responseBuilder.toString();
                System.out.println("API RESPONSE = " + response);

                // ✅ json-server returns [] if user not found
                JSONArray users = new JSONArray(response);

            if (users.length() == 0) {
                showAlert("Error", "User not found");
            } else {

                JSONObject user = users.getJSONObject(0);
                String dbPassword = user.getString("password");

                if (dbPassword.equals(logPass.getText().trim())) {

                    showAlert("Success", "Login Successful-->Setuping Peer Node");
                    openPeerSetup();

                } else {
                    showAlert("Error", "Wrong Password");
                }
            }

                        } else {
                            showAlert("Error", "Server Error");
                        }

                    } catch (Exception e) {
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
        private void openDashboard(){
            Platform.runLater(() -> {
               try{
            Parent root = FXMLLoader.load( getClass().getResource("/Dashboard.fxml"));
            Stage stage =(Stage) stageLabel.getScene().getWindow();
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
        }catch(Exception e){
            e.printStackTrace();
        }
    });
        }
    
    // ================= FORGOT PASSWORD =================
    @FXML
    private void showForgot() {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Password Reset");
        dialog.setContentText("Email dalo:");

        dialog.showAndWait()
                .ifPresent(email ->
                        showAlert("Sent", "Reset link sent"));
    }

    // ================= ALERT =================
    private void showAlert(String title, String msg) {

        Alert alert =
                new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}