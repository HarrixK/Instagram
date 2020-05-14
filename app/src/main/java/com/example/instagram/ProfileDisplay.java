package com.example.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileDisplay extends AppCompatActivity {

    private String URL, FullName, Comment;
    private ImageView Avatar;

    private TextView Name, Comments;
    private Button Back, Delete;

    private FirebaseFirestore objectFirebaseFirestore;
    private StorageReference objectStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_display);

        try {
            Avatar = findViewById(R.id.avatarIV);
            Name = findViewById(R.id.Name);

            Comments = findViewById(R.id.Comment);
            objectFirebaseFirestore = FirebaseFirestore.getInstance();

            objectStorageReference = FirebaseStorage.getInstance().getReference("Gallery");
            Delete = findViewById(R.id.Delete);
            Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Delete();
                }
            });

            Back = findViewById(R.id.back);
            Back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileDisplay.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            if (getIntent().hasExtra("URL") && getIntent().hasExtra("DisplayName")) {
                URL = getIntent().getStringExtra("URL");
                FullName = getIntent().getStringExtra("DisplayName");

                Comment = getIntent().getStringExtra("Comment");
                Comments.setText(Comment);


                Name.setText(FullName);
                Glide.with(this).asBitmap().load(URL).into(Avatar);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void Delete() {
        try {
            objectFirebaseFirestore.collection(FullName).document(Comment).delete();
            objectFirebaseFirestore.collection("Gallery").document(Comment).delete();
            Toast.makeText(this, "Post Deleted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileDisplay.this, MainActivity.class);
            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(this, "Delete Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
