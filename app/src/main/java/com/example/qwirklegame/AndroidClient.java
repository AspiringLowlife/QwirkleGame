package com.example.qwirklegame;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AndroidClient extends Thread {

    private  Object request;
    private String connectionString;

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
        DataInputStream input;
        ObjectOutputStream output;

        try {
            System.out.println("Client started");

            // Step 1: Create a Socket to make connection.
//            client = new Socket(InetAddress.getLocalHost(), 500);
            client = new Socket(connectionString, 500);
            System.out.println("Connected to: "
                    + client.getInetAddress().getHostName());

            // Step 2: Get the input and output streams.
            input = new DataInputStream(client.getInputStream());
            output = new ObjectOutputStream(client.getOutputStream());
            System.out.println("Got I/O Streams");

            // Step 3: Process connection.
            System.out.println("Server message: " + input.readUTF());


            output.writeObject(request);
            System.out.printf("Sent message \"%s\"","client request");
            System.out.println();

            // Step 4: Close connection.
            System.out.println("Transmission complete. "
                    + "Closing connection.");
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


