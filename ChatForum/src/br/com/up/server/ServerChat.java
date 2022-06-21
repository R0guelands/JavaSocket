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
    
    private ArrayList <Socket> allConnections = new ArrayList<Socket>();

    public ServerChat(ArrayList<Socket> array) {
        this.allConnections= array;
    }

    @Override
    public void run() {

        try {

            ObjectOutputStream inputToClient = null;

            Scanner scanner = new Scanner(System.in);

            while(true){

                String text = scanner.nextLine();

                for (Socket socket : allConnections) {

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");  
                    LocalDateTime now = LocalDateTime.now();  

                    JSONObject message = new JSONObject();
                    message.put("Identificador", "Admin");
                    message.put("Mensagem", text);
                    message.put("Data", dtf.format(now));

                    inputToClient = new ObjectOutputStream(socket.getOutputStream());

                    inputToClient.writeObject(message.toString());

                }

            }

        }catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

}