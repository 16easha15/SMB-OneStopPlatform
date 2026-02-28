module Loginapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    opens Loginapplication to javafx.fxml, javafx.graphics;
    exports Loginapplication;
}

