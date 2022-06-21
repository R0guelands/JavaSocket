package br.com.up.mainServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import br.com.up.server.ServerChat;
import br.com.up.server.Server;

public class MainServer {
    public static void main(String[] args) throws Exception {

        int port = 9876;

        ServerSocket server = new ServerSocket(port);

        ArrayList<Socket> allConnections = new ArrayList<Socket>();

        ServerChat servidorChat = new ServerChat(allConnections);

        servidorChat.start();

        while (true) {
            Socket socket = server.accept();
            allConnections.add(socket);
            System.out.println("Client connected : " + socket.getInetAddress());
            Server session = new Server(socket, allConnections);
            session.start();
        }

    }
}
