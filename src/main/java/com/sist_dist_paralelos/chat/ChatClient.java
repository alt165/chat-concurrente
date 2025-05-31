package com.sist_dist_paralelos.chat;

import java.io.*;
import java.net.*;

public class ChatClient {

    private static final String SERVER_ADDRESS = "localhost"; // Cambia si el servidor está en otra IP
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        ) {
            // Hilo para recibir mensajes del servidor
            Thread receiver = new Thread(() -> {
                try {
                    String incoming;
                    while ((incoming = in.readLine()) != null) {
                        System.out.println(incoming);
                    }
                } catch (IOException e) {
                    System.out.println("Conexión cerrada por el servidor.");
                }
            });
            receiver.start();

            // Enviar mensajes al servidor
            String message;
            while ((message = console.readLine()) != null) {
                out.println(message);
                if (message.equalsIgnoreCase("/salir")) {
                    break;
                }
            }

            socket.close();
            System.exit(0);

        } catch (IOException e) {
            System.err.println("No se pudo conectar al servidor: " + e.getMessage());
        }
    }
}
