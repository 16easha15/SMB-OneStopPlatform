module Loginapplication {
    requires javafx.controls;
    requires javafx.fxml;
    
    opens Loginapplication to javafx.fxml, javafx.graphics;
    exports Loginapplication;
}

