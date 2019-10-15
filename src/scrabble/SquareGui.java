package scrabble;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SquareGui extends Canvas {
    private GraphicsContext gc;

    public SquareGui(){
        gc = this.getGraphicsContext2D();
        this.setHeight(50);
        this.setWidth(50);
    }

    public void draw(int x, int y, char c, int wordMultiplier, int letterMultiplier){
        if(wordMultiplier == 2){
            gc.setFill(Color.PINK);
            gc.fillRect(x,y,50,50);
        }else if(wordMultiplier == 3){
            gc.setFill(Color.RED);
            gc.fillRect(x,y,50,50);
        }else if(letterMultiplier == 2){
            gc.setFill(Color.LIGHTBLUE);
            gc.fillRect(x,y,50,50);
        }else if(letterMultiplier == 3){
            gc.setFill(Color.DARKBLUE);
            gc.fillRect(x,y,50,50);
        }
        if(c>0){
            if(c<'a'){
                c = (char)(c + 32);
                gc.setFill(Color.BLACK);
                gc.fillText(""+c,15,15);
            }
        }
        gc.setStroke(Color.PURPLE);
        gc.strokeRect(x,y,50,50);
    }
}
