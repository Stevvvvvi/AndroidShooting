package com.test.shooting;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartGameActivity extends AppCompatActivity {

    private Button btnLogOut;
    private Button startGame;
    private Button btnLeaderboard;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        //delete main activity
        MainActivity.main.finish();

        btnLogOut=findViewById(R.id.logout);
        startGame=findViewById(R.id.startgame);
        btnLeaderboard=findViewById(R.id.leaderboard);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent inToMain=new Intent(StartGameActivity.this,LoginActivity.class);
                startActivity(inToMain);
                finish();
            }
        });


        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StartGameActivity.this,"The game started", Toast.LENGTH_LONG).show();
                Intent inToGame=new Intent(StartGameActivity.this,ShootingActivityAnimation.class);
                startActivity(inToGame);
                //finish();
            }
        });

        btnLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StartGameActivity.this,"The game started", Toast.LENGTH_LONG).show();
                Intent i=new Intent(StartGameActivity.this,LeaderboardActivity.class);
                startActivity(i);
                //finish();
            }
        });


    }

}
