package com.example.qwirklegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.codewithbill.Player;
import com.codewithbill.PlayerRequest;

public class WaitingRoom extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
    }

    public void btnClicked(View view) {
        Bundle extras = this.getIntent().getExtras();
        PlayerRequest playerRequest = new PlayerRequest("CheckIsGameReady", (Player) extras.get("player"));
        TimerThread timerThread = new TimerThread(playerRequest, ConnectActivity.connectionString);
        timerThread.start();
        while (!timerThread.getState().equals(Thread.State.TERMINATED)) {
            int c = 0;//hacky way to add some sequencing into this
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("player", (Player) extras.get("player"));
        this.startActivity(intent);
    }
}