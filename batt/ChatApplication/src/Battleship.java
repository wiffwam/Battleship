
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ryan
 */
public class Battleship implements Serializable {

    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private int[][] board1;
    private int[][] board2;
    private String player1;
    private String player2;
    private String activePlayer;
    private String attackOutcome;
    private boolean enabled;

    public Battleship() {}

    public Battleship(String p1, String p2) {

        setBoard1(board1);
        setBoard2(board2);
        setPlayer1(p1);
        setPlayer2(p2);
        setActivePlayer(p1);
    }

    public void attack(String row, int column, int[][] board) {
        if (activePlayer.equals(player1)) {
            board = board1;
        } else {
            board = board2;
        }
        int numberValueOfRow = convertLetterToInt(row);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j <= 6; j++) {

                if (activePlayer.equals(player1)) {
                    if (i == numberValueOfRow && j == column - 1) {
                        if (board1[numberValueOfRow][column - 1] == 1) {
                            board1[numberValueOfRow][column - 1] = 2;
                            String outcome = activePlayer + " hit at: " + row + " " + column;
                            setAttackOutcome(outcome);

                        } else {
                            board1[numberValueOfRow][column - 1] = 3;
                            String outcome = activePlayer + " missed at: " + row + " " + column;
                            setAttackOutcome(outcome);
                        }
                    }
                }
                if (activePlayer.equals(player2)) {
                    if (i == numberValueOfRow && j == column - 1) {
                        if (board2[numberValueOfRow][column - 1] == 1) {
                            board2[numberValueOfRow][column - 1] = 2;
                            String outcome = activePlayer + " hit at: " + row + " " + column;
                            setAttackOutcome(outcome);

                        } else {
                            board2[numberValueOfRow][column - 1] = 3;
                            String outcome = activePlayer + " miss at: " + row + " " + column;
                            setAttackOutcome(outcome);

                        }
                    }
                }
            }

        }
    }

    public String getAttackOutcome() {
        return attackOutcome;
    }

    public void setAttackOutcome(String attackOutcome) {
        this.attackOutcome = attackOutcome;
    }

    public void switchActivePlayer() {
        setEnabled(true);
        if (activePlayer.equals(player1)) {
            activePlayer = player2;
        } else {
            activePlayer = player1;
        }

    }
    public int[][] getInactivePlayerBoard(){
        if(activePlayer.equals(player1)){
            return board2;
        }else{
            return board1;
        }
    }

    /**
     * @return the board
     */
    public int[][] getBoard() {
        if (activePlayer == player1) {
            return board1;
        }
        return board2;
    }

    /**
     * @param board the board to set
     */
    public void setBoard1(int[][] board) {
        board = new int[10][6];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 6; j++) {
                board[i][j] = 0;
            }
        }
        //set ships for board 2    
        //ship 1
        board[1][5] = 1;
        board[1][4] = 1;
        board[1][3] = 1;
        
        //ship 2
        board[3][0] = 1;
        board[4][0] = 1;
        
        //ship 3
        board[9][3] = 1;
        board[9][4] = 1;
        board[9][5] = 1;
        board[9][2] = 1;
        
        //ship 4
        board[5][0] = 1;
        board[5][1] = 1;
        board[5][2] = 1;
        board[5][3] = 1;
        board[5][4] = 1;
        
        

        board1 = board;

    }

    public void setBoard2(int[][] board) {
        board = new int[10][6];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 6; j++) {
                board[i][j] = 0;
            }
        }
        //set ships for board 2    
        //ship 1
        board[1][5] = 1;
        board[2][5] = 1;
        board[3][5] = 1;
        
        //ship 2
        board[6][0] = 1;
        board[6][1] = 1;
        
        //ship 3
        board[8][3] = 1;
        board[8][4] = 1;
        board[8][5] = 1;
        board[8][2] = 1;
        
        //ship 4
        board[0][0] = 1;
        board[0][1] = 1;
        board[0][2] = 1;
        board[0][3] = 1;

        board2 = board;
    }

    /**
     * @return the player1
     */
    public String getPlayer1() {
        return player1;
    }

    /**
     * @param player1 the player1 to set
     */
    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    /**
     * @return the player2
     */
    public String getPlayer2() {
        return player2;
    }

    /**
     * @param player2 the player2 to set
     */
    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    /**
     * @return the activePlayer
     */
    public String getActivePlayer() {
        return activePlayer;
    }

    /**
     * @param activePlayer the activePlayer to set
     */
    public void setActivePlayer(String activePlayer) {
        this.activePlayer = activePlayer;
    }
    public boolean checkGameStatus() {
        int statusForBoardOne = -1;
        int statusForBoardTwo = -1;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 6; j++) {
                if (board1[i][j] == 1) {
                    statusForBoardOne = 1;
                }
            }
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 6; j++) {
                if (board2[i][j] == 1) {
                    statusForBoardTwo = 1;
                }
            }
        }
        if (statusForBoardOne == -1 || statusForBoardTwo == -1) {
            return false;
        }
        return true;
    }


    public int convertLetterToInt(String val) {
        int toReturn = -1;
        switch (val.toUpperCase()) {
            case "A":
                toReturn = 0;
                break;
            case "B":
                toReturn = 1;
                break;
            case "C":
                toReturn = 2;
                break;
            case "D":
                toReturn = 3;
                break;
            case "E":
                toReturn = 4;
                break;
            case "F":
                toReturn = 5;
                break;
            case "G":
                toReturn = 6;
                break;
            case "H":
                toReturn = 7;
                break;
            case "I":
                toReturn = 8;
                break;
            case "J":
                toReturn = 9;
                break;
            default:
                toReturn = -1;
                break;
        }

        return toReturn;
    }

    public String convertIntToLetter(int val) {
        String toReturn = "Z";
        switch (val) {
            case 0:
                toReturn = "A";
                break;
            case 1:
                toReturn = "B";
                break;
            case 2:
                toReturn = "C";
                break;
            case 3:
                toReturn = "D";
                break;
            case 4:
                toReturn = "E";
                break;
            case 5:
                toReturn = "F";
                break;
            case 6:
                toReturn = "G";
                break;
            case 7:
                toReturn = "H";
                break;
            case 8:
                toReturn = "I";
                break;
            case 9:
                toReturn = "J";
                break;
            default:
                toReturn = "Z";
                break;
        }
        return toReturn;
    }
}
