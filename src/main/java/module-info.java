module com.warofoop.warofoop {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.warofoop.warofoop to javafx.fxml;
    exports com.warofoop.warofoop;
    exports com.warofoop.warofoop.controllers;
    opens com.warofoop.warofoop.controllers to javafx.fxml;
}