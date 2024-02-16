package application.controllers;

import application.models.AntRepository;
import application.models.data.AbstractAnt;
import application.models.data.implement.WarriorAnt;
import application.models.data.implement.WorkerAnt;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Habitat implements Initializable {
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 700;
    private static final AntRepository antRepository = AntRepository.getInstance();
    private static final ArrayList<AbstractAnt> arrayOfAnts = antRepository.getArrayList();
    private static Timer timer = null;
    private static long startTime;

    private static boolean showStatistic = false;
    public Button exitButton;

    @FXML
    public ToggleGroup timerToggleGroup;
    @FXML
    public RadioButton timerRadioButtonShow;
    @FXML
    public CheckBox statisticCheckBox;
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
    public RadioButton timerRadioButtonHide;
    @FXML
    public TextField workerBornPeriodArea;
    @FXML
    public ComboBox<Double> probabilityBornWarriorArea;
    @FXML
    public ComboBox<Double> probabilityBornWorkerArea;
    @FXML
    public TextField warriorBornPeriodArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Заполняем комбобокс вероятности рождения WarriorAnt
        for (double i = 0; i <= 1; i += 0.1) {
            double roundedValue = Math.round(i * 10) / 10.0;
            probabilityBornWarriorArea.getItems().add(roundedValue);
        }

        // Заполняем комбобокс вероятности рождения WorkerAnt
        for (double i = 0; i <= 1; i += 0.1) {
            double roundedValue = Math.round(i * 10) / 10.0;
            probabilityBornWorkerArea.getItems().add(roundedValue);
        }
    }



    @FXML
    void switchingStatisticCheckBox(ActionEvent event) {
        showStatistic = statisticCheckBox.isSelected();
    }

    @FXML
    void timerRadioButtonPressed(ActionEvent event) {
        timerPane.setVisible(timerToggleGroup.getSelectedToggle().equals(timerRadioButtonShow));
    }

    @FXML
    void startPressed(ActionEvent event) {
        startSimulation();
        startButton.setDisable(true);
        stopButton.setDisable(false);
    }

    @FXML
    void stopPressed(ActionEvent event) throws Exception {
        if (showStatistic) {
            timer.cancel();
            StatisticController statisticController = new StatisticController();
            long currentTime = System.currentTimeMillis();
            if (!statisticController.newWindow(System.currentTimeMillis() - startTime)) {
                timer = null;
                timer = new Timer();
                startTime = startTime - currentTime + System.currentTimeMillis();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        long currentTime = System.currentTimeMillis();
                        long timePassed = currentTime - startTime;
                        update(timePassed);
                    }
                }, 0, 100);
                return;
            }
        }
        stopSimulation();
        startButton.setDisable(false);
        stopButton.setDisable(true);
    }

    @FXML
    void keyPressed(KeyEvent event) throws Exception {
        if (event.getCode() == KeyCode.B) {
            System.out.println(probabilityBornWarriorArea.getValue());
            startSimulation();
        } else if (event.getCode() == KeyCode.E) {
            stopSimulation();
        } else if (event.getCode() == KeyCode.T) {
            timerPane.setVisible(!timerPane.isVisible());
        } else if (event.getCode() == KeyCode.M) {
            System.out.println(this);
            MenuController menu = new MenuController();
            menu.newWindow(this);
        }
    }

    private void update(long timePassed) {
        AntRepository.createAntIfTimeElapsed(timePassed, WarriorAnt.class, WarriorAnt.APPEARANCE_TIME, WarriorAnt.APPEARANCE_CHANCE,
                (int) simulationPane.getWidth(), (int) simulationPane.getHeight());
        AntRepository.createAntIfTimeElapsed(timePassed, WorkerAnt.class, WorkerAnt.APPEARANCE_TIME, WorkerAnt.APPEARANCE_CHANCE,
                (int) simulationPane.getWidth(), (int) simulationPane.getHeight());
        Platform.runLater(() -> {
            timerLabel.setText((float) timePassed / 1000 + " c");
            updateAntsInView();
        });
    }

    private void updateAntsInView() {
        for (AbstractAnt ant : arrayOfAnts) {
            if (!simulationPane.getChildren().contains(ant.imageView)) {
                simulationPane.getChildren().add(ant.imageView);
            }
        }
    }

    public void startSimulation() {
        startTime = System.currentTimeMillis();
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

    public void exitApplication(ActionEvent actionEvent) {
        Platform.exit();
    }
}
