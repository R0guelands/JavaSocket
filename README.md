# JavaSocket
 A java socket (server + client) for a chat forum system.
 
 There are two main classes, one for server, one for client.
 
 Rules:
 - The server must accept all connections.
 - The server must check if the client sent the message with the right Json structure.
 - The server must pass the message to all connections.
 - The client must send the message with the right Json structure.
 
 The Json Structure:
 {
    Identificador: xxx,
    Mensagem: xxx,
    Data: dd/mm/yyy HH:mm;
 }
 
 The general flow:
 - A client connects to server.
 - The server responds to confirme connection
 - The chat is initialized
    
