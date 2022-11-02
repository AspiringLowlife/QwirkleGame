package com.example.qwirklegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.codewithbill.MoveResponse;
import com.codewithbill.Player;
import com.codewithbill.PlayerRequest;
import com.codewithbill.Tile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class WaitingRoom extends AppCompatActivity {

    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        extras = this.getIntent().getExtras();
        PlayerRequest playerRequest = new PlayerRequest("CheckIsGameReady", (Player) extras.get("player"));
        TimerTask timerTask = new TimerTask(playerRequest, ConnectActivity.connectionString, this);

        timerTask.execute();
        timerTask.informPlayer();
    }

    public void btnClicked(View view) {

    }

    public class TimerTask extends AsyncTask<Object, Object, Object> {
        private final Object request;
        private final String connectionString;
        private ArrayList<Tile> board;
        private boolean conditionMet = false;
        private final Context context;

        Socket client;
        ObjectInputStream input;
        ObjectOutputStream output;

        public TimerTask(Object request, String connectionString, Context context) {
            super();
            this.request = request;
            this.connectionString = connectionString;
            this.context = context;
        }

        @Override
        protected Object doInBackground(Object... request) {
            while (!conditionMet) {
                runClient();
            }
            return board;
        }

        @Override
        protected void onPostExecute(Object board) {
            // IMPORTANT: this method is synched with UI thread, so can access UI
            super.onPostExecute(board);

            //start intent
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("player", (Player) extras.get("player"));
            context.startActivity(intent);
        }

        public ArrayList<Tile> getBoard() {
            return board;
        }

//        public void setPlayer(Player player) {
//            this.player = player;
//        }

        public void runClient() {
            try {
                System.out.println("Client started");

                // Step 1: Create a Socket to make connection.
//            client = new Socket(InetAddress.getLocalHost(), 500);
                client = new Socket(connectionString, 500);
                System.out.println("Connected to: "
                        + client.getInetAddress().getHostName());

                // Step 2: Get the input and output streams.
                output = new ObjectOutputStream(client.getOutputStream());
                output.flush();
                // input = new DataInputStream(client.getInputStream());
                input = new ObjectInputStream(client.getInputStream());
                System.out.println("Got I/O Streams");

                // Step 3: Process connection.
                processConnection();

                // Step 4: Close connection.
                System.out.println("Transmission complete. "
                        + "Closing connection.");
                client.close();

                //every three seconds check
                Thread.sleep(3000);
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void processConnection() throws IOException, ClassNotFoundException, InterruptedException {
            System.out.println("Server message: " + input.readObject());

            //send request to server
            output.writeObject(request);
            output.flush();

            System.out.printf("Sent message \"%s\"", "client request");
            System.out.println();

            // Process any objects server passes
            Object serverResponse = input.readObject();
            //Response for isGameReady
            if (serverResponse.getClass().equals(Boolean.class)) {
                conditionMet = (Boolean) serverResponse;
            } else if (serverResponse.getClass().equals(MoveResponse.class)) {
                board = ((MoveResponse) serverResponse).board;
                conditionMet = true;
            }
        }

        //methods to alter views in UI
        public void informPlayer() {
            TextView txtBox = findViewById(R.id.descriptiveTxt);
            txtBox.setText("Waiting For More Players...");
        }
    }
}