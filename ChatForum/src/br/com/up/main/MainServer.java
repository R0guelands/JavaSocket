package br.com.up.main;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import br.com.up.server.ServerChat;
import br.com.up.server.ServerInit;

public class MainServer {

    // Port for the server connection
    private static final int port = 1234;

    public static void main(String[] args) throws Exception {

        // Init the serverSocket
        ServerSocket server = new ServerSocket(port);

        // Init the main array that will keep all sockets globally
        ArrayList<Socket> allConnections = new ArrayList<Socket>();

        // Get's the server ip. If it's 0.0.0.0 than it will accept all connections
        InetAddress inet = server.getInetAddress();
        System.out.println("HostAddress="+inet.getHostAddress());

        // Init the chat part for the server. This allows the server to type as 'admin' in the chat
        ServerChat servidorChat = new ServerChat(allConnections);
        servidorChat.start(); // Starts the first thread for the server chat

        // Main server loop that will accept connections and creat threads for each client
        while (true) {

            // Stops here and wiats for a client to request a connection
            Socket socket = server.accept();

            // When a connection is accepted, it stores the socket in the array
            allConnections.add(socket);

            // Prints to the admin that a connection whas made
            System.out.println("Client connected: " + socket.getInetAddress());

            // Starts the session for the client
            ServerInit session = new ServerInit(socket, allConnections);

            // Init the thread that will take care of this session
            session.start();

            // Counts and shows how many threads are active. 1 for the main program, 1 for the server chat and x for how many clients are connected
            int threads = java.lang.Thread.activeCount();
            System.out.println("Threads: " + threads); 

        }

    }
}
