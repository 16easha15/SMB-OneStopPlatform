package Loginapplication;
import javafx.animation.*;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/*import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;*/
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class DashboardController {
    
@FXML private Circle particle1;
@FXML private Circle particle2;
@FXML private Circle particle3;
@FXML private Circle particle4;
@FXML private VBox cardSmartContract;
@FXML private VBox cardSupplyChain;
@FXML
private void handleSmartContractClick(javafx.scene.input.MouseEvent event) {
    try {
        // 1. Path check karein: Agar file "src/main/resources/SmartContracts.fxml" mein hai
        URL fxmlLocation = getClass().getResource("/SmartContracts.fxml");
        
        if (fxmlLocation == null) {
            System.err.println("Error: SmartContracts.fxml nahi mili! Path check karo.");
            return;
        }

        Parent root = FXMLLoader.load(fxmlLocation);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // Premium transition ke liye scene set karein
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true); 
        stage.show();
        
    } catch (IOException e) {
        e.printStackTrace();
    }
}
@FXML
public void initialize() {
    animateParticles();
    addHoverEffect(cardSmartContract);
    addHoverEffect(cardSupplyChain);
    glowPulse(particle1);
    glowPulse(particle2);
}
private void animateParticles() {

    Timeline timeline = new Timeline(

        new KeyFrame(Duration.seconds(0),
                new KeyValue(particle1.translateXProperty(), 500),
                new KeyValue(particle2.translateYProperty(), 250),
                new KeyValue(particle3.translateXProperty(), 0),
                new KeyValue(particle4.translateYProperty(), -350)
        ),

        new KeyFrame(Duration.seconds(20),
                new KeyValue(particle1.translateXProperty(), 400),
                new KeyValue(particle2.translateYProperty(), 200),
                new KeyValue(particle3.translateXProperty(), 100),
                new KeyValue(particle4.translateYProperty(), -250)
        )
    );

    timeline.setAutoReverse(true);
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
}
private void animateCards() {

    cardSmartContract.setOpacity(0);
    cardSmartContract.setTranslateY(40);

    cardSupplyChain.setOpacity(0);
    cardSupplyChain.setTranslateY(40);

    FadeTransition fade1 = new FadeTransition(Duration.seconds(0.8), cardSmartContract);
    fade1.setToValue(1);

    TranslateTransition slide1 = new TranslateTransition(Duration.seconds(0.8), cardSmartContract);
    slide1.setToY(0);

    FadeTransition fade2 = new FadeTransition(Duration.seconds(0.8), cardSupplyChain);
    fade2.setToValue(1);
    fade2.setDelay(Duration.seconds(0.2));

    TranslateTransition slide2 = new TranslateTransition(Duration.seconds(0.8), cardSupplyChain);
    slide2.setToY(0);
    slide2.setDelay(Duration.seconds(0.2));

    fade1.play();
    slide1.play();
    fade2.play();
    slide2.play();
}
private void addHoverEffect(VBox card) {

    ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), card);
    scaleUp.setToX(1.04);
    scaleUp.setToY(1.04);

    ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), card);
    scaleDown.setToX(1);
    scaleDown.setToY(1);

    card.setOnMouseEntered(e -> scaleUp.playFromStart());
    card.setOnMouseExited(e -> scaleDown.playFromStart());
}
private void glowPulse(Circle particle) {

    ScaleTransition pulse = new ScaleTransition(Duration.seconds(6), particle);
    pulse.setToX(1.1);
    pulse.setToY(1.1);
    pulse.setAutoReverse(true);
    pulse.setCycleCount(Animation.INDEFINITE);
    pulse.play();
}
}
