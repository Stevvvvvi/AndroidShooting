package com.test.shooting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    public static MainActivity main = null; //allow other activity to finish this activity
    private Button button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = this;//allow other activity to finish this activity


        setContentView(R.layout.activity_main);


        button=findViewById(R.id.btn_tologin);


        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent i=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });



    }
}
