/**
 * @author D M Raisul Ahsan
 * @version 1.0
 *
 * This class is responsible for showing the current state of the game in a graphical interface
 */



package scrabble;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;


public class GameGui extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage){
        stage.setTitle("Scrabble");

        GameManager gameManager = new GameManager();



        BorderPane root = new BorderPane();
        BoardGui boardGui = new BoardGui(gameManager.getBoard());
        boardGui.draw();

        TrayMaker humanTray = new TrayMaker(gameManager.getHumanTray());
        humanTray.draw();

        ScoreBoard scoreBoard = new ScoreBoard();
        scoreBoard.draw();

        List<Character> letterChoices = new ArrayList<>();
        for(int i = 'A'; i<='Z'; i++){
            letterChoices.add((char)i);
        }

        ChoiceDialog<Character> choicedialog = new ChoiceDialog<>('A', letterChoices);
        choicedialog.setTitle("Empty tile");
        choicedialog.setHeaderText("What do you want to play for empty tile?");

        List<String> choiceList = new ArrayList<>();
        choiceList.add("Horizontal");
        choiceList.add("Vertical");
        ChoiceDialog<String> chooseAlignment = new ChoiceDialog<>("Horizontal", choiceList);
        chooseAlignment.setTitle(null);
        chooseAlignment.setHeaderText("Do you want to play horizontally or vertically?\n" +
                "If you are playing one letter, is your intended word vertical or horizontal?");

        Alert notMakingMove = new Alert(Alert.AlertType.WARNING);
        notMakingMove.setHeaderText(null);
        notMakingMove.setContentText("Please click on the \"Select Move\" button");

        Alert noAlignment = new Alert(Alert.AlertType.WARNING);
        noAlignment.setHeaderText(null);
        noAlignment.setContentText("Please click on \"Select Move\" button\nand choose if you want to play horizontal or vertical.");


        Button makeMoveButton =  new Button("Select Move");
        makeMoveButton.setStyle("-fx-background-color: #021b45; -fx-text-fill: white; -fx-font-size: 17px");
        makeMoveButton.setOnMouseClicked(event -> {
            if(!gameManager.getBoard().isHumanCurrentlyPlaying()){
                Optional<String> result = chooseAlignment.showAndWait();
                if(result.isPresent()){
                    gameManager.getBoard().setHumanCurrentlyPlaying(true);
                    humanTray.setMakeMoveOn(true);
                    if(result.get().equals("Horizontal")){
                        gameManager.getBoard().setHumanMovingHorizontal(true);
                    }else {
                        gameManager.getBoard().setHumanMovingHorizontal(false);
                    }
                }else {
                    humanTray.setMakeMoveOn(false);
                    gameManager.getBoard().setHumanCurrentlyPlaying(false);
                    noAlignment.showAndWait();
                }
            }
        });


        humanTray.setOnMouseClicked(event -> {
            if(humanTray.isMakeMoveOn()){
                LinkedList<Character> list = gameManager.getHumanTray().getTileList();
                double x = event.getX();
                int clickedX = (int)(x/40);
                LinkedList<Integer> played = humanTray.getPlayedTiles();
                if(x<=280 && !played.contains(clickedX)){
                    if(!humanTray.isLastCharPlayed()){
                        humanTray.getPlayedTiles().removeLast();
                    }
                    humanTray.setLastCharPlayed(false);
                    humanTray.getPlayedTiles().add(clickedX);
                    char c = list.get(clickedX);
                    if(c=='*'){
                        Optional<Character> result = choicedialog.showAndWait();
                        if (result.isPresent()){
                            c = result.get();
                        }else {
                            played.removeLast();
                            c = (char)0;
                        }
                    }
                    gameManager.getBoard().setNewCharSelected(true);
                    gameManager.getBoard().setHumanCurrentPlayingChar(c);
                    humanTray.draw();
                }
            }else {
                notMakingMove.showAndWait();
            }

        });

        Button disCardLastMove = new Button("Discard Last Move");
        disCardLastMove.setStyle("-fx-background-color: #021b45; -fx-text-fill: white; -fx-font-size: 17px");
        disCardLastMove.setOnMouseClicked(event -> {
            if(gameManager.getBoard().isHumanCurrentlyPlaying() && gameManager.getBoard().getHumanCurrMove().size()>0){
                int [] arr = gameManager.getBoard().getHumanCurrMove().getLast();
                gameManager.getBoard().removeChar(arr[0],arr[1]);
                boardGui.draw();
                gameManager.getBoard().getHumanCurrMove().removeLast();
                humanTray.getPlayedTiles().removeLast();
                humanTray.draw();
            }
        });


        Alert illegalChoice = new Alert(Alert.AlertType.WARNING);
        illegalChoice.setHeaderText(null);
        illegalChoice.setContentText("Sorry! Illegal move.");



        /**
         * Check is the move is adjacent to a tile
         */
        boardGui.setOnMouseClicked(event -> {
            if(gameManager.getBoard().isHumanCurrentlyPlaying() && gameManager.getBoard().isNewCharSelected() && !humanTray.isLastCharPlayed()){
                double x = event.getX();
                double y = event.getY();

                int indexY = (int)x/40;
                int indexX = (int)y/40;

                if(gameManager.getBoard().existChar(indexX, indexY)){
                    illegalChoice.showAndWait();
                }else {
                    gameManager.getBoard().getHumanCurrMove().add(new int[]{indexX, indexY});
                    if(gameManager.getBoard().isLegalHumanMove()){
                        gameManager.getBoard().putChar(gameManager.getBoard().getHumanCurrentPlayingChar(),indexX,indexY);
                        gameManager.getBoard().setNewCharSelected(false);
                        humanTray.setLastCharPlayed(true);
                        boardGui.draw();
                        LinkedList<int[]> list = gameManager.getBoard().getHumanCurrMove();
                    }else {
                        humanTray.setLastCharPlayed(true);
                        gameManager.getBoard().setNewCharSelected(false);
                        gameManager.getBoard().getHumanCurrMove().removeLast();
                        humanTray.getPlayedTiles().removeLast();
                        humanTray.draw();
                        illegalChoice.showAndWait();
                    }
                }
            }
        });

        Alert noWord = new Alert(Alert.AlertType.WARNING);
        noWord.setHeaderText(null);
        noWord.setContentText("Please choose a word that you want to play");

        Alert notValidWord = new Alert(Alert.AlertType.WARNING);
        notValidWord.setHeaderText(null);
        notValidWord.setContentText("Sorry! Not a valid word");

        Alert gameOver = new Alert(Alert.AlertType.INFORMATION);
        gameOver.setHeaderText("Game Over!");


        Button doneButton = new Button("Make Move");
        doneButton.setStyle("-fx-background-color: #021b45; -fx-text-fill: white; -fx-font-size: 17px");
        doneButton.setOnMouseClicked(event -> {
            if(humanTray.isMakeMoveOn()){
                String word = gameManager.getBoard().getWordPlayedByHuman();
                if(gameManager.getBoard().getHumanCurrMove().size() == 0){
                    noWord.showAndWait();
                }else {
                    if(!gameManager.continuousHumanMove()){
                        LinkedList<int[]> list = gameManager.getBoard().getHumanMoveIndexes();
                        LinkedList<int[]> list1 = gameManager.getBoard().getHumanCurrMove();
                        for(int[] arr: gameManager.getBoard().getHumanCurrMove()){
                            gameManager.getBoard().removeChar(arr[0],arr[1]);
                        }
                        illegalChoice.showAndWait();
                    }else if(gameManager.isFirstMove() &&
                            (!gameManager.getBoard().existChar(gameManager.getBoard().getDimension()/2, gameManager.getBoard().getDimension()/2) ||
                                    gameManager.getBoard().getHumanCurrMove().size()<2)){
                        for(int[] arr: gameManager.getBoard().getHumanCurrMove()){
                            gameManager.getBoard().removeChar(arr[0],arr[1]);
                        }
                        illegalChoice.showAndWait();
                    } else if(!gameManager.isAFinalValidWordByHuman(word)){
                        for(int[] arr: gameManager.getBoard().getHumanCurrMove()){
                            gameManager.getBoard().removeChar(arr[0],arr[1]);
                        }
                        notValidWord.showAndWait();
                    }else {
                        gameManager.getBoard().setFirstMove(false);
                        gameManager.setFirstMove(false);
                        boolean allTilesUsed = false;
                        if(humanTray.getPlayedTiles().size() == 7){
                            allTilesUsed = true;
                        }
                        for(int[] arr: gameManager.getBoard().getHumanCurrMove()){
                            gameManager.getBoard().removeChar(arr[0],arr[1]);
                        }
                        int score = gameManager.getHumanScore();
                        score = score + gameManager.getBoard().getTotalScore(gameManager.getBoard().getHumanMoveIndexes(), word,allTilesUsed);
                        gameManager.setHumanScore(score);
                        for(int i:humanTray.getPlayedTiles()){
                            gameManager.getHumanPlayer().getTray().getTileList().set(i, '?');
                        }
                        gameManager.getHumanPlayer().getTray().getTileList().removeAll(Collections.singletonList((Character)'?'));
                        if(gameManager.getTileBag().getSize()>=humanTray.getPlayedTiles().size()){
                            gameManager.getHumanTray().addRandomTiles(humanTray.getPlayedTiles().size(),gameManager.getTileBag());
                        }else if(gameManager.getTileBag().getSize()>0){
                            gameManager.getHumanTray().addRandomTiles(gameManager.getTileBag().getSize(),gameManager.getTileBag());
                        }
                        gameManager.getHumanPlayer().setNextMoveWord(word);
                        gameManager.getHumanPlayer().setNextMoveCoordinates(gameManager.getBoard().getHumanMoveIndexes());
                        gameManager.setHumanGaveUpTurn(false);
                        gameManager.play();
                        scoreBoard.setComputerScore(gameManager.getComScore());
                        scoreBoard.setHumanScore(gameManager.getHumanScore());
                        scoreBoard.setRemainingTiles(gameManager.getTileBag().getSize());
                        scoreBoard.draw();
                        if(gameManager.isGameOver()){

                            String str = "Your score: "+gameManager.getHumanScore()+"\nComputer score: "+gameManager.getComScore();
                            if(gameManager.getWinner().equals(gameManager.getComputerPlayer())){
                                gameOver.setContentText("Sorry! You lost!\n"+str);
                            }else if(gameManager.getWinner().equals(gameManager.getHumanPlayer())){
                                gameOver.setContentText("Congratulations! You won!\n"+str);
                            }else {
                                gameOver.setContentText("Scores tied\n"+str);
                            }
                            gameOver.showAndWait();
                            stage.close();
                        }
                    }
                    humanTray.setLastCharPlayed(true);
                    humanTray.setMakeMoveOn(false);
                    gameManager.getBoard().setNewCharSelected(false);
                    gameManager.getBoard().setHumanMovingHorizontal(false);
                    gameManager.getBoard().setHumanCurrentlyPlaying(false);
                    gameManager.getBoard().getHumanCurrMove().clear();
                    gameManager.getBoard().getHumanMoveIndexes().clear();
                    gameManager.getBoard().setWordPlayedByHuman("");
                    humanTray.getPlayedTiles().clear();
                    boardGui.draw();
                    humanTray.draw();
                }
            }

        });

        Alert noGiveUP = new Alert(Alert.AlertType.WARNING);
        noGiveUP.setHeaderText(null);
        noGiveUP.setContentText("You can't give up first move");


        Button giveUpButton = new Button("Give up turn");
        giveUpButton.setStyle("-fx-background-color: #021b45; -fx-text-fill: white; -fx-font-size: 17px");
        giveUpButton.setOnMouseClicked(event1 -> {
            if(gameManager.isFirstMove()){
                noGiveUP.showAndWait();
            }else{
                gameManager.setHumanGaveUpTurn(true);
                gameManager.play();
                scoreBoard.setComputerScore(gameManager.getComScore());
                scoreBoard.setHumanScore(gameManager.getHumanScore());
                scoreBoard.setRemainingTiles(gameManager.getTileBag().getSize());
                scoreBoard.draw();
                if(gameManager.isGameOver()){
                    String str = "Your score: "+gameManager.getHumanScore()+"\nComputer score: "+gameManager.getComScore();
                    if(gameManager.getWinner().equals(gameManager.getComputerPlayer())){
                        gameOver.setContentText("Sorry! You lost!\n"+str);
                    }else if(gameManager.getWinner().equals(gameManager.getHumanPlayer())){
                        gameOver.setContentText("Congratulations! You won!\n"+str);
                    }else {
                        gameOver.setContentText("Scores tied\n"+str);
                    }
                    gameOver.showAndWait();
                    stage.close();

                }
                humanTray.setLastCharPlayed(true);
                humanTray.setMakeMoveOn(false);
                gameManager.getBoard().setNewCharSelected(false);
                gameManager.getBoard().setHumanMovingHorizontal(false);
                gameManager.getBoard().setHumanCurrentlyPlaying(false);
                gameManager.getBoard().getHumanCurrMove().clear();
                gameManager.getBoard().getHumanMoveIndexes().clear();
                gameManager.getBoard().setWordPlayedByHuman("");
                humanTray.getPlayedTiles().clear();
                boardGui.draw();
                humanTray.draw();
            }
        });


        HBox hBox = new HBox();
        hBox.setPadding(new Insets(20,10,12,10));
        hBox.setSpacing(10);
        hBox.setStyle("-fx-background-color: #403d99");
        hBox.getChildren().add(humanTray);
        hBox.getChildren().add(makeMoveButton);
        hBox.getChildren().add(disCardLastMove);
        hBox.getChildren().add(doneButton);
        hBox.getChildren().add(giveUpButton);


        root.setBottom(hBox);
        root.setRight(scoreBoard);
        root.setCenter(boardGui);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
