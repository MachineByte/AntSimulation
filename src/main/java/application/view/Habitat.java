package application.view;

import application.models.AbstractAnt;
import application.models.implement.WarriorAnt;
import application.models.implement.WorkerAnt;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Habitat extends Application {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 500;
    private final ArrayList<AbstractAnt> arrayOfAnts = new ArrayList<>();

    private Group root = new Group();
    private final Scene scene = new Scene(root, WIDTH, HEIGHT);
    private Timer timer = null;
    private long startTime; // Время начала симуляции

    private void update(long timePassed) {
        AbstractAnt newAnt = null;
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
            for (AbstractAnt ant : arrayOfAnts) {
                if (ant.getClass() == WorkerAnt.class && !root.getChildren().contains(WorkerAnt.imageView)) {
                    root.getChildren().add(WorkerAnt.imageView);
                } else if (ant.getClass() == WarriorAnt.class && !root.getChildren().contains(WarriorAnt.imageView)) {
                    root.getChildren().add(WarriorAnt.imageView);
                }
            }
        });
//        System.out.println(timePassed);
//        System.out.println((Math.round(timePassed/100))*100 % (WorkerAnt.APPEARANCE_TIME * 1000));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        startTime = System.currentTimeMillis(); // Запоминаем время начала симуляции

        primaryStage.setScene(scene);
        primaryStage.show();

        // Запускаем таймер, который будет вызывать метод update каждые 100 миллисекунд
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.B) { // Запуск симуляции при нажатии клавиши "S"
                if (timer == null) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            long currentTime = System.currentTimeMillis();
                            long timePassed = currentTime - startTime;
                            update(timePassed);
                        }
                    }, 0, 100);
                }
            } else if (event.getCode() == KeyCode.E) { // Остановка симуляции при нажатии клавиши "P"
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                    arrayOfAnts.clear();
                    root.getChildren().clear();
                }
            }

            System.out.println(timer);
        });
    }
}
