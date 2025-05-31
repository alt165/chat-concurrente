import java.util.Scanner;

public class ServerConsoleManager implements Runnable {

    private final ChatServer server;

    public ServerConsoleManager(ChatServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String command;

        while (true) {
            command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "/usuarios":
                    server.listConnectedUsers();
                    break;

                case "/cantidad":
                    System.out.println("👥 Usuarios conectados: " + server.getConnectedUserCount());
                    break;

                case "/cerrar":
                    System.out.println("⛔ Cerrando servidor...");
                    server.shutdownServer();
                    return;

                default:
                    System.out.println("❓ Comando no reconocido. Usa: /usuarios, /cantidad, /cerrar");
            }
        }
    }
}
