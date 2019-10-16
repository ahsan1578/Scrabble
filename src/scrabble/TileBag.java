/**
 * @author D M Raisul Ahsan
 * @version 1.0
 *
 * This class extends TileList and creates a tileBag that is used to get the tiles from
 */


package scrabble;

public class TileBag extends TileList {
    private ScoreFrequency scoreFrequency;

    /**
     * Construct TileBag using the number of tiles for each letter to be used
     * @param filePath path of the file that gives information about number of tiles for each letter
     */
    public TileBag(String filePath){
        scoreFrequency = new ScoreFrequency(filePath);
        for(int i = 'a'; i<= 'z'; i++){
            int count  = scoreFrequency.getFrequencyMap().get((char)(i));
            if(count>0){
                for(int j = 0; j<count; j++){
                    addTile((char)i);
                }
            }
        }
        int count = scoreFrequency.getFrequencyMap().get('*');
        for(int i = 0; i<count; i++){
            addTile('*');
        }
    }
}
