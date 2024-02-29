package application.models.data;

public abstract class BaseAI {
    protected Thread thread = new Thread(() -> {
        while (true) {
                try {
                    isAlive();
                    move();
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
        }
    });

    protected synchronized void move() throws InterruptedException {
        // Implementation of move method
    }

    protected synchronized void isAlive() throws InterruptedException {

    }
}
