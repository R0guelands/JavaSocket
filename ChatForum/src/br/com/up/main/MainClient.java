package br.com.up.main;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import br.com.up.client.ClientInit;
import org.json.JSONObject;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;   

public class MainClient {

    // Defines the server ip and port
    private static final String hostIP = "192.168.1.105";
    private static final int port = 1234;
    
    public static void main(String[] args) throws Exception {

        // Init the output to server line
        ObjectOutputStream inputToServer = null;

        // Init the socket to the serves ip and port
        Socket socket = new Socket(hostIP, port);

        // Init a scanner to read keyboard
        Scanner scanner = new Scanner(System.in);

        // Init the client thread part of the program
        ClientInit cliente = new ClientInit(socket);

        // Asks for the client name
        System.out.println("Digite seu nome: ");
        String nome = scanner.nextLine();

        // Init the thread to listen to server messages
        cliente.start();

        // Main client loop
        while (true) {

            // Gets the current time
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");  
            LocalDateTime now = LocalDateTime.now();  

            // Waits here ulti the user types a message
            String messageToServer = scanner.nextLine();

            // Formats the message
            JSONObject message = new JSONObject();
            message.put("Identificador", nome);
            message.put("Mensagem", messageToServer);
            message.put("Data", dtf.format(now));

            // Sends the message to server
            inputToServer = new ObjectOutputStream(socket.getOutputStream());
            inputToServer.writeObject(message.toString());

            // If the message is exit closes the connection
            if (messageToServer.equals("/exit")) {

                // Closes all parts of the connection
                inputToServer.close();
                socket.close();
                scanner.close();

                // Breaks the main loop
                break;

            }

        }
    }
}
