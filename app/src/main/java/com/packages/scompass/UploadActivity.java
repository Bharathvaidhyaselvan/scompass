package com.packages.scompass;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {

    private FirebaseStorage storage;
    private FirebaseFirestore firestore;
    String username;

    private ImageView imageView;
    private EditText captionEditText;
    private Uri selectedImageUri;
    String photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();

        imageView = findViewById(R.id.imageView);
        captionEditText = findViewById(R.id.captionEditText);


        Intent intent = getIntent();
        if (intent != null) {
            username = intent.getStringExtra("user_name");
        }

        // get current user and get profile image
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
             photoUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null;
            if (photoUrl != null) {
            } else {
              photoUrl="-1";
            }
        } else {
            photoUrl="-1";
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UploadActivity.this, MomentsActivity.class));
        finish();
    }

    public void selectImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    public void uploadPost(View view) {
        if (selectedImageUri != null) {
            long timestamp = System.currentTimeMillis();
            Toast.makeText(this, "Please wait, uploading post...", Toast.LENGTH_SHORT).show();

            StorageReference storageRef = storage.getReference().child("images/" + timestamp + ".jpg");
            storageRef.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> {
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    String caption = captionEditText.getText().toString();

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("POSTS").push();
                    DatabaseReference imgurldb = db.child("imageUrl");              imgurldb.setValue(imageUrl);
                    DatabaseReference profilephotodb = db.child("profileImageUrl"); profilephotodb.setValue(photoUrl);
                    DatabaseReference captiondb = db.child("caption");              captiondb.setValue(caption);
                    DatabaseReference usernamedb = db.child("username");            usernamedb.setValue(username);

                    Toast.makeText(this, "Post uploaded successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UploadActivity.this,MomentsActivity.class));
                    finish();
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(UploadActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(UploadActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
        }
    }
}
