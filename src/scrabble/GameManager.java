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

    public void setFirstMove(boolean firstMove) {
        isFirstMove = firstMove;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public TileBag getTileBag() {
        return tileBag;
    }

    public int getHumanScore() {
        return humanScore;
    }

    public void setHumanScore(int humanScore) {
        this.humanScore = humanScore;
    }

    public Board getBoard() {
        return board;
    }

    public Tray getHumanTray() {
        return humanTray;
    }

    public Tray getComputerTray() {
        return computerTray;
    }

    public boolean isAFinalValidWordByHuman(String word){
     //   String word = board.getWordPlayedByHuman();
        if(dictionary.doesWordExist(word.toLowerCase())){
            return true;
        }else {
            return false;
        }
    }

    public boolean continuousHumanMove(){
    //    board.setWordAndIndexes();
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

    public void setHumanMoveCoordinates(LinkedList<int[]> coordinates){
        humanPlayer.setNextMoveCoordinates(coordinates);
    }

    public void setComMoveCoordinates(LinkedList<int[]> comMoveCoordinates){
        computerPlayer.setNextMoveCoordinates(comMoveCoordinates);
    }

    public void setTurn(char turn) {
        this.turn = turn;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public boolean checkGameOver(Player humanPlayer, Player computerPlayer){
        if(tileBag.getSize() == 0 &&(humanPlayer.getTray().getSize() == 0 || computerPlayer.getTray().getSize() == 0)){
            return true;
        }
        if(lastWordPlayedByComputer.equals("") && lastWordPlayedByHuman.equals("")){
            return true;
        }
        return false;
    }

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

    public Player getComputerPlayer() {
        return computerPlayer;
    }

    public Player getHumanPlayer() {
        return humanPlayer;
    }

    public void setHumanGaveUpTurn(boolean humanGaveUpTurn) {
        this.humanGaveUpTurn = humanGaveUpTurn;
    }

    public void setComputerTray(String word, LinkedList<int[]> coordinates){
        System.out.println("vfdhbfhbv "+word );
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
        System.out.println("This list is "+list);
        if(tileBag.getSize()>=list.size()){
            computerTray.addRandomTiles(list.size(),tileBag);
        }else if(tileBag.getSize()>0){
            computerTray.addRandomTiles(tileBag.getSize(),tileBag);
        }
        System.out.println("The computer tray: "+computerTray.getTileList());
        computerPlayer.setTray(computerTray);
        System.out.println("computer tray "+computerPlayer.getTray().getTileList());
    }

    public int getComScore() {
        return comScore;
    }

    public Player getWinner() {
        return winner;
    }

    public void play(){
        if(!isGameOver && turn == 'H'){
            if(humanGaveUpTurn){
                System.out.println("Human doesn't have any move");
                lastWordPlayedByHuman = "";
                turn = 'C';
            }else {
                String word = humanPlayer.getNextMoveWord();
                LinkedList<int[]> coordinates = humanPlayer.getNextMoveCoordinates();
                if(word.length()>0){
                    System.out.println("This is human word: "+word);
                    board.updateBoard(word,coordinates);
                    turn = 'C';
                }else {
                    turn = 'C';
                }
            }
        }

        if(!isGameOver && turn == 'C'){
            System.out.println("Here 1: "+computerPlayer.getTray().getTileList());
            computerPlayer.setNextMoveWordAndCoordinates();
            if(computerPlayer.isNoMove()){
                System.out.println("Computer doesn't have any move");
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
                System.out.println("here 2: "+computerPlayer.getTray().getTileList());
                System.out.println(tileBag.getSize());
                lastWordPlayedByComputer = word;
            }

        }
        if(checkGameOver(humanPlayer,computerPlayer)){
            System.out.println("The game is over");
            isGameOver = true;
            setWinner();
        }
        System.out.println("Human "+humanScore);
        System.out.println("Computer "+comScore);
    }
}
