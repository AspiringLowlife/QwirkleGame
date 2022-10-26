package com.example.qwirklegame;

import com.codewithbill.Player;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AndroidClient extends Thread {

    private  Object request;
    private String connectionString;
    private Player player;

    public AndroidClient(Object request, String connectionString) {
        this.request = request;
        this.connectionString=connectionString;
    }

    @Override
    public void run() {
        runClient();
    }

    public void runClient() {
        Socket client;
        ObjectInputStream input;
        ObjectOutputStream output;
        //DataInputStream input;

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
            input=new ObjectInputStream(client.getInputStream());
            System.out.println("Got I/O Streams");

            // Step 3: Process connection.
            System.out.println("Server message: " + input.readObject());

            output.writeObject(request);
            output.flush();

            System.out.printf("Sent message \"%s\"","client request");
            System.out.println();

           // Process any objects server passes
            Object serverRequest=input.readObject();
            if(serverRequest.getClass().equals(Player.class)){
                Player player=(Player) serverRequest;
                setPlayer(player);
            }

            // Step 4: Close connection.
            System.out.println("Transmission complete. "
                    + "Closing connection.");
            client.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}


