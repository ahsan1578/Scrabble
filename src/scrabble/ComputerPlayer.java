package scrabble;

import java.util.LinkedList;

public class ComputerPlayer extends Player{
    private Tray tray;
    private Solver solver;
    private int score;
    public boolean noMove;


    public ComputerPlayer(Tray tray, Board board, Dictionary dictionary){
        super(tray);
        System.out.println(tray.getTileList());
        solver = new Solver(board, dictionary);
        solver.setRack(tray.getTileList());
        noMove =false;
    }


    public void setNextMoveWordAndCoordinates() {
        solver.setMoveCoordinatesAndWord();
        setNextMoveCoordinates(solver.getMoveCoordinates());
        setNextMoveWord(solver.getWord());
        if(solver.getWord().equals("")){
            noMove = true;
        }else {
            noMove = false;
        }
    }


    public boolean isNoMove() {
        return noMove;
    }

    @Override
    public void setTray(Tray tray) {
        this.tray = tray;
        solver.setRack(tray.getTileList());
    }



    public int getScore() {
        return solver.getScore();
    }
}
