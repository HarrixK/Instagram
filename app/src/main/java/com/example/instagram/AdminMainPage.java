package com.example.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminMainPage extends AppCompatActivity {

    private Button Report, Settings;
    private TextView Name;

    private FirebaseUser firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_page);

        Settings = findViewById(R.id.Signout);
        Report = findViewById(R.id.Report);

        Name = findViewById(R.id.Name);
        firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseAuth != null) {
            Name.setText(firebaseAuth.getEmail());
        }

        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainPage.this, Settings.class);
                startActivity(intent);
            }
        });
        Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainPage.this, AdminReportCheck.class);
                startActivity(intent);
            }
        });
    }
}
