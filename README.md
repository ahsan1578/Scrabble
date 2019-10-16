Scrabble   
===

**Game Rules**  

*The rules of the game is taken from [Scrabble Game Rules (Hasbro Official Board Game Rules](https://scrabble.hasbro.com/en-us/rules)*  
*We will follow all the rules except for challeging and exchanging tile from the tray*    

**Set up**  
You should have a game board, 100 letter tiles, a letter bag, and four racks.  

Before the game begins, all players should agree upon the dictionary that they will use, in case of a challenge. All words labeled as a part of speech (including those listed of foreign origin, and as archaic, obsolete, colloquial, slang, etc.) are permitted with the exception of the following: words always capitalized, abbreviations, prefixes and suffixes standing alone, words requiring a hyphen or an apostrophe.  

Place all letters in the pouch, or facedown beside the board, and mix them up. Draw for first play. The player with the letter closest to "A" plays first. A blank tile beats any letter. Return the letters to the pool and remix. All players draw seven new letters and place them on their racks.  

**Game Play**  

* The first player combines two or more of his or her letters to form a word and places it on the board to read either across or down with one letter on the center square. Diagonal words are not allowed.

* Complete your turn by counting and announcing your score for that turn. Then draw as many new letters as you played; always keep seven letters on your rack, as long as there are enough tiles left in the bag.

* Play passes to the left. The second player, and then each in turn, adds one or more letters to those already played to form new words. All letters played on a turn must be placed in one row across or down the board, to form at least one complete word. If, at the same time, they touch others letters in adjacent rows, those must also form complete words, crossword fashion, with all such letters. The player gets full credit for all words formed or modified on his or her turn.

* New words may be formed by:
1. Adding one or more letters to a word or letters already on the board.
2. Placing a word at right angles to a word already on the board. The new word must use one of the letters already on the board or must add a letter to it. (See Turns 2, 3 and 4 below.)
3. Placing a complete word parallel to a word already played so that adjacent letters also form complete words. (See Turn 5 in the Scoring Examples section below.)

* No tile may be shifted or replaced after it has been played and scored.

* Blanks: The two blank tiles may be used as any letters. When playing a blank, you must state which letter it represents. It remains that letter for the rest of the game.

* You may use a turn to exchange all, some, or none of the letters. To do this, place your discarded letter(s) facedown. Draw the same number of letters from the pool, then mix your discarded letter(s) into the pool. This ends your turn.

* Any play may be challenged before the next player starts a turn. If the play challenged is unacceptable, the challenged player takes back his or her tiles and loses that turn. If the play challenged is acceptable, the challenger loses his or her next turn. Consult the dictionary for challenges only. All words made in one play are challenged simultaneously. If any word is unacceptable, then the entire play is unacceptable. Only one turn is lost on any challenge.

* The game ends when all letters have been drawn and one player uses his or her last letter; or when all possible plays have been made.  

**Scoring**  

* Use a score pad or piece of paper to keep a tally of each player's score, entering it after each turn. The score value of each letter is indicated by a number at the bottom of the tile. The score value of a blank is zero.

* The score for each turn is the sum of the letter values in each word(s) formed or modified on that turn, plus the additional points obtained from placing letters on Premium Squares.

* Premium Letter Squares: A light blue square doubles the score of a letter placed on it; a dark blue square triples the letter score.

* Premium Word Squares: The score for an entire word is doubled when one of its letters is placed on a pink square: it is tripled when one of its letters is placed on a red square. Include premiums for double or triple letter values, if any, before doubling or tripling the word score. If a word is formed that covers two premium word squares, the score is doubled and then re-doubled (4 times the letter count), or tripled and then re-tripled (9 times the letter count). NOTE: the center square is a pink square, which doubles the score for the first word.

* Letter and word premiums count only on the turn in which they are played. On later turns, letters already played on premium squares count at face value.

* When a blank tile is played on a pink or red square, the value of the word is doubled or tripled, even though the blank itself has no score value.

* When two or more words are formed in the same play, each is scored. The common letter is counted (with full premium value, if any) for each word. (See Turns 3, 4 and 5 in the Scoring Examples section.)

* BINGO! If you play seven tiles on a turn, it's a Bingo. You score a premium of 50 points after totaling your score for the turn.

* Unplayed Letters: When the game ends, each player's score is reduced by the sum of his or her unplayed letters. In addition, if a player has used all of his or her letters, the sum of the other players' unplayed letters is added to that player's score.

* The player with the highest final score wins the game. In case of a tie, the player with the highest score before adding or deducting unplayed letters wins.

---

**How to play**
* For making any move, first click on the "Select Move" button.  
* After choosing "Horizontal" or "Vertical", click on the tile you want to play and then click on the board where you want to play.  
* Always start making move with a square adjacent to an existing letter if it's not the first move. 
* You can discard the last tile move clicking on the "Discard Last Move" button.
* Once you are satisfied with the word you placed on the board, click on the "Make Move" button to play.
* If you can't come up with a word, click on the "Give Up Turn" button.
* You can't give up turn in the first move.
* If you make an illegal move or a wrong word, clicking on "Make Move" button will reset the board and your tray. In this case start a new move  clicking "Select Move" button.
* Light blue - doubles the letter score; Dark blue - triples the letter score; Pink - doubles the word score; Red - triples the word score.

---

**Features not implemented**  
* Use of own dictionary. You can't use use your own dictionary to play the game. There is a dictionary provided with the game that you will have to use.
---

**Solver**
* The solver .jar finds the best move for a given board and rack.
* It takes a file path name (used Scanner) to find the input file.
* Please use the complete absolute file path name of the input text.
* The output file with best move made on board should be in the same directory as the input file.
* Two examples of the input file and output file are provided in the resource folder.
---

**A small bug and way to avoid**  
*If you clone the entire repo and try to play the game compiling manually without using the .jar file, please move the .txt files from resources directory to the same directory as .java source files. Otherwise the program won't run.*  

---

**Design choices**  
* Strategy pattern has been used. Tile bag and Tray both are created from Tile list as parent, Computer Player is created from Player as Parent.
* Data structure used in the game is Trie and a backtracking algorigthm has been used to find the best possible move. 

**Trie**
* Trie is a prefix tree.
* The root node has 26 nodes each representing one letter.
* Each node also has 26 nodes.
* A wrod is made travelling through the node.
* To make it faster, array of size 26 is used to make 26 children nodes. Random access property of the array makes the search faster.

**Backtracking algorithm**
* The algorithm starts by finding anchor squares.
* Anchor squares are squares adjacent to the already placed words.
* The left part of the word is made first using a recursive method.
* The left part of the word means that the part that is going to be on the left of the anchor square.
* Each left part is then extended to right to make the complete word.
* Recursive call and using all the anchors squares ensure that the move is the best possible move.
---

![](Resources/view1.png)
![](Resources/view2.png)
![](Resources/view3.png)
![](Resources/view4.png)


