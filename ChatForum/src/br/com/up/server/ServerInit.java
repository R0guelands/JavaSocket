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

import br.com.up.image.ImageToASCII;

public class ServerInit extends Thread {

    // Receives the socket for that client
    private Socket socket;

    // Variable to check for link if the next message is expected to be an image
    private int image = 0;

    // Variable to send hello form server if this is the first time that clients
    // connects to server
    private int firstMessage = 0;

    // Init the array that will receive global array object
    private ArrayList<Socket> allConnections = new ArrayList<Socket>();

    // Constructor to receive the socket for that client and global array object
    public ServerInit(Socket socket, ArrayList<Socket> array) {

        this.socket = socket;
        this.allConnections = array;

    }

    // Multithreading funciton
    @Override
    public void run() {

        // Init the output and input lines with that socket
        ObjectOutputStream outputToClient = null;
        ObjectInputStream inputFromClient = null;

        // Try chatch because the client can disconnect the wrong way, the client can
        // send the wrong json format, the json can fail or the socket can fail
        try {

            // Main loop for the thread
            while (true) {

                // If this is the first time a client is connected this will send the hello from
                // server
                if (firstMessage == 0) {

                    // Gets the current time
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    LocalDateTime now = LocalDateTime.now();

                    // Formats the message in Json rules to the project
                    JSONObject message = new JSONObject();
                    message.put("Identificador", "Server");
                    message.put("Mensagem", "Seja bem vindo ao servidor. Digite '/exit' para sair e '/help' Para ver todos os comandos disponíveis. Pessoas online: " + allConnections.size());
                    message.put("Data", dtf.format(now));

                    // Sends the message to client
                    outputToClient = new ObjectOutputStream(socket.getOutputStream());
                    outputToClient.writeObject(message.toString());

                    // Adds one to the variable so that the client doesn't receives more than one
                    // hello from server
                    firstMessage++;

                }

                // Waits here until the respective client sends a message
                inputFromClient = new ObjectInputStream(socket.getInputStream());

                // Reads the string
                String stringFromClient = (String) inputFromClient.readObject();

                // Gets the string as Json and reads all of it's parts
                JSONObject objectFromClient = new JSONObject(stringFromClient);
                String nameFromClient = objectFromClient.getString("Identificador");
                String messageFromClient = objectFromClient.getString("Mensagem");
                String dateFromClient = objectFromClient.getString("Data");

                if (image == 1) {

                    // Init the convert to ASCII class
                    ImageToASCII imageToASCII = new ImageToASCII();

                    // Converts the link to ASCII 
                    String conversion = imageToASCII.convertToASCII(messageFromClient);

                    // If the ASCII conversion fails it throws error
                    if (conversion == "Erro") {
                        
                        // Gets the current time
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                        LocalDateTime now = LocalDateTime.now();

                        // Formats the message in Json rules to the project
                        JSONObject message = new JSONObject();
                        message.put("Identificador", "Server");
                        message.put("Mensagem", "O que você enviou não parece ser um link válido para uma imagem. Tente novamente com '/image'.");
                        message.put("Data", dtf.format(now));

                        // Sends the message to client
                        outputToClient = new ObjectOutputStream(socket.getOutputStream());
                        outputToClient.writeObject(message.toString());

                    // Else it will proceed to display the image
                    } else {

                        // sets the message to the ASCII image
                        messageFromClient = conversion;

                        // Gets the current time
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                        LocalDateTime now = LocalDateTime.now();

                        // Formats the message in Json rules to the project
                        JSONObject message = new JSONObject();
                        message.put("Identificador", "Server");
                        message.put("Mensagem", messageFromClient);
                        message.put("Data", dtf.format(now));

                        // Sends the message to client
                        outputToClient = new ObjectOutputStream(socket.getOutputStream());
                        outputToClient.writeObject(message.toString());

                    }

                    image --;

                }

                // If the message is exit closes the connection for that user
                if (messageFromClient.equalsIgnoreCase("/exit")) {

                    // Removes the socket from the global array
                    allConnections.remove(socket);

                    // Prints for the admin that the client was disconnected
                    System.out.println("Client disconnected: " + socket.getInetAddress());

                    // Counts and prints for the admin the number of active threads
                    int threads = java.lang.Thread.activeCount() - 1;
                    System.out.println("Threads: " + threads);

                    // Closes the thread itself
                    return;

                }

                // If the message is help or h returns the list of commands to the user 
                if (messageFromClient.equalsIgnoreCase("/help") || messageFromClient.equalsIgnoreCase("/h")) {

                    // Gets the current time
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    LocalDateTime now = LocalDateTime.now();

                    // Formats the message in Json rules to the project
                    JSONObject message = new JSONObject();
                    message.put("Identificador", "Server");
                    message.put("Mensagem", "Comandos disponíveis:\n'/exit' = Encerra a conexão.\n'/help' & '/h' = Exibe este menu de ações.\n'/image' = O servidor irá retornar pedindo um link válido de uma imagem. Após ser enviado, será exibido no chat a imagem em ASCII.\n'/online' = O servidor irá retornar quantas pessoas estão online no momento.");
                    message.put("Data", dtf.format(now));

                    // Sends the message to client
                    outputToClient = new ObjectOutputStream(socket.getOutputStream());
                    outputToClient.writeObject(message.toString());

                }

                // if the message is online returns the number os online users 
                if (messageFromClient.equalsIgnoreCase("/online")) {

                    // Gets the current time
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    LocalDateTime now = LocalDateTime.now();

                    // Formats the message in Json rules to the project
                    JSONObject message = new JSONObject();
                    message.put("Identificador", "Server");
                    message.put("Mensagem", "Pessoas online: " + allConnections.size());
                    message.put("Data", dtf.format(now));

                    // Sends the message to client
                    outputToClient = new ObjectOutputStream(socket.getOutputStream());
                    outputToClient.writeObject(message.toString());

                }

                //if the message is image init the process to display an image in the chat
                if (messageFromClient.equalsIgnoreCase("/image")) {

                    // Gets the current time
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    LocalDateTime now = LocalDateTime.now();

                    // Formats the message in Json rules to the project
                    JSONObject message = new JSONObject();
                    message.put("Identificador", "Server");
                    message.put("Mensagem", "Por favor, envie agora apenas o link para a imagem a ser exibida.");
                    message.put("Data", dtf.format(now));

                    // Sends the message to client
                    outputToClient = new ObjectOutputStream(socket.getOutputStream());
                    outputToClient.writeObject(message.toString());

                    image ++;

                }

                // Prints the message from the client to the admin
                System.out.println("\n{ " + dateFromClient + " } " + nameFromClient + ": " + messageFromClient + "\n");

                // Loop to send the client's message to all other clients
                for (Socket s : allConnections) {

                    // Checks if the socket in the global array is not the client's socket
                    if (s != socket) {

                        // Formats the message
                        JSONObject message = new JSONObject();
                        message.put("Identificador", nameFromClient);
                        message.put("Mensagem", messageFromClient);
                        message.put("Data", dateFromClient);

                        // Sends the message to the socket
                        outputToClient = new ObjectOutputStream(s.getOutputStream());
                        outputToClient.writeObject(message.toString());

                    }

                }

            }

        // Catches if the client disconnected the wrong way or if there was an
        // IOException
        } catch (IOException | ClassNotFoundException e) {

            // Prints to the admin that the client disconnected the wrong way (it was
            // problably ctrl+c)
            System.out.println("Client " + socket.getInetAddress() + " disconnected using the wrong method.");

            // Counts the number of threads and prints it to the admin
            int threads = java.lang.Thread.activeCount() - 1;
            System.out.println("Threads: " + threads);

            // Removes the socket from the global array
            allConnections.remove(socket);

            // Closes the thread itself
            return;

        // Catches if there was a Json problem (for example the client sent the wrong
        // Json format)
        } catch (JSONException e) {

            // Try chatch because Json needs it
            try {

                // Gets the current time
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                LocalDateTime now = LocalDateTime.now();

                // Formats the message saying to the user that his Json format is wrong
                JSONObject message = new JSONObject();
                message.put("Identificador", "Server");
                message.put("Mensagem",
                        "Formatacao incorreta. Por favor refira-se ao padrao selecionado para tentar enviar mensagens a este servidor.");
                message.put("Data", dtf.format(now));

                // Sends the message to the client
                outputToClient = new ObjectOutputStream(socket.getOutputStream());
                outputToClient.writeObject(message.toString());

                // Removes the socket from the global array
                allConnections.remove(socket);

                // Prints to the admin that the client was disconnected
                System.out.println("Client " + socket.getInetAddress() + " disconnected because Json format is wrong.");

                // Counts and prints the number of active threads
                int threads = java.lang.Thread.activeCount() - 1;
                System.out.println("Threads: " + threads);

                return;

            } catch (JSONException | IOException exception) {

                System.out.println(exception);

            }

        // Catches if there was a problem counting threads
        } catch (NumberFormatException e) {

            System.out.println(e);

        }

    }

}
