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

    private Timer timer = null;
    private long startTime;

    private void update(long timePassed) {
        createAntIfTimeElapsed(timePassed, WarriorAnt.class, WarriorAnt.APPEARANCE_TIME, WarriorAnt.APPEARANCE_CHANCE);
        createAntIfTimeElapsed(timePassed, WorkerAnt.class, WorkerAnt.APPEARANCE_TIME, WorkerAnt.APPEARANCE_CHANCE);

        Platform.runLater(() -> {
            controller.timerLabel.setText((float) timePassed / 1000 + " c");
            updateAntsInView();
        });
    }

    private void createAntIfTimeElapsed(long timePassed, Class<? extends AbstractAnt> antClass, float appearanceTime, float appearanceChance) {
        Random random = new Random();
        double probability = random.nextDouble();

        if ((Math.round(timePassed / 100.0) * 100) % appearanceTime == 0 && appearanceChance >= probability) {
            AbstractAnt newAnt = (antClass == WarriorAnt.class) ? new WarriorAnt(WIDTH, HEIGHT) : new WorkerAnt(WIDTH, HEIGHT);
            arrayOfAnts.add(newAnt);
        }
    }

    private void updateAntsInView() {
        for (AbstractAnt ant : arrayOfAnts) {
            if (!controller.simulationPane.getChildren().contains(ant.imageView)) {
                controller.simulationPane.getChildren().add(ant.imageView);
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Habitat.class.getResource("mainField.fxml"));
        Parent parent = fxmlLoader.load();
        controller = fxmlLoader.getController();

        Scene scene = new Scene(parent, WIDTH, HEIGHT);

        startTime = System.currentTimeMillis(); // Запоминаем время начала симуляции

        primaryStage.setScene(scene);
        primaryStage.show();

        controller.statisticalPane.setVisible(false);
        controller.timerPane.setVisible(false);
        // Запускаем таймер, который будет вызывать метод update каждые 100 миллисекунд
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.B) {
                if (timer == null) {
                    timer = new Timer();
                    startTime = System.currentTimeMillis();
                    controller.statisticalPane.setVisible(false);
                    controller.timerPane.setVisible(true);
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

                    long countWorker = arrayOfAnts.stream().filter(abstractAnt -> abstractAnt.getClass() == WorkerAnt.class).count();
                    long countWarrior = arrayOfAnts.stream().filter(abstractAnt -> abstractAnt.getClass() == WarriorAnt.class).count();

                    controller.statisticalPane.setVisible(true);
                    controller.timerPane.setVisible(false);

                    controller.firstCounterStatisticalLabel.setText(String.valueOf("Количество рабочих: " + countWorker));
                    controller.secondCounterStatisticalLabel.setText(String.valueOf("Количество воинов: " + countWarrior));
                    controller.timerStatisticalLabel.setText("Время симуляции: "+ controller.timerLabel.getText());

                    arrayOfAnts.clear();
                    controller.simulationPane.getChildren().clear();

                }
            } else if(event.getCode() == KeyCode.T){
                controller.timerPane.setVisible(!controller.timerPane.isVisible());
            }
        });
    }
}
