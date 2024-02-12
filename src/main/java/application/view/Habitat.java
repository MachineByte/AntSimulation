package application.view;

import application.models.AbstractAnt;
import application.models.implement.WarriorAnt;
import application.models.implement.WorkerAnt;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Habitat extends Application {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 500;
    private final ArrayList<AbstractAnt> arrayOfAnts = new ArrayList<>();
    PaneController controller;
    private Pane pane;
    private Text timerLabel;
    private Timer timer = null;
    private long startTime;

    private void update(long timePassed) {
        AbstractAnt newAnt;
        Random random = new Random();
        double probability = random.nextDouble();

        if ((Math.round(timePassed / 100)) * 100 % (WarriorAnt.APPEARANCE_TIME * 1000) == 0 && WarriorAnt.APPEARANCE_CHANCE>=probability) {
            newAnt = new WarriorAnt(WIDTH, HEIGHT);
            arrayOfAnts.add(newAnt);
        }

        if ((Math.round(timePassed / 100)) * 100 % (WorkerAnt.APPEARANCE_TIME * 1000) == 0 && WorkerAnt.APPEARANCE_CHANCE>=probability) {
            newAnt = new WorkerAnt(WIDTH, HEIGHT);
            arrayOfAnts.add(newAnt);
        }

        Platform.runLater(() -> {
            timerLabel.setText((float) timePassed / 1000 +" c");
            for (AbstractAnt ant : arrayOfAnts) {
                if (ant.getClass() == WorkerAnt.class && !pane.getChildren().contains(WorkerAnt.imageView)) {
                    pane.getChildren().add(WorkerAnt.imageView);
                } else if (ant.getClass() == WarriorAnt.class && !pane.getChildren().contains(WarriorAnt.imageView)) {
                    pane.getChildren().add(WarriorAnt.imageView);
                }
            }
        });
//        System.out.println(timePassed);
//        System.out.println((Math.round(timePassed/100))*100 % (WorkerAnt.APPEARANCE_TIME * 1000));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Habitat.class.getResource("mainField.fxml"));
        Parent parent = fxmlLoader.load();
        controller = fxmlLoader.getController();
        pane = controller.pane;
        timerLabel = controller.timerLabel;

        Scene scene = new Scene(parent, WIDTH, HEIGHT);

        startTime = System.currentTimeMillis(); // Запоминаем время начала симуляции

        primaryStage.setScene(scene);
        primaryStage.show();

        // Запускаем таймер, который будет вызывать метод update каждые 100 миллисекунд
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.B) {
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
            } else if (event.getCode() == KeyCode.E) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                    arrayOfAnts.clear();
                    pane.getChildren().clear();
                }
            } else if(event.getCode() == KeyCode.T){
                timerLabel.setVisible(!timerLabel.isVisible());
            }
        });
    }
}
