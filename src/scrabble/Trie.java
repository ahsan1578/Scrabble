/**
 * @author D M Raisul Ahsan
 * @version 1.0
 *
 * This class creates a Trie data structure, a prefix tree to conduct fast search for words
 */


package scrabble;

public class Trie {
    public static final int TOTAL_LETTERS = 26;


    /**
     * Inner class that creates a trie node
     */
    class Node{
        Node [] children = new Node[TOTAL_LETTERS];
        boolean isWord;

        /**
         * Construct a node with  children nodes
         */
        Node(){
            isWord = false;
            for(int i =0; i<TOTAL_LETTERS; i++){
                children[0] = null;
            }
        }

        /**
         * Set a children node
         * @param node the node to be set to
         * @param index the index of the node to set
         */
        public void setChildren(Node node, int index){
            children[index] = node;
        }

        /**
         * Sets true if the node is a end of a word
         * @param word is word
         */
        public void setWord(boolean word) {
            isWord = word;
        }

        /**
         * @return true if the node is a word's end
         */
        public boolean isWord() {
            return isWord;
        }

        /**
         * @param index of the children node
         * @return the children node at the given index
         */
        public Node getChildren(int index){
            return children[index];
        }

    }

    private Node rootNode;
    private int size;

    /**
     * Construct the trie
     */
    public Trie(){
        rootNode = new Node();
        size = 0;
    }

    /**
     * Insert a word in the trie
     * @param word the word to be inserted
     */
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


    /**
     * Check if the string is a valid word
     * @param word word string to be checked
     * @return true if it's a valid word
     */
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

    /**
     * @return the root node
     */
    public Node getRootNode() {
        return rootNode;
    }
}
