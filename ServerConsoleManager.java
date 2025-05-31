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
            command = scanner.nextLine().trim();

            if (command.isEmpty()) continue;

            String[] parts = command.split("\\s+", 2);
            String cmd = parts[0].toLowerCase();

            switch (cmd) {
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

                case "/kick":
                    if (parts.length < 2 || parts[1].isBlank()) {
                        System.out.println("❌ Uso: /kick <nombre_usuario>");
                    } else {
                        boolean kicked = server.kickUser(parts[1].trim());
                        if (kicked) {
                            System.out.println("✅ Usuario " + parts[1].trim() + " ha sido expulsado.");
                        } else {
                            System.out.println("❌ Usuario " + parts[1].trim() + " no encontrado.");
                        }
                    }
                    break;

                case "/broadcast":
                    if (parts.length < 2 || parts[1].isBlank()) {
                        System.out.println("❌ Uso: /broadcast <mensaje>");
                    } else {
                        String msg = "⚠️ [ADMIN]: " + parts[1].trim();
                        server.broadcast(msg);
                        System.out.println("✅ Mensaje enviado a todos.");
                    }
                    break;

                case "/historial":
                    if (ChatServer.messageHistory.isEmpty()) {
                        System.out.println("📭 No hay mensajes en el historial.");
                    } else {
                        System.out.println("📜 Historial de mensajes:");
                        for (String msg : ChatServer.messageHistory) {
                            System.out.println(msg);
                        }
                    }
                    break;

                default:
                    System.out.println("❓ Comando no reconocido. Usa: /usuarios, /cantidad, /kick <usuario>, /broadcast <mensaje>, /historial, /cerrar");
            }
        }
    }
}
