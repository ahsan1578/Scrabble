/**
 * @author D M Raisul Ahsan
 * @version 1.0
 *
 * This class creates the trays
 */


package scrabble;

public class Tray extends TileList{

    /**
     *Construct a tray taking 7 tiles from the tile bag
     * @param tileBag the tile bag to take tiles from
     */
    public Tray(TileBag tileBag){
        addRandomTiles(7,tileBag);
    }
}
