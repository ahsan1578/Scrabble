/**
 * @author D M Raisul Ahsan
 * @version 1.0
 *
 * This class maps the scores and number of tiles for a particular letter
 */


package scrabble;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ScoreFrequency {
    private HashMap<Character,Integer> scoreMap;
    private HashMap<Character, Integer> frequencyMap;


    /**
     * Constructs the map taking information about scores and frequency from a file
     * @param frequencyScoreFilePath the file path
     */
    public ScoreFrequency(String frequencyScoreFilePath){
        this.scoreMap = new HashMap<>();
        this.frequencyMap = new HashMap<>();

        InputStream inputStream = getClass().getResourceAsStream(frequencyScoreFilePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        try {
            while ((line = reader.readLine()) != null){
                String[] strings = line.split(" ");
                scoreMap.put(strings[0].charAt(0), Integer.parseInt(strings[1]));
                frequencyMap.put(strings[0].charAt(0), Integer.parseInt(strings[2]));
            }
        }catch (Exception e){
            System.out.println("Error in input stream reader for board");
        }
    }

    /**
     * @return the map that maps the number of tiles for each letter
     */
    public HashMap<Character, Integer> getFrequencyMap() {
        return frequencyMap;
    }

    /**
     * @return the map that maps the score each letter
     */
    public HashMap<Character, Integer> getScoreMap() {
        return scoreMap;
    }
}

