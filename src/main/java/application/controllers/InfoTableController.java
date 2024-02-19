package application.controllers;

import application.models.AntRepository;
import application.models.data.AbstractAnt;
import application.models.data.implement.WorkerAnt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class InfoTableController {

    public class AntInfo {
        private long birthTime;
        private long id;

        public AntInfo(long birthTime, long id) {
            this.birthTime = birthTime;
            this.id = id;
        }

        public long getBirthTime() {
            return birthTime;
        }

        public void setBirthTime(long birthTime) {
            this.birthTime = birthTime;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }

    private static AntRepository antRepository = AntRepository.getInstance();

    private static final int WIDTH = 532;
    private static final int HEIGHT = 400;



    @FXML
    private TableView<AntInfo> InfoTable;

    @FXML
    private TableColumn<AntInfo, Long> birthTimeColumn;

    @FXML
    private TableColumn<AntInfo, Long> IdColumn;

    @FXML
    void okButtonPress(ActionEvent event) {
        Stage stage = (Stage) InfoTable.getScene().getWindow();
        stage.close();
    }

    public void newWindow(TreeMap<Long, Set<Long>> mapOfBirthTime) throws Exception {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("infoTableField.fxml"));
        Parent parent = fxmlLoader.load();
        InfoTableController infoTableController  = fxmlLoader.getController();
        Scene scene = new Scene(parent, WIDTH, HEIGHT);
        stage.setScene(scene);

        infoTableController.IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        infoTableController.birthTimeColumn.setCellValueFactory(new PropertyValueFactory<>("birthTime"));
        // Очистка таблицы перед добавлением новых данных
        infoTableController.InfoTable.getItems().clear();

        // Добавление данных в таблицу
        for (Map.Entry<Long, Set<Long>> entry : mapOfBirthTime.entrySet()) {
            Long birthTime = entry.getKey();
            Set<Long> ids = entry.getValue();
            for (Long id : ids) {
                // Создание объекта информации о муравье
                AntInfo antInfo = new AntInfo(birthTime, id);
                // Добавление информации о муравье в таблицу
                infoTableController.InfoTable.getItems().add(antInfo);
            }
        }

        stage.showAndWait();
    }

}