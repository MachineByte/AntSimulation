package application.models.data.implement;

import application.models.data.AbstractAnt;
import application.models.data.IBehaviour;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public class WarriorAnt extends AbstractAnt implements IBehaviour {

    public final static double DEFAULT_APPEARANCE_CHANCE = 1d;
    public final static double DEFAULT_APPEARANCE_TIME = 1000d;
    public final static long DEFAULT_LIVE_TIME = 10000;
    private static double APPEARANCE_CHANCE = DEFAULT_APPEARANCE_CHANCE;
    private static double APPEARANCE_TIME = DEFAULT_APPEARANCE_TIME;
    private static long LIVE_TIME = DEFAULT_LIVE_TIME;
    public static final Image IMAGE = new Image("application/controllers/warriorAnt.png");
    public static float IMAGE_WIDTH = 30f;
    public static final float IMAGE_HEIGHT = 30f;

    public static double getAppearanceChance() {
        return APPEARANCE_CHANCE;
    }

    public static void setAppearanceChance(double appearanceChance) {
        if (appearanceChance < 0 || appearanceChance > 1) {
            throw new IllegalArgumentException("Appearance chance must be between 0 and 1");
        }
        APPEARANCE_CHANCE = appearanceChance;
    }

    public static double getAppearanceTime() {
        return APPEARANCE_TIME;
    }

    public static void setAppearanceTime(double appearanceTime) {
        if (appearanceTime < 0) {
            throw new IllegalArgumentException("Appearance time must be greater than 0");
        }
        APPEARANCE_TIME = appearanceTime;
    }

    public static long getLiveTime() {
        return LIVE_TIME;
    }

    public static void setLiveTime(long liveTime) {
        if (liveTime < 0) {
            throw new IllegalArgumentException("Live time must be greater than 0");
        }
        LIVE_TIME = liveTime;
    }

    public WarriorAnt(int widthScene, int heightScene) {
        Random random = new Random();

        this.x = (int) (random.nextDouble() * (widthScene - IMAGE_WIDTH));
        this.y = (int) (random.nextDouble() * (heightScene - IMAGE_HEIGHT));
        this.deathTime = birthTime+LIVE_TIME;

        imageView = new ImageView(IMAGE);
        imageView.setLayoutX(this.x);
        imageView.setLayoutY(this.y);
        imageView.setFitWidth(IMAGE_WIDTH);
        imageView.setFitHeight(IMAGE_HEIGHT);
    }


}
