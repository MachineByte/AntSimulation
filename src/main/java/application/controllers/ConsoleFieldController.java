package application.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ConsoleFieldController implements Initializable {

    @FXML
    private TextArea textField;

    private static Habitat habitat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void newWindow(Habitat habitat) throws IOException {
        ConsoleFieldController.habitat = habitat;
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(ConsoleFieldController.class.getResource("consoleField.fxml"));

        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent, Habitat.WIDTH, Habitat.HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void keyPressed(KeyEvent event) throws Exception {
        if (event.getCode() == KeyCode.ENTER) {
            parse();
        }
    }

    private void parse() throws Exception {
        String text = textField.getText();
        String[] lines = text.split("\n");
        if (lines.length > 0 && Objects.equals(lines[lines.length - 1], "Старт")) {
            habitat.startPressed();
            textField.appendText("Успешно запущено\n");
        } else if (lines.length > 0 && Objects.equals(lines[lines.length - 1], "Стоп")) {
            habitat.stopPressed();
            textField.appendText("Успешно остановлено\n");
        } else {
            textField.appendText("Error: команда не найдена\n");
        }
    }
}
