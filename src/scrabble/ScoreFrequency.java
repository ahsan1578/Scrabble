package scrabble;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ScoreFrequency {
    private HashMap<Character,Integer> scoreMap;
    private HashMap<Character, Integer> frequencyMap;


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

    public HashMap<Character, Integer> getFrequencyMap() {
        return frequencyMap;
    }

    public HashMap<Character, Integer> getScoreMap() {
        return scoreMap;
    }

    public static void main(String[] args) {
        ScoreFrequency scoreFrequency = new ScoreFrequency("tiles.txt");
        System.out.println(scoreFrequency.frequencyMap);
        System.out.println(scoreFrequency.scoreMap);
    }
}

