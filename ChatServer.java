import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;

public class ChatServer {

    private static final int MAX_USERS = 100;
    private static final int PORT = 12345;
    private static final int MAX_HISTORY = 100;

    private boolean isRunning = true;
    protected ServerSocket serverSocket;
    protected final List<ClientHandler> clientHandlers = new CopyOnWriteArrayList<>();
    public static final LinkedBlockingDeque<String> messageHistory = new LinkedBlockingDeque<>(MAX_HISTORY);

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("✅ Servidor iniciado en el puerto " + port);

            // Hilo de consola
            Thread consoleThread = new Thread(new ServerConsoleManager(this));
            consoleThread.start();

            // Aceptar clientes
            while (isRunning) {
                Socket clientSocket = serverSocket.accept();

                if (clientHandlers.size() >= MAX_USERS) {
                    System.out.println("🚫 Cliente rechazado: límite máximo alcanzado.");
                    clientSocket.close();
                    continue;
                }

                ClientHandler handler = new ClientHandler(clientSocket, this);
                clientJoined(handler); // <- aquí se usa el nuevo método
                new Thread(handler).start();
            }

        } catch (IOException e) {
            if (isRunning) {
                System.err.println("❌ Error en el servidor: " + e.getMessage());
            } else {
                System.out.println("✅ Servidor cerrado correctamente.");
            }
        }
    }

    // ✅ Nuevo método
    public void clientJoined(ClientHandler client) {
        clientHandlers.add(client);
        System.out.println("🔌 Cliente conectado. Total: " + clientHandlers.size());
    }

    public void broadcast(String message) {
        messageHistory.offer(message);
        for (ClientHandler client : clientHandlers) {
            client.sendMessage(message);
        }
    }

    public void removeClient(ClientHandler client) {
        clientHandlers.remove(client);
        System.out.println("🔌 Cliente desconectado. Total: " + clientHandlers.size());
    }

    public void listConnectedUsers() {
        if (clientHandlers.isEmpty()) {
            System.out.println("📭 No hay usuarios conectados.");
        } else {
            System.out.println("👥 Usuarios conectados:");
            for (ClientHandler client : clientHandlers) {
                System.out.println(" - " + client.getClientName());
            }
        }
    }
    // Expulsar a un usuario por nombre
    public boolean kickUser(String clientName) {
        for (ClientHandler client : clientHandlers) {
            if (client.getClientName().equalsIgnoreCase(clientName)) {
                client.sendMessage("⚠️ Has sido expulsado por el administrador.");
                client.closeConnection();
                removeClient(client);
                broadcast("⚠️ Usuario " + clientName + " fue expulsado.");
                return true;
            }
        }
        return false;
    }


    public int getConnectedUserCount() {
        return clientHandlers.size();
    }

    public void shutdownServer() {
        isRunning = false;

        try {
            for (ClientHandler client : clientHandlers) {
                client.sendMessage("⚠️ El servidor se está cerrando. Adiós.");
                client.closeConnection();
            }

            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }

            System.exit(0);

        } catch (IOException e) {
            System.err.println("❌ Error al cerrar el servidor: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.start(PORT);
    }
}
