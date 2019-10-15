package scrabble;

import java.util.LinkedList;
import java.util.Random;

public class TileList {
    private LinkedList<Character> tileList;

    public TileList(){
        tileList = new LinkedList<>();
    }

    public int getSize(){
        return tileList.size();
    }

    public LinkedList<Character> getTileList() {
        return tileList;
    }

    public void addRandomTiles(int numTiles, TileList list){
        for(int i = 0; i <numTiles; i++){
            int x = new Random().nextInt(list.getSize());
            this.tileList.add(list.getTileList().get(x));
            list.getTileList().remove(x);
        }
    }

    public void addTile(Character c){
        tileList.add(c);
    }

    public void removeTile(Character ... characters){
        if(characters.length == 0){
            return;
        }
        for(int i = 0; i<characters.length; i++){
            tileList.remove(characters[i]);
        }
    }
}
