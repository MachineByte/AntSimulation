module application.startproject {
    requires javafx.controls;
    requires javafx.fxml;

    opens application.models.data to javafx.base;

    opens application.controllers to javafx.fxml;
    exports application.controllers;
    exports application;
    opens application to javafx.fxml;
}