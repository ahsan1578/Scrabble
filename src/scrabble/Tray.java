package scrabble;

public class Tray extends TileList{
    public Tray(TileBag tileBag){
        addRandomTiles(7,tileBag);
    }

    public static void main(String[] args) {
        TileBag tileBag = new TileBag("tiles.txt");
        Tray tray = new Tray(tileBag);
        System.out.println(tray.getTileList());
    }
}
