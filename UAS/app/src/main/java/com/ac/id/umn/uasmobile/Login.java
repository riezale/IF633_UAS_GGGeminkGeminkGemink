package com.ac.id.umn.uasmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Login extends AppCompatActivity {

    private ImageView Home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Home = (ImageView) findViewById(R.id.btnlogin2);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome();
            }
        });
    }
    private void openHome() {
        Intent intent = new Intent(this,Home.class);
        startActivity(intent);
    }
}