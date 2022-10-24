package com.example.qwirklegame;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import java.util.ArrayList;

//This class is used to pass board state and player hand to the server
public class RequestForm {

    private LinearLayout hand, swapTarget;
    private TableLayout board;

    public RequestForm(LinearLayout hand, LinearLayout swapTarget, TableLayout board) {
        this.hand = hand;
        this.swapTarget = swapTarget;
        this.board = board;
    }

    public ArrayList<Tile> getSwapTargetList(){
        ArrayList<Tile> hand = GameModel.curPlayer.getHand();
       return null;
    }

    public ArrayList<Tile> getPlacedTilesList(){


        return null;
    }
}
