/**
 * @author D M Raisul Ahsan
 * @version 1.0
 *
 * This class find the best possible (int terms of score) move for a given board and tray
 */


package scrabble;

import java.util.*;

public class Solver {
    private Board board;
    private LinkedList<Character> rack;
    private Dictionary dictionary;
    private String[][] anchors;
    private LinkedList<String> left;
    private LinkedHashMap<Integer, LinkedHashMap<String, LinkedHashMap<int[], Boolean>>> words;
    private LinkedList<Trie.Node> nodes;
    private LinkedList<int []> moveCoordinates;
    private String word;
    private int score;


    /**
     * Construct the Solver
     * @param board the board to make the move on
     * @param dictionary the dictionary of the legal words
     */
    public Solver(Board board, Dictionary dictionary){
        this.score = 0;
        this.board = board;
        rack = new LinkedList<>();
        this.dictionary = dictionary;
        this.anchors = board.getAnchorPoints();
        left = new LinkedList<>();
        words = new LinkedHashMap<>();
        nodes = new LinkedList<>();
        this.moveCoordinates = new LinkedList<>();
        this.word = "";
    }


    /**
     * For a given anchor square creates parts of words to be placed to the left of the anchor point using the existing tray
     * @param partialWord partial word to add new letters on
     * @param node current node on Trie
     * @param limit the number letters can be added to the partial word (depends on the tray and the number of empty squares)
     * @param x row index on board
     * @param y column index on board
     * @param isRotated if the board is currently rotated
     * @param anchorX row index of anchor square
     * @param anchorY column index of the anchor square
     */
    private void leftPart(String partialWord, Trie.Node node, int limit, int x, int y, boolean isRotated, int anchorX, int anchorY){
        if(limit>0){
            for (int i = 0; i<26; i++){
                Trie.Node n = node.getChildren(i);
                Character c = (char) ('a' + i);
                if(n != null && (rack.contains(c)||rack.contains('*'))){
                    boolean usingWildTile = false;
                    Character character = c;
                    if(!(rack.contains(c))){
                        rack.remove(Character.valueOf('*'));
                        character = (char)('a'+i-32);
                        usingWildTile =true;
                    }else {
                        rack.remove(c);
                    }
                    leftPart(partialWord+character,n,limit-1, x, y,isRotated, anchorX, anchorY);
                    if(usingWildTile){
                        rack.add('*');
                    }else {
                        rack.add(c);
                    }
                }
            }
        }
        left.add(partialWord);
        nodes.add(node);
        if(board.legalVerticalMove(partialWord,x,y-1)){
            extendRight(partialWord,node,x,y, isRotated,anchorX, anchorY);
       }
    }


    /**
     * For a given left part of a word extends it to the right using the remaining letters on tray
     * Checks score of that word
     * Adds the word, scores, indexes of the word in a hashmap
     * @param partialWord the partial word to extend right
     * @param node current node on trie
     * @param x row index on board for current letter
     * @param y column index on board current letter
     * @param isRotated if the board is currently rotated
     * @param anchorX row index of the anchor square
     * @param anchorY column index of the anchor square
     */
    public void extendRight(String partialWord, Trie.Node node, int x, int y, boolean isRotated, int anchorX, int anchorY){
        if((x> anchorX || y> anchorY) && !board.existChar(x,y) && node.isWord()){
            LinkedHashMap<int[],Boolean> map = new LinkedHashMap<>();
            LinkedList<int[]> index = board.getWordCoordinates(x,y-1,isRotated,partialWord);
            boolean allTilesUsed = false;
            if(rack.size() == 0){
                allTilesUsed = true;
            }
            int score = board.getTotalScore(index,partialWord,allTilesUsed);
            map.put(new int[]{x,y-1,score}, isRotated);
            LinkedHashMap<String, LinkedHashMap<int[], Boolean>> wordXYMap = new LinkedHashMap<>();
            wordXYMap.put(partialWord, map);
            words.put(score,wordXYMap);
        }
        if(y>=0 && y<board.getDimension()){
            if(!board.existChar(x,y)){
                for(int i = 0; i<26; i++){
                    Trie.Node n = node.getChildren(i);
                    Character c = (char)('a'+i);
                    if(n != null && (rack.contains(c) || rack.contains('*')) && board.legalAcrossMove(x,y,c)){
                        boolean usingWildTile = false;
                        Character character = c;
                        if(!(rack.contains(c))){
                            rack.remove(Character.valueOf('*'));
                            character = (char)('a'+i-32);
                            usingWildTile =true;
                        }else {
                            rack.remove(c);
                        }

                        extendRight(partialWord+character,n,x,y+1,isRotated,anchorX,anchorY);
                        if(usingWildTile){
                            rack.add('*');
                        }else {
                            rack.add(c);
                        }
                    }
                }
            }else {
                char c = board.getChar(x,y);
                Trie.Node n;
                if(c<'a'){
                    n = node.getChildren(c+32-'a');
                }else {
                    n = node.getChildren(c-'a');
                }
                if(n != null){
                    extendRight(partialWord+c,n,x,y+1, isRotated,anchorX,anchorY);
                }
            }
        }
    }


    /**
     * Find all possible words/moves that can be made either horizontally or vertically
     */
    public void findAllPossibleWordsOneRoation(){
        for (int i = 0; i<board.getDimension(); i++){
            for (int j = 0; j<board.getDimension(); j++){
                if(board.getAnchorPoints()[i][j].equals("A")){
                    int leftEmptySqr = board.totalLeftLimit(i,j);
                    if(leftEmptySqr == 0){
                        String leftPart = board.findExistingWord(i,j,'L');
                        Trie.Node node= dictionary.getTrie().getRootNode();
                        for(int  k = 0; k<leftPart.length(); k++){
                            node = node.getChildren(leftPart.toLowerCase().charAt(k)-'a');
                            if(node == null){
                                break;
                            }
                        }
                        if(node == null){
                            break;
                        }
                        extendRight(leftPart,node,i,j,board.isRotated(),i,j);
                    }else {
                        if(leftEmptySqr>=7){
                            leftPart("",dictionary.getTrie().getRootNode(),7,i,j,board.isRotated(),i,j);
                        }else {
                            leftPart("",dictionary.getTrie().getRootNode(),leftEmptySqr,i,j,board.isRotated(),i,j);
                        }
                    }
                }
            }
        }
    }

    /**
     * Find all possible words
     */
    public void findAllPossWords(){
        words.clear();
        board.setAnchorPoints();
        findAllPossibleWordsOneRoation();
        board.rotateBoard();
        board.setAnchorPoints();
        findAllPossibleWordsOneRoation();
        board.rotateBack();
        board.setAnchorPoints();
    }


    /**
     * Finds the word that gets the most score
     * Finds the coordinates/indexes of that word on board
     */
    public void setMoveCoordinatesAndWord() {
        findAllPossWords();
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        };
        LinkedList<Integer> list = new LinkedList<>(words.keySet());
        list.sort(comparator);
        if(list.size() == 0 || (list.size() == 1 && list.getFirst() == 0)){
            word = "";
            moveCoordinates.clear();
            return;
        }
        this.score = list.getLast();
        HashMap<String, LinkedHashMap<int[],Boolean>> map = words.get(list.getLast());
        String string = map.keySet().toArray()[0].toString();
        HashMap<int[],Boolean> map2 = map.get(string);
        Set<int[]> set = map2.keySet();
        int[][] arr = set.toArray(new int[0][0]);
        int x = arr[0][0];
        int y = arr[0][1];
        boolean isRotated = map2.get(arr[0]);
        moveCoordinates = board.getWordCoordinates(x,y,isRotated,string);
        word = string;
    }


    /**
     * @return score for the best word
     */
    public int getScore() {
        return score;
    }


    /**
     * @return index of the best word
     */
    public LinkedList<int[]> getMoveCoordinates() {
        return moveCoordinates;
    }

    /**
     * @return best word
     */
    public String getWord() {
        return word;
    }

    /**
     * Sets the tray
     * @param rack the tray
     */
    public void setRack(LinkedList<Character> rack) {
        this.rack = rack;
    }
}
