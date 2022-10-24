package com.example.qwirklegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ConnectActivity extends AppCompatActivity {

    public static String connectionString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        TextView textIP=findViewById(R.id.textIP);
        Button btnConnect=findViewById(R.id.btnConnect);

        btnConnect.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
//            String connectionString=textIP.getText().toString();
//            intent.putExtra("connectionString",connectionString);
            connectionString=textIP.getText().toString();
            this.startActivity(intent);
        });
    }
}