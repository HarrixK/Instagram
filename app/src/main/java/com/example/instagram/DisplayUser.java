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

public class DisplayUser extends AppCompatActivity {
    private String URL, FullName;
    private ImageView Avatar;

    private TextView Name;
    private Button Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user);
        try {
            Avatar = findViewById(R.id.avatarIV);
            Name = findViewById(R.id.Name);

            Back = findViewById(R.id.back);
            Back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DisplayUser.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            if (getIntent().hasExtra("URL") && getIntent().hasExtra("DisplayName")) {
                URL = getIntent().getStringExtra("URL");
                FullName = getIntent().getStringExtra("DisplayName");

                Name.setText(FullName);
                Glide.with(this).asBitmap().load(URL).into(Avatar);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
