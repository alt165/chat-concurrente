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

        printHelp(); // Mostrar comandos al inicio

        while (true) {
            System.out.print("> ");
            command = scanner.nextLine().trim();

            if (command.isEmpty()) continue;

            String[] parts = command.split(" ", 2);
            String cmd = parts[0].toLowerCase();
            String args = parts.length > 1 ? parts[1].trim() : "";

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
                    scanner.close();
                    return;

                case "/ayuda":
                    printHelp();
                    break;

                case "/broadcast":
                    if (args.isEmpty()) {
                        System.out.println("⚠️ Uso: /broadcast <mensaje>");
                    } else {
                        String systemMessage = "📢 [Sistema]: " + args;
                        server.broadcast(systemMessage);
                        System.out.println("✅ Mensaje enviado.");
                    }
                    break;

                case "/kick":
                    if (args.isEmpty()) {
                        System.out.println("⚠️ Uso: /kick <nombre_usuario>");
                    } else {
                        boolean kicked = server.kickUser(args);
                        if (kicked) {
                            System.out.println("✅ Usuario '" + args + "' desconectado.");
                        } else {
                            System.out.println("⚠️ Usuario no encontrado.");
                        }
                    }
                    break;

                case "/historial":
                    System.out.println("📜 Últimos mensajes:");
                    for (String msg : ChatServer.messageHistory) {
                        System.out.println(msg);
                    }
                    break;

                default:
                    System.out.println("❓ Comando no reconocido. Usa /ayuda para ver los comandos.");
            }
        }
    }

    private void printHelp() {
        System.out.println("""
        📖 Comandos disponibles:
         /usuarios              - Lista usuarios conectados
         /cantidad              - Muestra cantidad de usuarios
         /broadcast <mensaje>   - Envia mensaje global como sistema
         /kick <usuario>        - Expulsa un usuario del chat
         /historial             - Muestra el historial de mensajes
         /cerrar                - Cierra el servidor
         /ayuda                 - Muestra esta ayuda
        """);
    }
}
