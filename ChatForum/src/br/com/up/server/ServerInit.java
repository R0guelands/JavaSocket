package br.com.up.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.Socket;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;   
import org.json.JSONException;
import org.json.JSONObject;

public class ServerInit extends Thread{

    private Socket socket;
    private int firstMessage = 0;
    private ArrayList <Socket> allConnections = new ArrayList<Socket>();

    public ServerInit(Socket socket, ArrayList<Socket> array) {
        this.socket = socket;
        this.allConnections= array;
    }

    @Override
    public void run()  {

        ObjectOutputStream outputToClient = null;
        ObjectInputStream inputFromClient = null;

        try {

            while(true){

                if(firstMessage == 0){

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");  
                    LocalDateTime now = LocalDateTime.now();  

                    JSONObject message = new JSONObject();
                    message.put("Identificador", "Server");
                    message.put("Mensagem", "Seja bem vindo ao servidor. Pessoas online: " + allConnections.size());
                    message.put("Data", dtf.format(now));

                    outputToClient = new ObjectOutputStream(socket.getOutputStream());
                    outputToClient.writeObject(message.toString());

                    firstMessage++;

                }

                inputFromClient = new ObjectInputStream(socket.getInputStream());
                String stringFromClient = (String) inputFromClient.readObject();
                JSONObject objectFromClient = new JSONObject(stringFromClient);
                String nameFromClient = objectFromClient.getString("Identificador");
                String messageFromClient = objectFromClient.getString("Mensagem");
                String dateFromClient = objectFromClient.getString("Data");

                if(messageFromClient.equalsIgnoreCase("exit")) {
                    inputFromClient.close();
                    outputToClient.close();
                    socket.close();   
                    System.out.println("Client disconnected: " + socket.getInetAddress()); 
                    return;
                }

                System.out.println("{ " + dateFromClient + " } " + nameFromClient + ": " + messageFromClient);

                for(Socket s : allConnections){
                    if (s != socket) {

                        JSONObject message = new JSONObject();
                        message.put("Identificador", nameFromClient);
                        message.put("Mensagem", messageFromClient);
                        message.put("Data", dateFromClient);

                        outputToClient = new ObjectOutputStream(s.getOutputStream());
                        outputToClient.writeObject(message.toString());

                    }
                }

            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        } catch (JSONException e) {

            try {

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");  
                LocalDateTime now = LocalDateTime.now();  

                JSONObject message = new JSONObject();
                message.put("Identificador", "Server");
                message.put("Mensagem", "Formatacao incorreta. Por favor refira-se ao padrao selecionado para tentar enviar mensagens a este servidor.");
                message.put("Data", dtf.format(now));

                outputToClient = new ObjectOutputStream(socket.getOutputStream());
                outputToClient.writeObject(message.toString());

                outputToClient.close();
                inputFromClient.close();
                socket.close();   
                System.out.println("Client disconnected: " + socket.getInetAddress()); 
                return;

            } catch (JSONException | IOException exception) {
                System.out.println(exception);
            }
            
        }

    }

}
