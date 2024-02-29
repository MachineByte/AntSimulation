package application.models.data;

import javafx.scene.image.ImageView;

public abstract class AbstractAnt extends BaseAI{
    public double x;
    public double y;

    public long id;

    public long birthTime;
    public long deathTime;
    public ImageView imageView;

    public double getX() {
        return x;
    }

    public double getY() {
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

    protected synchronized void move() throws InterruptedException {

    }

}
