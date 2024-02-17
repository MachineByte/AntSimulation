package application.models.data.implement;

import application.models.data.AbstractAnt;
import application.models.data.IBehaviour;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public class WarriorAnt extends AbstractAnt implements IBehaviour {
    public static double APPEARANCE_CHANCE = 1d;
    public static double APPEARANCE_TIME = 100d;
    public static final Image IMAGE = new Image("application/controllers/warriorAnt.png");
    public static float IMAGE_WIDTH = 30f;
    public static final float IMAGE_HEIGHT = 30f;

    public WarriorAnt(int widthScene, int heightScene) {
        Random random = new Random();

        this.x = (int) (random.nextDouble() * (widthScene - IMAGE_WIDTH));
        this.y = (int) (random.nextDouble() * (heightScene - IMAGE_HEIGHT));

        imageView = new ImageView(IMAGE);
        imageView.setLayoutX(this.x);
        imageView.setLayoutY(this.y);
        imageView.setFitWidth(IMAGE_WIDTH);
        imageView.setFitHeight(IMAGE_HEIGHT);
    }


}
