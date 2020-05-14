package com.example.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NameTag extends AppCompatActivity {

    private TextView NameTag, back;
    private FirebaseUser firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_tag);
        try {
            NameTag = findViewById(R.id.NameTag);

            back = findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NameTag.this, MainPage.class);
                    startActivity(intent);
                }
            });

            firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseAuth != null) {
                NameTag.setText(firebaseAuth.getDisplayName().toString());
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
