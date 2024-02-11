package application.models.implement;

import application.models.AbstractAnt;
import application.models.IBehaviour;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public class WorkerAnt extends AbstractAnt implements IBehaviour {
    public static final float APPEARANCE_CHANCE = 0.9f;
    public static final float APPEARANCE_TIME = 1f;
    public static final Image IMAGE = new Image("application/view/workerAnt.png");
    public static final float IMAGE_WIDTH = 30f;
    public static final float IMAGE_HEIGHT = 30f;
    public WorkerAnt(int widthScene, int heightScene) {
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
