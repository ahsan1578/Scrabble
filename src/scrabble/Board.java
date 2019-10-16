/**
 * @author D M Raisul Ahsan
 * @version 1.0
 *
 * This class is responsible for creating and maintaining the board for the game
 */

package scrabble;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class Board {
    private String [][] board;
    private LinkedList<String> boardConfig;
    private int dimension;
    private boolean isRotated;
    private String[][] anchorPoints;
    private ScoreFrequency scoreFrequency;
    private Dictionary dictionary;
    private boolean isHumanMovingHorizontal;
    private boolean isHumanCurrentlyPlaying;
    private LinkedList<int[]> humanCurrMove;
    private char humanCurrentPlayingChar;
    private boolean newCharSelected;
    private String wordPlayedByHuman;
    private LinkedList<int[]> humanMoveIndexes;
    private boolean isFirstMove;


    /**
     * Creates 2D board string array for a given length and width
     * @param boardPath the text file that has information about board dimensions
     */
    Board(String boardPath){
        this.isFirstMove = true;
        this.newCharSelected = false;
        this.isHumanMovingHorizontal = false;
        this.boardConfig = new LinkedList<>();
        this.isHumanCurrentlyPlaying = false;
        this.humanCurrMove = new LinkedList<>();
        InputStream inputStream = getClass().getResourceAsStream(boardPath);
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
        isRotated = false;

        anchorPoints = new String[dimension][dimension];

        for(int i = 0; i< dimension; i++){
            for(int j = 0; j< dimension; j++){
                anchorPoints[i][j] = "*";
            }
        }
        this.scoreFrequency = new ScoreFrequency("tiles.txt");
        this.dictionary = new Dictionary("sowpods.txt");
        humanMoveIndexes = new LinkedList<>();
    }


    /**
     * Fills the board array with strings
     * Creates the initial board state when no move has been made
     */
    public void fillBoard(){
        for(int i = 0; i<dimension; i++){
            String [] row = boardConfig.get(i+1).split(" ");
            for(int j = 0; j<dimension; j++){
                board[i][j] = row[j];
            }
        }
    }

    /**
     * Sets true if it's the first move of the game, false otherwise
     * @param firstMove if it's the first move of the game
     */
    public void setFirstMove(boolean firstMove) {
        isFirstMove = firstMove;
    }


    /**
     * Sets the word that human chose to play and the indexes on the board
     * Checks the adjacent already existing words on board and concatenate with the letters played by human
     */
    public void setWordAndIndexes(){
        humanMoveIndexes.clear();
        StringBuilder stringBuilder = new StringBuilder("");
        int x = humanCurrMove.getFirst()[0];
        int y = humanCurrMove.getFirst()[1];
        int x1 = x;
        int y1 = y;
        if(isHumanMovingHorizontal){
            stringBuilder.append(findExistingWord(x,y,'L'));
            y1 -=  stringBuilder.length();
            stringBuilder.append(getChar(x,y));
            stringBuilder.append(findExistingWord(x,y,'R'));
            wordPlayedByHuman = stringBuilder.toString();
            for(int i = 0; i<stringBuilder.length(); i++){
                humanMoveIndexes.add(new int[]{x1,y1+i});
            }
        }else {
            stringBuilder.append(findExistingWord(x,y,'U'));
            x1 -= stringBuilder.length();
            stringBuilder.append(getChar(x,y));
            stringBuilder.append(findExistingWord(x,y,'D'));
            wordPlayedByHuman = stringBuilder.toString();
            for(int i = 0; i<stringBuilder.length(); i++){
                humanMoveIndexes.add(new int[]{x1+i,y1});
            }
        }
    }

    /**
     * @return the word that human player chose to play
     */
    public String getWordPlayedByHuman() {
        setWordAndIndexes();
        return wordPlayedByHuman;
    }

    /**
     * Sets the word chosen by human player directly from argument
     * @param wordPlayedByHuman word chosen by human player
     */
    public void setWordPlayedByHuman(String wordPlayedByHuman) {
        this.wordPlayedByHuman = wordPlayedByHuman;
    }

    /**
     * @return indexes on the board for the word chosen by human player
     */
    public LinkedList<int[]> getHumanMoveIndexes() {
        return humanMoveIndexes;
    }

    public boolean isNewCharSelected() {
        return newCharSelected;
    }

    /**
     * Set true if human player discarded last move and chose a different letter from the tray
     * @param newCharSelected if the human selected a new letter fro the tray
     */
    public void setNewCharSelected(boolean newCharSelected) {
        this.newCharSelected = newCharSelected;
    }

    /**
     * @return indexes of human currents move
     */
    public LinkedList<int[]> getHumanCurrMove() {
        return humanCurrMove;
    }

    /**
     * sets the most recent letter played by human player
     * @param humanCurrentPlayingChar most recent letter played by human player
     */
    public void setHumanCurrentPlayingChar(char humanCurrentPlayingChar) {
        this.humanCurrentPlayingChar = humanCurrentPlayingChar;
    }

    /**
     * Checks if a given move at a particular index of the board adjacent any other index with already existing letter
     * @param x row index of the square being checked
     * @param y column index of the square being checked
     * @return true if the index is adjacent, false otherwise
     */
    public boolean isAdjacentPlay(int x, int y){
        int up = x - 1;
        int down = x+1;
        int left = y-1;
        int right = y+1;

        if(up>=0 && existChar(up,y)){
            return true;
        }else if(down<dimension && existChar(down,y)){
            return true;
        }else if(left >= 0 && existChar(x,left)){
            return true;
        }else if(right<dimension && existChar(x,right)){
            return true;
        }
        return false;
    }

    /**
     * Checks if human player is making move at the same row or column
     * checks if the first letter moved is adjacent move
     * @return true if above conditions are met, false otherwise
     */
    public boolean isLegalHumanMove(){
        if(humanCurrentPlayingChar == (char)0){
            return false;
        }
        if(isHumanMovingHorizontal){
            if(humanCurrMove.size()==1){
                int x = humanCurrMove.getLast()[0];
                int y = humanCurrMove.getLast()[1];
                if(!isFirstMove && !isAdjacentPlay(x,y)){
                    return false;
                }
            }
            if(humanCurrMove.size()>1){
                if(humanCurrMove.getLast()[0] != humanCurrMove.getFirst()[0]){
                    return false;
                }
            }
            int x = humanCurrMove.getLast()[0];
            int y = humanCurrMove.getLast()[1];

            if(!isFirstMove && !legalAcrossMove(x,y,humanCurrentPlayingChar)){
                return false;
            }
        }else {
            rotateBoard();
            if(humanCurrMove.size()==1){
                int x = dimension - humanCurrMove.getLast()[1]-1;
                int y = humanCurrMove.getLast()[0];
                if(!isFirstMove && !isAdjacentPlay(x,y)){
                    rotateBack();
                    return false;
                }
            }
            if(humanCurrMove.size()>1){
                int x1 = dimension - humanCurrMove.getLast()[1]-1;
                int x2 = dimension - humanCurrMove.getFirst()[1]-1;
                if(x1 != x2){
                    rotateBack();
                    return false;
                }
            }
            int x = dimension - humanCurrMove.getLast()[1]-1;
            int y = humanCurrMove.getLast()[0];
            if(!isFirstMove && !legalAcrossMove(x,y,humanCurrentPlayingChar)){
                rotateBack();
                return false;
            }
            if(isRotated){
                rotateBack();
            }
        }
        return true;
    }

    /**
     * @return most recent letter played by human
     */
    public char getHumanCurrentPlayingChar() {
        return humanCurrentPlayingChar;
    }



    /**
     * Put a char on the board at a given index
     * @param c the char to be put
     * @param x the row index of the square
     * @param y the column index of the square
     */
    public void putChar(char c, int x, int y){
        StringBuilder str = new StringBuilder(board[x][y]);
        for(int i = 0; i<str.length(); i++){
            if(str.charAt(i) == '.'){
                str.replace(i,i+1,""+c);
                break;
            }
        }
        board[x][y] = str.toString();
    }


    /**
     * Remove a char from a given index on the board
     * @param x the row index of the char to be removed
     * @param y coloumn index of the char to be removed
     */
    public void removeChar(int x, int y){
        char c1 = board[x][y].charAt(0);
        char c2 = board[x][y].charAt(1);

        if((c1>='a' && c1<='z') || (c1>='A' && c1<= 'Z')){
            String str = board[x][y].replaceFirst(""+c1, ".");
            board[x][y] = str;
        }else if((c2>='a' && c2<='z') || (c2>='A' && c2<= 'Z')){
            String str = board[x][y].replaceFirst(""+c2,".");
            board[x][y] = str;
        }
    }


    /**
     * Sets true is human is making current move
     * @param humanCurrentlyPlaying if human is making current move
     */
    public void setHumanCurrentlyPlaying(boolean humanCurrentlyPlaying) {
        isHumanCurrentlyPlaying = humanCurrentlyPlaying;
    }


    /**
     * checks if human is making the current move
     * @return true if human is making the current move, false otherwise
     */
    public boolean isHumanCurrentlyPlaying() {
        return isHumanCurrentlyPlaying;
    }


    /**
     * Checks if human is making a horizontal move
     * @return true if human is making a horizontal move, false otherwise
     */
    public boolean isHumanMovingHorizontal() {
        return isHumanMovingHorizontal;
    }


    /**
     * Sets tru if human is making a horizontal move
     * @param humanMovingHorizontal if human is making a horizontal move
     */
    public void setHumanMovingHorizontal(boolean humanMovingHorizontal) {
        isHumanMovingHorizontal = humanMovingHorizontal;
    }

    /**
     * Rotate the board 90 degrees anti-clockwise
     */
    public void rotateBoard(){
        String [][] temp = new String[dimension][dimension];
        for(int i = 0;i<dimension; i++){
            for(int j = 0; j<dimension; j++){
                temp[dimension-j-1][i] = board[i][j];
            }
        }
        this.board = temp;
        isRotated = true;
    }

    /**
     * @param x row index
     * @param y column index
     * @return the char at the given index on board
     */
    public char getChar(int x, int y){
        if(existChar(x,y)){
            if((board[x][y].charAt(0)>='a' && board[x][y].charAt(0)<='z') || (board[x][y].charAt(0)>='A' && board[x][y].charAt(0)<='Z')){
                return board[x][y].charAt(0);
            }else {
                return board[x][y].charAt(1);
            }
        }
        return 0;
    }



    /**
     * @param x row index
     * @param y column index
     * @return true if there is already an existing letter in the given index, false otherwise
     */
    public boolean existChar(int x, int y){
        if((x<dimension && x>=0 && y<dimension && y>=0) && ((board[x][y].charAt(0)>='a' && board[x][y].charAt(0)<='z')||
                (board[x][y].charAt(1)>='a' && board[x][y].charAt(1)<='z') ||
                (board[x][y].charAt(0)>='A' && board[x][y].charAt(0)<='Z')||
                (board[x][y].charAt(1)>='A' && board[x][y].charAt(1)<='Z'))){
            return true;
        }
        return false;
    }
    public String findExistingWord(int x, int y, char dir){
        int k1 = 0;
        int k2 = 0;
        switch (dir){
            case 'R':
                k2 = 1;
                break;
            case 'L':
                k2 = -1;
                break;
            case 'U':
                k1 = -1;
                break;
            case 'D':
                k1 = 1;
                break;
        }
        int i = x+k1;
        int j = y+k2;
        StringBuilder str = new StringBuilder("");
        while (i<dimension && i>=0 && j<dimension && j>=0 && existChar(i,j)){
            str.append(getChar(i,j));
            i+=k1;
            j+=k2;
        }
        if(k1<0 || k2<0){
            str.reverse();
        }
        return str.toString();
    }


    /**
     * Checks for a horizontal move, if the words being made vertically are valid words
     * @param word word to be checked
     * @param endX row index of the last letter of the word
     * @param endY column index of the last letter of the word
     * @return true if the words being made vertically are valid words
     */
    public boolean legalVerticalMove(String word, int endX, int endY){
        boolean isLegal = true;
        for(int i = 0; i<word.length(); i++){
            isLegal=legalAcrossMove(endX, endY-i, word.charAt(word.length()-1-i));
            if(!isLegal){
                break;
            }
        }
        return isLegal;
    }


    /**
     * For a letter to be played, checks if the move creates legal across word
     * @param i row index
     * @param j column index
     * @param c letter to be played
     * @return true if the move is legal
     */
    public boolean legalAcrossMove(int i, int j, char c){
        StringBuilder stringBuilder = new StringBuilder();
        if(i-1>=0 && i+1<dimension && existChar(i-1,j)&&existChar(i+1,j)){
            stringBuilder.append(findExistingWord(i,j,'U'));
            stringBuilder.append(c);
            stringBuilder.append(findExistingWord(i,j,'D'));
            if(isRotated){
                stringBuilder.reverse();
            }
            if(!dictionary.doesWordExist(stringBuilder.toString().toLowerCase())){
               return false;
            }
        }else if(i-1>=0 && existChar(i-1,j)){
            stringBuilder.append(findExistingWord(i,j,'U'));
            stringBuilder.append(c);
            if(isRotated){
                stringBuilder.reverse();
            }
            if(!dictionary.doesWordExist(stringBuilder.toString().toLowerCase())){
                return false;
            }
        }else if(i+1<dimension && existChar(i+1,j)){
            stringBuilder.append(c);
            stringBuilder.append(findExistingWord(i,j,'D'));
            if(isRotated){
                stringBuilder.reverse();
            }
            if(!dictionary.doesWordExist(stringBuilder.toString().toLowerCase())){
                return false;
            }
        }
        return true;
    }


    /**
     * @return number of rows/column of the board
     */
    public int getDimension() {
        return dimension;
    }


    /**
     * Rotate an already rotated board to it's original position
     */
    public void rotateBack(){
        String[][] temp = new String[dimension][dimension];
        if(isRotated) {
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    temp[j][dimension - i - 1] = board[i][j];
                }
            }
            this.board = temp;
            isRotated = false;
        }
    }

    /**
     * For a word and index of it's last letter finds the indexes of the whole word
     * @param x row index of the last letter
     * @param y column index of the last letter
     * @param isRotated if the board was rotated at the time of choosing the word
     * @param word the word
     * @return the indexes of the whole word
     */
    public LinkedList<int[]> getWordCoordinates(int x,int y, boolean isRotated, String word){
        LinkedList<int[]> list = new LinkedList<>();
        for(int i = 0; i<word.length(); i++){
            if(isRotated){
                int [] arr = rotateBackCoordinate(new int[]{x, y-i});
                list.addFirst(arr);
            }else {
                list.addFirst(new int[]{x,y-i});
            }
        }
        return list;
    }


    /**
     * Convert an index on the rotated board to index on the original board
     * @param arr index x, y array
     * @return new index
     */
    public int[] rotateBackCoordinate(int [] arr){
        int x = arr[1];
        int y = dimension - arr[0]-1;
        return new int[]{x,y};
    }


    /**
     * Updates the board by putting a word
     * @param word the word to be put
     * @param coordinates the indexes of the word
     */
    public void updateBoard(String word, LinkedList<int[]> coordinates){
        int putCount = 0;
        for(int [] arr: coordinates){
            if(!existChar(arr[0],arr[1])){
                putChar(word.charAt(putCount),arr[0],arr[1]);
                putCount++;
            }else {
                putCount++;
            }
        }
    }


    /**
     * @return the board
     */
    public String[][] getBoard(){
        return board;
    }


    /**
     * Sets the anchor points. Refer to README to learn more about anchor points
     */
    public void setAnchorPoints(){
        for(int i = 0; i<dimension; i++){
            for(int j = 0; j<dimension; j++){
                anchorPoints[i][j] = "*";
            }
        }
        for(int i = 0; i<dimension; i++){
            for(int j = 0; j<dimension; j++){
                if(existChar(i,j)){
                    if(j-1>=0 && !existChar(i,j-1)){
                        if(anchorPoints[i][j-1].equals("*")){
                            anchorPoints[i][j-1] = "A";
                        }
                    }
                    if(j+1<dimension && !existChar(i,j+1)){
                        if(anchorPoints[i][j+1].equals("*")){
                            anchorPoints[i][j+1] = "A";
                        }
                    }
                    if(i-1>=0 && !existChar(i-1,j)){
                        if(anchorPoints[i-1][j].equals("*")){
                            anchorPoints[i-1][j] = "A";
                        }
                    }
                    if(i+1<dimension && !existChar(i+1,j)){
                        if(anchorPoints[i+1][j].equals("*")){
                            anchorPoints[i+1][j] = "A";
                        }
                    }
                }
            }
        }
    }


    /**
     * Finds the number of empty squares to the left of a given square
     * @param x row index of a given square
     * @param y column index of a given square
     * @return the number of empty squares
     */
    public int totalLeftLimit(int x, int y){
        int count  = 0;
        if(y-1>=0 && !existChar(x,y-1)){
            while (y-1>=0 && !existChar(x,y-1)){
                count++;
                y--;
            }
            if(y-1>=0 && existChar(x,y-1)){
                count--;
            }
        }
        return count;
    }


    /**
     * @return a 2D array with anchor points indicated (Number of rows and columns is same as the board)
     */
    public String[][] getAnchorPoints() {
        return anchorPoints;
    }


    /**
     * Get score of a letter on a given square, consider the multipliers
     * @param c the letter
     * @param x row index
     * @param y column index
     * @return the score
     */
    private int getLetterScore(char c, int x, int y){
        int score = 0;
        if(!(c>='A' && c <= 'Z')){
            score = this.scoreFrequency.getScoreMap().get(c);
        }
        char c1 = board[x][y].charAt(1);
        if(!existChar(x,y) && c1>='0' && c1<='9'){
            score = score*(c1-'0');
        }
        return score;
    }


    /**
     * Calculate score of a word without considering the across words
     * @param coordinates the indexes of the word on the board
     * @param word the word
     * @return the score
     */
    public int getScoreForOneWord(LinkedList<int[]> coordinates, String word){
        int score = 0;
        int wordMultiplier = 1;
        boolean wasRotated = false;
        if(isRotated){
            rotateBack();
            wasRotated = true;
        }
        for(int i = 0; i<word.length(); i++){
            char c = word.charAt(i);
            int x = coordinates.get(i)[0];
            int y = coordinates.get(i)[1];
            score += getLetterScore(c,x,y);
            if(!existChar(x,y) && board[x][y].charAt(0)>='0' && board[x][y].charAt(0)<='9'){
                wordMultiplier *= board[x][y].charAt(0)-'0';
            }
        }
        score *= wordMultiplier;
        if(wasRotated){
            rotateBoard();
        }
        return score;
    }



    /**
     * Calculate a word score with considering the across words
     * @param coordinates indexes of the word on the board
     * @param word the word
     * @param allTilesUsed if all the tiles have been used to made the move
     * @return the score
     */
    public int getTotalScore(LinkedList<int[]> coordinates, String word, boolean allTilesUsed){
        int score =0;
        LinkedList<LinkedList<int[]>> indexes = new LinkedList<>();
        LinkedList<String> words = new LinkedList<>();
        indexes.add(coordinates);
        words.add(word);
        boolean wasRotated = false;
        if(isRotated){
            rotateBack();
            wasRotated = true;
        }
        char dir = '*';
        if(coordinates.getFirst()[0] == coordinates.get(1)[0]){
            dir = 'H';
        }else {
            dir = 'V';
        }
        int count = 0;
        for(int[] arr: coordinates){
            int x = arr[0];
            int y = arr[1];
            char c = word.charAt(count);
            StringBuilder stringBuilder = new StringBuilder();
            if(!existChar(x,y)){
                if(dir=='H'){
                    String up = findExistingWord(x,y,'U');
                    String down = findExistingWord(x,y,'D');
                    int newX = x;
                    if(up.length()>0){
                        newX = newX - up.length();
                    }
                    stringBuilder.append(up);
                    stringBuilder.append(c);
                    stringBuilder.append(down);
                    String newWord = stringBuilder.toString();
                    if(newWord.length()>1){
                        LinkedList<int[]> index = new LinkedList<>();
                        for (int i = 0; i<newWord.length(); i++){
                            int indexX = newX + i;
                            int indexY = y;
                            index.add(new int[]{indexX,y});
                        }
                        indexes.add(index);
                        words.add(newWord);
                    }
                }else{
                    String left = findExistingWord(x,y,'L');
                    String right = findExistingWord(x,y,'R');
                    int newY = y;
                    if(left.length()>0){
                        newY = newY-left.length();
                    }
                    stringBuilder.append(left);
                    stringBuilder.append(c);
                    stringBuilder.append(right);
                    String newWord = stringBuilder.toString();
                    if(newWord.length()>1){
                        LinkedList<int[]> index = new LinkedList<>();
                        for(int i = 0; i<newWord.length(); i++){
                            int indexX = x;
                            int indexY = newY + i;
                            index.add(new int[]{indexX,indexY});
                        }
                        indexes.add(index);
                        words.add(newWord);
                    }
                }
            }
            count++;
        }
        for(int i = 0; i<words.size(); i++){
            score+=getScoreForOneWord(indexes.get(i),words.get(i));
        }
        if(wasRotated){
            rotateBoard();
        }
        if(allTilesUsed){
            score+=50;
        }
        return score;
    }


    /**
     * @return string representation of the board
     */
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

    /**
     * @return true if the board is currently rotated
     */
    public boolean isRotated() {
        return isRotated;
    }
}
