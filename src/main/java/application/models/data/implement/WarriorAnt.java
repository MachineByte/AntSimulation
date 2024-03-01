package application.models.data.implement;

import application.controllers.Habitat;
import application.models.data.AbstractAnt;
import application.models.data.IBehaviour;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public class WarriorAnt extends AbstractAnt implements IBehaviour {
    public static final float IMAGE_WIDTH = 30f;
    public static final float IMAGE_HEIGHT = 30f;
    public static final double DEFAULT_APPEARANCE_CHANCE = 1;
    public static final long DEFAULT_APPEARANCE_TIME = 1000;
    public static final long DEFAULT_LIVE_TIME = 10000;
    public static final Image IMAGE = new Image("application/controllers/warriorAnt.png");
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

    public WarriorAnt(int widthScene, int heightScene, long birthTime, long id) {
        Random random = new Random();

        this.x = (int) (random.nextDouble() * (widthScene - IMAGE_WIDTH));
        this.y = (int) (random.nextDouble() * (heightScene - IMAGE_HEIGHT));
        this.id = id;
        this.birthTime = birthTime;
        this.deathTime = birthTime+ liveTime;

        imageView = new ImageView(IMAGE);
        imageView.setLayoutX(this.x);
        imageView.setLayoutY(this.y);
        imageView.setFitWidth(IMAGE_WIDTH);
        imageView.setFitHeight(IMAGE_HEIGHT);
//        thread.start();
    }


    private double angle = 1;
    private final double radius = 50.0;
    private final double centerX = this.x - radius;
    private final double centerY = this.y - radius;

    @Override
    protected synchronized void move() throws InterruptedException {
        int deltaAngle = 10;
        angle+= deltaAngle;

        this.x = (int) (centerX - radius * Math.cos(Math.toRadians(angle)));
        this.y = (int) (centerY - radius * Math.sin(Math.toRadians(angle)));

        Platform.runLater(() -> {
            imageView.setTranslateX(this.x);
            imageView.setTranslateY(this.y);
            imageView.setRotate(angle);
        });

        if (angle >= 360) {
            angle = 0;
        }
    }


}
