package br.com.up.main;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import br.com.up.server.ServerChat;
import br.com.up.server.ServerInit;

public class MainServer {

    private static final int port = 9876;

    public static void main(String[] args) throws Exception {

        ServerSocket server = new ServerSocket(port);
        ArrayList<Socket> allConnections = new ArrayList<Socket>();

        ServerChat servidorChat = new ServerChat(allConnections);
        servidorChat.start();

        while (true) {

            Socket socket = server.accept();
            allConnections.add(socket);
            System.out.println("Client connected: " + socket.getInetAddress());
            ServerInit session = new ServerInit(socket, allConnections);
            session.start();
            int threads = java.lang.Thread.activeCount();
            System.out.println("Threads: " + threads); 

        }

    }
}
