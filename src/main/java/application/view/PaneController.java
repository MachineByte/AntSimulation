package application.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private Button stopButton;

    @FXML
    private Button startButton;

    @FXML
    void startPressed(ActionEvent event) {
        Habitat.startSimulation();
    }

    @FXML
    void stopPressed(ActionEvent event) {
        Habitat.stopSimulation();
    }
}
