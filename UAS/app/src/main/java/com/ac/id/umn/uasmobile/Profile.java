package com.ac.id.umn.uasmobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    private TextView nama, email, username;
    private CircleImageView profileimg;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;
    private String userID;
    private Button editprofile;
    private StorageReference storageReference;
    private ImageView KeProfile,KePost,KeFriend,KeHome,popupmenuicon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nama = findViewById(R.id.name);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        editprofile = (Button) findViewById(R.id.gantiprofile);
        profileimg = findViewById(R.id.imguser2);
        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userID = mAuth.getCurrentUser().getUid();

        popupmenuicon = findViewById(R.id.popupicon);

        popupmenuicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });

        KeFriend = findViewById(R.id.queue_bottom);
        KeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keFriend();
            }
        });
        KeHome = findViewById(R.id.home_bottom);
        KeHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keHome();
            }
        });
        KePost = findViewById(R.id.post_bottom);
        KePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kePost();
            }
        });

        DocumentReference documentReference = fstore.collection("user").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                nama.setText(documentSnapshot.getString("name"));
                email.setText(documentSnapshot.getString("email"));
                username.setText(documentSnapshot.getString("username"));
            }
        });
        StorageReference profileRef = storageReference.child("user/" + mAuth.getCurrentUser().getUid() + "/logo.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileimg);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        editprofile.setOnClickListener((v) -> {
            Intent i = new Intent(v.getContext(),EditProfile.class);
            i.putExtra("name", nama.getText().toString());
            i.putExtra("email",email.getText().toString());
            i.putExtra("username", username.getText().toString());
            startActivity(i);
    });



    }

    private void showMenu(View v) {
        PopupMenu popupmenu = new PopupMenu(Profile.this,v);
        popupmenu.getMenuInflater().inflate(R.menu.popupmenu,popupmenu.getMenu());
        popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.aboutusbtn2)
                    aboutus();
                if (item.getItemId() == R.id.logoutbtn)
                    Logout();
                return false;
            }

        });
    }

    private void Logout() {
        mAuth.signOut();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void aboutus() {
        Intent intent = new Intent(this, AboutUs.class);
        startActivity(intent);
    }

    private void keHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    private void keFriend() {
        Intent intent = new Intent(this, Friends.class);
        startActivity(intent);
    }

    private void kePost(){
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);

    }
}

