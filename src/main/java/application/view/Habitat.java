package application.view;

import application.models.AbstractAnt;
import application.models.implement.WarriorAnt;
import application.models.implement.WorkerAnt;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Habitat extends Application {
    private Timer m_timer = new Timer();

    private final int width = 1000;
    private final int height = 500;

    private final float warriorAppearanceChance = 0.1f;
    private final float workerAppearanceChance = 0.5f;

    private final float warriorAppearanceTime = 1f;
    private final float workerAppearanceTime = 1f;

    private final ArrayList<AbstractAnt> arrayOfAnts = new ArrayList<>();

    private final Image workerAntImage = new Image("application/view/workerAnt.png");
    private final Image warriorAntImage = new Image("application/view/warriorAnt.png");
    private final ImageView workerAntImageView = new ImageView(workerAntImage);
    private final ImageView warriorAntImageView = new ImageView(workerAntImage);
    private final Group root = new Group();
    private final Scene scene = new Scene(root, width, height);

    private long startTime; // Время начала симуляции

    private void update(long timePassed) {
//        System.out.println("called");
        createAnt();
        Platform.runLater(() -> {
            for (AbstractAnt ant : arrayOfAnts) {
                if(ant.getClass() == WorkerAnt.class){
                    ImageView antImageView = new ImageView(workerAntImage);
                    antImageView.setLayoutX(ant.x);
                    antImageView.setLayoutY(ant.y);
                    antImageView.setFitWidth(20);
                    antImageView.setFitHeight(20);
                    root.getChildren().add(antImageView);
                } else {
                    ImageView antImageView = new ImageView(warriorAntImage);
                    antImageView.setLayoutX(ant.x);
                    antImageView.setLayoutY(ant.y);
                    antImageView.setFitWidth(30);
                    antImageView.setFitHeight(30);
                    root.getChildren().add(antImageView);
                }
            }
        });
    }

    private void createAnt(){
        Random random = new Random();
        double x;
        double y;
        AbstractAnt newAnt;

        if(random.nextDouble()<warriorAppearanceChance){
            x = random.nextDouble() * (scene.getWidth() - warriorAntImageView.getFitWidth());
            y = random.nextDouble() * (scene.getHeight() - warriorAntImageView.getFitHeight());
            newAnt = new WarriorAnt((int) x, (int) y);
        } else{
            x = random.nextDouble() * (scene.getWidth() - workerAntImageView.getFitWidth());
            y = random.nextDouble() * (scene.getHeight() - workerAntImageView.getFitHeight());
            newAnt = new WorkerAnt((int) x, (int) y);
        }

        arrayOfAnts.add(newAnt);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        startTime = System.currentTimeMillis(); // Запоминаем время начала симуляции

        primaryStage.setScene(scene);
        primaryStage.show();

        // Запускаем таймер, который будет вызывать метод update каждые 100 миллисекунд
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.S) { // Запуск симуляции при нажатии клавиши "S"
                if (m_timer == null) {
                    m_timer = new Timer();
                    m_timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            long currentTime = System.currentTimeMillis();
                            long timePassed = currentTime - startTime;
                            update(timePassed);
                        }
                    }, 0, 100);
                }
            } else if (event.getCode() == KeyCode.P) { // Остановка симуляции при нажатии клавиши "P"
                if (m_timer != null) {
                    m_timer.cancel();
                    m_timer = null;
                    arrayOfAnts.clear();
                }
            }
        });
    }
}
