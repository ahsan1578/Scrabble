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

    Board(String boardPath){
        this.boardConfig = new LinkedList<>();
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
    }

    public void fillBoard(){
        for(int i = 0; i<dimension; i++){
            String [] row = boardConfig.get(i+1).split(" ");
            for(int j = 0; j<dimension; j++){
                board[i][j] = row[j];
            }
        }
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
            if((board[x][y].charAt(0)>='a' && board[x][y].charAt(0)<='z')){
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
                (board[x][y].charAt(1)>='a' && board[x][y].charAt(1)<='z'))){
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
            if(!dictionary.doesWordExist(stringBuilder.toString())){
               return false;
            }
        }else if(i-1>=0 && existChar(i-1,j)){
            stringBuilder.append(findExistingWord(i,j,'U'));
            stringBuilder.append(c);
            if(isRotated){
                stringBuilder.reverse();
            }
            if(!dictionary.doesWordExist(stringBuilder.toString())){
                return false;
            }
        }else if(i+1<dimension && existChar(i+1,j)){
            stringBuilder.append(c);
            stringBuilder.append(findExistingWord(i,j,'D'));
            if(isRotated){
                stringBuilder.reverse();
            }
            if(!dictionary.doesWordExist(stringBuilder.toString())){
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
        int score = this.scoreFrequency.getScoreMap().get(c);
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
