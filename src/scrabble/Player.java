package scrabble;

import java.util.LinkedList;

public class Player {
    private Tray tray;
    private boolean isMyMove;
    private LinkedList<int[]> nextMoveCoordinates;
    private String nextMoveWord;

    public Player(Tray tray){
        this.tray = tray;
        isMyMove = false;
        nextMoveCoordinates = new LinkedList<>();
        nextMoveWord = "";
    }

    public void setMyMove(boolean myMove) {
        isMyMove = myMove;
    }

    public void setTray(Tray tray) {
        this.tray = tray;
    }

    public Tray getTray() {
        return tray;
    }

    public boolean isMyMove() {
        return isMyMove;
    }

    public LinkedList<int[]> getNextMoveCoordinates() {
        return nextMoveCoordinates;
    }

    public void setNextMoveCoordinates(LinkedList<int[]> nextMoveCoordinates) {
        this.nextMoveCoordinates = nextMoveCoordinates;
    }

    public String getNextMoveWord() {
        return nextMoveWord;
    }

    public void setNextMoveWord(String nextMoveWord) {
        this.nextMoveWord = nextMoveWord;
    }
}
