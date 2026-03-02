module Loginapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.base;
    opens Loginapplication to javafx.fxml, javafx.graphics;
    exports Loginapplication;
}

