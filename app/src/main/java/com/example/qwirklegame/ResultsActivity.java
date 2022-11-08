package com.example.qwirklegame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codewithbill.Player;
import com.codewithbill.PlayerRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ResultsActivity extends AppCompatActivity {

    LinearLayout table;
    Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        table = findViewById(R.id.bigBox);
        Bundle extras = this.getIntent().getExtras();
        player = (Player) extras.get("player");
        PlayerRequest playerRequest = new PlayerRequest("GetAllPlayers", player);
        AndroidClient androidClient = new AndroidClient(playerRequest, ConnectActivity.connectionString);
        androidClient.start();
        while (!androidClient.getState().equals(Thread.State.TERMINATED)) {
            int c = 0;//hacky way to add some sequencing into this
        }
        fillResultsTable(androidClient.getPlayers());
    }

    private void fillResultsTable(ArrayList<Player> players) {
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        //sort players list into descending order by score
        Collections.sort(players);

        int c = 1;
        for (Player player : players) {
            LinearLayout playerRow = new LinearLayout(this);
            playerRow.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView playerName = new TextView(this);
            playerName.setLayoutParams(param);
            playerName.setGravity(Gravity.CENTER);
//            if (player.getScore().equals(this.player.getScore())) {
//                playerName.setText("player " + c + "(you)");
            if(this.player.getPlayerID()==player.getPlayerID()){
                playerName.setText("player " + c + "(you)");
            } else playerName.setText("player " + c);

            TextView score = new TextView(this);
            score.setLayoutParams(param);
            score.setGravity(Gravity.CENTER);
            score.setText(player.getScore().toString());
            playerRow.addView(playerName);
            playerRow.addView(score);
            table.addView(playerRow);
            c++;
        }
    }

    public void btnReturnClicked(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        this.startActivity(intent);
    }
}