package application.models.data.implement;

import application.models.data.AbstractAnt;
import application.models.data.IBehaviour;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public class WorkerAnt extends AbstractAnt implements IBehaviour {
    public static final float IMAGE_WIDTH = 30f;
    public static final float IMAGE_HEIGHT = 30f;
    public static final double DEFAULT_APPEARANCE_CHANCE = 1d;
    public static final long DEFAULT_APPEARANCE_TIME = 1000;
    public static final long DEFAULT_LIVE_TIME = 10000;
    public static final Image IMAGE = new Image("application/controllers/workerAnt.png");
    private static double appearanceChance = DEFAULT_APPEARANCE_CHANCE;
    private static long appearanceTime = DEFAULT_APPEARANCE_TIME;
    private static long liveTime = DEFAULT_LIVE_TIME;


    public static double getAppearanceChance() {
        return appearanceChance;
    }

    public static void setAppearanceChance(double appearanceChance) {
        if (appearanceChance < 0 || appearanceChance > 1) {
            throw new IllegalArgumentException("Вероятность появления должна быть от 0 до 1");
        }
        WorkerAnt.appearanceChance = appearanceChance;
    }

    public static long getAppearanceTime() {
        return appearanceTime;
    }

    public static void setAppearanceTime(long appearanceTime) {
        if (appearanceTime < 0) {
            throw new IllegalArgumentException("Время появления должно быть больше 0");
        }
        WorkerAnt.appearanceTime = appearanceTime;
    }

    public static long getLiveTime() {
        return liveTime;
    }

    public static void setLiveTime(long liveTime) {
        if (liveTime < 0) {
            throw new IllegalArgumentException("Время жизни должно быть больше 0");
        }
        WorkerAnt.liveTime = liveTime;
    }
    public WorkerAnt(int widthScene, int heightScene, long birthTime, long id) {
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
    }
//    public int getX() {
//        return x;
//    }
//
//    public int getY() {
//        return y;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public long getBirthTime() {
//        return birthTime;
//    }
//
//    public long getDeathTime() {
//        return deathTime;
//    }

}
