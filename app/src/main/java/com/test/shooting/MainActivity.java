package com.test.shooting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText emailId, password;
    private Button btnSignUp;
    private TextView tvLogin;
    FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth=FirebaseAuth.getInstance();
        emailId=findViewById(R.id.signup_email);
        password=findViewById(R.id.signup_password);
        btnSignUp=findViewById(R.id.btnSignUp);
        tvLogin=findViewById(R.id.tvLogin);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailId.getText().toString();
                String pwd=password.getText().toString();
                if (email.isEmpty()){
                    emailId.setError("please enter email id");
                    emailId.requestFocus();
                }
                else if (pwd.isEmpty()){
                    password.setError("please enter your password");
                    password.requestFocus();

                }
                else if (email.isEmpty()&&pwd.isEmpty()){
                    Toast.makeText(MainActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();

                }
                else if (!(email.isEmpty()&&pwd.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"SignUp Unsuccessful, Please Try Again!",Toast.LENGTH_SHORT).show();

                            }else {
                                startActivity(new Intent(MainActivity.this,StartGameActivity.class));
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(MainActivity.this,"Error!",Toast.LENGTH_SHORT).show();
                }
            }
        });


        tvLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent i=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });



    }
}
