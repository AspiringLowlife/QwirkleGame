package com.example.qwirklegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codewithbill.Player;

import java.util.ArrayList;

public class ConnectActivity extends AppCompatActivity {

    public static String connectionString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        TextView textIP = findViewById(R.id.textIP);
        Button btnConnect = findViewById(R.id.btnConnect);

        btnConnect.setOnClickListener(view -> {
            Intent intent = new Intent(this, MenuActivity.class);
            connectionString = textIP.getText().toString();

            //Pass requested players for multiplayer passing just an int will create a new player
            AndroidClient androidClient = new AndroidClient("CheckConnection", connectionString);
            androidClient.start();

            while (!androidClient.getState().equals(Thread.State.TERMINATED)) {
                int c = 0;//hacky way to add some sequencing into this
            }
            //go to menu
            if (androidClient.getIsConnected()) {
                this.startActivity(intent);
            } else
                Toast.makeText(this, "Re-enter correct IP", Toast.LENGTH_SHORT).show();
        });
    }
}