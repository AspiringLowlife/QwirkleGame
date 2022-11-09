package com.example.qwirklegame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.codewithbill.GameModel;
import com.codewithbill.MoveResponse;
import com.codewithbill.Player;
import com.codewithbill.PlayerRequest;
import com.codewithbill.Tile;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView txtScore;
    private LinearLayout handView, swapTarget;
    //board fields
    LinearLayout outer;
    private List<ImageView> handViewCopy = new ArrayList<>();
    private Player player;
    private ArrayList<Tile> myBoard = new ArrayList<>();
    private boolean isCancelled;
    Button btnConfirm;
    ImageView btnCancel;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handView = findViewById(R.id.tileBar);
        swapTarget = findViewById(R.id.swapTarget);
        //Board
        outer = findViewById(R.id.outer);
        txtScore = findViewById(R.id.txtScore);
        Bundle extras = this.getIntent().getExtras();
        player = (Player) extras.get("player");
        btnConfirm = findViewById(R.id.confirmBtn);
        btnCancel = findViewById(R.id.cancelBtn);
        setTilesAndListeners(handView, swapTarget);
    }

    public void confirmClicked(View view) {
        ArrayList<Tile> hand = player.getHand();
        //check for swap then exchange pieces
        if (swapTarget.getChildCount() > 0) {
            ArrayList<Tile> swappingPieces = new ArrayList<>();
            for (int i = 0; i < swapTarget.getChildCount(); i++) {
                ImageView swapView = (ImageView) swapTarget.getChildAt(i);
                int resId = (int) swapView.getTag();
                String drawableName = this.getResources().getResourceEntryName(resId);
                for (Tile tile : hand) {
                    if (tile.toString().equals(drawableName)) {
                        tile.setState(Tile.State.swapping);
                        break;
                    }
                }
            }
            swapTarget.removeAllViews();
        }
        //check for tiles placed on board
        else if (isHandOnBoard()) {
            hand = placeTiles(hand);
        }
        //checks if any move was made prior to pressing confirm btn
        if (checkForNoAction(hand)) {
            Toast.makeText(this, "Please swap or place tile on board before confirming", Toast.LENGTH_LONG).show();
            return;
        }
        this.handView.removeAllViews();
//        this.boardView.removeAllViews();

        //inform Server of move and swap cur player
        AndroidClient androidClient = new AndroidClient(player, ConnectActivity.connectionString);
        androidClient.start();
        while (!androidClient.getState().equals(Thread.State.TERMINATED)) {
            int c = 0;//hacky way to add some sequencing into this
        }
        player = androidClient.getPlayer();
        myBoard = androidClient.getBoard();
        fillHandView();
        txtScore.setText("Score: " + player.getScore());
        PlayerRequest playerRequest = new PlayerRequest("CheckTurn", player);
        TimerTask timerTask = new TimerTask(playerRequest, ConnectActivity.connectionString, this);
        timerTask.execute();
    }

    //add drag and drop and sets initial tiles from initial player hand
    private void setTilesAndListeners(LinearLayout tileBar, LinearLayout swapTarget) {
        //setting the image for tiles in hand initially
        fillHandView();
        //check if current player initial
        PlayerRequest playerRequest = new PlayerRequest("CheckTurn", player);
        TimerTask timerTask = new TimerTask(playerRequest, ConnectActivity.connectionString, this);
        timerTask.execute();

        //Target Listeners dropping tiles into

        tileBar.setOnDragListener(new MyDragListener());
        swapTarget.setOnDragListener(new MyDragListener());
    }

    private void fillHandView() {
        ArrayList<Tile> hand = player.getHand();
        ArrayList<ImageView> temp = new ArrayList<>();
        for (int j = 0; j < hand.size(); j++) {
            //creating image view to fill into tileBar
            ImageView viewTile = new ImageView(this);
            String drawableName = hand.get(j).toString();
            viewTile.setImageResource(getResources().getIdentifier(drawableName, "drawable", this.getPackageName()));
            //attaching resource id as tag
            viewTile.setTag(getResources().getIdentifier(drawableName, "drawable", this.getPackageName()));
            viewTile.setOnTouchListener(new MyTouchListener());
            handView.addView(viewTile);
            temp.add(viewTile);
        }
        handViewCopy = temp;
    }

    private void fillBoard(ArrayList<Tile> board) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        //fill board
        for (int i = 0; i < 20; i++) {
            LinearLayout col = new LinearLayout(this);
            col.setOrientation(LinearLayout.VERTICAL);
            for (int j = 0; j < 20; j++) {
                LinearLayout cell = new LinearLayout(this);
                cell.setBackgroundResource(R.color.purple_200);
                params.setMargins(20, 20, 20, 20);
                cell.setLayoutParams(params);
                cell.setPadding(54, 54, 54, 54);
                cell.setOnDragListener(new MyDragListener());
                //fill cell with tile if present
                for (Tile tile : board) {
                    if (tile.row == j && tile.col == i) {
                        ImageView viewTile = new ImageView(this);
                        viewTile.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        String drawableName = tile.toString();
                        viewTile.setImageResource(getResources().getIdentifier(
                                drawableName, "drawable", this.getPackageName()));

                        //attaching resource id as tag
                        viewTile.setTag(getResources().getIdentifier(drawableName, "drawable", this.getPackageName()));
                        //highlight pieces of other player
                        if (isNewPiece(tile)) {
                            viewTile.setBackgroundResource(R.color.green_highlight);
                            viewTile.setPadding(6, 6, 6, 6);
                        }
                        cell.addView(viewTile);
                        cell.setPadding(0, 0, 0, 0);
                        break;
                    }
                }
                col.addView(cell);
            }
            outer.addView(col);
        }
    }

    private boolean isNewPiece(Tile checkTile) {
        int row = checkTile.row;
        int col = checkTile.col;
        for (Tile tile : myBoard) {
            if (tile.row == row && tile.col == col) return false;
        }
        return true;
    }

    private boolean isHandOnBoard() {
        try {
            for (int i = 0; i < outer.getChildCount(); i++) {
                LinearLayout col = (LinearLayout) outer.getChildAt(i);
                for (int j = 0; j < col.getChildCount(); j++) {
                    LinearLayout cell = (LinearLayout) col.getChildAt(j);
                    if (cell.getChildCount() < 1) {
                        continue;
                    } else {
                        for (int c = 0; c < handViewCopy.size(); c++) {
                            if (cell.getChildAt(0).equals(handViewCopy.get(c))) return true;
                        }
                    }
                }
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    private ArrayList<Tile> placeTiles(ArrayList<Tile> hand) {
        for (int i = 0; i < outer.getChildCount(); i++) {
            LinearLayout col = (LinearLayout) outer.getChildAt(i);
            for (int j = 0; j < col.getChildCount(); j++) {
                LinearLayout cell = (LinearLayout) col.getChildAt(j);
                if (cell.getChildCount() < 1) {
                    continue;
                } else {
                    for (int c = 0; c < handViewCopy.size(); c++) {
                        if (cell.getChildAt(0).equals(handViewCopy.get(c))) {
                            int resId = (int) cell.getChildAt(0).getTag();
                            String drawableName = this.getResources().getResourceEntryName(resId);
                            for (Tile tile : hand) {
                                if (tile.toString().equals(drawableName)) {
                                    tile.row = j;
                                    tile.col = i;
                                    tile.setState(Tile.State.placing);//this will go onto board in server
                                    break;
                                }
                            }
                        }
                    }
                }
            }

        }
        return hand;
    }
//        for (int i = 0; i < boardView.getChildCount(); i++) {
//            for (int j = 0; j < handViewCopy.size(); j++) {
//                if (boardView.getChildAt(i).equals(handViewCopy.get(j))) {
//                    int resId = (int) boardView.getChildAt(i).getTag();
//                    String drawableName = this.getResources().getResourceEntryName(resId);
//                    for (Tile tile : hand) {
//                        if (tile.toString().equals(drawableName)) {
//                            tile.setState(Tile.State.placing);
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//    }

//    private ArrayList<String> getBoardList() {
//    }

    private boolean isTileInHand(View viewTile) {
        String drawableName = this.getResources().getResourceEntryName((int) viewTile.getTag());
        for (Tile tile : player.getHand()) {
            if (tile.toString().equals(drawableName)) return true;
        }
        return false;
    }

    private boolean checkForNoAction(ArrayList<Tile> hand) {
        for (Tile tile : hand) {
            if (!tile.state.equals(Tile.State.inHand)) return false;
        }
        return true;
    }

    public void btnCancelClicked(View view) {
        isCancelled = true;
        PlayerRequest playerRequest = new PlayerRequest("LeaveGame", player);
        AndroidClient androidClient = new AndroidClient(playerRequest, ConnectActivity.connectionString);
        androidClient.start();
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("player", player);
        this.startActivity(intent);
    }

    final class MyTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (isTileInHand(view)) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDrag(data, shadowBuilder, view, 0);
                    view.setVisibility(View.VISIBLE);
                    return true;
                } else return false;
            }
            return false;
        }
    }

    final class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
//                    v.setBackgroundDrawable(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
//                    v.setBackgroundDrawable(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    int diff = 6 - swapTarget.getChildCount();
                    if (isTileOnBoard(v) && swapTarget.getChildCount() == 0 || v.equals(swapTarget) && handView.getChildCount() == diff || v.equals(handView)) {

                        View view = (View) event.getLocalState();
                        ViewGroup owner = (ViewGroup) view.getParent();
                        owner.removeView(view);
                        if (owner != handView) {
                            owner.setPadding(54, 54, 54, 54);
                        }
                        LinearLayout container = (LinearLayout) v;
                        container.addView(view);
                        container.setPadding(0, 0, 0, 0);
                        view.setVisibility(View.VISIBLE);
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            return true;
        }

        private boolean isTileOnBoard(View v) {
            return v.getParent().getParent() == outer;

        }
    }


    public class TimerTask extends AsyncTask<Object, Object, Object> {
        private final Object request;
        private final String connectionString;
        private ArrayList<Tile> board;
        private boolean conditionMet = false;
        private final Context context;
        private boolean gameDone = false;

        Socket client;
        ObjectInputStream input;
        ObjectOutputStream output;

        public TimerTask(Object request, String connectionString, Context context) {
            super();
            this.request = request;
            this.connectionString = connectionString;
            this.context = context;

            informPlayer("OPPONENT'S TURN");
            btnCancel.setVisibility(View.INVISIBLE);
            btnConfirm.setVisibility(View.INVISIBLE);
            outer.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Object doInBackground(Object... request) {
            while (!conditionMet) {
                runClient();
                if (isCancelled) break;
            }
            return board;
        }

        @Override
        protected void onProgressUpdate(Object... progress) {
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(Object board) {
            super.onPostExecute(board);
            //This task ends once condition met
            //request defines what the ending condition is
            //Game ending
            if (gameDone) {
                Intent intent = new Intent(context, ResultsActivity.class);
                intent.putExtra("player", (Player) player);
                context.startActivity(intent);
                return;
            } else if (isCancelled) {
                Intent intent = new Intent(context, MenuActivity.class);
                context.startActivity(intent);
            }
            //clear board
            outer.removeAllViews();
            //unlock controls and update textBox
            informPlayer("YOUR TURN");
            fillBoard(this.board);
            btnCancel.setVisibility(View.VISIBLE);
            outer.setVisibility(View.VISIBLE);
            btnConfirm.setVisibility(View.VISIBLE);
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

                input = new ObjectInputStream(client.getInputStream());
                System.out.println("Got I/O Streams");

                // Step 3: Process connection.
                processConnection();

                // Step 4: Close connection.
                System.out.println("Transmission complete. "
                        + "Closing connection.");
                client.close();

                //every three seconds check
                Thread.sleep(2000);
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
            //checks for turn
            if (serverResponse.getClass().equals(Boolean.class)) {
                conditionMet = (Boolean) serverResponse;
            } else if (serverResponse.getClass().equals(MoveResponse.class)) {
                board = ((MoveResponse) serverResponse).board;
                conditionMet = true;
            }
            //checks if game is done
            else if (serverResponse.getClass().equals(String.class)) {
                if (serverResponse.equals("GameDone")) {
                    gameDone = true;
                    conditionMet = true;
                }
            }
        }

        //methods to alter views in UI
        public void informPlayer(String msg) {
            TextView txtBox = findViewById(R.id.txtCurPlayer);
            txtBox.setText(msg);
        }
    }
}
