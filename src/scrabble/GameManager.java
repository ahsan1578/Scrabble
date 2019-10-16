/**
 * @author D M Raisul Ahsan
 * @version 1.0
 *
 * This class controls the game and the moves, keeps the score, finds the winner
 */


package scrabble;

import java.util.LinkedList;

public class GameManager {
    private Board board;
    private Tray humanTray;
    private Tray computerTray;
    private TileBag tileBag;
    private Dictionary dictionary;
    private ScoreFrequency scoreFrequency;
    private Player humanPlayer;
    private ComputerPlayer computerPlayer;
    private char turn;
    private boolean isGameOver;
    private int humanScore;
    private int comScore;
    private String lastWordPlayedByHuman;
    private String lastWordPlayedByComputer;
    private Player winner;
    private boolean humanGaveUpTurn;
    private boolean isFirstMove;


    /**
     * Constructs the game manager
     */
    public GameManager(){
        this.comScore = 0;
        board = new Board("board.txt");
        board.fillBoard();
        tileBag = new TileBag("tiles.txt");
        scoreFrequency = new ScoreFrequency("tiles.txt");
        humanTray = new Tray(tileBag);
        computerTray = new Tray(tileBag);
        dictionary = new Dictionary("sowpods.txt");
        humanPlayer = new Player(humanTray);
        computerPlayer = new ComputerPlayer(computerTray,board,dictionary);
        turn = 'H';
        isGameOver = false;
        humanScore = 0;
        lastWordPlayedByHuman = "";
        lastWordPlayedByComputer = "";
        winner = null;
        humanGaveUpTurn = false;
        this.isFirstMove = true;
    }



    /**
     * Sets true if it's the first move, false otherwise
     * @param firstMove if it's the first move
     */
    public void setFirstMove(boolean firstMove) {
        isFirstMove = firstMove;
    }



    /**
     * @return true if it's the first move, false otherwise
     */
    public boolean isFirstMove() {
        return isFirstMove;
    }


    /**
     * @return the tilebag
     */
    public TileBag getTileBag() {
        return tileBag;
    }


    /**
     * @return human player's score
     */
    public int getHumanScore() {
        return humanScore;
    }


    /**
     * Sets human player's score
     * @param humanScore human player's score
     */
    public void setHumanScore(int humanScore) {
        this.humanScore = humanScore;
    }


    /**
     * @return the Board object
     */
    public Board getBoard() {
        return board;
    }


    /**
     * @return human current tray
     */
    public Tray getHumanTray() {
        return humanTray;
    }


    /**
     * Checks if the word played by human is a valid word
     * @param word the word
     * @return true if the word is a valid word
     */
    public boolean isAFinalValidWordByHuman(String word){
        if(dictionary.doesWordExist(word.toLowerCase())){
            return true;
        }else {
            return false;
        }
    }


    /**
     * Checks if there is any gap squares in the human move
     * @return true if there is no gap squares
     */
    public boolean continuousHumanMove(){
        LinkedList<int[]> moveIndex = board.getHumanCurrMove();
        LinkedList<int[]> wordIndex = board.getHumanMoveIndexes();
        for(int [] arr : moveIndex){
            boolean hasArr = false;
            for(int [] arr2: wordIndex){
                if(arr[0] == arr2[0] && arr[1] == arr2[1]){
                    hasArr = true;
                    break;
                }
            }
            if(!hasArr){
                return false;
            }
        }
        return true;
    }


    /**
     * Checks if the game is over
     * @param humanPlayer the human player
     * @param computerPlayer the computer player
     * @return true if the game is over
     */
    public boolean checkGameOver(Player humanPlayer, Player computerPlayer){
        if(tileBag.getSize() == 0 &&(humanPlayer.getTray().getSize() == 0 || computerPlayer.getTray().getSize() == 0)){
            return true;
        }
        if(lastWordPlayedByComputer.equals("") && lastWordPlayedByHuman.equals("")){
            return true;
        }
        return false;
    }


    /**
     * Finds the winners and end scores
     */
    public void setWinner() {
        int comDeductScore = 0;
        int humDeductScore = 0;
        int comAddScore = 0;
        int humAddScore = 0;
        int comFinScore = 0;
        int humFinScore = 0;
        for(int  i = 0; i<computerPlayer.getTray().getSize(); i++){
            comDeductScore = comDeductScore + scoreFrequency.getScoreMap().get(computerPlayer.getTray().getTileList().get(i));
        }
        for(int i = 0; i<humanPlayer.getTray().getSize(); i++){
            humDeductScore = humDeductScore + scoreFrequency.getScoreMap().get(humanPlayer.getTray().getTileList().get(i));
        }
        if(computerPlayer.getTray().getSize() == 0){
            comAddScore = humDeductScore;
        }
        if(humanPlayer.getTray().getSize() == 0){
            humAddScore = comDeductScore;
        }
        comFinScore = comScore - comDeductScore + comAddScore;
        humAddScore = humanScore - humDeductScore + humAddScore;
        if(comFinScore != humFinScore){
            if(comFinScore>humFinScore){
                comScore = comFinScore;
                winner = computerPlayer;
            }else {
                humanScore = humFinScore;
                winner = humanPlayer;
            }
        }else {
            if(comScore>humanScore){
                winner = computerPlayer;
            }else if(humanScore>comScore){
                winner = humanPlayer;
            }
        }
    }


    /**
     * @return computer player
     */
    public Player getComputerPlayer() {
        return computerPlayer;
    }


    /**
     * @return human player
     */
    public Player getHumanPlayer() {
        return humanPlayer;
    }


    /**
     * Sets true if human gave up the turn
     * @param humanGaveUpTurn if human gave up the turn
     */
    public void setHumanGaveUpTurn(boolean humanGaveUpTurn) {
        this.humanGaveUpTurn = humanGaveUpTurn;
    }


    /**
     * Sets the computer tray with new letters
     * @param word the last word computer played
     * @param coordinates indexes of the word
     */
    public void setComputerTray(String word, LinkedList<int[]> coordinates){
        LinkedList<Character> list = new LinkedList<>();
        int count = 0;
        for(int[] arr:coordinates){
            if(board.existChar(arr[0],arr[1])){
                count++;
            }else {
                list.add(word.charAt(count));
                count++;
            }
        }
        for(Character c: list){
            if(c>='A' && c<='Z'){
                computerTray.getTileList().removeFirstOccurrence('*');
            }else {
                computerTray.getTileList().removeFirstOccurrence(c);
            }
        }
        if(tileBag.getSize()>=list.size()){
            computerTray.addRandomTiles(list.size(),tileBag);
        }else if(tileBag.getSize()>0){
            computerTray.addRandomTiles(tileBag.getSize(),tileBag);
        }
        computerPlayer.setTray(computerTray);
    }


    /**
     * @return computer score
     */
    public int getComScore() {
        return comScore;
    }


    /**
     * @return the winner
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * @return true if game is over
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Alternate moves and updates board, checks if the game is over
     */
    public void play(){
        if(!isGameOver && turn == 'H'){
            if(humanGaveUpTurn){
                lastWordPlayedByHuman = "";
                turn = 'C';
            }else {
                String word = humanPlayer.getNextMoveWord();
                LinkedList<int[]> coordinates = humanPlayer.getNextMoveCoordinates();
                if(word.length()>0){
                    board.updateBoard(word,coordinates);
                    turn = 'C';
                }else {
                    turn = 'C';
                }
            }
        }

        if(!isGameOver && turn == 'C'){
            computerPlayer.setNextMoveWordAndCoordinates();
            if(computerPlayer.isNoMove()){
                lastWordPlayedByComputer = "";
                turn = 'H';
            }else {
                String word = computerPlayer.getNextMoveWord();
                LinkedList<int[]> coordinates = computerPlayer.getNextMoveCoordinates();
                setComputerTray(word,coordinates);
                if(word.length()>0){
                    board.updateBoard(word,coordinates);
                    comScore += computerPlayer.getScore();
                    turn = 'H';
                }else{
                    turn = 'C';
                }
                lastWordPlayedByComputer = word;
            }

        }
        if(checkGameOver(humanPlayer,computerPlayer)){
            isGameOver = true;
            setWinner();
        }
    }
}
