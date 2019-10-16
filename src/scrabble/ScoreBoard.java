/**
 * @author D M Raisul Ahsan
 * @version 1.0
 *
 * This class creates the right bar of the gui to show scores and instructions
 */


package scrabble;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ScoreBoard extends Canvas {
    private GraphicsContext gc;
    private  int computerScore;
    private int humanScore;
    private int remainingTiles;

    public ScoreBoard(){
        this.gc = this.getGraphicsContext2D();
        this.computerScore = 0;
        this.humanScore = 0;
        this.remainingTiles = 86;
        this.setHeight(600);
        this.setWidth(370);
    }

    public void setComputerScore(int computerScore) {
        this.computerScore = computerScore;
    }

    public void setHumanScore(int humanScore){
        this.humanScore = humanScore;
    }

    public void setRemainingTiles(int remainingTiles) {
        this.remainingTiles = remainingTiles;
    }

    public void draw(){
        gc.setFill(Color.rgb(52,31,94));
        gc.fillRect(0,0,370,600);
        gc.setFont(new Font("Arial", 25));
        gc.setFill(Color.WHITE);
        gc.fillText("Your score: "+humanScore,20,40);
        gc.fillText("Computer score: "+computerScore, 20, 70);
        gc.fillText("Tiles remaining: "+remainingTiles, 20, 100);
        gc.setFont(new Font("Arial", 20));
        gc.fillText("Instructions", 20, 140);
        gc.setFont(new Font("Arial", 15));
        gc.fillText("* For making any move, first\n  click on the \"Select Move\" button", 20, 160);
        gc.fillText("* After choosing \"Horizontal\" or \"Vertical\",\n  click on the tile you want to play and then\n" +
                "  click on the board where you want to play", 20, 205);
        gc.fillText("* You can discard the last tile move\n  clicking on the \"Discard Last Move\" button", 20, 265);
        gc.fillText("* Once you are satisfied with the word\n  you placed on the board, click on the\n  \"Make Move\" button to play", 20, 305);
        gc.fillText("* If you can't come up with a word,\n  click on the \"Give Up Turn\" button", 20, 365);
        gc.fillText("* You can't give up turn in the first move", 20, 405);
        gc.fillText("* If you make an illegal move or\n  a wrong word, clicking on \"Make Move\"\n  button will reset " +
                "the board and your tray.\n  In this case start a new move\n  clicking \"Select Move\" button", 20, 430);
        gc.fillText("* Light blue - doubles the letter score\n  Dark blue - triples the letter score\n" +
                "  Pink - doubles the word score\n  Red - triples the word score", 20, 525);

    }
}
