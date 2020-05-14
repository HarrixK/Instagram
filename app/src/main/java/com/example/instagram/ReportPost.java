package com.example.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class ReportPost extends AppCompatActivity {
    private String URL, FullName, Comment;
    private ImageView Avatar;

    private TextView Name, Comments;
    private Button Back, Report;

    private FirebaseFirestore objectFirebaseFirestore;
    private StorageReference objectStorageReference;

    private LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_post);

        try {
            lottie = findViewById(R.id.Lottie);
            Avatar = findViewById(R.id.avatarIV);
            Name = findViewById(R.id.Name);

            Comments = findViewById(R.id.Comment);
            objectFirebaseFirestore = FirebaseFirestore.getInstance();

            Back = findViewById(R.id.back);
            Back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ReportPost.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            Report = findViewById(R.id.Report);
            Report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lottie.playAnimation();
                    lottie.setVisibility(View.VISIBLE);
                    ReportPostToAdmin();
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

    private void ReportPostToAdmin() {
        try {
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("URL", URL);
            objectMap.put("Comments", Comment);
            objectMap.put("By", FullName);
            objectFirebaseFirestore.collection("Report").document(Comment).set(objectMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    lottie.setVisibility(View.INVISIBLE);
                    Toast.makeText(ReportPost.this, "Post Reported", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    lottie.setVisibility(View.INVISIBLE);
                    Toast.makeText(ReportPost.this, "Post Failed Reported", Toast.LENGTH_SHORT).show();
                }
            });
//            objectFirebaseFirestore.collection("Report")
//                    .document(Comment)
//                    .set(objectMap)
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            lottie.setVisibility(View.INVISIBLE);
//                            Toast.makeText(ReportPost.this, "Failed To Report: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            lottie.setVisibility(View.INVISIBLE);
//                            Toast.makeText(ReportPost.this, "Post Reported", Toast.LENGTH_SHORT).show();
//                        }
//                    });
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
