package application.models.data;

import javafx.application.Platform;

public abstract class BaseAI implements Runnable {
    protected boolean running = true;
    public Thread thread;

    public BaseAI() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            while(running){
                try {
                    move();
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    // Обработка прерывания потока
                    e.printStackTrace();
                }
            }
        }
    }

    protected abstract void move() throws InterruptedException;

    public void A() throws InterruptedException {
        running = false;
    }

    public void B() throws InterruptedException {
        running = true;
    }
}
