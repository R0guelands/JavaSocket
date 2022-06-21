package br.com.up.main;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import br.com.up.client.ClientInit;
import org.json.JSONObject;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;   

public class MainClient {

    private static final String hostIP = "localhost";
    private static final int port = 9876;

    public static void main(String[] args) throws Exception {

        ObjectOutputStream inputToServer = null;
        Socket socket = new Socket(hostIP, port);
        Scanner scanner = new Scanner(System.in);
        ClientInit cliente = new ClientInit(socket);

        System.out.println("Digite seu nome: ");
        String nome = scanner.nextLine();

        cliente.start();

        while (true) {

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");  
            LocalDateTime now = LocalDateTime.now();  

            inputToServer = new ObjectOutputStream(socket.getOutputStream());

            String messageToServer = scanner.nextLine();

            JSONObject message = new JSONObject();
            message.put("Identificador", nome);
            message.put("Mensagem", messageToServer);
            message.put("Data", dtf.format(now));

            inputToServer.writeObject(message.toString());

            if (messageToServer.equals("exit")) {

                inputToServer.close();
                socket.close();
                scanner.close();

                break;

            }

        }
    }
}
