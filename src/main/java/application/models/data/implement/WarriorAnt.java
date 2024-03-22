package application.models.data.implement;

import application.models.data.AbstractAnt;
import application.models.data.IBehaviour;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.Random;

public class WarriorAnt extends AbstractAnt implements IBehaviour, Serializable {
    public static final float IMAGE_WIDTH = 30f;
    public static final float IMAGE_HEIGHT = 30f;
    public static final double DEFAULT_APPEARANCE_CHANCE = 1;
    public static final long DEFAULT_APPEARANCE_TIME = 1000;
    public static final long DEFAULT_LIVE_TIME = 10000;
    public static final Image IMAGE = new Image("application/controllers/mexicanAnt.png");
    private static double appearanceChance = DEFAULT_APPEARANCE_CHANCE;
    private static long appearanceTime = DEFAULT_APPEARANCE_TIME;
    private static long liveTime = DEFAULT_LIVE_TIME;

    public static boolean isEnabled = true;

    public static double getAppearanceChance() {
        return appearanceChance;
    }

    public static void setAppearanceChance(double appearanceChance) {
        if (appearanceChance < 0 || appearanceChance > 1) {
            throw new IllegalArgumentException("Вероятность появления должна быть от 0 до 1");
        }
        WarriorAnt.appearanceChance = appearanceChance;
    }

    public static long getAppearanceTime() {
        return appearanceTime;
    }

    public static void setAppearanceTime(long appearanceTime) {
        if (appearanceTime < 0) {
            throw new IllegalArgumentException("Время появления должно быть больше 0");
        }
        WarriorAnt.appearanceTime = appearanceTime;
    }

    public static long getLiveTime() {
        return liveTime;
    }

    public static void setLiveTime(long liveTime) {
        if (liveTime < 0) {
            throw new IllegalArgumentException("Время жизни должно быть больше 0");
        }
        WarriorAnt.liveTime = liveTime;
    }

    public WarriorAnt(int widthScene, int heightScene, long birthTime, long id) throws InterruptedException {
        Random random = new Random();

        this.x = (int) (random.nextDouble() * (widthScene - IMAGE_WIDTH));
        this.y = (int) (random.nextDouble() * (heightScene - IMAGE_HEIGHT));
        this.id = id;
        this.birthTime = birthTime;
        this.deathTime = birthTime+ liveTime;

        initImageView();
        if(!isEnabled) {
            waitThread();
        }
    }

    @Override
    public void initImageView() {
        this.imageView = new ImageView(IMAGE);
        imageView.setX(this.x);
        imageView.setY(this.y);
        imageView.setFitWidth(IMAGE_WIDTH);
        imageView.setFitHeight(IMAGE_HEIGHT);
    }
    private double angle = 1;
    private final double radius = 50.0;
    private final double centerX = this.x;
    private final double centerY = this.y;

    @Override
    protected synchronized void move() {
        int deltaAngle = 10;
        angle += deltaAngle;

        double newX = x - radius * Math.cos(Math.toRadians(angle));
        double newY = y - radius * Math.sin(Math.toRadians(angle));

        Platform.runLater(() -> {
            // Обновлено для вывода новых координат X и Y, убран вывод для Translate и Layout
            System.out.printf("New X: %.2f, New Y: %.2f%n", newX, newY);

            imageView.setX(newX); // Здесь используется setX
            imageView.setY(newY); // И здесь используется setY
            imageView.setRotate(angle);
        });

        if (angle >= 360) {
            angle = 0;
        }
    }
}
