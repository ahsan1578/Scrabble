package scrabble;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Dictionary {
    private Trie trie;

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

    public boolean doesWordExist(String word){
        return trie.isWord(word);
    }

    public Trie getTrie() {
        return trie;
    }

    public static void main(String[] args) {
        Dictionary dictionary = new Dictionary("sowpods.txt");
        System.out.println(dictionary.doesWordExist("overfl"));
        System.out.println(dictionary.doesWordExist("overflow"));
    }
}
