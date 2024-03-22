package server;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TcpServer {
    private static final int PORT = 12345;
    private static final Set<String> clientAddresses = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен на порту " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                String clientAddress = String.valueOf(clientSocket.getRemoteSocketAddress());
                clientAddresses.add(clientAddress);
                System.out.println("Клиент подключен: " + clientAddress);

                // Создаем новый поток для обработки подключения
                new Thread(() -> {
                    try {
                        handleClient(clientSocket);
                    } finally {
                        clientAddresses.remove(clientAddress);
                        System.out.println("Клиент отключен: " + clientAddress);
                    }
                }).start();
            }
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                switch (inputLine){
                    case "1":
                        sendClientList(out);
                        break;
                    case "2":
                        // TODO: 22.03.2024
                        break;
                }

                System.out.println("Получено сообщение от " + clientSocket.getRemoteSocketAddress() + ": " + inputLine + " " + System.currentTimeMillis());
            }
        } catch (IOException e) {
            System.out.println("Ошибка при обработке клиента: " + e.getMessage());
        }
    }

    private static void sendClientList(ObjectOutputStream out) throws IOException {
        out.writeObject(clientAddresses);
        out.flush();
    }
}
