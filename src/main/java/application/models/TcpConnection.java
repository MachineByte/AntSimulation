package application.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class TcpConnection {
    private static volatile TcpConnection instance;
    private Socket socket;
    private PrintWriter out;
    private ObjectInputStream in;
    private Thread listenerThread;

    private final Thread senderThread;
    private volatile boolean listenerRunning = true;
    private volatile boolean senderRunning = true;
    private final String serverAddress = "127.0.0.1"; // Адрес сервера
    private final int port = 12345; // Порт сервера
    private Set<String> connectionsSet = new HashSet<>();
    AtomicInteger connectionAttemptCounter = new AtomicInteger();

    private TcpConnection() {
        senderThread = new Thread(() -> {
            // Цикл для чтения входящих сообщений
            while (senderRunning) {
                try {
                    if (socket == null || socket.isClosed() || connectionAttemptCounter.get() > 5) {
                        closeResources();
                        connectToServer(); // Попытка подключения к серверу
                        connectionAttemptCounter.set(0);
                    }

                    out.println("1");
                    connectionAttemptCounter.getAndIncrement();
                    Thread.sleep(1000); // Небольшая задержка, чтобы избежать чрезмерного использования CPU
//                    System.out.println(connectionAttemptCounter+"!");

                } catch (IOException e) {
                    System.out.println("Ошибка соединения. Попытка переподключения...");
                    try {
                        closeResources(); // Закрытие текущего соединения
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                closeResources(); // Закрытие ресурсов при завершении работы
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        senderThread.start();
    }

    private void connectToServer() throws IOException {
        socket = new Socket(serverAddress, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new ObjectInputStream(socket.getInputStream());
        System.out.println("Успешное подключение к серверу.");
        listenerThread = new Thread(() -> {
            while(listenerRunning){
                Object obj = null;
                try {
                    obj = in.readObject();
                    connectionsSet.addAll(((Set<String>) obj));
                    System.out.println("Получено сообщение: " + connectionsSet + " " + System.currentTimeMillis());
                    connectionAttemptCounter.set(0);
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Ошибка при чтении");
                    listenerRunning =  false;
                }
            }
        });
        listenerThread.start();
        listenerThread.interrupt();
    }


    public static TcpConnection getInstance() {
        if (instance == null) {
            synchronized (TcpConnection.class) {
                if (instance == null) {
                    instance = new TcpConnection();
                }
            }
        }
        return instance;
    }


    private void closeResources() throws InterruptedException {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket = null;
        out = null;
        in = null;
    }

    public String getIp() {
        try {
            return socket.getLocalSocketAddress().toString();
        } catch (NullPointerException e) {
            return null;
        }

    }
}
