import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserManager {
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public void addUser(ClientHandler client) {
        clients.add(client);
    }

    public void removeUser(ClientHandler client) {
        clients.remove(client);
    }

    public boolean isNameTaken(String name, ClientHandler requester) {
        for (ClientHandler c : clients) {
            if (c != requester && name.equalsIgnoreCase(c.getClientName())) {
                return true;
            }
        }
        return false;
    }

    public List<ClientHandler> getClients() {
        return clients;
    }

    public int getUserCount() {
        return clients.size();
    }

    public void listUsers() {
        if (clients.isEmpty()) {
            System.out.println("📭 No hay usuarios conectados.");
        } else {
            System.out.println("👥 Usuarios conectados:");
            for (ClientHandler client : clients) {
                System.out.println(" - " + client.getClientName());
            }
        }
    }
}

