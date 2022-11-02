package com.example.qwirklegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.codewithbill.Main;

public class MenuActivity extends AppCompatActivity {

    private RadioButton radioTwo,radioThree,radioFour;
    Button btnExisting;
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
        Button btnCheck=findViewById(R.id.btnCheck);
        btnExisting=findViewById(R.id.btnJoinExisting);
        radioTwo=findViewById(R.id.radioTwo);
        radioThree=findViewById(R.id.radioThree);
        radioFour=findViewById(R.id.radioFour);

        btnExisting.setVisibility(View.INVISIBLE);
        if(androidClient.getAreExistingGames())btnExisting.setVisibility(View.VISIBLE);
        if(btnExisting.getVisibility()==View.VISIBLE)btnCheck.setVisibility(View.INVISIBLE);
    }

    public void newGameClicked(View view) {
        Integer playerRequest=0;
        if(radioTwo.isChecked()){playerRequest=2;}
        else if(radioThree.isChecked()){ playerRequest=3;}
        else if(radioFour.isChecked()){playerRequest=4;}
        if(playerRequest==0){
            Toast.makeText(this, "Please choose how many players for your game", Toast.LENGTH_SHORT).show();
            return;
        }
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

    public void btnCheckClicked(View view) {
        AndroidClient androidClient=new AndroidClient("CheckExisting",ConnectActivity.connectionString);
        androidClient.start();
        while (!androidClient.getState().equals(Thread.State.TERMINATED)) {
            int c = 0;//hacky way to add some sequencing into this
        }
        if(androidClient.getAreExistingGames())btnExisting.setVisibility(View.VISIBLE);
    }
}