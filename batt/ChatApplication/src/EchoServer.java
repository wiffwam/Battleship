

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.

 */
public class EchoServer extends AbstractServer
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  @Override
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    System.out.println("Message received: " + msg + " from " + 
            client.getInfo("user") + " at: " + client);
    
    String message = msg.toString();
    if(message.charAt(0) == '#') {
                handleServerCommand(message, client);
      } 
    if( msg instanceof Envelope){
        System.out.println("Envelope found");
        Envelope e = (Envelope)msg;
        System.out.println("Envelope extracted");
        
        
        
        String recipient = e.getName();
        sendToAClient("You're Playing Battleship!", recipient);
        Battleship ttt = new Battleship(client.getInfo("user").toString(), 
                recipient);
    
        handleBattleship(ttt);
    }
        else if(msg instanceof Battleship){
          handleBattleship((Battleship)(msg));
    }
    else {
          String user = client.getInfo("user").toString();
          msg = user + " : " + msg;
          //this.sendToAllClients(msg);
          String room = client.getInfo("room").toString();
          this.sendToAllClientsInRoom(msg, room);
      }
    
  }
public void sendToAllClientsInRoom(Object msg, String room)
  {

    Thread[] clientThreadList = getClientConnections();

    for (int i=0; i<clientThreadList.length; i++)
    {
      ConnectionToClient client = (ConnectionToClient)clientThreadList[i];
      if(client.getInfo("room").equals(room)){
      try
      {
        client.sendToClient(msg);
      }
      catch (Exception ex) {
      }
      
      }
    }
  }
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  protected void clientConnected(ConnectionToClient client) {
      System.out.println("Client: " + client.getInfo("user") + " has connected");
  }
  
  synchronized protected void clientDisconnected(ConnectionToClient client) {
      System.out.println("Client: " + client.getInfo("user") + " has disconnected");
  }
    synchronized protected void clientException(
    ConnectionToClient client, Throwable exception) {
        clientDisconnected(client);
        //System.out.println("Client has disconnected...");
    }
  public void handleServerCommand(String message, ConnectionToClient client)
  {
        if (message.startsWith("#join")) {
            joinRoom(message, client);
        } 
        else if (message.indexOf("#signin") == 0) {
            signIn(message, client);
        }
        else if (message.indexOf("#yell")==0)
        {
            yell(message, client);
        }
        else if (message.indexOf("#pm")==0)
        {
            privateMessage(message, client);
        }
        else if (message.indexOf("#intercom")==0){
           intercom(message,client);
        }
        else if(message.indexOf("#ison")==0){
           ison(message,client);
        }
        else if(message.indexOf("#who")==0){
            getUserList(client);
        }
        else if(message.indexOf("#battlewith")==0){
            getBattlelist(message,client);
        
        }

        
    }
  
  public void getBattlelist(String message,ConnectionToClient client){
      GUIConsole name = new GUIConsole();
      boolean valid = true;
      String users = 
                message.substring(message.indexOf(" "),message.length());
        users = users.trim();
        ConnectionToClient clientProxy1 = client;
        
        Thread[] clientThreadList = getClientConnections();
       for (int i=0; i<clientThreadList.length; i++)
    {
        
      ConnectionToClient clientProxy = ((ConnectionToClient)clientThreadList[i]);
      
      if(clientProxy.getInfo("user").equals(users))
        {
            valid = true;
            break;
      }
      valid = false;
    }
      if(valid){
          try{
         client.sendToClient("#Validuser");}
          catch(Exception ex2){}
      }
      else{
          try{
         client.sendToClient("#NotValiduser");}
          catch(Exception ex2){}
      }
  }
  
  
  
    public void getUserList(ConnectionToClient sender) {
        Thread[] clientThreadList = getClientConnections();
        String[] names = new String[clientThreadList.length];

        for (int i = 0; i < clientThreadList.length; i++) {
            ConnectionToClient client = (ConnectionToClient) clientThreadList[i];
            names[i] = client.getInfo("user").toString();
        }
        Envelope e = new Envelope("UserList", names);

        try {
            sender.sendToClient(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
   public void ison(String message, ConnectionToClient client){
        
        Object room1;
        String msg;
        String users = 
                message.substring(message.indexOf(" "),message.length());
        users = users.trim();
        ConnectionToClient clientProxy1 = client;
        
        Thread[] clientThreadList = getClientConnections();

    for (int i=0; i<clientThreadList.length; i++)
    {
        
      ConnectionToClient clientProxy = ((ConnectionToClient)clientThreadList[i]);
      
      if(clientProxy.getInfo("user").equals(users))
        {
            clientProxy1 = ((ConnectionToClient)clientThreadList[i]);
            break;
      }
    }
    if(clientProxy1.getInfo("user").equals(users)){
         try
        {
            room1 = client.getInfo("room");
            msg = users + " is on and is in room: " + room1;
            System.out.println(msg);
            client.sendToClient(msg);
        }
        catch (Exception ex) {
            System.out.println("Failed to send PM ");
            ex.printStackTrace();
        }
    }
        else {
            try {               
                msg = users + " is not on";
                client.sendToClient(msg);
                System.out.println(msg); 
            } catch (Exception ex) {
                System.out.println("Failed to send PM ");
                ex.printStackTrace();
            }
        }
    }
       
       
    public void intercom(String message, ConnectionToClient client){
        String messageWithoutComand = 
                message.substring(message.indexOf(" "),message.length());
        messageWithoutComand = messageWithoutComand.trim();
        
        String room =   
        messageWithoutComand.substring(0, messageWithoutComand.indexOf(" "));
        
        String msg =messageWithoutComand.substring(messageWithoutComand.indexOf(" "),messageWithoutComand.length());
        sendToAllClientsInRoom(client.getInfo("user")+msg.trim(), room.trim());

    }
    public void handleBattleship(Battleship ttt){
    ttt.setEnabled(false);
    sendToAClient(ttt, ttt.getActivePlayer());
    ttt.switchActivePlayer();
    sendToAClient(ttt, ttt.getActivePlayer());
    
    
}
  public void yell(String message, ConnectionToClient client)
  {
        String msg = message.substring(message.indexOf(" "), message.length());
            sendToAllClients(client.getInfo("user") + ": " + msg.trim());
  }
  public void privateMessage(String message, ConnectionToClient client)
  {
      String msgWithoutCommand = message.substring(message.indexOf(" "), message.length());
      msgWithoutCommand = msgWithoutCommand.trim();
      
      String target = msgWithoutCommand.substring(0, msgWithoutCommand.indexOf(" "));
      target = target.trim();
      
      String msg = msgWithoutCommand = msgWithoutCommand.substring(msgWithoutCommand.indexOf(" "), msgWithoutCommand.length());
      msg = msg.trim();
      
      sendToAClient(client.getInfo("user")+": "+msg, target);
  }
  public void signIn(String message, ConnectionToClient client)
  {
      String userName = message.substring(message.indexOf(" "), message.length());
      userName = userName.trim();
      
      client.setInfo("user", userName);
      joinRoom("#room lobby", client);
  }
  public void joinRoom(String message, ConnectionToClient client)
  {
        String roomName = message.substring(message.indexOf(" "), message.length());
        roomName = roomName.trim();
        
        client.setInfo("room", roomName);
  }
  public void sendToAClient(Object message, String target)
  {
    Thread[] clientThreadList = getClientConnections();

    for (int i=0; i<clientThreadList.length; i++)
    {
        
      ConnectionToClient clientProxy = ((ConnectionToClient)clientThreadList[i]);
      
      if(clientProxy.getInfo("user").equals(target))
        {
        try
        {
            clientProxy.sendToClient(message);
        }
        catch (Exception ex) {
            System.out.println("Failed to send PM ");
            ex.printStackTrace();
        }
      }
      
    }
  }
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
