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

public class MenuController implements Initializable {
    private static final AntRepository antRepository = AntRepository.getInstance();
    private static final int WIDTH = 642;
    private static final int HEIGHT = 280;
    private static Habitat habitat;

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
        habitat.startPressed(event);

    }

    @FXML
    void stopPressed(ActionEvent event) throws Exception {
        habitat.stopPressed(event);
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
        habitat.timerRadioButtonPressed(event);
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
    void changeWarriorBornPeriod(ActionEvent event) {
        try {
            String text = warriorBornPeriodArea.getText();
            if (text.isEmpty()) {
                throw new Exception("Введено пустое значение");
            }

            double value = Double.parseDouble(text);
            if (value < 0) {
                throw new Exception("Значение не может быть меньше нуля");
            } else {
                WarriorAnt.APPEARANCE_TIME = value;
            }
        } catch (Exception e) {
            WorkerAnt.APPEARANCE_TIME = 100;
            warriorBornPeriodArea.setText("100");
        }

        habitat.warriorBornPeriodArea.setText(warriorBornPeriodArea.getText());
        warriorBornPeriodArea.getScene().getRoot().requestFocus(); //перевод фокуса на основную сцену
    }

    @FXML
    void changeWorkerBornPeriod(ActionEvent event) {
        try {
            String text = workerBornPeriodArea.getText();
            if (text.isEmpty()) {
                throw new Exception("Введено пустое значение");
            }

            double value = Double.parseDouble(text);
            if (value < 0) {
                throw new Exception("Значение не может быть меньше нуля");
            } else {
                WorkerAnt.APPEARANCE_TIME = value;
            }
        } catch (Exception e) {
            WorkerAnt.APPEARANCE_TIME = 100;
            workerBornPeriodArea.setText("100");
        }

        habitat.workerBornPeriodArea.setText(workerBornPeriodArea.getText());
        workerBornPeriodArea.getScene().getRoot().requestFocus(); //перевод фокуса на основную сцену
    }

    public void newWindow(Habitat habitat) throws Exception {
        Stage stage = new Stage();
        MenuController.habitat = habitat;
        System.out.println(habitat);
        FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource("menuField.fxml"));

        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent, WIDTH, HEIGHT);
        stage.setScene(scene);

        stage.show();
    }


}
