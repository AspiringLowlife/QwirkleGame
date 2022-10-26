package com.example.qwirklegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.codewithbill.Player;

import java.util.ArrayList;

public class ConnectActivity extends AppCompatActivity {

    //TODO remove static declaration
    public static String connectionString = "10.107.12.15";
    public static Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        TextView textIP = findViewById(R.id.textIP);
        Button btnConnect = findViewById(R.id.btnConnect);

        btnConnect.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
//            String connectionString=textIP.getText().toString();
//            intent.putExtra("connectionString",connectionString);
            // connectionString=textIP.getText().toString();

            //get the new player info to pass to MainActivity
            AndroidClient androidClient = new AndroidClient("NewPlayer", connectionString);
            androidClient.start();
            while (!androidClient.getState().equals(Thread.State.TERMINATED)) {
                int c = 0;//hacky way to add some sequencing into this
            }
            player = androidClient.getPlayer();
            if (player != null) {
                this.startActivity(intent);
            }
        });
    }
}