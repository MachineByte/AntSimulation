package application.startproject;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SampleController {

    @FXML
    private TextField inputField;

    @FXML
    public void handleSubmitButtonAction() {
        String inputText = inputField.getText();
        System.out.println("Input text: " + inputText);
    }
}
