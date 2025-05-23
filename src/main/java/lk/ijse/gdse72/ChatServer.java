package lk.ijse.gdse72;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;

public class ChatServer {

    private static String upTime;

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(3000);
                upTime = LocalTime.now().toString();

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new ClientHandler(clientSocket).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static class ClientHandler extends Thread {
        private Socket clientSocket;
        private DataInputStream in;
        private DataOutputStream out;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());

                while (true) {
                    String message = in.readUTF();

                    switch (message) {
                        case "TIME":
                            out.writeUTF(LocalTime.now().toString());
                            break;
                        case "DATE":
                            out.writeUTF(LocalDate.now().toString());
                            break;
                        case "UPTIME":
                            out.writeUTF(upTime);
                            break;
                        case "BYE":
                            clientSocket.close();
                            return;
                        case "HELP":
                            out.writeUTF("Available commands: TIME, DATE, UPTIME, BYE");
                            break;
                        default:
                            out.writeUTF("Echo: " + message);
                    }
                    out.flush();
                }
            } catch (IOException e) {
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}