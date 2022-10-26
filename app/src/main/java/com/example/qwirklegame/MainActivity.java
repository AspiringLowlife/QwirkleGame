package com.example.qwirklegame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.codewithbill.GameModel;
import com.codewithbill.MoveRequest;

import com.codewithbill.Player;
import com.codewithbill.Tile;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public GameModel game;
    private LinearLayout handView, swapTarget;
    private TableLayout board;
    private List<ImageView> handViewCopy = new ArrayList<>();
    private Player player;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        game = new GameModel(2);
        handView = findViewById(R.id.tileBar);
        swapTarget = findViewById(R.id.swapTarget);
        board = findViewById(R.id.dropTarget);
        player=ConnectActivity.player;
        setTilesAndListeners(handView, swapTarget, board);
    }

    public void confirmClicked(View view) {

        ArrayList<Tile> hand = GameModel.curPlayer.getHand();
        //check for swap then exchange pieces
        if (swapTarget.getChildCount() > 0) {
            ArrayList<Tile> swappingPieces = new ArrayList<>();
            for (int i = 0; i < swapTarget.getChildCount(); i++) {
                ImageView swapView = (ImageView) swapTarget.getChildAt(i);
                int resId = (int) swapView.getTag();
                String drawableName = this.getResources().getResourceEntryName(resId);
                for (Tile tile : hand) {
                    if (tile.toString().equals(drawableName)) {
                        tile.setState(Tile.State.swapping);
                        break;
                    }
                }
            }

            swapTarget.removeAllViews();
        }
        //check for tiles placed on board
        else if (isHandOnBoard()) {
            ArrayList<Tile> temp = new ArrayList<>();
            for (int i = 0; i < board.getChildCount(); i++) {
                for (int j = 0; j < handViewCopy.size(); j++) {
                    if (board.getChildAt(i).equals(handViewCopy.get(j))) {
                        int resId = (int) board.getChildAt(i).getTag();
                        String drawableName = this.getResources().getResourceEntryName(resId);
                        for (Tile tile : hand) {
                            if (tile.toString().equals(drawableName)) {
                                tile.setState(Tile.State.placing);
                                break;
                            }
                        }
                    }
                }
            }
            //TODO remove for server functionality
            for (Tile tile : temp) {
                hand.remove(tile);
            }
            //add new tiles to hand
            game.fillHand();
        }
        //swap player
        //TODO move to server
        game.changeCurPlayer();
        this.handView.removeAllViews();
        List<ImageView> tempList = new ArrayList<>();
        ArrayList<Tile> newHand = GameModel.curPlayer.getHand();
        for (int i = 0; i < newHand.size(); i++) {
            ImageView viewTile = new ImageView(this);
            String drawableName = newHand.get(i).toString();
            viewTile.setImageResource(getResources().getIdentifier(drawableName, "drawable", this.getPackageName()));
            //attaching resource id as tag
            viewTile.setTag(getResources().getIdentifier(drawableName, "drawable", this.getPackageName()));
            viewTile.setOnTouchListener(new MyTouchListener());
            this.handView.addView(viewTile);
            tempList.add(viewTile);
        }
        handViewCopy = tempList;
        //inform Server of move
       // MoveRequest request = new MoveRequest(hand);
        //new AndroidClient(request, ConnectActivity.connectionString).start();
    }

//    public void confirmClicked(View view) {
//
//        ArrayList<Tile> hand = GameModel.curPlayer.getHand();
//        //check for swap then exchange pieces
//        if (swapTarget.getChildCount() > 0) {
//            ArrayList<Tile> swappingPieces = new ArrayList<>();
//            for (int i = 0; i < swapTarget.getChildCount(); i++) {
//                ImageView swapView = (ImageView) swapTarget.getChildAt(i);
//                int resId = (int) swapView.getTag();
//                String drawableName = this.getResources().getResourceEntryName(resId);
//                for (Tile tile : hand) {
//                    if (tile.toString().equals(drawableName)) {
//                        tile.setState(Tile.State.swapping);
//                        break;
//                    }
//                }
//            }
//            //TODO move to server
//            game.swapPieces(hand);
//            //
//            swapTarget.removeAllViews();
//        }
//        //check for tiles placed on board
//        else if (isHandOnBoard()) {
//            ArrayList<Tile> temp = new ArrayList<>();
//            for (int i = 0; i < board.getChildCount(); i++) {
//                for (int j = 0; j < handViewCopy.size(); j++) {
//                    if (board.getChildAt(i).equals(handViewCopy.get(j))) {
//                        int resId = (int) board.getChildAt(i).getTag();
//                        String drawableName = this.getResources().getResourceEntryName(resId);
//                        for (Tile tile : hand) {
//                            if (tile.toString().equals(drawableName)) {
//                                tile.setState(Tile.State.placing);
//                                //TODO remove for server functionality
//                                temp.add(tile);
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//            //TODO remove for server functionality
//            for (Tile tile : temp) {
//                hand.remove(tile);
//            }
//            //add new tiles to hand
//            game.fillHand();
//        }
//        //swap player
//        //TODO move to server
//        game.changeCurPlayer();
//        this.handView.removeAllViews();
//        List<ImageView> tempList = new ArrayList<>();
//        ArrayList<Tile> newHand = GameModel.curPlayer.getHand();
//        for (int i = 0; i < newHand.size(); i++) {
//            ImageView viewTile = new ImageView(this);
//            String drawableName = newHand.get(i).toString();
//            viewTile.setImageResource(getResources().getIdentifier(drawableName, "drawable", this.getPackageName()));
//            //attaching resource id as tag
//            viewTile.setTag(getResources().getIdentifier(drawableName, "drawable", this.getPackageName()));
//            viewTile.setOnTouchListener(new MyTouchListener());
//            this.handView.addView(viewTile);
//            tempList.add(viewTile);
//        }
//        handViewCopy = tempList;
//        //inform Server of move
//        RequestForm request = new RequestForm(hand);
//        new AndroidClient(request, connectionString).start();
//    }

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
                    if (v.equals(board) && swapTarget.getChildCount() == 0 || v.equals(swapTarget) && handView.getChildCount() == diff || v.equals(handView)) {
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
            //attaching resource id as tag
            viewTile.setTag(getResources().getIdentifier(drawableName, "drawable", this.getPackageName()));
            viewTile.setOnTouchListener(new MyTouchListener());
            tileBar.addView(viewTile);
            handViewCopy.add(viewTile);
            //set reference for data tile
//            hand.get(j).setImageView(viewTile);
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
                for (int j = 0; j < handViewCopy.size(); j++) {
                    if (board.getChildAt(i).equals(handViewCopy.get(j))) return true;
                }
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    private boolean isTileInHand(View viewTile) {
        String drawableName = this.getResources().getResourceEntryName((int) viewTile.getTag());
        for (Tile tile : game.curPlayer.getHand()) {
            if (tile.toString().equals(drawableName)) return true;
        }
        return false;
    }
}
