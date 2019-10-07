package com.test.shooting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GameResultActivity extends AppCompatActivity {

    private TextView timeDisplay,BesttimeDisplay;
    private Button back;
    private Button restart;
    private FirebaseFirestore db;
    private static final String TAG = "GameResultActivity";
    FirebaseAuth mFirebaseAuth;
    private String best_time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);

        db = FirebaseFirestore.getInstance();
        mFirebaseAuth=FirebaseAuth.getInstance();

        timeDisplay=findViewById(R.id.time);
        BesttimeDisplay = findViewById(R.id.besttime);
        restart=findViewById(R.id.restart);
        back=findViewById(R.id.back);

        //get the time value from shooting activity
        Intent intent=getIntent();
        String gametime=intent.getStringExtra("EXTRA_MESSAGE");
        timeDisplay.setText(transformTime(gametime));


        //read the time from user database and get the best time. Then write the best time
        readvalue(gametime);



        back.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent inToMain=new Intent(GameResultActivity.this,StartGameActivity.class);
            startActivity(inToMain);
            finish();
        });

        restart.setOnClickListener(view -> {
            Toast.makeText(GameResultActivity.this,"The game started", Toast.LENGTH_LONG).show();
            Intent inToGame=new Intent(GameResultActivity.this,ShootingActivity.class);
            startActivity(inToGame);
            finish();
        });
    }



    public void readvalue(String gametime){
        //read the time from user database
        db.collection("Users")
                .document(mFirebaseAuth.getCurrentUser().getEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserList userList = documentSnapshot.toObject(UserList.class);
//                        Log.d(TAG, "ReadFromeFirebase: "+userList.getUserTime());

                        if (userList.getUserTime().isEmpty()){
                            best_time = gametime;
//                            Log.d(TAG, "besttime0: "+ best_time);
                            writevalue(best_time);
                            BesttimeDisplay.setText(transformTime(best_time));


                        }else {
                            best_time = getBestTime(userList.getUserTime(),gametime);
//                            Log.d(TAG, "besttime1: "+ best_time);
                            writevalue(best_time);
                            BesttimeDisplay.setText(transformTime(best_time));

                        }

                    }
                });
    }

    public void writevalue(String gametime){
//        Log.d(TAG, "besttime2: "+ best_time);
        UserList userList = new UserList(gametime);
        db.collection("Users")
                .document(mFirebaseAuth.getCurrentUser().getEmail())
                .set(userList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(GameResultActivity.this, "Successful!", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error = e.getMessage();
                        Toast.makeText(GameResultActivity.this, "Error"+error, Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public String getBestTime(String best, String thisgame){
        String result = "";
        String[] best_arrary = best.split(":");
        String[] thisgame_arrary = thisgame.split(":");

        if (Integer.parseInt(best_arrary[0]) < Integer.parseInt(thisgame_arrary[0])){
            result = best;
        }else if (Integer.parseInt(best_arrary[0]) > Integer.parseInt(thisgame_arrary[0])){
            result = thisgame;

        }else if (Integer.parseInt(best_arrary[0]) == Integer.parseInt(thisgame_arrary[0])){
            if (Integer.parseInt(best_arrary[1]) <= Integer.parseInt(thisgame_arrary[1])){
                result = best;
            }else if (Integer.parseInt(best_arrary[0]) > Integer.parseInt(thisgame_arrary[0])){
                result = thisgame;
            }
        }

        return result;
    }

    public String transformTime(String time){
        String[] time_arrary = time.split(":");

        return time_arrary[0]+"min "+time_arrary[1]+"s";
    }

    /**
     * user to write and read data with firebase
     */
    public static class UserList {
        private String userTime;

        public UserList() {}

        public UserList(String userTime) {
            this.userTime = userTime;

        }
        public String getUserTime() {return userTime;}
    }

}




