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



    public Solver(Board board, Dictionary dictionary){
        this.board = board;
        rack = new LinkedList<>();
        this.dictionary = dictionary;
        this.anchors = board.getAnchorPoints();
        left = new LinkedList<>();
        words = new LinkedHashMap<>();
        nodes = new LinkedList<>();
    }

    private void leftPart(String partialWord, Trie.Node node, int limit, int x, int y, boolean isRotated, int anchorX, int anchorY){
        if(limit>0){
            for (int i = 0; i<26; i++){
                Trie.Node n = node.getChildren(i);
                Character c = (char) ('a' + i);
                if(n != null && rack.contains(c)){
                    rack.remove(c);
                    leftPart(partialWord+c,n,limit-1, x, y,isRotated, anchorX, anchorY);
                    rack.add(c);
                }
            }
        }
        left.add(partialWord);
        nodes.add(node);
      //  System.out.println("From " + partialWord);
     //   System.out.println(rack);
        if(board.legalVerticalMove(partialWord,x,y-1)){
            extendRight(partialWord,node,x,y, isRotated,anchorX, anchorY);
       }

//        for (int i = 0; i<partialWord.length(); i++){
//            rack.add(partialWord.charAt(i));
//        }
//        System.out.println(rack);
    }



    public void extendRight(String partialWord, Trie.Node node, int x, int y, boolean isRotated, int anchorX, int anchorY){
        if((x> anchorX || y> anchorY) && !board.existChar(x,y) && node.isWord()){
            if(partialWord.equals("lemoned")){
                System.out.println(x+" "+y);
            }

            //    System.out.println("word: "+partialWord+" is: "+isRotated);
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
        //System.out.println("partial "+ partialWord + " x "+x+" y "+y);
        if(y>=0 && y<board.getDimension()){
            if(!board.existChar(x,y)){
                for(int i = 0; i<26; i++){
                    Trie.Node n = node.getChildren(i);
                    Character c = (char)('a'+i);
                    if(n != null && rack.contains(c) && board.legalAcrossMove(x,y,c)){
                        rack.remove(c);
                        extendRight(partialWord+c,n,x,y+1,isRotated,anchorX,anchorY);
                        rack.add(c);
          //              System.out.println(partialWord+"==  "+x+"==  "+y);
                    }
                }
            }else {
                char c = board.getChar(x,y);
                Trie.Node n = node.getChildren(c-'a');
                if(n != null){
                    extendRight(partialWord+c,n,x,y+1, isRotated,anchorX,anchorY);
                }
            }
        }
        //System.out.println(partialWord+"  "+x+"  "+y);

    }

    public void findAllPossibleWordsOneRoation(){
        for (int i = 0; i<board.getDimension(); i++){
            for (int j = 0; j<board.getDimension(); j++){
                if(board.getAnchorPoints()[i][j].equals("A")){
                    int leftEmptySqr = board.totalLeftLimit(i,j);
                    if(leftEmptySqr == 0){
                        String leftPart = board.findExistingWord(i,j,'L');
                        Trie.Node node= dictionary.getTrie().getRootNode();
                        for(int  k = 0; k<leftPart.length(); k++){
                            node = node.getChildren(leftPart.charAt(k)-'a');
                            if(node == null){
                                break;
                            }
                        }
                        if(node == null){
                            break;
                        }
                        System.out.println("Anchor points "+i+" "+j);
                        extendRight(leftPart,node,i,j,board.isRotated(),i,j);
                    }else {
                        System.out.println("Anchor point "+i+" "+j);
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

    public void findAllPossWords(){
        System.out.println(board);
        board.setAnchorPoints();
        System.out.println("bbh "+board.isRotated());
        findAllPossibleWordsOneRoation();
        board.rotateBoard();
        board.setAnchorPoints();
        System.out.println("hhg "+board.isRotated() );
        findAllPossibleWordsOneRoation();
        board.rotateBack();
        board.setAnchorPoints();
    }



    public static void main(String[] args) {
        Board board = new Board("d.txt");
        board.fillBoard();
        Dictionary dictionary = new Dictionary("sowpods.txt");
        Solver solver = new Solver(board,dictionary);
        solver.rack.add('n');
        solver.rack.add('t');
        solver.rack.add('n');
        solver.rack.add('b');solver.rack.add('t');solver.rack.add('o');solver.rack.add('i');
        long start = System.currentTimeMillis();
        solver.findAllPossWords();
        //Collections.sort(solver.words);
//        int i = 0;
//        for (String s: solver.left){
//            for(int j = 0; j<s.length(); j++){
//                solver.rack.remove(Character.valueOf(s.charAt(j)));
//            }
//            solver.makeWord(s,solver.nodes.get(i),10,10);
//            i++;
//        }
        System.out.println(solver.words);
        System.out.println(solver.words.size());
        System.out.println(solver.left.contains(""));
        long end = System.currentTimeMillis();
        long runtime = end-start;
        System.out.println(runtime);
        HashMap<String, LinkedHashMap<int[],Boolean>> map = solver.words.get(24);
        String string = map.keySet().toArray()[0].toString();
        HashMap<int[],Boolean> map2 = map.get(string);
        Set<int[]> set = map2.keySet();
        int[][] arr = set.toArray(new int[0][0]);
        int x = arr[0][0];
        int y = arr[0][1];
        boolean isRotated = map2.get(arr[0]);
        System.out.println(string + " "+isRotated);
        System.out.println(board.isRotated());
        System.out.println("x: "+x+" y: "+y);
        for(int[] ar: board.getWordCoordinates(x,y,isRotated,string)){
            System.out.println(ar[0]+"   "+ar[1]);
        }
        //board.rotateBoard();
        System.out.println(board.toString());
        System.out.println(board.anchorPointsToString());


        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        };
        LinkedList<Integer> list = new LinkedList<>(solver.words.keySet());
        list.sort(comparator);
        System.out.println(list);
        System.out.println(solver.words.get(list.getLast()));
    }

}
