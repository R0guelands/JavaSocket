package br.com.up.main;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import br.com.up.client.ClientSocket;


public class MainClient {
    public static void main(String[] args) throws Exception {

        ObjectOutputStream inputToServer = null;
        Socket socket = new Socket("192.168.18.5", 9876);
        Scanner scanner = new Scanner(System.in);
        ClientSocket cliente = new ClientSocket(socket);

        cliente.start();

        while (true) {

            inputToServer = new ObjectOutputStream(socket.getOutputStream());
            String messageToServer = scanner.nextLine();
            inputToServer.writeObject(messageToServer);

            if (messageToServer.equals("exit")) {
                inputToServer.close();
                socket.close();
                scanner.close();
                break;
            }

        }
    }
}
