package com.example.qwirklegame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public GameModel game;
    private LinearLayout tileBar, swapTarget;
    private TableLayout board;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        game = new GameModel(2);
        tileBar = findViewById(R.id.tileBar);
        swapTarget = findViewById(R.id.swapTarget);
        board = findViewById(R.id.dropTarget);
        setTilesAndListeners(tileBar, swapTarget, board);
    }

    public void confirmClicked(View view) {
        ArrayList<Tile> hand = game.getPlayers().get(game.curPlayerNo).getHand();
        //check for swap then exchange pieces
        if (swapTarget.getChildCount() > 0) {
            ArrayList<Tile> swappingPieces = new ArrayList<>();
            for (int i = 0; i < swapTarget.getChildCount(); i++) {
                ImageView swapView = (ImageView) swapTarget.getChildAt(i);
                for (Tile tile : hand) {
                    if (tile.imageView.equals(swapView)) {
                        swappingPieces.add(tile);
                        break;
                    }
                }
            }
            game.swapPieces(swappingPieces);
            swapTarget.removeAllViews();
        }
        //check for tiles placed on board
        else if (isHandOnBoard()) {
            ArrayList<Tile> temp = new ArrayList<>();
            for (int i = 0; i < board.getChildCount(); i++) {
                for (Tile tile : hand) {
                    if (tile.imageView.equals(board.getChildAt(i))) {
                        temp.add(tile);
                        break;
                    }
                }
            }
            //remove tiles from hand
            for (Tile tile : temp) {
                hand.remove(tile);
            }
            //add new tiles to hand
            game.fillHand();
        }
        //swap player
        int curPlayer = game.changeCurPlayer();
        tileBar.removeAllViews();
        ArrayList<Tile> newHand = game.getPlayers().get(curPlayer).getHand();
        for (int i = 0; i < newHand.size(); i++) {
            ImageView viewTile = new ImageView(this);
            String drawableName = newHand.get(i).toString();
            viewTile.setImageResource(getResources().getIdentifier(drawableName, "drawable", this.getPackageName()));
            viewTile.setOnTouchListener(new MyTouchListener());
            tileBar.addView(viewTile);
            newHand.get(i).setImageView(viewTile);
        }
    }

    final class MyTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (isTileInHand(view)) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDrag(data, shadowBuilder, view, 0);
                    view.setVisibility(View.VISIBLE);
                    return true;
                } else return false;
            }
            return false;
        }
    }

    final class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
//                    v.setBackgroundDrawable(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
//                    v.setBackgroundDrawable(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    int diff = 6 - swapTarget.getChildCount();
                    if (v.equals(board) && swapTarget.getChildCount() == 0 || v.equals(swapTarget) && tileBar.getChildCount() == diff || v.equals(tileBar)) {
                        View view = (View) event.getLocalState();
                        ViewGroup owner = (ViewGroup) view.getParent();
                        owner.removeView(view);
                        LinearLayout container = (LinearLayout) v;
                        container.addView(view);
                        view.setVisibility(View.VISIBLE);
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            return true;
        }
    }

    //add drag and drop and sets initial tiles from initial player hand
    private void setTilesAndListeners(LinearLayout tileBar, LinearLayout swapTarget, TableLayout boardTarget) {

        //setting the image for tiles in hand initially results in the last players hand being the starting hand
        ArrayList<Tile> hand = game.getPlayers().get(0).getHand();
        for (int j = 0; j < hand.size(); j++) {
            //creating image view to fill into tileBar
            ImageView viewTile = new ImageView(this);
            String drawableName = hand.get(j).toString();
            viewTile.setImageResource(getResources().getIdentifier(drawableName, "drawable", this.getPackageName()));
            viewTile.setOnTouchListener(new MyTouchListener());
            tileBar.addView(viewTile);

            //set reference for data tile
            hand.get(j).setImageView(viewTile);
            //  ImageView viewTile = (ImageView) tileBar.getChildAt(j);
        }

        //Target Listeners dropping tiles into
        boardTarget.setOnDragListener(new MyDragListener());
        tileBar.setOnDragListener(new MyDragListener());
        swapTarget.setOnDragListener(new MyDragListener());
    }

    private boolean isHandOnBoard() {
        try {
            for (int i = 0; i < board.getChildCount(); i++) {
                //board has current player's tiles from hand on board
                ImageView tileView = (ImageView) board.getChildAt(i);
                ArrayList<Tile> hand = game.getPlayers().get(game.curPlayerNo).getHand();
                for (Tile tile : hand) {
                    if (tile.imageView.equals(tileView)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    private boolean isTileInHand(View viewTile) {
        for (Tile tile : game.curPlayer.getHand()) {
            if (tile.imageView.equals(viewTile)) return true;
        }
        return false;
    }
}
