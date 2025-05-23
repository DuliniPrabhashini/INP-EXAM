package lk.ijse.gdse72;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final Set<DataOutputStream> clientOutputStreams = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        System.out.println("Server started !");
        try (ServerSocket serverSocket = new ServerSocket(3000)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static class ClientHandler extends Thread {
        private final Socket socket;
        private DataInputStream input;
        private DataOutputStream output;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
                clientOutputStreams.add(output);

                output.writeUTF("Enter your name:");
                clientName = input.readUTF();
                broadcast(clientName + " joined !");

                String message;
                while ((message = input.readUTF()) != null) {
                    broadcast(clientName + ": " + message);
                }

            } catch (IOException e) {
                System.out.println(clientName + " disconnected.");
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {}
                clientOutputStreams.remove(output);
                broadcast(clientName + " leaved");
            }
        }

        private void broadcast(String message) {
            synchronized (clientOutputStreams) {
                for (DataOutputStream out : clientOutputStreams) {
                    try {
                        out.writeUTF(message);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}

