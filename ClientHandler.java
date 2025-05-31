import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final ChatServer server;
    private PrintWriter out;
    private BufferedReader in;
    private String clientName;

    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    public String getClientName() {
        return clientName;
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar conexión del cliente " + clientName + ": " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("📝 Ingresá tu nombre de usuario:");

            int attempts = 0;
            while (attempts < 3) {
                clientName = in.readLine();

                if (clientName == null || clientName.isBlank()) {
                    out.println("❌ El nombre no puede estar vacío. Intenta de nuevo:");
                } else if (server.isNameTaken(clientName, this)) {
                    out.println("❌ El nombre ya está en uso. Intenta con otro:");
                } else {
                    break; // Nombre válido y disponible
                }

                attempts++;
            }

            if (clientName == null || clientName.isBlank() || server.isNameTaken(clientName, this)) {
                clientName = "Anónimo" + (int)(Math.random() * 1000);
                out.println("⚠️ Se asignó un nombre automático: " + clientName);
            }

            server.broadcast("🟢 " + clientName + " se ha unido al chat.");

            if (!ChatServer.messageHistory.isEmpty()) {
                out.println("📜 Historial de mensajes:");
                for (String msg : ChatServer.messageHistory) {
                    out.println(msg);
                }
            }

            String message;
            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("/salir")) {
                    break;
                }

                String formatted = "💬 [" + clientName + "]: " + message;
                server.broadcast(formatted);
            }

        } catch (IOException e) {
            System.err.println("❌ Error con cliente " + clientName + ": " + e.getMessage());

        } finally {
            server.removeClient(this);
            server.broadcast("🔴 " + clientName + " ha salido del chat.");
            closeConnection();
        }
    }
}
