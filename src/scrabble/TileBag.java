package scrabble;

public class TileBag extends TileList {
    private ScoreFrequency scoreFrequency;

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

    public static void main(String[] args) {
        TileBag tileBag = new TileBag("tiles.txt");
       System.out.println(tileBag.getSize());
    }
}
