package application.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class PaneController {
    @FXML
    public Pane pane;
    @FXML
    public Text timeElement;
    public Pane mainPane;

    // Метод, который может быть использован для установки текста в TextField
    public void setText(String text) {
        timeElement.setText(text);
    }
}
