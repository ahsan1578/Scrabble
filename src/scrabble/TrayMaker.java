/**
 * @author D M Raisul Ahsan
 * @version 1.0
 *
 * This class creates the graphical representation of the Human Player's Tray
 */



package scrabble;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.LinkedList;


public class TrayMaker extends Canvas {
    private Tray tray;
    private boolean isMakeMoveOn;
    private GraphicsContext gc;
    private LinkedList<Integer> playedTiles;
    private boolean lastCharPlayed;

    public TrayMaker(Tray tray){
        lastCharPlayed = true;
        this.tray = tray;
        gc = this.getGraphicsContext2D();
        this.setHeight(50);
        this.setWidth(350);
        playedTiles =new LinkedList<>();
        isMakeMoveOn = false;
    }

    public void draw(){
        LinkedList<Character> list = tray.getTileList();

        int count = 0;
        for(Character c: list){
            drawOneLetter(count*40,0,c);
            count++;
        }

    }

    public void drawOneLetter(int x, int y, char c){
        gc.setStroke(Color.PURPLE);
        gc.setFill(Color.WHITE);
        gc.fillRect(x,y,40,40);
        gc.setFill(Color.BLACK);
        gc.strokeRect(x,y,40,40);

        gc.setFont(new Font("Arial", 30));
        gc.fillText(""+c, x+10,y+28);
        if(playedTiles.contains(x/40)){
            gc.setFill(Color.rgb(255,0,0,0.2));
            gc.fillRect(x,y,40,40);
        }
    }

    public void setTray(Tray tray) {
        this.tray = tray;
    }

    public void setPlayedTiles(LinkedList<Integer> playedTiles) {
        this.playedTiles = playedTiles;
    }

    public LinkedList<Integer> getPlayedTiles() {
        return playedTiles;
    }

    public boolean isMakeMoveOn() {
        return isMakeMoveOn;
    }

    public boolean isLastCharPlayed() {
        return lastCharPlayed;
    }

    public void setLastCharPlayed(boolean lastCharPlayed) {
        this.lastCharPlayed = lastCharPlayed;
    }

    public void setMakeMoveOn(boolean makeMoveOn) {
        isMakeMoveOn = makeMoveOn;
    }
}
