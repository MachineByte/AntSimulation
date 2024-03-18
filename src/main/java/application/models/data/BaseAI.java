package application.models.data;

import java.io.Serializable;

public abstract class BaseAI implements Runnable {
    protected boolean running = true;

    protected boolean interrupted = false;
    public Thread thread;

    public  Object object = new Object();

    public BaseAI() {
        thread = new Thread(this);
        thread.start();
        thread.setPriority(Thread.MIN_PRIORITY);
    }

    @Override
    public void run() {
        while (!interrupted) {
            synchronized (this) {
                try {
                    move();
                    Thread.sleep(25);
                    while (!running) {
                            this.wait();
                    }
                } catch (InterruptedException e) {
                    // Обработка прерывания потока
                    e.printStackTrace();
                }
            }
        }
    }

    protected abstract void move() throws InterruptedException;

    public void waitThread() throws InterruptedException {
        running = false;
    }

    public  void notifyThread() throws InterruptedException {
        if(!running) {
            synchronized (this) {
                running = true;
                this.notify();
            }
        }
    }

    public void killThread() throws InterruptedException {
        interrupted = true;
    }

    public void setPriority(int newPriority) {
        thread.setPriority(newPriority);
    }
}
