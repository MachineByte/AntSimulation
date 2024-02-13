package application.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;


public class PaneController {


    @FXML
    public GridPane statisticalPane;
    @FXML
    public Label firstCounterStatisticalLabel;

    @FXML
    public Label secondCounterStatisticalLabel;

    @FXML
    public Label timerStatisticalLabel;

    @FXML
    public Pane timerPane;

    @FXML
    public Label timerLabel;

    @FXML
    public Pane simulationPane;
    @FXML
    public Button stopButton;

    @FXML
    public Button startButton;
    @FXML
    public ToggleButton statisticButton;

    @FXML
    void startPressed(ActionEvent event) {
        Habitat.startSimulation();
        statisticalPane.setVisible(false);
        timerPane.setVisible(true);
        startButton.setDisable(true);
        stopButton.setDisable(false);
    }

    @FXML
    void stopPressed(ActionEvent event) {
        Habitat.stopSimulation();
        updateStatisticalLabel();
        statisticalPane.setVisible(true);
        timerPane.setVisible(false);
        startButton.setDisable(false);
        stopButton.setDisable(true);


    }

    @FXML
    void statisticPressed(ActionEvent event) {
        updateStatisticalLabel();
        statisticalPane.setVisible(!statisticalPane.isVisible());
    }

    void updateStatisticalLabel(){
        firstCounterStatisticalLabel.setText(String.valueOf("Количество рабочих: " + Habitat.getWorkerCount()));
        secondCounterStatisticalLabel.setText(String.valueOf("Количество воинов: " + Habitat.getWarriorCount()));
        timerStatisticalLabel.setText("Время симуляции: "+ timerLabel.getText());
    }
}
