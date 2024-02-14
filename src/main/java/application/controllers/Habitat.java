package application.controllers;

import application.models.data.AbstractAnt;
import application.models.AntRepository;
import application.models.data.implement.WarriorAnt;
import application.models.data.implement.WorkerAnt;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Habitat extends Application {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;
    private static AntRepository antRepository = AntRepository.getInstance();
    private static ArrayList<AbstractAnt> arrayOfAnts = antRepository.getArrayList();
    private static Timer timer = null;
    private static long startTime;

    private static boolean showStatistic = false;

    @FXML
    private ToggleGroup timerToggleGroup;
    @FXML
    private RadioButton timerRadioButtonShow;
    @FXML
    private RadioButton timerRadioButtonHide;
    @FXML
    private CheckBox statisticCheckBox;
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
    void switchingStatisticCheckBox(ActionEvent event) {
        if(statisticCheckBox.isSelected()){
            showStatistic = true;
        } else{
            showStatistic = false;
        }
    }

    @FXML
    void timerRadioButtonPressed(ActionEvent event) {
        if(timerToggleGroup.getSelectedToggle().equals(timerRadioButtonShow)){
            timerPane.setVisible(true);
        } else{
            timerPane.setVisible(false);
        }
    }

    @FXML
    void startPressed(ActionEvent event) {
        startSimulation();
        statisticalPane.setVisible(false);
        startButton.setDisable(true);
        stopButton.setDisable(false);
    }

    @FXML
    void stopPressed(ActionEvent event) {
        if(showStatistic == true) {
            updateStatisticalLabel();
            statisticalPane.setVisible(true);
        }
        stopSimulation();

        startButton.setDisable(false);
        stopButton.setDisable(true);
    }

    void updateStatisticalLabel() {
        firstCounterStatisticalLabel.setText(String.valueOf("Количество рабочих: " + antRepository.getWorkerCount()));
        secondCounterStatisticalLabel.setText(String.valueOf("Количество воинов: " + antRepository.getWarriorCount()));
        timerStatisticalLabel.setText("Время симуляции: " + timerLabel.getText());
    }

    @FXML
    void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.B) {
            startSimulation();
        } else if (event.getCode() == KeyCode.E) {
            stopSimulation();
        } else if (event.getCode() == KeyCode.T) {
            timerPane.setVisible(!timerPane.isVisible());
        }
    }
    private void update(long timePassed) {
        antRepository.createAntIfTimeElapsed(timePassed, WarriorAnt.class, WarriorAnt.APPEARANCE_TIME, WarriorAnt.APPEARANCE_CHANCE,
                (int) simulationPane.getWidth(), (int) simulationPane.getHeight());
        antRepository.createAntIfTimeElapsed(timePassed, WorkerAnt.class, WorkerAnt.APPEARANCE_TIME, WorkerAnt.APPEARANCE_CHANCE,
                (int) simulationPane.getWidth(), (int) simulationPane.getHeight());
        Platform.runLater(() -> {
            timerLabel.setText((float) timePassed / 1000 + " c");
            updateAntsInView();
        });
    }

    private void updateAntsInView() {
        for (AbstractAnt ant : arrayOfAnts) {
            if (! simulationPane.getChildren().contains(ant.imageView)) {
                simulationPane.getChildren().add(ant.imageView);
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Habitat.class.getResource("mainField.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent, WIDTH, HEIGHT);
        startTime = System.currentTimeMillis(); // Запоминаем время начала симуляции


        primaryStage.setScene(scene);
        primaryStage.show();
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {

        });

    }

    public void startSimulation() {
        if (timer == null) {
            timer = new Timer();
            startTime = System.currentTimeMillis();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    long currentTime = System.currentTimeMillis();
                    long timePassed = currentTime - startTime;
                    update(timePassed);
                }
            }, 0, 100);
        }
    }

    public void stopSimulation() {
        if (timer != null) {
            timer.cancel();
            timer = null;

            arrayOfAnts.clear();
            simulationPane.getChildren().clear();

        }
    }


}
