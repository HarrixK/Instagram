package com.example.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

public class RemovePost extends AppCompatActivity {

    private String URL, FullName, Comment;
    private ImageView Avatar;

    private TextView Name, Comments;
    private Button Back, Remove, OK;

    private FirebaseFirestore objectFirebaseFirestore;
    private StorageReference objectStorageReference;

    private LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_post);

        try {
            lottie = findViewById(R.id.Lottie);
            Avatar = findViewById(R.id.avatarIV);

            OK = findViewById(R.id.OK);
            Name = findViewById(R.id.Name);

            Comments = findViewById(R.id.Comment);
            objectFirebaseFirestore = FirebaseFirestore.getInstance();

            Back = findViewById(R.id.back);
            Back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RemovePost.this, AdminReportCheck.class);
                    startActivity(intent);
                }
            });

            Remove = findViewById(R.id.Remove);
            Remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lottie.playAnimation();
                    lottie.setVisibility(View.VISIBLE);

                    RemovePostFunction();
                }
            });

            OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lottie.playAnimation();
                    lottie.setVisibility(View.VISIBLE);

                    OKPost();
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

    private void RemovePostFunction() {
        try {
            objectFirebaseFirestore.collection(FullName).document(Comment).delete();
            objectFirebaseFirestore.collection("Gallery").document(Comment).delete();
            objectFirebaseFirestore.collection("Report").document(Comment).delete();

            Toast.makeText(this, "Post Deleted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RemovePost.this, AdminReportCheck.class);

            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(this, "Delete Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void OKPost() {
        try {
            objectFirebaseFirestore.collection("Report").document(Comment).delete();
            Toast.makeText(this, "The Post is Ok", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(RemovePost.this, AdminReportCheck.class);
            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(this, "OK Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
