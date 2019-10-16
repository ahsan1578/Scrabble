/**
 * @author D M Raisul Ahsan
 * @version 1.0
 *
 * This class extends the Player class and modifies for computer player
 */


package scrabble;


public class ComputerPlayer extends Player{
    private Tray tray;
    private Solver solver;
    private int score;
    public boolean noMove;


    /**
     * Contructs the computer player
     * @param tray the tray for the computer
     * @param board the current board
     * @param dictionary the word dictionary
     */
    public ComputerPlayer(Tray tray, Board board, Dictionary dictionary){
        super(tray);
        solver = new Solver(board, dictionary);
        solver.setRack(tray.getTileList());
        noMove =false;
    }


    /**
     * Sets next move word and indexes
     */
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


    /**
     * @return true is no move is possible
     */
    public boolean isNoMove() {
        return noMove;
    }

    /**
     * Sets computer tray
     * @param tray the tray
     */
    @Override
    public void setTray(Tray tray) {
        this.tray = tray;
        solver.setRack(tray.getTileList());
    }


    /**
     * Get the score for current move
     * @return the score
     */
    public int getScore() {
        return solver.getScore();
    }
}
