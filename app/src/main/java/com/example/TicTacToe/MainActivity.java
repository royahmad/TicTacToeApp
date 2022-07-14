package com.example.TicTacToe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    EditText player1name, player2name;
    String player1, player2;

    GridLayout grid;
    LinearLayout startGame;

    TextView playersTurnText;
    Button playAgainBtn, changeNames;

    int player = 0;
    int [][] winningMoments = {{0,1,2},{3,4,5},{6,7,8},{0,4,8},{2,4,6},{0,3,6},{1,4,7},{2,5,8}}; // mögliche gewinnreihen
    int [] tagState = {2,2,2,2,2,2,2,2,2}; //0 = x , 1 = o, 2 = leer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        grid = (GridLayout) findViewById(R.id.grid);
        grid.setVisibility(View.INVISIBLE);
        startGame = (LinearLayout) findViewById(R.id.startGame);
        startGame.setVisibility(View.VISIBLE);

        player1name = findViewById(R.id.player1name);
        player2name = findViewById(R.id.player2name);
        playersTurnText = findViewById(R.id.turnText);
        playAgainBtn = findViewById(R.id.playAgain);
        playAgainBtn.setVisibility(View.INVISIBLE);
        changeNames = findViewById(R.id.changeNames);
        changeNames.setVisibility(View.INVISIBLE);
    }

    protected void onStop(){
        super.onStop();
    }

    boolean gameActive = true;

    public void dropDown(View view){

        ImageView imgNumber = (ImageView) view; //angeklicktes Bild
        int tappedImg = Integer.parseInt(imgNumber.getTag().toString()); //Bild-tag in variable

        if (tagState[tappedImg] == 2 && gameActive){ //Bedingung --> Freies Feld und gameActive
            tagState[tappedImg] = player;
            imgNumber.setTranslationY(-1500);

            if (player == 0){
                player++;
                imgNumber.setImageResource(R.drawable.x);
                imgNumber.animate().alpha(0);
                playersTurnText.setText(player2 + "'s turn");
            }else{
                player--;
                imgNumber.setImageResource(R.drawable.o);
                imgNumber.animate().alpha(0);
                playersTurnText.setText(player1 + "'s turn");
            }
            imgNumber.animate().translationYBy(1500).alpha(1).setDuration(500);
            for(int[] winningOption : winningMoments){ //für jedes stück (winningOption) in winningMoments soll er die gewinnmöglichkeiten checken
                if (tagState[winningOption[0]] == tagState[winningOption[1]] && tagState[winningOption[1]] == tagState[winningOption[2]] && tagState[winningOption[0]] != 2){
                    String winner = "";
                    gameActive = false;
                    if (player == 0){
                        winner = player2;
                    }else{
                        winner = player1;
                    }
                    playersTurnText.setText(winner + " won");
                    playAgainBtn.setVisibility(View.VISIBLE);
                    changeNames.setVisibility(View.VISIBLE);
                    break;
                }else {
                    gameActive = false;
                    for (int tags : tagState){ //freie Felder verfügbar? --> Spiel geht weiter
                        if (tags == 2) gameActive = true;
                    }
                    if (!gameActive){  //unentschieden
                        playersTurnText.setText("draw");
                        playAgainBtn.setVisibility(View.VISIBLE);
                        changeNames.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }
    public void start (View view){
        grid.setVisibility(View.VISIBLE);
        startGame.setVisibility(View.INVISIBLE);
        reset();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(player1name.getWindowToken(), 0);
    }
    public void playAgain(View view){
        playAgainBtn.setVisibility(View.INVISIBLE);
        changeNames.setVisibility(View.INVISIBLE);
        reset();
        }
    public void changeNames (View view){
        grid.setVisibility(View.INVISIBLE);
        startGame.setVisibility(View.VISIBLE);
        playAgainBtn.setVisibility(View.INVISIBLE);
        changeNames.setVisibility(View.INVISIBLE);
        playersTurnText.setText("");
    }
    public void reset(){
        gameActive = true;
        for (int i = 0; i < grid.getChildCount(); i++){ //solange i kleiner ist als die childZahl vom layout
            ImageView counter = (ImageView) grid.getChildAt(i); //gebe counter das bild vom Layout Child i
            counter.setImageDrawable(null); //setze das bild auf leer
            tagState[i] = 2;
        }
        Random rdmBeginner = new Random();
        int beginner = rdmBeginner.nextInt(2);
        player = beginner; //zufälliger spieler beginnt
        player1 = player1name.getText().toString();
        player2 = player2name.getText().toString();
        if (player == 0){ playersTurnText.setText(player1 + " begins"); }
        else{ playersTurnText.setText(player2 + " begins"); }
    }
}