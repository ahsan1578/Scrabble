package scrabble;

public class Trie {
    public static final int TOTAL_LETTERS = 26;


    class Node{
        Node [] children = new Node[TOTAL_LETTERS];
        boolean isWord;

        Node(){
            isWord = false;
            for(int i =0; i<TOTAL_LETTERS; i++){
                children[0] = null;
            }
        }

        public void setChildren(Node node, int index){
            children[index] = node;
        }

        public void setWord(boolean word) {
            isWord = word;
        }

        public boolean isWord() {
            return isWord;
        }

        public Node getChildren(int index){
            return children[index];
        }

        public Node[] getAllChildren(){
            return children;
        }

    }

    private Node rootNode;
    private int size;

    public Trie(){
        rootNode = new Node();
        size = 0;
    }

    public void insert(String word){
        int index;
        Node parentNode = rootNode;

        for(int i = 0; i<word.length(); i++){
            index = word.charAt(i) - 'a';
            if(parentNode.getChildren(index)==null){
                parentNode.setChildren(new Node(), index);
            }
            parentNode = parentNode.getChildren(index);
        }

        parentNode.setWord(true);
    }

    public boolean isWord(String word){
        int index;
        Node parentNode = rootNode;

        for(int i = 0; i<word.length(); i++){
            index = word.charAt(i) - 'a';
            if(parentNode.getChildren(index) == null){
                return false;
            }

            parentNode = parentNode.getChildren(index);
        }
        return parentNode.isWord;
    }

    public Node getRootNode() {
        return rootNode;
    }
}
