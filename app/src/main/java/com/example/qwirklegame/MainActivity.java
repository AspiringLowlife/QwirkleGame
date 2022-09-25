package com.example.qwirklegame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GameModel game = new GameModel(2);

        TableLayout target = findViewById(R.id.dropTarget);
        //TO DO
        LinearLayout tileBar = findViewById(R.id.tileBar);
        //TO DO
        LinearLayout swapTarget = findViewById(R.id.swapTarget);

        //region tiles
        ImageView tileHand0 = findViewById(R.id.tileHand0);
        ImageView tileHand1 = findViewById(R.id.tileHand1);
        ImageView tileHand2 = findViewById(R.id.tileHand2);
        ImageView tileHand3 = findViewById(R.id.tileHand3);
        ImageView tileHand4 = findViewById(R.id.tileHand4);
        ImageView tileHand5 = findViewById(R.id.tileHand5);

        //setting the image for tiles in hand initially results in the last players hand being the starting hand
        ArrayList<Tile> hand = game.getPlayers().get(0).getHand();
        for (int j = 0; j < hand.size(); j++) {
            ImageView viewTile = (ImageView) tileBar.getChildAt(j);
            String drawableName = hand.get(j).toString();
            viewTile.setImageResource(getResources().getIdentifier(drawableName, "drawable", this.getPackageName()));
        }


        //Tile listeners Drag and drop
        tileHand0.setOnTouchListener(new MyTouchListener());
        tileHand1.setOnTouchListener(new MyTouchListener());
        tileHand3.setOnTouchListener(new MyTouchListener());
        tileHand2.setOnTouchListener(new MyTouchListener());
        tileHand4.setOnTouchListener(new MyTouchListener());
        tileHand5.setOnTouchListener(new MyTouchListener());
        //endregion

        //Target Listeners dropping tiles into
        target.setOnDragListener(new MyDragListener());
        tileBar.setOnDragListener(new MyDragListener());
        swapTarget.setOnDragListener(new MyDragListener());
    }

    final class MyTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.VISIBLE);
                return true;
            } else return false;
        }
    }

    final class MyDragListener implements View.OnDragListener {

//        Drawable enterShape = getDrawable(R.drawable.bluecircle);
//        Drawable normalShape = getDrawable(R.drawable.bluecircle);

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
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
//                    v.setBackgroundDrawable(normalShape);
                default:
                    break;
            }
            return true;
        }
    }

    public void confirmClicked(View view) {
    }
}
