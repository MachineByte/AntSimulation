package application.controllers;

import application.models.AntRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class MenuController {
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
    void menuStartButtonPressed(ActionEvent event) {
        System.out.println(habitat);
        habitat.startPressed(event);

    }

    @FXML
    void stopPressed(ActionEvent event) throws Exception {
        habitat.stopPressed(event);
    }

    @FXML
    void switchingStatisticCheckBox(ActionEvent event) {
        habitat.statisticCheckBox.setSelected(true); //Пока костыль. Нужно сделать красивее
        habitat.switchingStatisticCheckBox(event);
    }

    @FXML
    void timerRadioButtonPressed(ActionEvent event) {
        //Тоже костыль, но таков путь...
        if(timerToggleGroup.getSelectedToggle().equals(timerRadioButtonShow)){
            habitat.timerToggleGroup.selectToggle(habitat.timerRadioButtonShow);
        } else {
            habitat.timerToggleGroup.selectToggle(habitat.timerRadioButtonHide);
        }

        habitat.timerRadioButtonPressed(event);
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
