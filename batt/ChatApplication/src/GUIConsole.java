
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ryan
 */
public class GUIConsole extends JFrame implements Displayable {

    /**
     * @return the validuser
     */
    public boolean isValiduser() {
        return validuser;
    }

    /**
     * @param validuser the validuser to set
     */
    public void setValiduser(boolean validuser) {
        this.validuser = validuser;
    }
   public boolean test;
    private ChatClient client;
    //Close
    private JButton closeB = new JButton("Log Off");
    //Open
    private JButton openB = new JButton("Login");
    private JButton sendB = new JButton("Send");
    private JButton quitB = new JButton("Quit");
    private JButton battleB = new JButton("Battle Ship");
    private JButton attackB = new JButton("Attack");
     private JButton quitGameB = new JButton("Quit Game");
     
     private boolean validuser;

    JPanel cent = new JPanel();

    private JTextField portTxF = new JTextField("5555");
    private JTextField hostTxF = new JTextField("127.0.0.1");
    public JTextField messageTxF = new JTextField("");
    private JTextField loginTxF = new JTextField("Bob");
    private JTextField rowTxF = new JTextField("A");
    private JTextField ColumnTxF = new JTextField("1");

    private JLabel portLB = new JLabel("Port: ", JLabel.RIGHT);
    private JLabel blank = new JLabel("", JLabel.RIGHT);
    private JLabel hostLB = new JLabel("Host: ", JLabel.RIGHT);
    private JLabel messageLB = new JLabel("Message: ", JLabel.RIGHT);
    private JLabel loginLB = new JLabel("Login User: ", JLabel.RIGHT);
    private JLabel rowLB = new JLabel("Row: ", JLabel.RIGHT);
    private JLabel ColumnLB = new JLabel("Column: ", JLabel.RIGHT);
    final public static int DEFAULT_PORT = 5555;

    JPanel bottom = new JPanel();

    private String host;
    private int port;
    private String user;
    private boolean Battle;
    
    private JTextArea messageList = new JTextArea();
    private JTextArea battleList = new JTextArea();
    
    

    public GUIConsole() {
    }
    
    public GUIConsole(GUIConsole another){
    this.validuser = another.validuser;
    
    }

    public GUIConsole(String host, int port, String user) {

        super("Simple Chat GUI");
        setSize(300, 400);

        setLayout(new BorderLayout(5, 5));

        //JPanel top = new JPanel();

        add("Center", cent);
        //add("North", top);
        //  add("Center", messageList);
        add("South", bottom);
        messageList.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        battleList.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        cent.setLayout(new GridLayout(1, 1, 1, 1));
        cent.add(messageList);

        bottom.setLayout(new GridLayout(8, 2, 5, 5));
        bottom.add(hostLB);
        bottom.add(hostTxF);
        bottom.add(portLB);
        bottom.add(portTxF);
        bottom.add(loginLB);
        bottom.add(loginTxF);
        bottom.add(messageLB);
        bottom.add(messageTxF);
        bottom.add(openB);
        bottom.add(sendB);
        bottom.add(battleB);
        bottom.add(attackB);
        bottom.add(closeB);
        bottom.add(quitB);

        attackB.setEnabled(false);
        battleB.setEnabled(false);

        setVisible(true);
        sendB.setEnabled(false);
        closeB.setEnabled(false);

        sendB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                send();
            }
        });

        quitB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (sendB.isEnabled() == true) {
                    send("#quit");
                } else {
                    System.exit(1);
                }
            }
        });

        battleB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Battle = true;
                send();
//                test=test;
//                if(!isValiduser()){
//                    messageTxF.setText("No one in the room named that");
//                }
//                else{
//                
//                if (sendB.isEnabled() == true) {
//                    cent.setLayout(new GridLayout(1, 2, 1, 1));
//                    cent.add(messageList);
//                    cent.add(battleList);
//                    battleB.setEnabled(false);
//                    setSize(800, 800);
//                    battleshipStartUp(bottom);
//
//                    String action = "#battleship " + messageTxF.getText();
//                    send("#battleship " + messageTxF.getText());
//                }
//                }
            }
        });

        attackB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            
                String validColumn = ColumnTxF.getText();
                if (sendB.isEnabled() == true) {

                    
                    if(!rowTxF.getText().matches("^[a-jA-J]*$")){
                        rowTxF.setBackground(Color.red);
                        rowTxF.setText("Invalid Row Choice");
                    }
                    else if(!isStringInt(validColumn) || Integer.parseInt(validColumn) >6 || Integer.parseInt(validColumn) < 1 ){
                         rowTxF.setBackground(Color.white);
                         ColumnTxF.setBackground(Color.red);
                         ColumnTxF.setText("Invalid Column Choice");
                    }
                    else{
                      rowTxF.setBackground(Color.white);
                      ColumnTxF.setBackground(Color.white);  
                      attackB.setEnabled(false);
                      send("#attack " + rowTxF.getText() + " " + ColumnTxF.getText());
                    }
                } 
            }
        });
        
        
        closeB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                send("#logoff");
 
                sendB.setEnabled(false);
                closeB.setEnabled(false);
                battleB.setEnabled(false);
                openB.setEnabled(true);
            }
        });

        openB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                open();
            }
        });
        
        quitGameB.addActionListener( new ActionListener() 
         {
            public void actionPerformed(ActionEvent e)
            {   
                battleshipCloseDown(bottom);
            }
         });
    }
    
    public void battleDraw(){
        cent.setLayout(new GridLayout(1, 2, 1, 1));
                    cent.add(messageList);
                    cent.add(battleList);
                    battleB.setEnabled(false);
                    setSize(800, 800);
                    battleshipStartUp(bottom);

                    String action = "#battleship " + messageTxF.getText();
                    send("#battleship " + messageTxF.getText());
    }
    
    public boolean isStringInt(String S){
        try
        {
             Integer.parseInt(S);
             return true;
             
        } catch (NumberFormatException ex){
            return false;
        }
        }

    public void battleshipStartUp(JPanel bottom) {

        bottom.setLayout(new GridLayout(0, 2, 2, 2));

 
        bottom.add(rowLB);
        bottom.add(rowTxF);
        bottom.add(ColumnLB);
        bottom.add(ColumnTxF);
        bottom.add(quitGameB);
        bottom.add(attackB);
        bottom.add(battleB);
        bottom.add(blank);
        bottom.add(hostLB);
        bottom.add(hostTxF);
        bottom.add(portLB);
        bottom.add(portTxF);
        bottom.add(loginLB);
        bottom.add(loginTxF);
        bottom.add(messageLB);
        bottom.add(messageTxF);
        bottom.add(openB);
        bottom.add(sendB);

        bottom.add(closeB);
        bottom.add(quitB);

    }

    public void battleshipCloseDown(JPanel bottom){

        setSize(300, 400);
        cent.remove(battleList);
        battleB.setEnabled(true);
        attackB.setEnabled(false);

        bottom.setLayout(new GridLayout(8, 2, 5, 5));
        bottom.remove(rowLB);
        bottom.remove(rowTxF);
        bottom.remove(ColumnLB);
        bottom.remove(ColumnTxF);
        bottom.remove(quitGameB);
        bottom.remove(blank);
        bottom.add(hostLB);
        bottom.add(hostTxF);
        bottom.add(portLB);
        bottom.add(portTxF);
        bottom.add(loginLB);
        bottom.add(loginTxF);
        bottom.add(messageLB);
        bottom.add(messageTxF);
        bottom.add(openB);
        bottom.add(sendB);
        bottom.add(battleB);
        bottom.add(attackB);
        bottom.add(closeB);
        bottom.add(quitB);

        attackB.setEnabled(false);

        setVisible(true);

        cent.remove(cent);
    }
    public void open() {

        user = loginTxF.getText();
        host = hostTxF.getText();
        port = Integer.parseInt(portTxF.getText());
        connect(host, port, user);
        sendB.setEnabled(true);
        closeB.setEnabled(true);
        openB.setEnabled(false);
        battleB.setEnabled(true);

    }

    public void connect(String host, int port, String user) {
        try {
            client = new ChatClient(host, port, user, this);

        } catch (IOException exception) {
            System.out.println("Error: Can't setup connection!"
                    + " Terminating client.");
            exception.printStackTrace();
            System.exit(1);
        }

    }

    public void display(String message) {
        messageList.insert("\n" + message, 0);
    }

    public void Battledisplay(String message) {
        battleList.insert("\n" + message, 0);
    }

    public void displayList(Object contents) {
        String[] names = (String[]) contents;
        for (int i = 0; i < names.length; i++) {
            messageList.insert(names[i] + "\n", 0);
        }
    }

    public void displayOpponentsBoard(Battleship bs) {
       if (bs.checkGameStatus()) {
        setSize(800, 800);
        battleB.setEnabled(false);
        cent.setLayout(new GridLayout(1, 2, 1, 1));
        cent.add(messageList);
        cent.add(battleList);
        battleshipStartUp(bottom);
        if (bs.isEnabled()) {
            attackB.setEnabled(true);
        }
        battleList.setText(" ");
        int[][] board = bs.getBoard();
        String line = "";
        int num_rows;
        line += "\nOpponent's Board:";
        line += "\n\t\t";
        for (int i = 1; i <= 6; i++) {
            line += i + "      ";
        }
        num_rows = (10 - 1) + 65;
        for (int i = 0; i < 10; i++) {
            line += "\n";
            char theChar = (char) (i + 65);
            line += "         " + theChar + " \t";
            for (int j = 0; j < 6; j++) {

                if (board[i][j] == 2) {
                    line += "X      ";
                } else if (board[i][j] == 3) {
                    line += "O      ";
                } else {
                    line += "+      ";
                }
            }
            
            battleList.append(line);
            line = "";
            
        }
        displayPlayersBoard(bs);
        if (bs.isEnabled()) {
            if (bs.getAttackOutcome() != null) {
                battleList.append("\n" + bs.getAttackOutcome());
                battleList.append("\n\n Your Move:");
            } else {
                battleList.append("\n\n Your Move:");
            }
        } else {
            battleList.append("\n\n Please wait for your turn...");
        }
       }else {
            if(bs.isEnabled()){
                messageList.setText("");
                messageList.append("Game Over. You Lose!"); 
            } else{      
                messageList.setText("");
                messageList.append("Game Over. You win!"); 
            }
             battleshipCloseDown(bottom);
        }
    }
    public void displayPlayersBoard(Battleship bs){
        int[][] board = bs.getInactivePlayerBoard();
        String line = "";
        int num_rows;
        line += "\nYour Board:";
        line += "\n\t\t";
        for (int i = 1; i <= 6; i++) {
            line += i + "      ";
        }
        num_rows = (10 - 1) + 65;
        for (int i = 0; i < 10; i++) {
            line += "\n";
            char theChar = (char) (i + 65);
            line += "         " + theChar + " \t";
            for (int j = 0; j < 6; j++) {

                if (board[i][j] == 2) {
                    line += "X      ";
                } else if (board[i][j] == 3) {
                    line += "O      ";
                } else if(board[i][j] == 1){
                    line += "#      ";
                } else {
                    line += "+      ";
                }
            }

            battleList.append(line);
            line = "";
        }
    }

    public void send() {
        String message;
        if(Battle){
            message = "#battlewith " + messageTxF.getText();
            Battle = false;
        }
        else message = messageTxF.getText();
        client.handleMessageFromClientUI(message);

    }

    public void send(String command) {
        client.handleMessageFromClientUI(command);
    }

    public static void main(String[] args) {

        String host = "";

        int port = 0;  //The port number

        try {
            host = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            host = "localhost";     //10.52.32.27
        }

        try {
            port = Integer.parseInt(args[1]);
        } catch (Exception ex) {
            port = DEFAULT_PORT;
        }
        String user;
        try {
            user = args[2];
        } catch (ArrayIndexOutOfBoundsException e) {
            user = "Anon";     //10.52.32.27
        }
        GUIConsole clientConsole = new GUIConsole(host, port, user);

    }

}
