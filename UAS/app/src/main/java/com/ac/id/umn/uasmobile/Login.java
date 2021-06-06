package com.ac.id.umn.uasmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    private ImageView Login;
    private EditText UserEmail, UserPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth= FirebaseAuth.getInstance();

        UserEmail = (EditText) findViewById(R.id.emaillogin);
        UserPassword = (EditText) findViewById(R.id.passwordlogin);
        Login = (ImageView) findViewById(R.id.btnlogin2);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowingUserToLogin();
            }
        });
    }

    private void AllowingUserToLogin() {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Input Email", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Input Password", Toast.LENGTH_SHORT).show();
        }
        else
            {
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                sendUserToHome();
                                String message = "You are Logged in Qilogram App";
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(Login.this )
                                        .setSmallIcon(R.drawable.ic_baseline_message)
                                        .setContentTitle("Qilogram")
                                        .setContentText(message)
                                        .setAutoCancel(true);
                                NotificationManager notificationManager = (NotificationManager)getSystemService(
                                        Context.NOTIFICATION_SERVICE
                                );
                                notificationManager.notify(0,builder.build());
                                Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                String message = task.getException().getMessage();
                                Toast.makeText(Login.this, "Error Occured" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void sendUserToHome()
    {
        String message = "Qilogram Login Success";
        Intent mainIntent = new Intent(Login.this, Home.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainIntent.putExtra("message",message);
        startActivity(mainIntent);
        finish();
    }


}