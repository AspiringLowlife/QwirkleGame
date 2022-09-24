package com.example.qwirklegame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // GameModel game=new GameModel(2);
        ImageView tileHand0=findViewById(R.id.tileHand0);
        ImageView tileHand1=findViewById(R.id.tileHand1);
        ImageView tileHand2=findViewById(R.id.tileHand2);
        ImageView tileHand3=findViewById(R.id.tileHand3);
        ImageView tileHand4=findViewById(R.id.tileHand4);
        ImageView tileHand5=findViewById(R.id.tileHand5);

        LinearLayout target=findViewById(R.id.dropTarget);
        LinearLayout tileBar=findViewById(R.id.tileBar);

        //Tile listeners
        tileHand0.setOnTouchListener(new MyTouchListener());
        tileHand1.setOnTouchListener(new MyTouchListener());
        tileHand3.setOnTouchListener(new MyTouchListener());
        tileHand2.setOnTouchListener(new MyTouchListener());
        tileHand4.setOnTouchListener(new MyTouchListener());
        tileHand5.setOnTouchListener(new MyTouchListener());
        //Target Listeners
        target.setOnDragListener(new MyDragListener());
        tileBar.setOnDragListener(new MyDragListener());
    }

    final class MyTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if(event.getAction()==MotionEvent.ACTION_DOWN ){
                ClipData data=ClipData.newPlainText("","");
                View.DragShadowBuilder shadowBuilder=new View.DragShadowBuilder(view);
                view.startDrag(data,shadowBuilder,view,0);
                view.setVisibility(View.VISIBLE);
                return true;
            }
            else return false;
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
}
