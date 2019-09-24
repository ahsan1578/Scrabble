package scrabble;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class Board {
    private String [][] board;
    private LinkedList<String> boardConfig;
    private int dimension;

    Board(){
        this.boardConfig = new LinkedList<>();
        InputStream inputStream = getClass().getResourceAsStream("board.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        try {
            while ((line = reader.readLine()) != null){
                boardConfig.add(line);
            }
        }catch (Exception e){
            System.out.println("Error in input stream reader for board");
        }
        this.dimension = Integer.parseInt(boardConfig.get(0));
        board = new String[dimension][dimension];
    }

    public void fillBoard(){
        for(int i = 0; i<dimension; i++){
            String [] row = boardConfig.get(i+1).split(" ");
            for(int j = 0; j<dimension; j++){
                board[i][j] = row[j];
            }
        }
    }

    public String toString(){
        String str = "";
        for(int i = 0; i<dimension; i++){
            for(int j = 0; j<dimension; j++){
                str+=board[i][j]+" ";
            }
            str+="\n";
        }
        return str;
    }

    public static void main(String[] args) {
        Board board = new Board();
        board.fillBoard();
        System.out.println(board);
    }

}
