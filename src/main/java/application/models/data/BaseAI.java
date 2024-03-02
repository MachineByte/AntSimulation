package application.models.data;

public abstract class BaseAI implements Runnable {
    protected boolean running = true;

    protected boolean interapted = false;
    public Thread thread;

    public  Object object = new Object();

    public BaseAI() {
        thread = new Thread(this);
        thread.start();
        thread.setPriority(Thread.MIN_PRIORITY);
    }

    @Override
    public void run() {
        while (true) {
            synchronized (object) {
                try {
                    move();
                    Thread.sleep(25);
                    if(interapted){
                        thread.interrupt();
                        break;
                    }
                    while (!running) {
                            object.wait();
                    }
                } catch (InterruptedException e) {
                    // Обработка прерывания потока
                    e.printStackTrace();
                }
            }
        }
        System.out.println("rth");
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
        interapted = true;
    }

//    public void С() throws InterruptedException {
//        synchronized (object) {
//            object.;
//        }
//    }

}
