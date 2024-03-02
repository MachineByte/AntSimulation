package application.models.data;

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
            synchronized (object) {
                try {
                    move();
                    Thread.sleep(25);
                    while (!running) {
                            object.wait();
                    }
                } catch (InterruptedException e) {
                    // Обработка прерывания потока
                    e.printStackTrace();
                }
            }
            System.out.println("rth");
        }
    }

    protected abstract void move() throws InterruptedException;

    public void A() throws InterruptedException {
        running = false;
    }

    public void B() throws InterruptedException {
        if(!running) {
            synchronized (object) {
                running = true;
                object.notify();
            }
        }
    }

    public void C() throws InterruptedException {
        running = false;
        interrupted = true;
    }

//    public void С() throws InterruptedException {
//        synchronized (object) {
//            object.;
//        }
//    }

}
