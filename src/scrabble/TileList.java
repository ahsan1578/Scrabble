/**
 * @author D M Raisul Ahsan
 * @version 1.0
 *
 * This class is responsible for creating a list of tiles
 */


package scrabble;

import java.util.LinkedList;
import java.util.Random;

public class TileList {
    private LinkedList<Character> tileList;

    /**
     * Constructs tile list as a LinkedList
     */
    public TileList(){
        tileList = new LinkedList<>();
    }

    /**
     * @return the size of the tile list
     */
    public int getSize(){
        return tileList.size();
    }

    /**
     * @return the tile list
     */
    public LinkedList<Character> getTileList() {
        return tileList;
    }


    /**
     * Adds a number of random tiles to the list
     * @param numTiles number of tiles to be added
     * @param list the tile list to take the tiles from
     */
    public void addRandomTiles(int numTiles, TileList list){
        for(int i = 0; i <numTiles; i++){
            int x = new Random().nextInt(list.getSize());
            this.tileList.add(list.getTileList().get(x));
            list.getTileList().remove(x);
        }
    }

    /**
     * Adds a single tile to the tile list
     * @param c the letter on the tile
     */
    public void addTile(Character c){
        tileList.add(c);
    }
}
