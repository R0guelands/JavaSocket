package br.com.up.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientSocket extends Thread {

    private Socket socket;

    public ClientSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {

            ObjectInputStream outputFromServer = null;
            System.out.println("Sending request to Socket Server");

            while (true) {

                outputFromServer = new ObjectInputStream(socket.getInputStream());
                String messageFromServer = (String) outputFromServer.readObject();
                System.out.println(messageFromServer);

            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }

    }
}