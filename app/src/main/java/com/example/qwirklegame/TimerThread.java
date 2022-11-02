package com.example.qwirklegame;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.codewithbill.MoveResponse;
import com.codewithbill.Player;
import com.codewithbill.Tile;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class TimerThread extends Thread {
    private final Object request;
    private final String connectionString;
    private Player player;
    private ArrayList<Tile> board;
    private boolean conditionMet = false;
    private Context context;

    Socket client;
    ObjectInputStream input;
    ObjectOutputStream output;

    public TimerThread(Object request, String connectionString, Context context) {
        this.request = request;
        this.connectionString = connectionString;
        this.context=context;
    }

    @Override
    public void run() {
        while (!conditionMet) {
            runClient();

        }
    }

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

    public ArrayList<Tile> getBoard() {
        return board;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

