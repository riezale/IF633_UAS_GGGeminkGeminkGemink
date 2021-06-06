package com.ac.id.umn.uasmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    public static final String TAG = "TAG";
    private EditText UserFirstName, UserLastName, UserEmail, UserName, UserPassword, UserConfirmPassword;
    private ImageView Register;
    private ProgressDialog loadingBar;
    private FirebaseFirestore fstore;
    String userID;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fstore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        UserFirstName = (EditText) findViewById(R.id.textView2);
        UserLastName = (EditText) findViewById(R.id.textView5);
        UserEmail = (EditText) findViewById(R.id.emailregister);
        UserName = (EditText) findViewById(R.id.usernameregister);
        UserPassword = (EditText) findViewById(R.id.passwordregister);
        UserConfirmPassword = (EditText) findViewById(R.id.cpasswordregister);
        Register = (ImageView) findViewById(R.id.btnregister2);
        loadingBar = new ProgressDialog(this);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createaccount();
            }
        });

    }

    private void createaccount() {
        String FirstName = UserFirstName.getText().toString();
        String LastName = UserLastName.getText().toString();
        String Email = UserEmail.getText().toString();
        String Name = UserName.getText().toString();
        String Password = UserPassword.getText().toString();
        String ConfirmPassword = UserConfirmPassword.getText().toString();


        if(TextUtils.isEmpty(FirstName)){
            Toast.makeText(this, "Tolong Masukan Nama Depan", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Email)){
            Toast.makeText(this, "Tolong Masukan Email", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Name)){
            Toast.makeText(this, "Tolong Masukan Username", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Password)){
            Toast.makeText(this, "Tolong Masukan Password", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(ConfirmPassword)){
            Toast.makeText(this, "Tolong Masukan Password ulang", Toast.LENGTH_SHORT).show();
        }
        else if(!Password.equals(ConfirmPassword)){
            Toast.makeText(this, "Password Tidak Sesuai", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Register New Account");
            loadingBar.setMessage("Mohon Tunggu");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                SendUserToSetUpActivity();

                                Toast.makeText(Register.this, "Anda Berhasil Register", Toast.LENGTH_SHORT).show();
                                userID = mAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fstore.collection("user").document(userID);
                                Map<String,Object> user = new HashMap<>();
                                user.put("name", FirstName);
                                user.put("email",Email);
                                user.put("username",Name);
                                user.put("password",Password);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "On Success: user profile is created for "+ userID );
                                    }
                                });
                                loadingBar.dismiss();
                            }
                            else {
                                String message = task.getException().getMessage();
                                Toast.makeText(Register.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }
    }

    private void SendUserToSetUpActivity() {
        Intent setupIntent = new Intent(Register.this, Login.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }
}