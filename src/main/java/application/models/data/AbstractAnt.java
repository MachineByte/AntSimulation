package application.models.data;

import javafx.scene.image.ImageView;

public abstract class AbstractAnt {
    public int x;
    public int y;

    public long birthTime = System.currentTimeMillis();
    public long deathTime;
    public ImageView imageView;

}
