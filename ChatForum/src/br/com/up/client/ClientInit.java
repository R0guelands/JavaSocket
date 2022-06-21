package br.com.up.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientInit extends Thread {

    private Socket socket;

    public ClientInit(Socket socket) {

        this.socket = socket;

    }

    @Override
    public void run() {

        try {

            ObjectInputStream outputFromServer = null;
            System.out.println("Sending request to Socket Server");

            while (true) {

                outputFromServer = new ObjectInputStream(socket.getInputStream());
                String stringFromServer = (String) outputFromServer.readObject();
                JSONObject objectFromServer = new JSONObject(stringFromServer);
                String messageFromServer = objectFromServer.getString("Mensagem");
                String dateFromServer = objectFromServer.getString("Data");
                String nameFromServer = objectFromServer.getString("Identificador");                

                System.out.println("\n{ " + dateFromServer + " } " + nameFromServer + ": " + messageFromServer + "\n");

            }

        } catch (IOException | ClassNotFoundException | JSONException e) {

            System.out.println(e);

        }

    }
}