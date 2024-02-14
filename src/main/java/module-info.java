module application.startproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens application.controllers to javafx.fxml;
    exports application.controllers;
}