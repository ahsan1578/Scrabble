package scrabble;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class BoardGui extends Canvas {
    private GraphicsContext gc;
    private Board board;


    public BoardGui(Board board){
        this.board = board;
        gc = this.getGraphicsContext2D();
        this.setHeight(board.getDimension()*40);
        this.setWidth(board.getDimension()*40);
    }

    public void draw(){
        if(board.isRotated()){
            board.rotateBack();
        }
        int dim = board.getDimension();
        String [][] strings = board.getBoard();

        for(int i = 0; i<dim; i++){
            for(int j = 0; j<dim; j++){
                int x= i*40;
                int y = j*40;
                int letterMultiplier = 1;
                int wordMultiplier = 1;
                if(strings[i][j].charAt(0)=='2'){
                    wordMultiplier = 2;
                }else if(strings[i][j].charAt(0)=='3'){
                    wordMultiplier = 3;
                }else if(strings[i][j].charAt(1)=='2'){
                    letterMultiplier = 2;
                }else if(strings[i][j].charAt(1)=='3'){
                    letterMultiplier = 3;
                }
                char c = (char)0;
                if(board.existChar(i,j)){
                    c = board.getChar(i,j);
                }

                drawOneSquare(y,x,c,wordMultiplier,letterMultiplier);
            }
        }
    }

    public void drawOneSquare(int x, int y, char c, int wordMultiplier, int letterMultiplier){
        gc.setFill(Color.WHITE);
        gc.fillRect(x,y,40,40);
        if(wordMultiplier == 2){
            gc.setFill(Color.PINK);
            gc.fillRect(x,y,40,40);
        }else if(wordMultiplier == 3){
            gc.setFill(Color.RED);
            gc.fillRect(x,y,40,40);
        }else if(letterMultiplier == 2){
            gc.setFill(Color.LIGHTBLUE);
            gc.fillRect(x,y,40,40);
        }else if(letterMultiplier == 3){
            gc.setFill(Color.DARKBLUE);
            gc.fillRect(x,y,40,40);
        }
        if(c>0){
            if(c<'a'){
                c = (char)(c + 32);
            }
            if(gc.getFill().equals(Color.DARKBLUE)){
                gc.setFill(Color.WHITE);
            }else{
                gc.setFill(Color.BLACK);
            }
            gc.setFont(new Font("Arial",26));
            gc.fillText(""+c,x+8,y+27);
        }
        gc.setStroke(Color.PURPLE);
        gc.strokeRect(x,y,40,40);
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}

