package com.example.qwirklegame;

import java.util.ArrayList;

public class Player {
    private ArrayList<Tile> hand;

    public Player(ArrayList<Tile> hand)
    {
        this.hand = hand;
    }

    public ArrayList<Tile> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Tile> hand) {
        this.hand = hand;
    }
}
