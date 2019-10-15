package scrabble;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
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




    public void fillBoard(){
        for(int i = 0; i<dimension; i++){
            String [] row = boardConfig.get(i+1).split(" ");
            for(int j = 0; j<dimension; j++){
                board[i][j] = row[j];
            }
        }
    }

    public void setFirstMove(boolean firstMove) {
        isFirstMove = firstMove;
    }


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
            System.out.println("uhuh "+wordPlayedByHuman);
            for(int i = 0; i<stringBuilder.length(); i++){
                humanMoveIndexes.add(new int[]{x1,y1+i});
                System.out.println("gfhgfhg"+humanMoveIndexes);
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

    public String getWordPlayedByHuman() {
        setWordAndIndexes();
        return wordPlayedByHuman;
    }

    public void setWordPlayedByHuman(String wordPlayedByHuman) {
        this.wordPlayedByHuman = wordPlayedByHuman;
    }

    public LinkedList<int[]> getHumanMoveIndexes() {
        return humanMoveIndexes;
    }

    public boolean isNewCharSelected() {
        return newCharSelected;
    }

    public void setNewCharSelected(boolean newCharSelected) {
        this.newCharSelected = newCharSelected;
    }

    public LinkedList<int[]> getHumanCurrMove() {
        return humanCurrMove;
    }

    public void setHumanCurrentPlayingChar(char humanCurrentPlayingChar) {
        this.humanCurrentPlayingChar = humanCurrentPlayingChar;
    }

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

    public boolean isLegalHumanMove(){
        if(humanCurrentPlayingChar == (char)0){
            return false;
        }
        if(isHumanMovingHorizontal){
            System.out.println(isHumanMovingHorizontal);
            if(humanCurrMove.size()==1){
                int x = humanCurrMove.getLast()[0];
                int y = humanCurrMove.getLast()[1];
                if(!isFirstMove && !isAdjacentPlay(x,y)){
                    return false;
                }
            }
            if(humanCurrMove.size()>1){
                if(humanCurrMove.getLast()[0] != humanCurrMove.getFirst()[0]){
                    System.out.println(humanCurrMove.getLast()[0]+" "+humanCurrMove.getFirst()[0]);
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
                    System.out.println("move okay");
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
                System.out.println("rotated");
            }
        }
        return true;
    }

    public char getHumanCurrentPlayingChar() {
        return humanCurrentPlayingChar;
    }

    public void putChar(char c, int x, int y){
     //   System.out.println("The char is: "+c);
        //String str = board[x][y].replaceFirst(".",""+c);
        StringBuilder str = new StringBuilder(board[x][y]);
        for(int i = 0; i<str.length(); i++){
            if(str.charAt(i) == '.'){
                str.replace(i,i+1,""+c);
                break;
            }
        }
        board[x][y] = str.toString();
    }

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

    public void setHumanCurrentlyPlaying(boolean humanCurrentlyPlaying) {
        isHumanCurrentlyPlaying = humanCurrentlyPlaying;
    }

    public boolean isHumanCurrentlyPlaying() {
        return isHumanCurrentlyPlaying;
    }

    public boolean isHumanMovingHorizontal() {
        return isHumanMovingHorizontal;
    }

    public void setHumanMovingHorizontal(boolean humanMovingHorizontal) {
        isHumanMovingHorizontal = humanMovingHorizontal;
    }

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

    public boolean existChar(int x, int y){
        //System.out.println(board[x][y]);
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
               // System.out.println(stringBuilder.toString() + " "+i+" "+j);
                return false;
            }
        }
        return true;
    }

    public int getDimension() {
        return dimension;
    }

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

    public int[] rotateBackCoordinate(int [] arr){
        int x = arr[1];
        int y = dimension - arr[0]-1;
        return new int[]{x,y};
    }

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

    public String[][] getBoard(){
        return board;
    }

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


    public String[][] getAnchorPoints() {
        return anchorPoints;
    }

    private int getLetterScore(char c, int x, int y){
        int score = 0;
        if(!(c>='A' && c <= 'Z')){
         //   System.out.println("getting score for "+c);
            score = this.scoreFrequency.getScoreMap().get(c);
        }
        char c1 = board[x][y].charAt(1);
        if(!existChar(x,y) && c1>='0' && c1<='9'){
            score = score*(c1-'0');
        }
        return score;
    }

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
//        System.out.println(words);
//        for(int[] arr: indexes.get(4)){
//            System.out.println(words.get(4));
//            System.out.println(arr[0]+" "+arr[1]);
//        }
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

    public String anchorPointsToString(){
        String str = "";
        for(int i = 0; i<dimension; i++){
            for(int j = 0; j<dimension; j++){
                str+=anchorPoints[i][j]+" ";
            }
            str+="\n";
        }
        return str;
    }

    public boolean isRotated() {
        return isRotated;
    }

    public static void main(String[] args) {
       Board board = new Board("b.txt");
       board.fillBoard();
       LinkedList<int[]> list = board.getWordCoordinates(2,14,false,"lemoned");
       for(int[] x: list){
           System.out.println(x[0]+"  "+x[1]);
       }
        System.out.println(board.getTotalScore(list,"lemoned",true));

    }

}
