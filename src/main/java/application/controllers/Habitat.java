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

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public class Habitat implements Initializable {

    String file = "antSave.ser";
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 700;
    private static final AntRepository antRepository = AntRepository.getInstance();
    private static final Vector<AbstractAnt> vectorOfAnt = antRepository.getVector();
    private static Timer timer = null;
    public static long startTime;
    public static boolean showStatistic = false;
    public Button exitButton;
    private final Alert errorAlert = new Alert(Alert.AlertType.ERROR);

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
    public Button objectTableButton;
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

    @FXML
    private ComboBox<Integer> workerAntThreadPriority;

    @FXML
    private ComboBox<Integer> warriorAntThreadPriority;

    @FXML
    private ToggleButton WorkerAntThreadButton;

    @FXML
    private ToggleButton WarriorAntThreadButton;


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

        probabilityBornWarriorArea.setPromptText(String.valueOf(WarriorAnt.DEFAULT_APPEARANCE_CHANCE));
        probabilityBornWorkerArea.setPromptText(String.valueOf(WorkerAnt.DEFAULT_APPEARANCE_CHANCE));

        for (int i : new int[]{1, 5, 10}) {
            workerAntThreadPriority.getItems().add(i);
            warriorAntThreadPriority.getItems().add(i);
        }

        try {
            List<String> lines = Files.readAllLines(Path.of("src/main/resources/filename.txt"));
            Iterator<String> iterator = lines.iterator();

            String checkBoxValue = iterator.next();

            showStatistic = Boolean.parseBoolean(String.valueOf(checkBoxValue));
            statisticCheckBox.setSelected(Boolean.parseBoolean(checkBoxValue));

            probabilityBornWorkerArea.setValue(Double.valueOf(iterator.next()));
            changeWorkerBornProbability();
            probabilityBornWarriorArea.setValue(Double.valueOf(iterator.next()));
            changeWarriorBornProbability();

            workerBornPeriodArea.setText(iterator.next());
            changeWorkerBornPeriod();
            warriorBornPeriodArea.setText(iterator.next());
            changeWarriorBornPeriod();

            workerDeathPeriodArea.setText(iterator.next());
            changeWorkerDeathPeriod();
            warriorDeathPeriodArea.setText(iterator.next());
            changeWarriorDeathPeriod();

            workerAntThreadPriority.setValue(Integer.valueOf(iterator.next()));
            changeWorkerAntThreadPriority();
            warriorAntThreadPriority.setValue(Integer.valueOf(iterator.next()));
            changeWarriorAntThreadPriority();

            WorkerAntThreadButton.setSelected(!Boolean.parseBoolean(iterator.next()));
            changeWorkerAntThread();
            WarriorAntThreadButton.setSelected(!Boolean.parseBoolean(iterator.next()));
            changeWarriorAntThread();

            timerRadioButtonShow.setSelected(Boolean.parseBoolean(iterator.next()));
            timerRadioButtonPressed();

        } catch (IOException | NumberFormatException e) {
            System.err.println("Ошибка чтения из файла: " + e.getMessage());
        }
    }

    public void changeWarriorBornProbability() {
        WarriorAnt.setAppearanceChance(probabilityBornWarriorArea.getValue());

    }

    public void changeWorkerBornProbability() {
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
        if (bornPeriodArea.getScene() != null) { // Проверка на наличие сцены
            bornPeriodArea.getScene().getRoot().requestFocus(); // перевод фокуса на основную сцену
        }
    }

    @FXML
    void changeWorkerBornPeriod() {
        changeAntTextBoxTimePeriod(workerBornPeriodArea, WorkerAnt::setAppearanceTime, WorkerAnt.DEFAULT_APPEARANCE_TIME);
    }

    @FXML
    void changeWarriorBornPeriod() {
        changeAntTextBoxTimePeriod(warriorBornPeriodArea, WarriorAnt::setAppearanceTime, WarriorAnt.DEFAULT_APPEARANCE_TIME);
    }

    @FXML
    void changeWorkerDeathPeriod() {
        changeAntTextBoxTimePeriod(workerDeathPeriodArea, WorkerAnt::setLiveTime, WorkerAnt.DEFAULT_LIVE_TIME);
    }

    @FXML
    void changeWarriorDeathPeriod() {
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
    void timerRadioButtonPressed() {
        timerPane.setVisible(timerToggleGroup.getSelectedToggle().equals(timerRadioButtonShow));
    }

    @FXML
    void menuHideTimerButtonPressed(ActionEvent event) {
        timerPane.setVisible(false);
    }

    @FXML
    void menuShowTimerButtonPressed(ActionEvent event) {
        timerPane.setVisible(true);
    }

    @FXML
    void waitWarriorAnt(ActionEvent event) {
        antRepository.setClassThreadStatus(WarriorAnt.class, false);
    }

    @FXML
    void notifyWarriorAnt(ActionEvent event) {
        antRepository.setClassThreadStatus(WarriorAnt.class, true);
    }

    @FXML
    void waitWorkerAnt(ActionEvent event) {
        antRepository.setClassThreadStatus(WorkerAnt.class, false);
    }

    @FXML
    void notifyWorkerAnt(ActionEvent event) {
        antRepository.setClassThreadStatus(WorkerAnt.class, true);
    }

    @FXML
    public void changeWarriorAntThread() {
        antRepository.setClassThreadStatus(WarriorAnt.class, !WarriorAntThreadButton.isSelected());
    }

    @FXML
    public void changeWorkerAntThread() {
        antRepository.setClassThreadStatus(WorkerAnt.class, !WorkerAntThreadButton.isSelected());
    }

    @FXML
    public void changeWorkerAntThreadPriority() {
        antRepository.setClassThreadPriority(WorkerAnt.class, workerAntThreadPriority.getValue());
    }

    @FXML
    public void changeWarriorAntThreadPriority() {
        antRepository.setClassThreadPriority(WarriorAnt.class, warriorAntThreadPriority.getValue());
    }

    @FXML
    public void hideStatisticPressed() {
        showStatistic = false;
        statisticCheckBox.setSelected(false);
    }

    @FXML
    public void showStatisticPressed() {
        showStatistic = true;
        statisticCheckBox.setSelected(true);
    }
    @FXML
    void objectTablePress(ActionEvent event) throws Exception {
        InfoTableController infoTable = new InfoTableController();
        infoTable.newWindow(antRepository.getMapOfBirthTime());
    }

    @FXML
    void openConsole(ActionEvent event) throws IOException {
        ConsoleFieldController console = new ConsoleFieldController();
        console.newWindow(this);
    }

    @FXML
    void startPressed() {
        startSimulation();
        startButton.setDisable(true);
        stopButton.setDisable(false);
    }

    @FXML
    void stopPressed() throws Exception {
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
                        try {
                            update(timePassed);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
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
        } else if(event.getCode() == KeyCode.W){
            antRepository.setClassThreadStatus(WarriorAnt.class, true);
        } else if(event.getCode() == KeyCode.N){
            antRepository.setClassThreadStatus(WarriorAnt.class, false);
        } else if(event.getCode() == KeyCode.O){
            antRepository.setClassThreadStatus(WorkerAnt.class, true);
        } else if(event.getCode() == KeyCode.P){
            antRepository.setClassThreadStatus(WorkerAnt.class, false);
        } else if(event.getCode() == KeyCode.S) {
            AntRepository.serializeVectorOfAnts(file);
        } else if(event.getCode() == KeyCode.D) {
            stopPressed();
            AntRepository.deserializeVectorOfAnts(file);
            startPressed();
        }
    }

    private void update(long timePassed) throws InterruptedException {
        AntRepository.createAntIfTimeElapsed(timePassed, WarriorAnt.class, WarriorAnt.getAppearanceTime(), WarriorAnt.getAppearanceChance(),
                (int) simulationPane.getWidth(), (int) simulationPane.getHeight());
        AntRepository.createAntIfTimeElapsed(timePassed, WorkerAnt.class, WorkerAnt.getAppearanceTime(), WorkerAnt.getAppearanceChance(),
                (int) simulationPane.getWidth(), (int) simulationPane.getHeight());
        AntRepository.deleteAntsIfLifeTimeElapsed(timePassed);
        Platform.runLater(() -> {
            timerLabel.setText((float) timePassed / 1000 + " c");
            try {
                updateAntsInView();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private void updateAntsInView() throws InterruptedException {
        synchronized (vectorOfAnt) {
            simulationPane.getChildren().removeIf(node ->
                    node instanceof ImageView &&
                            vectorOfAnt.stream().noneMatch(ant -> ant.imageView.equals(node)));

            for (AbstractAnt ant : vectorOfAnt) {
                if (!simulationPane.getChildren().contains(ant.imageView)) {
                    simulationPane.getChildren().add(ant.imageView);
                }
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
                    try {
                        update(timePassed);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, 0, 10);
        }
    }

    public void stopSimulation() throws InterruptedException {
        if (timer != null) {
            timer.cancel();
            timer = null;
            for (AbstractAnt ant : vectorOfAnt) {
                ant.killThread();
            }
            vectorOfAnt.clear();
            simulationPane.getChildren().clear();
        }
    }

    public void exitApplication(ActionEvent actionEvent) {
        writeConfigFile();
        Platform.exit();
        System.exit(0);
    }

    public void writeConfigFile() {
        try {
            FileWriter writer = new FileWriter("src/main/resources/filename.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(showStatistic + "\n");
            bufferedWriter.write(WorkerAnt.getAppearanceChance() + "\n");
            bufferedWriter.write(WarriorAnt.getAppearanceChance() + "\n");
            bufferedWriter.write(WorkerAnt.getAppearanceTime() + "\n");
            bufferedWriter.write(WarriorAnt.getAppearanceTime() + "\n");
            bufferedWriter.write(WorkerAnt.getLiveTime() + "\n");
            bufferedWriter.write(WarriorAnt.getLiveTime() + "\n");

            if (workerAntThreadPriority.getValue() == null) {
                bufferedWriter.write("5\n");
            } else {
                bufferedWriter.write(workerAntThreadPriority.getValue() + "\n");
            }

            if (warriorAntThreadPriority.getValue() == null) {
                bufferedWriter.write("5\n");
            } else {
                bufferedWriter.write(warriorAntThreadPriority.getValue() + "\n");
            }

            bufferedWriter.write(WorkerAnt.isEnabled + "\n");
            bufferedWriter.write(WarriorAnt.isEnabled + "\n");
            bufferedWriter.write(timerRadioButtonShow.isSelected() + "\n");
            bufferedWriter.close();
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл: " + e.getMessage());
        }
    }
}
