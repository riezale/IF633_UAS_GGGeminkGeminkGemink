package com.ac.id.umn.uasmobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity {

    private ImageView KeProfile,KePost,KeFriend;
    private TextView username;

    private FirebaseFirestore fstore;
    private String userID;
    private FirebaseAuth mAuth;
    private CircleImageView profileimg;
    private StorageReference storageReference;
    private RecyclerView recyclerView;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Post");
    private PostAdapter adapter;
    private ArrayList<Post> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        fstore = FirebaseFirestore.getInstance();
        username = findViewById(R.id.usernamehome);
        profileimg = findViewById(R.id.profile_image);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        list = new ArrayList<>();
        adapter = new PostAdapter(this,list,Home.this);

        recyclerView.setAdapter(adapter);

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Post model = dataSnapshot.getValue(Post.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        DocumentReference documentReference = fstore.collection("user").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                username.setText(documentSnapshot.getString("username"));
            }
        });
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("user/" + mAuth.getCurrentUser().getUid() + "/logo.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileimg);
                                                             }
            });



        KeProfile = findViewById(R.id.profile_bottom);
        KeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keProfile();
            }
        });
        KeFriend = findViewById(R.id.queue_bottom);
        KeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keFriend();
            }
        });
        KePost = findViewById(R.id.post_bottom);
        KePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kePost();
            }
        });
    }


    private void keFriend() {
        Intent intent = new Intent(this, Friends.class);
        startActivity(intent);
    }

    private void keProfile(){
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
    private void kePost(){
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);

    }
}
