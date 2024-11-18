module com.warofoop.warofoop {
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires javafx.controls;
    requires java.desktop;

    opens com.warofoop.warofoop to javafx.fxml;
    opens com.warofoop.warofoop.build to javafx.fxml;
    exports com.warofoop.warofoop;
    exports com.warofoop.warofoop.controllers;
    opens com.warofoop.warofoop.controllers to javafx.fxml;
    exports com.warofoop.warofoop.build;
}