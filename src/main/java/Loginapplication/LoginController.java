package Loginapplication;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import java.util.Optional;

public class LoginController {

    @FXML private VBox loginPanel, signupPanel; 
    @FXML private Region bgShape;   
    @FXML private TextField logUser, regName, regEmail, regMobile;
    @FXML private PasswordField logPass, regPass;

    @FXML
    public void initialize() {
        // Signup ko invisible karke screen ke right side "Wait" pe rakho
        signupPanel.setOpacity(0);
        signupPanel.setVisible(false);
        signupPanel.setTranslateX(400); // Pehle se hi thoda side mein
    }

    // --- 1. SLIDE TO SIGNUP (The Smooth Way) ---
    @FXML
    private void switchToSignup() {
        signupPanel.setVisible(true);
        
        // Login bahar jayega
        TranslateTransition outLog = new TranslateTransition(Duration.millis(500), loginPanel);
        outLog.setToX(-400);
        FadeTransition fadeOutLog = new FadeTransition(Duration.millis(400), loginPanel);
        fadeOutLog.setToValue(0);

        // Signup andar aayega
        TranslateTransition inReg = new TranslateTransition(Duration.millis(500), signupPanel);
        inReg.setToX(0);
        FadeTransition fadeInReg = new FadeTransition(Duration.millis(400), signupPanel);
        fadeInReg.setToValue(1);

        // Background Shape Rotation
        RotateTransition rt = new RotateTransition(Duration.millis(600), bgShape);
        rt.setFromAngle(bgShape.getRotate());
        rt.setToAngle(-160);

        ParallelTransition pt = new ParallelTransition(outLog, fadeOutLog, inReg, fadeInReg, rt);
        pt.setOnFinished(e -> loginPanel.setVisible(false));
        pt.play();
    }

    // --- 2. SLIDE BACK TO LOGIN ---
    @FXML
    private void switchToLogin() {
        loginPanel.setVisible(true);
        
        TranslateTransition outReg = new TranslateTransition(Duration.millis(500), signupPanel);
        outReg.setToX(400);
        FadeTransition fadeOutReg = new FadeTransition(Duration.millis(400), signupPanel);
        fadeOutReg.setToValue(0);

        TranslateTransition inLog = new TranslateTransition(Duration.millis(500), loginPanel);
        inLog.setToX(0);
        FadeTransition fadeInLog = new FadeTransition(Duration.millis(400), loginPanel);
        fadeInLog.setToValue(1);

        RotateTransition rt = new RotateTransition(Duration.millis(600), bgShape);
        rt.setFromAngle(bgShape.getRotate());
        rt.setToAngle(10);   // SAME as FXML default

        ParallelTransition pt = new ParallelTransition(outReg, fadeOutReg, inLog, fadeInLog, rt);
        pt.setOnFinished(e -> signupPanel.setVisible(false));
        pt.play();
    }

    // --- 3. ALL VALIDATIONS (Working) ---
    @FXML
    private void handleLogin() {
        if (logUser.getText().equals("admin") && logPass.getText().equals("123")) {
            showAlert("Login Success", "System mein swagat hai, Bhai!");
        } else {
            showAlert("Login Failed", "Username/Password galat hai.");
        }
    }

    @FXML
    private void handleRegistration() {
        if (regName.getText().isEmpty() || regMobile.getText().length() != 10) {
            showAlert("Validation Error", "Mobile number 10 digit ka hona chahiye!");
        } else {
            showAlert("Success", "Account ban gaya! Ab login karo.");
            switchToLogin();
        }
    }

    @FXML
    private void showForgot() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Password Reset");
        dialog.setContentText("Email dalo:");
        dialog.showAndWait().ifPresent(email -> showAlert("Sent", "Link bhej diya gaya hai."));
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}