package application.models;

import java.io.*;
import java.net.Socket;

public class TcpConnection {
    private static volatile TcpConnection instance;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final Thread listenerThread;
    private volatile boolean running = true;
    private final String serverAddress = "127.0.0.1"; // Адрес сервера
    private final int port = 12345; // Порт сервера
    private TcpConnection() {
        listenerThread = new Thread(() -> {
            while (running) {
                try {
                    if (socket == null || socket.isClosed()) {
                        // Попытка подключения к серверу
                        connectToServer();
                    }

                    // Цикл для чтения входящих сообщений
                    if (in.ready()) {
                        String receivedMessage = in.readLine();
                        System.out.println("Получено сообщение: " + receivedMessage);
                        // Обработка полученного сообщения
                    } else {
                        // Небольшая задержка, чтобы избежать чрезмерного использования CPU
                        Thread.sleep(1000);
                    }
                } catch (IOException e) {
                    System.out.println("Ошибка соединения. Попытка переподключения...");
                    closeResources(); // Закрытие текущего соединения
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Поток прерван.");
                }
            }

            closeResources(); // Закрытие ресурсов при завершении работы
        });
        listenerThread.start();
    }

    private void connectToServer() throws IOException {
        socket = new Socket(serverAddress, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Успешное подключение к серверу.");
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

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public void closeConnection() {
        running = false;
        listenerThread.interrupt();
        closeResources();
    }

    private void closeResources() {
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
}
