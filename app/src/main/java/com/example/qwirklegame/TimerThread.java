package com.example.qwirklegame;

import com.codewithbill.MoveResponse;
import com.codewithbill.Player;
import com.codewithbill.Tile;

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
    private boolean isGameFull=false;

    Socket client;
    ObjectInputStream input;
    ObjectOutputStream output;
    //DataInputStream input;

    public TimerThread(Object request, String connectionString) {
        this.request = request;
        this.connectionString = connectionString;
    }

    @Override
    public void run() {
        while (!isGameFull) {
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
                isGameFull = (Boolean) serverResponse;
            }
            //every three seconds check
//            Thread.sleep(3000);

    }
}
