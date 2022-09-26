package com.example.qwirklegame;

import java.util.ArrayList;
import java.util.Random;

import static com.example.qwirklegame.Tile.Color;
import static com.example.qwirklegame.Tile.Shape;

public class GameModel {

    //108 tiles in the game bag
    private ArrayList<Tile> bag = new ArrayList<>();
    //2-4 players
    private ArrayList<Player> players = new ArrayList<>();
    //player info
    public static int playerTotal = 0;
    public int curPlayerNo = 0;
    public Player curPlayer;

    public GameModel(int playerNo) {
        GameModel.playerTotal = playerNo;
        //generate all 108 tiles in random order in the pieces bag
        generatePieces();
        //initial hand for each player
        createPlayerHand(playerNo);
        curPlayer = players.get(0);
        //generate board
    }

    //fills bag with random pieces
    public void generatePieces() {
        //repeat this three times so each shape has three in the same color
        for (int i = 0; i < 3; i++) {

            for (int s = 0; s < 6; s++) {

                //each color gets one shape
                for (int c = 0; c < 6; c++) {
                    Tile tile = new Tile(Color.values()[c], Shape.values()[s]);
                    bag.add(tile);
                }
            }
        }
        shuffle();
    }

    public void shuffle() {
        // Very basic shuffle
        Random r = new Random();
        for (int j = 0; j < 500; j++) {
            for (int i = 0; i < bag.size(); i++) {
                int randomPos = r.nextInt(bag.size());
                Tile newPiece = bag.get(randomPos);
                bag.remove(randomPos);
                bag.add(newPiece);
            }
        }
    }

    //initial hand
    public void createPlayerHand(int playerNo) {
        Random r = new Random();
        for (int i = 0; i < playerNo; i++) {
            ArrayList<Tile> hand = new ArrayList<>();
            for (int x = 0; x < 6; x++) {
                int randomPos = r.nextInt(bag.size());
                Tile yoinkedPiece = bag.get(randomPos);
                bag.remove(randomPos);
                hand.add(yoinkedPiece);
            }
            Player player = new Player(hand);
            players.add(player);
        }
    }

    public int changeCurPlayer() {
        if (curPlayerNo == 0) {
            curPlayerNo++;
            curPlayer = players.get(curPlayerNo);
        } else if (curPlayerNo == 1) {
            curPlayerNo--;
            curPlayer = players.get(curPlayerNo);
        }
        return curPlayerNo;
    }

    public void swapPieces(ArrayList<Tile> tempList) {
        if(tempList.size()>bag.size())return;
        ArrayList<Tile> playerHand = players.get(curPlayerNo).getHand();
        for (Tile tile : tempList) {
            playerHand.remove(tile);
            bag.add(tile);
        }
        shuffle();
        for (Tile tile : tempList){
            Tile newTile=bag.remove(bag.size()-1);
            playerHand.add(newTile);
        }
    }

    public ArrayList<Tile> getBag() {
        return bag;
    }

    public void setBag(ArrayList<Tile> bag) {
        this.bag = bag;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }


}
// public final String[] colors = {"Blue", "Green", "Red", "Yellow", "Purple", "Orange"};
//    public final String[] shapes = {"Square", "Circle", "Start", "Diamond", "Cross", "Club"};
//    public void generatePiece() {
//        //three sets of 36 tiles ctrl+alt+l
//        //the repeat to do each shape in the same color three times
//        for (int i = 0; i < 3; i++) {
//
//            for (String color : colors) {
//
//                for (String shape : shapes) {
//
//                   // Tile tile = new Tile(color, shape);
//                    //pieces.add(tile);
//                }
//            }
//        }
//        Shuffle();
//    }
