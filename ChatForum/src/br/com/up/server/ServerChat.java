package br.com.up.server;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

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

                String message = scanner.nextLine();

                for (Socket socket : allConnections) {

                    inputToClient = new ObjectOutputStream(socket.getOutputStream());

                    inputToClient.writeObject("Admin: " + message);

                }

            }

        }catch (IOException e) {
            e.printStackTrace();
        }

    }

}