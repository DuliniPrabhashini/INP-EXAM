package lk.ijse.gdse72;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        try{
            Socket socket = new Socket("localhost", 3000);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in);

            String massage="";
            while (true){
                System.out.print("Enter your massage: ");
                massage = scanner.nextLine();
                out.writeUTF(massage);
                out.flush();

                String response = in.readUTF();

                if(response.equals("exit")){
                    System.out.println("You have been exited");
                    break;
                }else {
                    System.out.println("Server response : "+response);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
    }
}