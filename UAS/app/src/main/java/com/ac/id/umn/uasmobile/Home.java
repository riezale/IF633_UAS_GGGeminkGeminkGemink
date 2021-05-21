package com.ac.id.umn.uasmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    private Button KeProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        KeProfile = (Button) findViewById(R.id.keprofile);
        KeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keProfile();
            }
        });
    }

    private void keProfile(){
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
}
