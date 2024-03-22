package server;

import java.io.*;
import java.net.*;
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
                String clientAddress = clientSocket.getInetAddress().getHostAddress();
                clientAddresses.add(clientAddress);
                System.out.println("Клиент подключен: " + clientAddress);

                // Создаем новый поток для обработки подключения
                new Thread(() -> {
                    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                    try {
                        // Запускаем планировщик для отправки списка клиентов каждые 500 мс
//                        scheduler.scheduleAtFixedRate(() -> sendClientList(clientSocket), 0, 500, TimeUnit.MILLISECONDS);

                        handleClient(clientSocket);
                    } finally {
//                        scheduler.shutdown(); // Остановка планировщика при отключении клиента
                        clientAddresses.remove(clientAddress);
                        System.out.println("Клиент отключен: " + clientAddress);
                    }
                }).start();
            }
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Получено сообщение от " + clientSocket.getInetAddress().getHostAddress() + ": " + inputLine);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при обработке клиента: " + e.getMessage());
        }
    }

    private static void sendClientList(Socket clientSocket) {
        String clientAddress = clientSocket.getInetAddress().getHostAddress();
        String clientList = getClientListExcluding(clientAddress);

        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            out.println(clientList);
        } catch (IOException e) {
            System.out.println("Не удалось отправить список клиентов: " + e.getMessage());
        }
    }

    private static String getClientListExcluding(String excludeAddress) {
        StringBuilder sb = new StringBuilder();
        for (String addr : clientAddresses) {
            if (!addr.equals(excludeAddress)) {
                sb.append(addr).append("\n");
            }
        }
        return sb.toString();
    }
}
