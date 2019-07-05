
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 *
 */
public class ChatClient extends AbstractClient {
    //Instance variables **********************************************

    /**
     * The interface type variable. It allows the implementation of the display
     * method in the client.
     */
    Displayable clientUI;
    Battleship game;

    //Constructors ****************************************************
    /**
     * Constructs an instance of the chat client.
     *
     * @param host The server to connect to.
     * @param port The port number to connect on.
     * @param clientUI The interface type variable.
     */
    public ChatClient(String host, int port, Displayable clientUI)
            throws IOException {
        super(host, port); //Call the superclass constructor
        this.clientUI = clientUI;
        openConnection();
    }

    //2.   Add userName to ChatClient (overload constructor)
    public ChatClient(String host, int port, String userName, Displayable clientUI)
            throws IOException {
        super(host, port); //Call the superclass constructor
        this.clientUI = clientUI;
        openConnection();
        processClientCommand("#signin " + userName);
    }

    //Instance methods ************************************************
    public void handleMessageFromServer(Object msg) {

        if (msg instanceof Envelope) {
            handleEnvelope((Envelope) msg);
        } else if (msg instanceof Battleship) {
            Battleship bs = (Battleship) msg;
            game = bs;
            ((GUIConsole) clientUI).displayOpponentsBoard(bs);
            clientUI.Battledisplay("BLOW EACH OTHER UP!!!\n\n");
        } 
        else if(msg.toString().indexOf("#Validuser")==0){
            ((GUIConsole) clientUI).battleDraw();
           //  clientUI.battleDraw();
        }
        else if(msg.toString().indexOf("#NotValiduser")==0){
            ((GUIConsole) clientUI).messageTxF.setText("User is not in the Room");
        
        }
        else {
            clientUI.display(msg.toString());
        }
    }

    public void handleEnvelope(Envelope e) {
        if (e.getName().equals("UserList")) {
            ((GUIConsole) clientUI).displayList(e.getContents());
        }
    }

    public void handleMessageFromClientUI(String message) {
        try {
            if (message.charAt(0) == '#') {
                processClientCommand(message);
            } else {
                sendToServer(message);
            }
        } catch (IOException e) {
            clientUI.display("Could not send message to server.  Terminating client.");
            quit();
        }
    }

    public void processClientCommand(String message) {
        if (message.equals("#quit")) {
            quit();
        } else if (message.equals("#logoff")) {
            logoff();
        } else if (message.startsWith("#login")) {
            login();
        } else if (message.equals("#getHost")) {
            clientUI.display("Host: " + getHost());
        } else if (message.indexOf("#setHost") == 0) {
            setNewHost(message);
        } else if (message.equals("#getPort")) {
            clientUI.display("Port: " + getPort());
        } else if (message.indexOf("#setPort") == 0) {
            setNewPort(message);
        } else if (message.startsWith("#battleship")) {
            try {
                String recipient = message.split(" ")[1];
                int[][] board = new int[10][6];
                Envelope e = new Envelope(recipient, board);
                sendToServer(e);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (message.startsWith("#attack")) {
            try {
                int[][] attackboard = new int[10][6];
                String msg = message.substring(message.indexOf(" "), message.length());
                msg = msg.trim();
                String row = msg.substring(0, msg.indexOf(" "));
                row = row.trim();
                int column = Integer.parseInt(msg.split(" ")[1]);

                if (game.getActivePlayer().equals(game.getPlayer1())) {
                    game.attack(row, column, attackboard);
                } else {
                    game.attack(row, column, attackboard);
                }
                sendToServer(game);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isConnected()) {
            try {
                sendToServer(message);
            } catch (Exception e) {

            }
        }
    }

    private void setNewHost(String message) {
        //stop this if isConnected()
        if (!isConnected()) {
            String host = message.substring(message.indexOf(" "), message.length());
            clientUI.display("New host: " + host);
            setHost(host.trim());  //.trim() will remove any white space from the host
        } else {
            clientUI.display("You must be logged out before you can set a new host...");
        }
    }

    private void setNewPort(String message) {
        //stop this if isConnected()
        if (!isConnected()) {
            String port = message.substring(message.indexOf(" "), message.length());
            clientUI.display("New port " + port);
            setPort(Integer.parseInt(port.trim()));
        } else {
            clientUI.display("You must be logged out before you can set a new port...");
        }
    }

    public void logoff() {
        try {
            closeConnection();
        } catch (IOException e) {
            System.out.println("User has logged off");
        }

    }

    public void login() {
        if (!isConnected()) {
            try {
                openConnection();
            } catch (IOException ioe) {
                System.out.println("Something terrible has happened. Login error.");
                ioe.printStackTrace();
            }
        } else {
            clientUI.display("Already connected...");
        }
    }

    /**
     * This method terminates the client.
     */
    public void quit() {
        try {
            closeConnection();
        } catch (IOException e) {
        }
        System.exit(0);
    }

    protected void connectionException(Exception exception) {
        clientUI.display("Server appears to have disconnected...");
    }
//End of ChatClient class

}
