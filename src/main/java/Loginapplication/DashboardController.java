package Loginapplication;

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
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class DashboardController {
    
@FXML private Circle particle1;
@FXML private Circle particle2;
@FXML private Circle particle3;
@FXML private Circle particle4;
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
}
