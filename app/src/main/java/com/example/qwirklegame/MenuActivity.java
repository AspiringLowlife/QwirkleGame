package com.example.qwirklegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codewithbill.Main;

public class MenuActivity extends AppCompatActivity {

    private TextView playerCountView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //Thread to check if any existing games available
        AndroidClient androidClient=new AndroidClient("CheckExisting",ConnectActivity.connectionString);
        androidClient.start();
        while (!androidClient.getState().equals(Thread.State.TERMINATED)) {
            int c = 0;//hacky way to add some sequencing into this
        }
        playerCountView=findViewById(R.id.txtPlayerNo);
        Button btnExisting=findViewById(R.id.btnJoinExisting);

        btnExisting.setVisibility(View.INVISIBLE);
        if(androidClient.getAreExistingGames())btnExisting.setVisibility(View.VISIBLE);
    }

    public void newGameClicked(View view) {
        Integer playerRequest=Integer.parseInt(playerCountView.getText().toString());
        AndroidClient androidClient=new AndroidClient(playerRequest,ConnectActivity.connectionString);
        androidClient.start();
        while (!androidClient.getState().equals(Thread.State.TERMINATED)) {
            int c = 0;//hacky way to add some sequencing into this
        }
        //start the main to play the game
        Intent intent = new Intent(this, WaitingRoom.class);
        intent.putExtra("player",androidClient.getPlayer());
        this.startActivity(intent);
    }

    public void existingGameClicked(View view) {
        AndroidClient androidClient=new AndroidClient("JoinExisting",ConnectActivity.connectionString);
        androidClient.start();
        while (!androidClient.getState().equals(Thread.State.TERMINATED)) {
            int c = 0;//hacky way to add some sequencing into this
        }
        Intent intent = new Intent(this, WaitingRoom.class);
        intent.putExtra("player",androidClient.getPlayer());
        this.startActivity(intent);
    }
}