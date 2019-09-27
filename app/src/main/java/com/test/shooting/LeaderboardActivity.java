package com.test.shooting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Array;

public class LeaderboardActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private static final String TAG = "LeaderboardActivity";
    TextView top[] = new TextView[10];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        top[0]=findViewById(R.id.top1);
        top[1]=findViewById(R.id.top2);
        top[2]=findViewById(R.id.top3);
        top[3]=findViewById(R.id.top4);
        top[4]=findViewById(R.id.top5);
        top[5]=findViewById(R.id.top6);
        top[6]=findViewById(R.id.top7);
        top[7]=findViewById(R.id.top8);
        top[8]=findViewById(R.id.top9);
        top[9]=findViewById(R.id.top10);


        db = FirebaseFirestore.getInstance();

        db.collection("Users")
                .orderBy("userTime")
                .limit(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                UserList userList = document.toObject(UserList.class);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Log.d(TAG, "userTime" + " => " + userList.getUserTime());
                                top[i].setText(document.getId() + " => " + userList.getUserTime());
                                i++;

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
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
