package application.models.data;

import javafx.scene.image.ImageView;

public abstract class AbstractAnt {
    public int x;
    public int y;

    public long id;

    public long birthTime;
    public long deathTime;
    public ImageView imageView;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public long getId() {
        return id;
    }

    public long getBirthTime() {
        return birthTime;
    }

    public long getDeathTime() {
        return deathTime;
    }


}
