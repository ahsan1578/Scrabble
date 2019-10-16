/**
 * @author D M Raisul Ahsan
 * @version 1.0
 *
 * This class creates a player with own tray
 */


package scrabble;

import java.util.LinkedList;

public class Player {
    private Tray tray;
    private boolean isMyMove;
    private LinkedList<int[]> nextMoveCoordinates;
    private String nextMoveWord;


    /**
     * Construct the player
     * @param tray the tray that the player will use
     */
    public Player(Tray tray){
        this.tray = tray;
        isMyMove = false;
        nextMoveCoordinates = new LinkedList<>();
        nextMoveWord = "";
    }


    /**
     * Sets the tray of the player
     * @param tray the tray
     */
    public void setTray(Tray tray) {
        this.tray = tray;
    }


    /**
     * @return the tray
     */
    public Tray getTray() {
        return tray;
    }

    /**
     * @return next move indexes on board
     */
    public LinkedList<int[]> getNextMoveCoordinates() {
        return nextMoveCoordinates;
    }


    /**
     * @param nextMoveCoordinates sets the next move indexes on board
     */
    public void setNextMoveCoordinates(LinkedList<int[]> nextMoveCoordinates) {
        this.nextMoveCoordinates = nextMoveCoordinates;
    }


    /**
     * @return the word to be played next
     */
    public String getNextMoveWord() {
        return nextMoveWord;
    }


    /**
     * @param nextMoveWord sets the next word to be played
     */
    public void setNextMoveWord(String nextMoveWord) {
        this.nextMoveWord = nextMoveWord;
    }
}
