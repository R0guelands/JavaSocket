package br.com.up.server;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;   

public class ServerChat extends Thread {
    
    // Init the array that will receive the global array object
    private ArrayList <Socket> allConnections = new ArrayList<Socket>();

    // Constructor to receive the global array object
    public ServerChat(ArrayList<Socket> array) {
        this.allConnections= array;
    }

    // Main thread function for multithreading this part
    @Override
    public void run() {

        // Try catch because Json can be wrong or the socket can fail
        try {

            // Init the input line to client
            ObjectOutputStream inputToClient = null;

            // Init a scanner to read keyboard
            Scanner scanner = new Scanner(System.in);

            // Main server chat loop
            outer: while(true){

                // Waits here until admin types something into the chat
                String text = scanner.nextLine();

                if (text.equalsIgnoreCase("/help") || text.equalsIgnoreCase("/h")) {

                    System.out.println("Comandos disponíveis:\n'/help' & '/h' = Exibe este menu de ações\n'/online' = O servidor irá retornar quantas pessoas estão online no momento.");

                    continue outer;

                }

                if (text.equalsIgnoreCase("/online")) {

                    System.out.println("Pessoas online: " + allConnections.size());

                    continue outer;

                }

                // Loop to send the admins message to all clients
                for (Socket socket : allConnections) {

                    // Gets the current time to send via Json
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");  
                    LocalDateTime now = LocalDateTime.now();  

                    // Formats the message in Json rules for the project
                    JSONObject message = new JSONObject();
                    message.put("Identificador", "Admin");
                    message.put("Mensagem", text);
                    message.put("Data", dtf.format(now));

                    // Sends the message
                    inputToClient = new ObjectOutputStream(socket.getOutputStream());
                    inputToClient.writeObject(message.toString());

                }

            }
        
        }catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

}