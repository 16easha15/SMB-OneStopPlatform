package Loginapplication;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.concurrent.Task;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PeerSetupController {

    // ================= STEPS =================
    @FXML private StackPane step1,step2,step3,step4,step5,
                             step6,step7,step8,step9,step10;

    @FXML private Region line1,line2,line3,line4,line5,
                          line6,line7,line8,line9;
    @FXML private Label stageLabel;
    @FXML private Label progressPercent;
    @FXML private Canvas glassCanvas;
    @FXML private Pane bgLayer;
    private ScaleTransition currentPulse;
    private final int TOTAL_STEPS = 10;
    private DoubleProperty progressValue =
            new SimpleDoubleProperty(0);
    public DoubleProperty progressValueProperty(){
        return progressValue;
    }
    // ================= INITIALIZE =================
    @FXML private ProgressBar mainProgressBar;

public void initialize() {
    // This links the logic progress to the visual bar
    mainProgressBar.progressProperty().bind(progressValue);
    
    progressValue.addListener((obs, o, n) ->
            progressPercent.setText(String.format("%.0f%%", n.doubleValue() * 100)));
     
    startGlassBackground();
    startSetup();
}
    // ================= MAIN SETUP =================
    private void startSetup(){
        Task<Void> task = new Task<>() {         
        @Override
        protected Void call() throws Exception {
            runStep(step1,line1,"Initializing Environment",1);
            runStep(step2,line2,"Loading Configuration",2);
            runStep(step3,line3,"Creating Channel",3);
            runStep(step4,line4,"Starting Peer Node",4);
            runStep(step5,line5,"Joining Network",5);
            runStep(step6,line6,"Ledger Synchronization",6);
            runStep(step7,line7,"Deploying Contract",7);
            runStep(step8,line8,"Security Validation",8);
            runStep(step9,line9,"Final Verification",9);
            activate(step10,"Setup Complete");
            PeerSetupController.this.updateProgress(10);
            Thread.sleep(900);
            complete(step10, null);   // ✅ mark final step green
            //open dashboard
            Platform.runLater(() ->
                    PeerSetupController.this.openDashboard());
    return null;
    }
    };
        new Thread(task).start();
    }
    // ================= STEP EXECUTION =================
    private void runStep(StackPane step,
                         Region line,
                         String msg,
                         int stepNo) throws Exception {
        activate(step,msg);
        updateProgress(stepNo);
        Thread.sleep(900);
        complete(step,line);
    }
    // ================= PROGRESS UPDATE =================
    private void updateProgress(int step){
        double target =(double) step / TOTAL_STEPS;
        Platform.runLater(() -> {
            Timeline timeline =
                    new Timeline(
                        new KeyFrame(
                            Duration.seconds(0.6),
                            new KeyValue(
                                progressValue,
                                target,
                                Interpolator.EASE_BOTH)));
            timeline.play();
        });
    }
    // ================= STEP ACTIVE =================
    private void activate(StackPane step, String msg) {
        Platform.runLater(() -> {
            stageLabel.setText(msg);
            // Pehle ke rules clear karein
            step.getStyleClass().removeAll("step-complete", "step-pending", "step-active");
            step.getStyleClass().add("step-active");

            // Neon pulse animation call karein image_0.png visual proportions ko match karne ke liye
            glow(step);
        });
    }
    // ================= STEP COMPLETE =================
    private void complete(StackPane step, Region line) {
    Platform.runLater(() -> {
        // Active pulse animation stop karein jab step complete ho jaye image_0.png look ke liye
        if (currentPulse != null) currentPulse.stop();

        // Step active state aur pending state hatayein, final color set karein
        step.getStyleClass().removeAll("step-active", "step-pending");
        step.getStyleClass().add("step-complete");

        // Connecting line complete color mein karein agar line hai
        if (line != null) {
            line.getStyleClass().removeAll("step-line-active"); // Base color pe layein
            line.setStyle("-fx-background-color: #34d399;"); // Completed color set karein visually logic ke hisaab se image_0.png visual proportions ko maintain karne ke liye
        }
    });
}
    // ================= SOFT NEON GLOW =================
    private void glow(StackPane step){
        if(currentPulse!=null)
            currentPulse.stop();
        currentPulse =
                new ScaleTransition(
                        Duration.seconds(1.4),step);
        currentPulse.setFromX(1);
        currentPulse.setToX(1.04);
        currentPulse.setFromY(1);
        currentPulse.setToY(1.04);
        currentPulse.setAutoReverse(true);
        currentPulse.setCycleCount(
                Animation.INDEFINITE);
        currentPulse.play();
    }

    // ================= OPEN DASHBOARD =================
    private void openDashboard() {
    Platform.runLater(() -> {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Dashboard.fxml"));
            Stage stage = (Stage) step1.getScene().getWindow();
            
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // ✅ FullScreen hata kar sirf Maximized rakhein
            stage.setFullScreen(false); 
            stage.setMaximized(true);
            
            // Ye ensure karega ki window taskbar ke upar na chadh jaye
            Screen screen = Screen.getPrimary();
            stage.setX(screen.getVisualBounds().getMinX());
            stage.setY(screen.getVisualBounds().getMinY());
            stage.setWidth(screen.getVisualBounds().getWidth());
            stage.setHeight(screen.getVisualBounds().getHeight());

        } catch (Exception e) {
            e.printStackTrace();
        }
    });
    }
    
    
    private double offset = 0;

private void startGlassBackground() {

    GraphicsContext gc =
            glassCanvas.getGraphicsContext2D();

    // Auto resize
    glassCanvas.widthProperty()
            .bind(((StackPane)glassCanvas.getParent()).widthProperty());

    glassCanvas.heightProperty()
            .bind(((StackPane)glassCanvas.getParent()).heightProperty());

    AnimationTimer timer = new AnimationTimer() {

        @Override
        public void handle(long now) {

            double w = glassCanvas.getWidth();
            double h = glassCanvas.getHeight();

            offset += 0.003; // SPEED (smooth)

            gc.clearRect(0,0,w,h);

            // ===== LIGHT GLASS GRADIENT =====
            LinearGradient gradient =
                    new LinearGradient(
                            Math.sin(offset),
                            0,
                            1,
                            Math.cos(offset),
                            true,
                            CycleMethod.NO_CYCLE,

                            new Stop[]{
                                new Stop(0,
                                    Color.web("#1e293b")),
                                new Stop(0.4,
                                    Color.web("#334155")),
                                new Stop(0.7,
                                    Color.web("#0ea5e9")),
                                new Stop(1,
                                    Color.web("#22d3ee"))
                            });

            gc.setGlobalAlpha(0.35); // LIGHT EFFECT
            gc.setFill(gradient);
            gc.fillRect(0,0,w,h);
        }
    };

    timer.start();
}
public void setTheme(String cssFile){

    Scene scene = bgLayer.getScene();

    scene.getStylesheets().clear();

    scene.getStylesheets().add(
        getClass().getResource(cssFile).toExternalForm()
    );
}
}