package com.test.shooting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LeaderBoardActivity extends AppCompatActivity {


    TextView timeDisplay;
    Button logOut;
    Button restart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        Intent intent=getIntent();

        String message=intent.getStringExtra("EXTRA_MESSAGE");
        String newMessage="You Time is:"+message;
        timeDisplay=findViewById(R.id.textView4);
        timeDisplay.setText(newMessage);


        restart=findViewById(R.id.button3);
        logOut=findViewById(R.id.button4);

        logOut.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent inToMain=new Intent(LeaderBoardActivity.this,LoginActivity.class);
            startActivity(inToMain);
        });

        restart.setOnClickListener(view -> {
            Toast.makeText(LeaderBoardActivity.this,"The game started", Toast.LENGTH_LONG).show();
            Intent inToGame=new Intent(LeaderBoardActivity.this,ShootingActivityAnimation.class);
            startActivity(inToGame);
        });


    }
}
