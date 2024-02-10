module application.startproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens application.startproject to javafx.fxml;
    exports application.startproject;
}