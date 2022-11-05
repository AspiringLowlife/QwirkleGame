package com.example.qwirklegame;

import com.codewithbill.MoveResponse;
import com.codewithbill.Player;
import com.codewithbill.Tile;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class AndroidClient extends Thread {

    private final Object request;
    private final String connectionString;
    private Player player;
    private ArrayList<Tile> board;
    private ArrayList<Player> players;
    private boolean isConnected;
    private boolean areExistingGames;

    Socket client;
    ObjectInputStream input;
    ObjectOutputStream output;
    //DataInputStream input;

    public AndroidClient(Object request, String connectionString) {
        this.request = request;
        this.connectionString = connectionString;
    }

    @Override
    public void run() {
        runClient();
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
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void processConnection() throws IOException, ClassNotFoundException {
        System.out.println("Server message: " + input.readObject());
        isConnected=true;

        //send request to server
        output.writeObject(request);
        output.flush();

        System.out.printf("Sent message \"%s\"", "client request");
        System.out.println();

        // Process any objects server passes
        Object serverResponse = input.readObject();
        //newPlayer assigned from server
        if (serverResponse.getClass().equals(Player.class)) {
            Player player = (Player) serverResponse;
            setPlayer(player);
        }
        //Receiving board state and updated hand for mainActivity
        else if(serverResponse.getClass().equals(MoveResponse.class)){
            MoveResponse moveResponse= (MoveResponse) serverResponse;
            setBoard(moveResponse.board);
            setPlayer(moveResponse.player);
        }
        //Checking if any existing games for menuActivity
        else if(serverResponse.getClass().equals(String.class)){
            areExistingGames=true;
        }else if(serverResponse.getClass().equals(ArrayList.class)){
            players=(ArrayList<Player>) serverResponse;
        }
    }

    public ArrayList<Tile> getBoard() {
        return board;
    }

    public void setBoard(ArrayList<Tile> board) {
        this.board = board;
    }

    public boolean getIsConnected(){
        return isConnected;
    }

    public boolean getAreExistingGames(){
        return areExistingGames;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}


