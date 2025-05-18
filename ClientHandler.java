import java.io.*;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private CopyOnWriteArrayList<ClientHandler> clients;
    private String clientName;

    public ClientHandler(Socket socket, CopyOnWriteArrayList<ClientHandler> clients) {
        this.socket = socket;
        this.clients = clients;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Conectado al servidor. ¡Bienvenido al chat!");
        } catch (IOException e) {
            System.err.println("Error al crear streams: " + e.getMessage());
            closeEverything();
        }
    }

    @Override
    public void run() {
        try {
            // Nombre de usuario opcional (puede omitirse si no se desea)
            out.println("Ingresa tu nombre:");
            clientName = in.readLine();

            broadcast("🔵 " + clientName + " se ha unido al chat.");

            String message;
            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("/salir")) {
                    break;
                }
                broadcast("💬 " + clientName + ": " + message);
            }
        } catch (IOException e) {
            System.out.println("Cliente desconectado: " + clientName);
        } finally {
            broadcast("🔴 " + clientName + " ha salido del chat.");
            closeEverything();
        }
    }

    private void broadcast(String message) {
        for (ClientHandler client : clients) {
            if (client != this) {
                client.out.println(message);
            }
        }
    }

    private void closeEverything() {
        try {
            clients.remove(this);
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}
