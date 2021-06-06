package com.ac.id.umn.uasmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class Friends extends AppCompatActivity {

    private ImageButton searchbutton;
    private EditText searchbar;

    private RecyclerView searchresult;
    private DatabaseReference allUserDatabaseRef;
    private ImageView KeProfile,KePost,KeFriend,KeHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        allUserDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

        searchresult = (RecyclerView) findViewById(R.id.searchlist);
        searchresult.setHasFixedSize(true);
        searchresult.setLayoutManager(new LinearLayoutManager(this));

        searchbutton = (ImageButton) findViewById(R.id.searchbut);
        searchbar = (EditText) findViewById(R.id.boxsearch);

        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchBoxInput = searchbar.getText().toString();
                SearchFriends(searchBoxInput);
            }
        });

        KeProfile = findViewById(R.id.profile_bottom);
        KeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keProfile();
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

    }

    private void SearchFriends(String searchBoxInput) {

        Toast.makeText(this, "searching........", Toast.LENGTH_LONG).show();
        Query searchPeopleandFriendsQuery = allUserDatabaseRef.orderByChild("Fullname")
                .startAt(searchBoxInput).endAt(searchBoxInput + "\uf8ff");

        FirebaseRecyclerAdapter<FindFriends, FindFriendsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<FindFriends, FindFriendsViewHolder>
                (
                        FindFriends.class,
                        R.layout.all_display_layout,
                        FindFriendsViewHolder.class,
                        searchPeopleandFriendsQuery

                )
        {
            @Override
            protected void populateViewHolder(FindFriendsViewHolder findFriendsViewHolder, FindFriends findFriends, int i)
            {
                findFriendsViewHolder.setName(findFriends.getName());
                findFriendsViewHolder.setUsername(findFriends.getUsername());
            }
        };
        searchresult.setAdapter(firebaseRecyclerAdapter);
    }


    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public FindFriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setName(String name){
            TextView myName = (TextView) mView.findViewById(R.id.userfullname);
            myName.setText(name);
        }
        public void setUsername(String username){
            TextView myName = (TextView) mView.findViewById(R.id.username);
            myName.setText(username);
        }
    }

    private void keHome() {
        Intent intent = new Intent(this, Home.class);
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