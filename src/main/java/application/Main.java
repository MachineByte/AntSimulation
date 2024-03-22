package application;

import application.controllers.Habitat;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main  extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        startScene("mainField.fxml");
    }

    public void startScene(String path) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Habitat.class.getResource(path));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent, Habitat.WIDTH, Habitat.HEIGHT);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setOnCloseRequest(Habitat.getCloseEventHandler());
        stage.show();

    }



    public static void main(String[] args) {
        launch();
    }


}
