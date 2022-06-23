package br.com.up.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientInit extends Thread {

    // Gets the socket for this connection
    private Socket socket;

    // Constructor to get the socket
    public ClientInit(Socket socket) {

        this.socket = socket;

    }

    // Function for multithreading
    @Override
    public void run() {

        // Try catch because Json can be wrong or fail, socket can fail
        try {
            
            // Init the object to read messages from server
            ObjectInputStream outputFromServer = null;

            // Prints to the user that the request was made
            System.out.println("Sending request to Socket Server");

            // Main loop for the thread
            while (true) {

                // Waits here until the serves sends something
                outputFromServer = new ObjectInputStream(socket.getInputStream());

                // Gets the string from the server
                String stringFromServer = (String) outputFromServer.readObject();

                // Gets the Json from the string and reads its parts
                JSONObject objectFromServer = new JSONObject(stringFromServer);
                String messageFromServer = objectFromServer.getString("Mensagem");
                String dateFromServer = objectFromServer.getString("Data");
                String nameFromServer = objectFromServer.getString("Identificador");                

                // Prints the message from the server to the client
                System.out.println("\n{ " + dateFromServer + " } " + nameFromServer + ": " + messageFromServer + "\n");

            }

        } catch (IOException | ClassNotFoundException | JSONException e) {

            System.out.println(e);

        }

    }
}