package com.sist_dist_paralelos.chat;

import com.sist_dist_paralelos.chat.ClientHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientHandlerTest {

    private Socket mockSocket;
    private ChatServer mockServer;
    private BufferedReader mockIn;
    private PrintWriter mockOut;

    @BeforeEach
    void setUp() throws IOException {
        mockSocket = mock(Socket.class);
        mockServer = mock(ChatServer.class);

        // Simulamos InputStream y OutputStream para BufferedReader y PrintWriter
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream(pos);

        mockIn = new BufferedReader(new InputStreamReader(pis));
        mockOut = mock(PrintWriter.class);

        when(mockSocket.getInputStream()).thenReturn(pis);
        when(mockSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());
    }

    @Test
    void testGetClientNameInitiallyNull() {
        ClientHandler handler = new ClientHandler(mockSocket, mockServer);
        assertNull(handler.getClientName());
    }

    @Test
    void testSendMessageCallsPrintWriter() throws IOException {
        ClientHandler handler = new ClientHandler(mockSocket, mockServer);
        handler.out = mockOut;

        String msg = "hola";
        handler.sendMessage(msg);

        verify(mockOut).println(msg);
    }

    @Test
    void testCloseConnectionClosesSocket() throws IOException {
        ClientHandler handler = new ClientHandler(mockSocket, mockServer);
        handler.closeConnection();

        verify(mockSocket).close();
    }
}
