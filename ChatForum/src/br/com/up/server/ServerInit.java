package br.com.up.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.Socket;
import java.util.ArrayList;


public class ServerInit extends Thread{

    private Socket socket;
    private int firstMessage = 0;
    private String name = "Default";
    private ArrayList <Socket> allConnections = new ArrayList<Socket>();

    public ServerInit(Socket socket, ArrayList<Socket> array) {
        this.socket = socket;
        this.allConnections= array;
    }

    @Override
    public void run()  {

        try {

            ObjectOutputStream outputToClient = null;
            ObjectInputStream inputFromClient = null;

            while(true){

                if(firstMessage == 0){

                    outputToClient = new ObjectOutputStream(socket.getOutputStream());
                    outputToClient.writeObject("\nSeja bem vindo ao servidor.\nIdentificamos que esta é sua primeira mensagem usando esta conexão.\nPor favor, informe o seu nome: \n");
                    inputFromClient = new ObjectInputStream(socket.getInputStream());
                    name = (String) inputFromClient.readObject();
                    firstMessage++;
                    outputToClient = new ObjectOutputStream(socket.getOutputStream());
                    outputToClient.writeObject("\nOlá " + name + "!\nSuas mensagens serão enviadas a todos os usuários conectados ao servidor.\nPara enviar uma mensagem, digite seu texto no terminal e aperte enter.\nPara encerrar a conexão, digite 'exit' e aperte enter.\n");
                    System.out.println("Client name is: " + name);

                }

                inputFromClient = new ObjectInputStream(socket.getInputStream());
                String messageFromClient = (String) inputFromClient.readObject();

                if(messageFromClient.equalsIgnoreCase("exit")) {
                    inputFromClient.close();
                    outputToClient.close();
                    socket.close();   
                    System.out.println("Client disconnected: " + socket.getInetAddress() + " : " + name); 
                    break;
                }

                System.out.println(name + ": " + messageFromClient);

                for(Socket s : allConnections){
                    if (s != socket) {
                        outputToClient = new ObjectOutputStream(s.getOutputStream());
                        outputToClient.writeObject(name + ": " + messageFromClient);
                    }
                }

            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }

    }

}
