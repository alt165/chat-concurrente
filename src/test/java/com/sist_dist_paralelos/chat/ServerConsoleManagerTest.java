package com.sist_dist_paralelos.chat;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ServerConsoleManagerTest {

    @Test
    void testCommands() throws Exception {
        String input = "/usuarios\n/cantidad\n/comando_invalido\n/cerrar\n";
        InputStream sysInBackup = System.in;
        PrintStream sysOutBackup = System.out;

        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();

        System.setIn(testIn);
        System.setOut(new PrintStream(testOut));

        ChatServer mockServer = mock(ChatServer.class);
        doNothing().when(mockServer).listConnectedUsers();
        doNothing().when(mockServer).shutdownServer();

        ServerConsoleManager manager = new ServerConsoleManager(mockServer);
        manager.run();

        System.setIn(sysInBackup);
        System.setOut(sysOutBackup);

        String output = testOut.toString();
        assertTrue(output.contains("Usuarios conectados") || output.contains("No hay usuarios conectados"));
        assertTrue(output.contains("Comando no reconocido"));
        verify(mockServer).listConnectedUsers();
        verify(mockServer).shutdownServer();
    }
}
