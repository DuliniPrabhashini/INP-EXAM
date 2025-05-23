package lk.ijse.gdse72;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            Socket socket = new Socket("localhost", 3000);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            System.out.println(input.readUTF());
            String name = scanner.nextLine();
            output.writeUTF(name);

            new Thread(() -> {
                try {
                    while (true) {
                        String response = input.readUTF();
                        System.out.println(response);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            }).start();

            while (true) {
                String msg = scanner.nextLine();
                output.writeUTF(msg);
            }

        } catch (IOException e) {
            System.out.println("Connection Error: " + e.getMessage());
        }
    }
}

