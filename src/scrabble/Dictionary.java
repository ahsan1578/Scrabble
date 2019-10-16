/**
 * @author D M Raisul Ahsan
 * @version 1.0
 *
 * This class creates the dictionary in trie structure
 */


package scrabble;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Dictionary {
    private Trie trie;

    /**
     * Construct the dictionary
     * @param dictionaryPathName the file path of the given dictionary
     */
    public Dictionary(String dictionaryPathName){
        trie = new Trie();
        InputStream inputStream = getClass().getResourceAsStream(dictionaryPathName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        try {
            while ((line=reader.readLine()) !=null){
                trie.insert(line);
            }
        }catch (Exception e){
            System.out.println("Wrong dictionary file");
        }
    }


    /**
     * Checks if the word exists
     * @param word the word to be checked
     * @return true if the word exists
     */
    public boolean doesWordExist(String word){
        return trie.isWord(word);
    }


    /**
     * @return the trie
     */
    public Trie getTrie() {
        return trie;
    }
}
