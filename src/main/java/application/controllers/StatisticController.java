package application.controllers;

import application.models.AntRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StatisticController {

    private static AntRepository antRepository = AntRepository.getInstance();
    private  final int WIDTH = 642;
    private  final int HEIGHT = 280;

    // continueSimulation - статическая, так как другими способами не поменять, так как
    //евенты кнопок не могут их нормадно менять из за какой-то специфики этих функциий
    //я этим заниматься не хочу. Уже 2 часа ночи, а мне завтра в качалку 1 парой
    private static boolean continueSimulation = true;


    @FXML
    private Button statisticPaneCancelButton;
    @FXML
    private TextArea statisticTextArea;
    @FXML
    private Button statisticPaneOkButton;

    @FXML
    void statisticPaneOkButtonPressed(ActionEvent event) {
        continueSimulation = true;
        Stage stage = (Stage) statisticPaneOkButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void statisticPaneCancelButtonPressed(ActionEvent event) {
        continueSimulation = false;
        Stage stage = (Stage) statisticPaneCancelButton.getScene().getWindow();
        stage.close();
    }

    public boolean newWindow(long timerTime ) throws Exception {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("statisticField.fxml"));
        Parent parent = fxmlLoader.load();
        StatisticController statisticController  = fxmlLoader.getController();
        Scene scene = new Scene(parent, WIDTH, HEIGHT);
        stage.setScene(scene);
        statisticController.statisticTextArea.setText("Количество рабочих муравьёв:" + antRepository.getWorkerCount() + "\n"+
                "Количество воинов муравьёв:" + antRepository.getWarriorCount() + "\n"+
                "Время с начала симуляции:" +  (float) timerTime / 1000 + " c");
        stage.showAndWait();
        return continueSimulation;
    }

}
