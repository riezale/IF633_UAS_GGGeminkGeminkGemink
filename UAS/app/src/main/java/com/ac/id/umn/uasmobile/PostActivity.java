package com.ac.id.umn.uasmobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity{

    private ImageButton Selectpostimage;
    private Button Updatepostbutton;
    private EditText PostDescription;
    private ProgressDialog loadingBar;
    private static final int Gallery_pick = 1;
    private TextView username;

    private FirebaseFirestore fstore;
    private String userID;
    private FirebaseAuth mAuth;

    private ImageView KeProfile,KePost,KeFriend,KeHome;


    Uri imageUri;
    boolean isImageAdded=false;

    DatabaseReference Dataref;
    StorageReference Storageref;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Selectpostimage = (ImageButton) findViewById(R.id.pencils);
        Updatepostbutton = (Button) findViewById(R.id.btnPosting);
        PostDescription = (EditText) findViewById(R.id.descriptiontext);
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        fstore = FirebaseFirestore.getInstance();
        username = findViewById(R.id.username);

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
        KeHome = findViewById(R.id.home_bottom);
        KeHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keHome();
            }
        });



        Dataref = FirebaseDatabase.getInstance().getReference().child("Post");
        Storageref = FirebaseStorage.getInstance().getReference().child("PostImage");

        DocumentReference documentReference = fstore.collection("user").document(userID);


        Selectpostimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,Gallery_pick);
            }
        });

        Updatepostbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String imageName=PostDescription.getText().toString();
                if (isImageAdded != false && imageName != null){
                    uploadImage(imageName);
                }
            }
        });

    }

    private void keHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    private void keFriend() {
        Intent intent = new Intent(this, Friends.class);
        startActivity(intent);
    }

    private void keProfile(){
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    private void choosePicture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_picture, null);
        builder.setCancelable(false);
        builder.setView(dialogView);

        ImageView pilihkamera = dialogView.findViewById(R.id.pilihcamera);
        ImageView pilihgallery = dialogView.findViewById(R.id.pilihgallery);

        AlertDialog alertDialogPicture = builder.create();
        alertDialogPicture.show();

        pilihgallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogPicture.cancel();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
        pilihkamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogPicture.cancel();
                takepicturefromcamera();
            }
        });
    }

    private void takepicturefromcamera() {
        Intent takepicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takepicture.setType("image/*");
        if (takepicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takepicture, 2);
        }
    }

    private void  uploadImage(final String imageName) {
        final String key=Dataref.push().getKey();
        Storageref.child(key + ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Storageref.child(key + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("Description", imageName);
                        hashMap.put("ImageUrl", uri.toString());
                        hashMap.put("Username", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        Dataref.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(PostActivity.this, "Data Success", Toast.LENGTH_SHORT).show();
                                keHome();
                            }
                        });

                    }
                });
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallery_pick && data != null) {
            imageUri = data.getData();
            isImageAdded = true;
            Selectpostimage.setImageURI(imageUri);
        }

    }

}
