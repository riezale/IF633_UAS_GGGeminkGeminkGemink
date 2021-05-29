package com.ac.id.umn.uasmobile;

import android.app.Activity;
import android.app.DirectAction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText gantinama, gantiemail, gantiusername;
    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    FirebaseUser userID;
    Button savebtn;
    ImageView gantigambar;
    StorageReference storageReference;
    private Button foto;
    private ImageView kotakFoto;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private DirectAction data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent data = getIntent();
        String name = data.getStringExtra("name");
        String email = data.getStringExtra("email");
        String username = data.getStringExtra("username");

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser();

        foto = findViewById(R.id.opencam);
        kotakFoto = findViewById(R.id.imguser2);
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager())
                        != null) {
                    startActivityForResult(takePictureIntent,
                            REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        kotakFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
            }
        });

        gantinama = findViewById(R.id.gantinama);
        gantiemail = findViewById(R.id.gantiemail);
        gantiusername = findViewById(R.id.gantiusername);
        savebtn = findViewById(R.id.save);
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("user/" + mAuth.getCurrentUser().getUid() + "/logo.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
            Picasso.get().load(uri).into(gantigambar);
            }
        });


        gantigambar = findViewById(R.id.imguser2);

        gantigambar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gantinama.getText().toString().isEmpty() || gantiemail.getText().toString().isEmpty() || gantiusername.getText().toString().isEmpty()) {
                    Toast.makeText(EditProfile.this, "One or many fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                String email = gantiemail.getText().toString();
                userID.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference docRef = fstore.collection("user").document(userID.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("email",email);
                        edited.put("name",gantinama.getText().toString());
                        edited.put("username",gantiusername.getText().toString());
                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Profile.class));
                                finish();
                            }
                        });
                        Toast.makeText(EditProfile.this, "Email is Changed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

        gantinama.setText(name);
        gantiemail.setText(email);
        gantiusername.setText(username);

        Log.d(TAG, "onCreate: " + name + " " + email + " " + username );
    }
    @Override
    protected void onActivityResult(int requestcode, int resultcode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
        if (requestcode == 1000){
            if (resultcode == Activity.RESULT_OK){
                Uri imageUri = data.getData();

                uploadimageToFirebase(imageUri);
            }

        }
    }

    private void uploadimageToFirebase(Uri imageUri) {
        final StorageReference fileRef = storageReference.child("user/"+mAuth.getCurrentUser().getUid()+"/logo.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(kotakFoto);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
