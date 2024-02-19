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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class Habitat implements Initializable {
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 700;
    private static final AntRepository antRepository = AntRepository.getInstance();
    private static final Vector<AbstractAnt> vectorOfAnt = antRepository.getVector();
    private static Timer timer = null;
    private static long startTime;
    public static boolean showStatistic = false;
    public Button exitButton;
    private Alert errorAlert = new Alert(Alert.AlertType.ERROR);

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
    public TextField warriorBornPeriodArea;
    @FXML
    public TextField workerDeathPeriodArea;
    @FXML
    public TextField warriorDeathPeriodArea;
    @FXML
    public ComboBox<Double> probabilityBornWarriorArea;
    @FXML
    public ComboBox<Double> probabilityBornWorkerArea;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Заполняем комбобокс вероятности рождения WarriorAnt
        for (double i = 0; i <= 1; i += 0.1) {
            double roundedValue = Math.round(i * 10) / 10.0;
            probabilityBornWarriorArea.getItems().add(roundedValue);
            probabilityBornWorkerArea.getItems().add(roundedValue);
        }
        workerBornPeriodArea.setPromptText(String.valueOf(WorkerAnt.DEFAULT_APPEARANCE_TIME));
        warriorBornPeriodArea.setPromptText(String.valueOf(WarriorAnt.DEFAULT_APPEARANCE_TIME));
    }

    public void changeWarriorBornProbability(ActionEvent actionEvent) {
        WarriorAnt.setAppearanceChance(probabilityBornWarriorArea.getValue());

    }

    public void changeWorkerBornProbability(ActionEvent actionEvent) {
        WorkerAnt.setAppearanceChance(probabilityBornWorkerArea.getValue());
    }

    @FXML
    void changeAntTextBoxTimePeriod(TextField bornPeriodArea, Consumer<Long> setAppearanceTime, long defaultAppearanceTime) {
        boolean errorOccurred = false;
        try {
            String text = bornPeriodArea.getText();
            setAppearanceTime.accept(Long.parseLong(text));
        } catch (NumberFormatException e) {
            errorAlert.setContentText("Введено недопустимое значение");
            errorOccurred = true;
        } catch (Exception e) {
            errorAlert.setContentText(e.getMessage());
            errorOccurred = true;
        } finally {
            if (errorOccurred) {
                errorAlert.show();
                setAppearanceTime.accept(defaultAppearanceTime);
                bornPeriodArea.setText(String.valueOf(defaultAppearanceTime));
            }
        }
        bornPeriodArea.getScene().getRoot().requestFocus(); //перевод фокуса на основную сцену
    }

    @FXML
    void changeWorkerBornPeriod(ActionEvent event) {
        changeAntTextBoxTimePeriod(workerBornPeriodArea, WorkerAnt::setAppearanceTime, WorkerAnt.DEFAULT_APPEARANCE_TIME);
    }

    @FXML
    void changeWarriorBornPeriod(ActionEvent event) {
        changeAntTextBoxTimePeriod(warriorBornPeriodArea, WarriorAnt::setAppearanceTime, WarriorAnt.DEFAULT_APPEARANCE_TIME);
    }

    @FXML
    void changeWorkerDeathPeriod(ActionEvent event) {
        changeAntTextBoxTimePeriod(workerDeathPeriodArea, WorkerAnt::setLiveTime, WorkerAnt.DEFAULT_LIVE_TIME);
    }

    @FXML
    void changeWarriorDeathPeriod(ActionEvent event) {
        changeAntTextBoxTimePeriod(warriorDeathPeriodArea, WarriorAnt::setLiveTime, WarriorAnt.DEFAULT_LIVE_TIME);
    }

    public void setStatisticCheckBoxValue(boolean value) {
        statisticCheckBox.setSelected(value);
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
            startSimulation();
        } else if (event.getCode() == KeyCode.E) {
            stopSimulation();
        } else if (event.getCode() == KeyCode.T) {
            timerPane.setVisible(!timerPane.isVisible());
        } else if (event.getCode() == KeyCode.M) {
            MenuController menu = new MenuController();
            menu.newWindow(this);
        }
    }

    private void update(long timePassed) {
        AntRepository.createAntIfTimeElapsed(timePassed, WarriorAnt.class, WarriorAnt.getAppearanceTime(), WarriorAnt.getAppearanceChance(),
                (int) simulationPane.getWidth(), (int) simulationPane.getHeight());
        AntRepository.createAntIfTimeElapsed(timePassed, WorkerAnt.class, WorkerAnt.getAppearanceTime(), WorkerAnt.getAppearanceChance(),
                (int) simulationPane.getWidth(), (int) simulationPane.getHeight());
        AntRepository.deleteAntsIfLifeTimeElapsed(timePassed);
        Platform.runLater(() -> {
            timerLabel.setText((float) timePassed / 1000 + " c");
            updateAntsInView();
        });
    }


       private void updateAntsInView() {
           simulationPane.getChildren().removeIf(node ->
                   node instanceof ImageView &&
                           vectorOfAnt.stream().noneMatch(ant -> ant.imageView.equals(node)));
        for (AbstractAnt ant : vectorOfAnt) {
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

            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    long currentTime = System.currentTimeMillis();
                    long timePassed = currentTime - startTime;
                    update(timePassed);
                }
            }, 0, 10);
        }
    }

    public void stopSimulation() {
        if (timer != null) {
            timer.cancel();
            timer = null;

            vectorOfAnt.clear();
            simulationPane.getChildren().clear();

        }
    }

    public void exitApplication(ActionEvent actionEvent) {
        Platform.exit();
    }



}
