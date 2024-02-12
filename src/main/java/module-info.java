module application.startproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens application.view to javafx.fxml;
    exports application.view;
}