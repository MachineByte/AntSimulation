package application.view;

import application.models.AbstractAnt;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;

public class Habitat extends Application {
    private Timer m_timer = new Timer();
    private final int width = 1000;
    private final int height = 500;

    private final float warriorAppearenceChance = 0.1f;
    private final float workerAppearenceChance = 0.5f;

    private final float warriorAppearenceTime = 1;
    private final float workerAppearenceTime = 1;

    private ArrayList<AbstractAnt> arrayAnts = new ArrayList<>();

    private void update(long timePassed){
//        arrayAnts.add(new )
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        primaryStage.setTitle("Simple JavaFX App");
        primaryStage.setScene(new Scene(root, 300, 200));
        primaryStage.show();
    }

}
