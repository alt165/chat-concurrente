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

            // Inicia el hilo de consola de administración
            Thread consoleThread = new Thread(new ServerConsoleManager(this));
            consoleThread.start();

            // Bucle principal del servidor
            while (isRunning) {
                Socket clientSocket = serverSocket.accept();

                if (clientHandlers.size() >= MAX_USERS) {
                    System.out.println("🚫 Nuevo cliente rechazado: límite máximo de usuarios alcanzado.");
                    clientSocket.close();
                    continue;
                }

                ClientHandler handler = new ClientHandler(clientSocket, this);
                clientHandlers.add(handler);
                new Thread(handler).start();

                System.out.println("🔌 Cliente conectado. Total: " + clientHandlers.size());
            }

        } catch (IOException e) {
            if (isRunning) {
                System.err.println("❌ Error en el servidor: " + e.getMessage());
            } else {
                System.out.println("✅ Servidor cerrado correctamente.");
            }
        }
    }

    // Enviar mensaje a todos los clientes
    public void broadcast(String message) {
        messageHistory.offer(message); // Guardar en historial
        if (messageHistory.size() > MAX_HISTORY) {
            messageHistory.poll(); // Mantener tamaño máximo
        }

        for (ClientHandler client : clientHandlers) {
            client.sendMessage(message);
        }
    }

    // Eliminar un cliente del sistema
    public void removeClient(ClientHandler client) {
        clientHandlers.remove(client);
        System.out.println("🔌 Cliente desconectado. Total: " + clientHandlers.size());
    }

    // Listar usuarios conectados
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

    // Contar usuarios conectados
    public int getConnectedUserCount() {
        return clientHandlers.size();
    }

    // Expulsar un usuario por nombre
    public boolean kickUser(String username) {
        for (ClientHandler client : clientHandlers) {
            if (client.getClientName().equalsIgnoreCase(username)) {
                client.sendMessage("⚠️ Has sido expulsado del chat por el administrador.");
                client.closeConnection();
                removeClient(client);
                broadcast("⚠️ Usuario '" + username + "' fue expulsado del chat.");
                return true;
            }
        }
        return false; // Usuario no encontrado
    }

    // Apagar el servidor desde la consola
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

    // Método principal
    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.start(PORT);
    }
}
