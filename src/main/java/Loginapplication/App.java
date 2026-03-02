package Loginapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        
        // Create the Scene
        Scene scene = new Scene(root,javafx.stage.Screen.getPrimary().getVisualBounds().getWidth(),
                                     javafx.stage.Screen.getPrimary().getVisualBounds().getHeight());
        scene.getStylesheets().add(getClass().getResource("/Login.css").toExternalForm());
        // Optional: Make the window transparent if you want custom rounded corners for the whole app
        //scene.setFill(Color.TRANSPARENT);
        //primaryStage.initStyle(StageStyle.TRANSPARENT);

        primaryStage.setTitle("SMB-One Stop Platform");
        primaryStage.setScene(scene);
        
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


