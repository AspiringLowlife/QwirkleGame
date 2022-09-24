package com.example.qwirklegame;

import java.util.ArrayList;
import java.util.Random;
import static com.example.qwirklegame.Tile.Color;
import static com.example.qwirklegame.Tile.Shape;

public class GameModel {

    //108 tiles in the game bag
    public ArrayList<Tile> bag = new ArrayList<>();
    //2-4 players
    public ArrayList<Player> players = new ArrayList<>();

    public GameModel(int playerNo) {
        //generate all 108 tiles in random order in the pieces bag
        generatePieces();
        createPlayerHand(playerNo);
        //generate board
    }

    public void generatePieces() {
        //repeat this three times so each shape has three in the same color
        for (int i = 0; i < 3; i++) {

            for (int s = 0; s < 6; s++) {

                //each color gets one shape
                for (int c = 0; c < 6; c++) {
                    Tile tile=new Tile(Color.values()[c],Shape.values()[s]);
                    bag.add(tile);
                }
            }
        }
        Shuffle();
    }

    public void Shuffle() {
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
