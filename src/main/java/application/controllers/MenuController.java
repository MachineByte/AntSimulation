package application.controllers;

import application.models.AntRepository;
import application.models.data.implement.WarriorAnt;
import application.models.data.implement.WorkerAnt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MenuController implements Initializable {
    private static final AntRepository antRepository = AntRepository.getInstance();
    private static final int WIDTH = 642;
    private static final int HEIGHT = 380;
    private static Habitat habitat;
    private Alert errorAlert = new Alert(Alert.AlertType.ERROR);

    public MenuController() {

    }

    @FXML
    private RadioButton timerRadioButtonShow;
    @FXML
    private CheckBox statisticCheckBox;
    @FXML
    private Button startButton;
    @FXML
    private RadioButton timerRadioButtonHide;
    @FXML
    private ToggleGroup timerToggleGroup;
    @FXML
    private Button stopButton;
    @FXML
    private TextField warriorBornPeriodArea;
    @FXML
    private TextField workerBornPeriodArea;
    @FXML
    public TextField workerDeathPeriodArea;
    @FXML
    public TextField warriorDeathPeriodArea;
    @FXML
    private ComboBox<Double> probabilityBornWorkerArea;
    @FXML
    private ComboBox<Double> probabilityBornWarriorArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Заполняем комбобокс вероятности рождения WarriorAnt
        for (double i = 0; i <= 1; i += 0.1) {
            double roundedValue = Math.round(i * 10) / 10.0;
            probabilityBornWarriorArea.getItems().add(roundedValue);
            probabilityBornWorkerArea.getItems().add(roundedValue);
        }
    }

    @FXML
    void menuStartButtonPressed(ActionEvent event) {
        habitat.startPressed();
        startButton.setDisable(habitat.startButton.isDisable());
        stopButton.setDisable(habitat.stopButton.isDisable());
    }

    @FXML
    void menuStopButtonPressed(ActionEvent event) throws Exception {
        habitat.stopPressed();
        startButton.setDisable(habitat.startButton.isDisable());
        stopButton.setDisable(habitat.stopButton.isDisable());
    }


    @FXML
    void switchingStatisticCheckBox(ActionEvent event) {
        boolean selected = statisticCheckBox.isSelected();
        Habitat.showStatistic = selected;
        habitat.setStatisticCheckBoxValue(selected);
    }

    @FXML
    void timerRadioButtonPressed(ActionEvent event) {
        //Везде костыли, но таков путь...
        if(timerToggleGroup.getSelectedToggle().equals(timerRadioButtonShow)){
            habitat.timerToggleGroup.selectToggle(habitat.timerRadioButtonShow);
        } else {
            habitat.timerToggleGroup.selectToggle(habitat.timerRadioButtonHide);
        }
        habitat.timerRadioButtonPressed();
    }

    @FXML
    void changeWarriorBornProbability(ActionEvent event) {
        habitat.probabilityBornWarriorArea.setValue(probabilityBornWarriorArea.getValue());
    }

    @FXML
    void changeWorkerBornProbability(ActionEvent event) {
        habitat.probabilityBornWorkerArea.setValue(probabilityBornWorkerArea.getValue());
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

    public void newWindow(Habitat habitat) throws Exception {
        Stage stage = new Stage();
        MenuController.habitat = habitat;
        FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource("menuField.fxml"));

        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent, WIDTH, HEIGHT);
        stage.setScene(scene);

        stage.show();
    }


}
